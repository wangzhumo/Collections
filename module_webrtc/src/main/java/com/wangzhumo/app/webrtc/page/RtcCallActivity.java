package com.wangzhumo.app.webrtc.page;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.logger.Logger;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.BaseActivity;
import com.wangzhumo.app.webrtc.R;
import com.wangzhumo.app.webrtc.func.*;
import com.wangzhumo.app.webrtc.func.CameraVideoCapturer;
import com.wangzhumo.app.webrtc.signal.SignalEventListener;
import com.wangzhumo.app.webrtc.signal.SignalType;
import com.wangzhumo.app.webrtc.signal.Signaling;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.json.JSONObject;
import org.webrtc.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:15
 * <p>
 * 实现通话的地方
 */
@Route(path = IRoute.WEBRTC_CALL)
public class RtcCallActivity extends BaseActivity implements SignalEventListener {

    private static final String TAG = "RtcCall";

    //Views
    public TextView mLogTextView;
    public ImageButton mBtHangup;
    private SurfaceViewRenderer mLocalSurfaceRenderer;
    private SurfaceViewRenderer mRemoteSurfaceRenderer;

    //filed
    private VideoFormat mVideoConfig = StreamFormatConfig.VIDEO_NORMAL;
    private CompositeDisposable mDisposable;

    private static final String VIDEO_TRACK_ID = "ARDAMSv0";
    private static final String AUDIO_TRACK_ID = "ARDAMSa0";

    private PeerConnection mPeerConnect;
    private PeerConnectionFactory mPeerFactory;


    //状态
    private String mState = CallState.INIT;

    //OpenGL ES
    private EglBase mRootEglBase;
    //渲染
    private SurfaceTextureHelper mSurfacetureHelper;
    //轨道
    private AudioTrack mAudioTrack;
    private VideoTrack mVideoTrack;

    //录制捕捉(自己实现)
    private VideoCapturer mVideoCapturer;

    @Autowired(name = "address")
    public String roomAddress;
    @Autowired(name = "roomName")
    public String roomName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rtc_call;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(RtcCallActivity.this);
        mLocalSurfaceRenderer = findViewById(R.id.local_surface);
        mRemoteSurfaceRenderer = findViewById(R.id.remote_surface);
        mRemoteSurfaceRenderer.setVisibility(View.GONE);

        mLogTextView = findViewById(R.id.log_textview);
        mLogTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mBtHangup = findViewById(R.id.bt_hangup);
        mBtHangup.setOnClickListener(v -> onClickHangup());
        //初始化一些变量
        mRootEglBase = EglBase.create();

        /*Create Renderer*/
        initStreamRenderer(mRootEglBase);

        /*Create PeerConnectionFactory*/
        mPeerFactory = createPeerFactory(mRootEglBase,this);
        //打开日志
        //NOTE: this _must_ happen while PeerConnectionFactory is alive!
        Logging.enableLogToDebugOutput(Logging.Severity.LS_VERBOSE);

        /*Capture And Tracks*/
        //创建视屏/音频轨道
        initStreamTracks(mPeerFactory);

        /*connect signal server*/
        Signaling.getInstance().addSignalListener(this);
        Signaling.getInstance().joinRoom(roomAddress, roomName);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoCapturer != null){
            mVideoCapturer.startCapture(mVideoConfig.getWidth(),mVideoConfig.getHeight(),mVideoConfig.getFps());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mVideoCapturer != null){
                mVideoCapturer.stopCapture();
            }
        } catch (InterruptedException e) {
            Logger.e(e,"VideoCapturer#stopCapture");
        }
    }


    /**
     * 退出房间
     */
    private void onClickHangup() {
        if (mPeerConnect == null) {
            return;
        }
        mPeerConnect.close();
        mPeerConnect = null;
        addLocalLogCat("Hangup Done");
        updateCallState(false);
    }

    /**
     * 创建Renderer
     *
     * @param rootEglBase EglBase
     */
    private void initStreamRenderer(EglBase rootEglBase) {
        //*设置OpenGl的上下文
        mLocalSurfaceRenderer.init(rootEglBase.getEglBaseContext(), null);
        //*缩放类型  填充缩放
        mLocalSurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        //*镜像翻转
        mLocalSurfaceRenderer.setMirror(true);
        //*缩放时是否使用硬件加速
        mLocalSurfaceRenderer.setEnableHardwareScaler(true);
        mLocalSurfaceRenderer.setZOrderMediaOverlay(true);

        /*远程的Renderer*/
        mRemoteSurfaceRenderer.init(rootEglBase.getEglBaseContext(), null);
        mRemoteSurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        mRemoteSurfaceRenderer.setMirror(true);
        mRemoteSurfaceRenderer.setEnableHardwareScaler(true);

    }


    /**
     * 创建视屏/音频轨道
     *
     * @param peerConnectionFactory peerConnectionFactory
     */
    private void initStreamTracks(PeerConnectionFactory peerConnectionFactory) {
        /*捕捉*/
        mVideoCapturer = CameraVideoCapturer.createCapturer(RtcCallActivity.this);

        //创建视屏源
        mSurfacetureHelper = SurfaceTextureHelper.create("CaptureThread", mRootEglBase.getEglBaseContext());
        //isScreencast 是否屏幕
        VideoSource videoSource = peerConnectionFactory.createVideoSource(false);
        //初始化捕捉
        mVideoCapturer.initialize(mSurfacetureHelper, getApplicationContext(), videoSource.getCapturerObserver());

        /*创建Tracks*/
        mVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        mVideoTrack.setEnabled(true);  //设置可用
        mVideoTrack.addSink(mLocalSurfaceRenderer);

        AudioSource audioSource = peerConnectionFactory.createAudioSource(StreamFormatConfig.getAudioConstraint());
        mAudioTrack = peerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
        mAudioTrack.setEnabled(true);
    }


    /**
     * 创建一个PeerConnectionFactory
     *
     * @param rootEglBase  EglBase
     * @param context ctx
     * @return PeerConnectionFactory
     */
    private PeerConnectionFactory createPeerFactory(EglBase rootEglBase,Context context) {
        final VideoDecoderFactory videoDecoderFactory = new DefaultVideoDecoderFactory(
                rootEglBase.getEglBaseContext());

        //移动端推荐使用H264,不适用VP8
        final VideoEncoderFactory videoEncoderFactory = new DefaultVideoEncoderFactory(
                rootEglBase.getEglBaseContext(),
                false,
                true);

        //PeerConnectionFactory  进行初始化,
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(
                        context.getApplicationContext()
                )
                .setEnableInternalTracer(true) //打开内部日志追踪迹
                .createInitializationOptions());

        PeerConnectionFactory.Builder builder = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(videoDecoderFactory)
                .setVideoEncoderFactory(videoEncoderFactory);
        builder.setOptions(null);
        return builder.createPeerConnectionFactory();
    }

    /**
     * 创建PeerConnection
     */
    private PeerConnection createPeerConnection(VideoTrack videoTrack, AudioTrack audioTrack) {
        //准备ICE - Server
        LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<>();
        PeerConnection.IceServer myIceServer = PeerConnection.IceServer.builder(
                "turn:stun.wangzhumo.com:3478")
                .setUsername("wangzhumo")
                .setPassword("wangzhumo")
                .createIceServer();
        iceServers.add(myIceServer);
        //增加了一种candidate
        PeerConnection.RTCConfiguration rtcConfiguration = new PeerConnection.RTCConfiguration(iceServers);
        rtcConfiguration.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfiguration.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        rtcConfiguration.enableDtlsSrtp = true;

        //创建PeerConnection
        PeerConnection peerConnect = mPeerFactory.createPeerConnection(rtcConfiguration, mPeerObserver);
        if (peerConnect == null) {
            Logger.e("createPeerConnection: 创建失败");
            addLocalLogCat("createPeerConnection: 创建失败");
            return null;
        }

        List<String> mediaStreamLabels = Collections.singletonList("ARDAMS");
        peerConnect.addTrack(videoTrack, mediaStreamLabels);
        peerConnect.addTrack(audioTrack, mediaStreamLabels);
        return peerConnect;
    }


    /**
     * 开始于远端建立连接,推送数据
     */
    private void doStartCallRemote() {
        addLocalLogCat("对方加入,开始媒体协商,创建 Offer");
        if (mPeerConnect == null) {
            Logger.e("如果mPeerConnect为Null,则创建PeerConnection");
            mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
        }
        //建立一个媒体约束,进行媒体协商
        MediaConstraints mediaConstraints = createMediaConstraints();
        //创建一个Offer
        createLocalOffer(mPeerConnect, mediaConstraints);
    }

    /**
     * 收到了远端的Offer,应该回应一个answer
     */
    private void doCreateOffer() {
        addLocalLogCat("Answer Call, Wait ...");
        if (mPeerConnect != null) {
            mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
        }
        //建立一个媒体约束,进行媒体协商
        MediaConstraints mediaConstraints = new MediaConstraints();
        addLocalLogCat("收到对方的Offer,创建一个Answer给对方");
        mPeerConnect.createAnswer(new SdpObserverAdapter() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Logger.d("创建Answer成功,设置到setLocalDescription");
                addLocalLogCat("创建Answer成功 signalingState = " + mPeerConnect.signalingState().name());
                addLocalLogCat("创建Answer成功,设置到setLocalDescription");
                mPeerConnect.setLocalDescription(new SdpObserverAdapter(), sessionDescription);
                Logger.d("创建Answer成功,发送到对面去");
                Signaling.getInstance().sendMessage(
                        "type", SignalType.ANSWER,
                        "sdp", sessionDescription.description);
                addLocalLogCat("创建Answer成功,发送到对面去");
            }

            @Override
            public void onCreateFailure(String s) {
                super.onCreateFailure(s);
                Logger.d("创建Answer失败" + s);
                addLocalLogCat("创建Answer失败" + s);
                addLocalLogCat("创建Answer失败 signalingState = " + mPeerConnect.signalingState().name());
            }
        }, mediaConstraints);

        updateCallState(true);
    }


    /**
     * 远端是否可已使用 (一些机型,如果SurfaceView可见且不设置一个Render,会报错崩溃)
     *
     * @param enable
     */
    private void updateCallState(boolean enable) {
        Disposable disposable = Observable.just(enable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        mRemoteSurfaceRenderer.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                    }
                });
        addDisposable(disposable);
    }

    /**
     * 创建本地的Offer
     */
    private void createLocalOffer(PeerConnection peerConnection, MediaConstraints mediaConstraints) {
        Logger.d("收到对端加入频道后,创建一个Offer给对方.");
        peerConnection.createOffer(new SdpObserverAdapter() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Logger.d("创建一个Offer成功,并且 setLocalDescription");
                addLocalLogCat("创建一个Offer成功,并且 setLocalDescription");
                mPeerConnect.setLocalDescription(new SdpObserverAdapter(), sessionDescription);
                //创建完毕后传递给远端
                Signaling.getInstance().sendMessage("type", SignalType.OFFER, "sdp", sessionDescription.description);
                Logger.d("创建一个Offer成功,发送Offer给对方.");
                addLocalLogCat("创建一个Offer成功,发送Offer给对方");
            }

            @Override
            public void onCreateFailure(String s) {
                super.onCreateFailure(s);
                addLocalLogCat("创建一个Offer失败" + s);
                Logger.d("创建一个Offer失败" + s);
            }
        }, mediaConstraints);
    }

    /**
     * 创建媒体协商的约束
     *
     * @return MediaConstraints
     */
    private MediaConstraints createMediaConstraints() {
        MediaConstraints mediaConstraints = new MediaConstraints();
        //接收音频/视频
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        //打开Dtls
        mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        return mediaConstraints;
    }


    private StringBuffer stringBuffer;

    /**
     * 把Log显示到桌面上
     */
    private void addLocalLogCat(String message) {
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }
        stringBuffer.append(message).append("\n");
        mLogTextView.setText(stringBuffer.toString());
    }

    /**
     * 收到远端candidate
     *
     * @param message data
     */
    private void onCandidateReceived(JSONObject message) {
        Logger.d("收到对面的Candidate,加入到 addIceCandidate");
        addLocalLogCat("收到对面的Candidate,加入到 addIceCandidate");
        Logger.json(message.toString());
        IceCandidate candidate = new IceCandidate(
                message.optString("id"),
                message.optInt("label"),
                message.optString("candidate"));
        mPeerConnect.addIceCandidate(candidate);
    }

    /**
     * 收到远端Offer
     *
     * @param message data
     */
    private void onRemoteOfferReceived(JSONObject message) {
        Logger.json(message.toString());
        if (mPeerConnect == null) {
            mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
        }

        addLocalLogCat("收到对方的Offer,setRemoteDescription");
        SessionDescription description = new SessionDescription(
                SessionDescription.Type.OFFER,
                message.optString("sdp"));
        mPeerConnect.setRemoteDescription(new SdpObserverAdapter(){
            @Override
            public void onSetFailure(String s) {
                super.onSetFailure(s);
                addLocalLogCat("onRemoteOfferReceived 设置setRemoteDescription失败"+s);
                Logger.e("设置setRemoteDescription失败"+s);
            }

            @Override
            public void onSetSuccess() {
                super.onSetSuccess();
                addLocalLogCat("onRemoteOfferReceived 设置setRemoteDescription成功");
                Logger.e("设置setRemoteDescription成功");
            }
        }, description);

        //对面传递了一个Offer,那我我们应该回应
        Logger.d("收到对方的Offer,创建一个Answer给对方");
        //创建给对方的offer
        doCreateOffer();
    }


    /**
     * 收到远端answer
     *
     * @param message data
     */
    private void onAnswerReceived(JSONObject message) {
        Logger.d("收到对面的Answer,加入到 setRemoteDescription");
        addLocalLogCat("收到对面的Answer,加入到 setRemoteDescription");
        Logger.json(message.toString());
        SessionDescription description = new SessionDescription(SessionDescription.Type.ANSWER, message.optString("sdp"));
        mPeerConnect.setRemoteDescription(new SdpObserverAdapter(){
            @Override
            public void onSetSuccess() {
                super.onSetSuccess();
                Logger.e("设置setRemoteDescription成功");
                addLocalLogCat("设置setRemoteDescription成功");
            }

            @Override
            public void onSetFailure(String s) {
                super.onSetFailure(s);
                Logger.e("设置setRemoteDescription失败"+s);
                addLocalLogCat("设置setRemoteDescription失败" + s);
            }
        }, description);

        //更新状态
        updateCallState(true);
    }

    /**
     * PeerConnection的监听
     */
    private PeerConnection.Observer mPeerObserver = new PeerConnection.Observer() {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Logger.d("onSignalingChange", signalingState);
            addLocalLogCat("onSignalingChange 修改为 : "  + signalingState.name());
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Logger.d("onIceConnectionChange", iceConnectionState);
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Logger.d("onIceConnectionReceivingChange");
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Logger.d("onIceGatheringChange", iceGatheringState);
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Logger.d("本地的IceCandidate获取,发送给对端.");
            addLocalLogCat("本地的IceCandidate获取,发送给对端");
            //当获取到iceCandidate就发送给对端
            Signaling.getInstance().sendMessage(
                    "type", SignalType.CANDIDATE,
                    "label", String.valueOf(iceCandidate.sdpMLineIndex),
                    "id", iceCandidate.sdpMid,
                    "candidate", iceCandidate.sdp);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
            Logger.e("onIceCandidatesRemoved");
            addLocalLogCat("onIceCandidatesRemoved");
            mPeerConnect.removeIceCandidates(iceCandidates);
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            Logger.d(mediaStream);
        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {
            Logger.d(mediaStream);
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Logger.d(dataChannel);
        }

        @Override
        public void onRenegotiationNeeded() {
            Logger.d("onRenegotiationNeeded");
            addLocalLogCat("onRenegotiationNeeded");
        }

        @Override
        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
            Logger.d("收到对端的Track数据");
            addLocalLogCat("收到对端的Track数据");
            //加入轨道
            MediaStreamTrack track = rtpReceiver.track();
            if (track instanceof VideoTrack) {
                addLocalLogCat("收到对端的Track数据,设置到SurfaceView上去");
                Logger.d("收到对端的Track数据,且是VideoTrack,设置到SurfaceView上去.");
                VideoTrack remoteVideoTrack = (VideoTrack) track;
                remoteVideoTrack.setEnabled(true);
                remoteVideoTrack.addSink(mRemoteSurfaceRenderer);
            }
        }
    };


    //--------------信令服务器------------------

    @Override
    public void onConnected() {
        addLocalLogCat("onConnected");
    }

    @Override
    public void onConnecting() {
        addLocalLogCat("onConnecting");
    }

    @Override
    public void onDisconnect() {
        addLocalLogCat("onDisconnect");
    }

    @Override
    public void onUserJoined(@NonNull String room, @NonNull String uid) {
        //修改状态
        addLocalLogCat("local user joined!");
        mState = CallState.JOINED;
        //创建PeerConnecting
        if (mPeerConnect == null){
            Logger.d("加入频道成功,生成一个PeerConnection");
            addLocalLogCat("加入频道成功,生成一个PeerConnection");
            mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
        }
    }

    @Override
    public void onUserLeaved(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onUserLeaved : 离开房间 %s", room));
        mState = CallState.LEAVED;
    }

    @Override
    public void onRemoteUserJoin(@NonNull String room, @NonNull String uid) {
        addLocalLogCat("Remote User Joined, room: " + roomName);
        if (TextUtils.equals(mState, CallState.JOINED_UNBIND)) {
            if (mPeerConnect == null) {
                addLocalLogCat("createPeerConnection ");
                createPeerConnection(mVideoTrack, mAudioTrack);
            }
        }
        mState = CallState.JOINED_CONN;
        //开始媒体协商
        Logger.d("对端有人加入,可以开始媒体协商");
        doStartCallRemote();
    }


    @Override
    public void onRemoteUserLeave(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onRemoteUserLeave : %s 离开了房间 %s", uid, room));
        mState = CallState.JOINED_UNBIND;
        if (mPeerConnect != null){
            mPeerConnect.close();
            mPeerConnect = null;
        }
    }

    @Override
    public void onMessage(@NonNull JSONObject message) {
        //与JavaScript一样,针对不同的类型进行处理即可
        if (message.has("type")) {
            switch (message.optString("type")) {
                case SignalType.ANSWER:
                    onAnswerReceived(message);
                    break;
                case SignalType.OFFER:
                    onRemoteOfferReceived(message);
                    break;
                case SignalType.CANDIDATE:
                    onCandidateReceived(message);
                    break;
                default:
                    Logger.json(message.toString());
                    break;
            }
        } else {
            Logger.json(message.toString());
        }
    }


    @Override
    public void onJoinError(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onJoinError : %s 加入房间 %s 失败", uid, room));
        mState = CallState.LEAVED;
        //退出这个房间,onDestroy 会销毁资源
        RtcCallActivity.this.finish();
    }

    @Override
    public void onError(Exception e) {
        addLocalLogCat(String.format("onError :  %s 连接失败", e.getMessage()));
    }

    @Override
    public void onSend(String type) {
        addLocalLogCat(String.format("Send Message : %s",type));
    }

    public void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        //释放两个SurfaceView
        if (mLocalSurfaceRenderer != null) {
            mLocalSurfaceRenderer.release();
            mLocalSurfaceRenderer = null;
        }
        if (mRemoteSurfaceRenderer != null) {
            mRemoteSurfaceRenderer.release();
            mRemoteSurfaceRenderer = null;
        }
        //停止摄像头采集
        if (mVideoCapturer != null) {
            mVideoCapturer.dispose();
            mVideoCapturer = null;
        }
        //销毁帮助类
        if (mSurfacetureHelper != null) {
            mSurfacetureHelper.dispose();
            mSurfacetureHelper = null;
        }
        //释放工厂类
        PeerConnectionFactory.stopInternalTracingCapture();
        PeerConnectionFactory.shutdownInternalTracer();
        if (mPeerFactory != null) {
            mPeerFactory.dispose();
            mPeerFactory = null;
        }
        Signaling.getInstance().leaveRoom();
        Signaling.getInstance().onDestroy();
    }
}
