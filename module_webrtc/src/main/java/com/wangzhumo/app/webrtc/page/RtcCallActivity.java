package com.wangzhumo.app.webrtc.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
    public TextView mLocalName;
    public TextView mRemoteName;
    public TextView mLogTextView;


    //filed
    private VideoFormat mVideoConfig = StreamFormatConfig.VIDEO_NORMAL;

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
    private SurfaceViewRenderer mLocalSurfaceRenderer;
    private SurfaceViewRenderer mRemoteSurfaceRenderer;
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
        mLocalName = findViewById(R.id.local_name);
        mLocalSurfaceRenderer = findViewById(R.id.local_surface);
        mRemoteName = findViewById(R.id.remote_name);
        mRemoteSurfaceRenderer = findViewById(R.id.remote_surface);
        mLogTextView = findViewById(R.id.log_textview);
        mLogTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        //初始化一些变量
        mRootEglBase = EglBase.create();

        /*Create Renderer*/
        initStreamRenderer(mRootEglBase);

        /*Create PeerConnectionFactory*/
        mPeerFactory = createPeerFactory(mRootEglBase);
        Logging.enableLogToDebugOutput(Logging.Severity.LS_VERBOSE);      //打开日志

        /*Capture And Tracks*/
        //创建视屏/音频轨道
        initStreamTracks(mPeerFactory);

        /*connect signal server*/
        Signaling.getInstance().addSignalListener(this);
        Signaling.getInstance().joinRoom(roomAddress, roomName);
    }


    /**
     * 创建Renderer
     *
     * @param mRootEglBase EglBase
     */
    private void initStreamRenderer(EglBase mRootEglBase) {
        //*设置OpenGl的上下文
        mLocalSurfaceRenderer.init(mRootEglBase.getEglBaseContext(), null);
        //*缩放类型  填充缩放
        mLocalSurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        //*镜像翻转
        mLocalSurfaceRenderer.setMirror(true);
        //*缩放时是否使用硬件加速
        mLocalSurfaceRenderer.setEnableHardwareScaler(false);

        /*远程的Renderer*/
        mRemoteSurfaceRenderer.init(mRootEglBase.getEglBaseContext(), null);
        mRemoteSurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        mRemoteSurfaceRenderer.setMirror(true);
        mRemoteSurfaceRenderer.setEnableHardwareScaler(true);
        mRemoteSurfaceRenderer.setZOrderMediaOverlay(true);
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
     * @param context ctx
     * @return PeerConnectionFactory
     */
    private PeerConnectionFactory createPeerFactory(EglBase context) {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(getApplicationContext())
                .setEnableInternalTracer(true).createInitializationOptions());
        VideoDecoderFactory videoDecoderFactory = new DefaultVideoDecoderFactory(context.getEglBaseContext());

        //移动端推荐使用H264,不适用VP8
        VideoEncoderFactory videoEncoderFactory = new DefaultVideoEncoderFactory(context.getEglBaseContext(),
                false, true);
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
        PeerConnection.IceServer myIceServer = PeerConnection.IceServer.builder("turn:stun.wangzhumo.com:3478")
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
            Log.e(TAG, "createPeerConnection: 创建失败");
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
        addLocalLogCat("doStartCallRemote 开始与远端建立连接");
        if (mPeerConnect == null) {
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
    private void doOnRemoteOfferReceived() {
        if (mPeerConnect != null) {
            mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
        }
        //建立一个媒体约束,进行媒体协商
        MediaConstraints mediaConstraints = createMediaConstraints();
        mPeerConnect.createAnswer(new SdpObserverAdapter(){
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                mPeerConnect.setLocalDescription(new SdpObserverAdapter(), sessionDescription);
                Signaling.getInstance().sendMessage("type", SignalType.ANSWER, "sdp", sessionDescription.description);
            }
        },mediaConstraints);
    }

    /**
     * 创建本地的Offer
     */
    private void createLocalOffer(PeerConnection peerConnection, MediaConstraints mediaConstraints) {
        peerConnection.createOffer(new SdpObserverAdapter() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                mPeerConnect.setLocalDescription(new SdpObserverAdapter(), sessionDescription);
                //创建完毕后传递给远端
                Signaling.getInstance().sendMessage("type", SignalType.OFFER, "sdp", sessionDescription.description);
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
     * PeerConnection的监听
     */
    private PeerConnection.Observer mPeerObserver = new PeerConnection.Observer() {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Logger.d("onSignalingChange",signalingState);
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Logger.d("onIceConnectionChange",iceConnectionState);
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Logger.d("onIceConnectionReceivingChange");
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Logger.d("onIceGatheringChange",iceGatheringState);
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Logger.d(iceCandidate);
            //当获取到iceCandidate就发送给对端
            Signaling.getInstance().sendMessage("type", SignalType.CANDIDATE,
                    "label", String.valueOf(iceCandidate.sdpMLineIndex),
                    "id", iceCandidate.sdpMid,
                    "candidate", iceCandidate.sdp);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
            Logger.d(iceCandidates);
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
        }

        @Override
        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
            Logger.d("onAddTrack %s",rtpReceiver);
            for (MediaStream mediaStream : mediaStreams) {
                Logger.d("onAddTrack MediaStream ID = &s",mediaStream.getId());
            }
            //加入轨道
            MediaStreamTrack track = rtpReceiver.track();
            if (track instanceof VideoTrack){
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
        addLocalLogCat(String.format("onUserJoined : 加入了房间 %s", room));
        //修改状态
        mState = CallState.JOINED_UNBIND;
        //创建PeerConnecting
        mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
    }

    @Override
    public void onUserLeaved(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onUserLeaved : 离开房间 %s", room));
        mState = CallState.JOINED_UNBIND;
        //删除
        if (mPeerConnect != null){
            mPeerConnect.close();
            mPeerConnect = null;
        }
    }

    @Override
    public void onRemoteUserJoin(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onRemoteUserJoin : %s 加入了房间 %s", uid, room));
        if (TextUtils.equals(mState, CallState.JOINED_UNBIND)) {
            if (mPeerConnect == null) {
                createPeerConnection(mVideoTrack, mAudioTrack);
            }
            doStartCallRemote();
        }
        mState = CallState.JOINED_CONN;
    }


    @Override
    public void onRemoteUserLeave(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onRemoteUserLeave : %s 离开了房间 %s", uid, room));
    }

    @Override
    public void onMessage(@NonNull JSONObject message) {
        addLocalLogCat(String.format("onMessage : %s", message.toString()));
        //与JavaScript一样,针对不同的类型进行处理即可
        if (message.has("type")){
            switch (message.optString("type")){
                case SignalType.ANSWER:
                    onAnswerReceived(message);
                    break;
                case SignalType.OFFER:
                    onOfferReceived(message);
                    break;
                case SignalType.CANDIDATE:
                    onCandidateReceived(message);
                    break;
            }
        }
    }

    /**
     * 收到远端candidate
     * @param message data
     */
    private void onCandidateReceived(JSONObject message) {
        Logger.d("onCandidateReceived");
        Logger.json(message.toString());
        IceCandidate candidate = new IceCandidate(
                message.optString("id"),
                message.optInt("label"),
                message.optString("candidate"));
        mPeerConnect.addIceCandidate(candidate);
    }

    /**
     * 收到远端Offer
     * @param message data
     */
    private void onOfferReceived(JSONObject message) {
        Logger.d("onOfferReceived");
        Logger.json(message.toString());
        if (mPeerConnect == null){
            mPeerConnect = createPeerConnection(mVideoTrack, mAudioTrack);
        }
        SessionDescription description = new SessionDescription(SessionDescription.Type.OFFER,message.optString("sdp"));
        mPeerConnect.setRemoteDescription(new SdpObserverAdapter(), description);

        //对面传递了一个Offer,那我我们应该回应
        doOnRemoteOfferReceived();
    }




    /**
     * 收到远端answer
     * @param message data
     */
    private void onAnswerReceived(JSONObject message) {
        Logger.d("onAnswerReceived");
        Logger.json(message.toString());
        SessionDescription description = new SessionDescription(SessionDescription.Type.ANSWER,message.optString("sdp"));
        mPeerConnect.setRemoteDescription(new SdpObserverAdapter(), description);
    }

    @Override
    public void onJoinError(@NonNull String room, @NonNull String uid) {
        addLocalLogCat(String.format("onJoinError : %s 加入房间 %s 失败", uid, room));
        mState = CallState.LEAVED;
        //释放两个SurfaceView
        if (mLocalSurfaceRenderer != null){
            mLocalSurfaceRenderer.release();
            mLocalSurfaceRenderer = null;
        }
        if (mRemoteSurfaceRenderer != null) {
            mRemoteSurfaceRenderer.release();
            mRemoteSurfaceRenderer = null;
        }
        //停止摄像头采集
        if (mVideoCapturer != null){
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
            mPeerConnect.dispose();
            mPeerConnect = null;
        }
        //退出这个房间
        RtcCallActivity.this.finish();
    }

    @Override
    public void onError(Exception e) {
        addLocalLogCat(String.format("onError :  %s 连接失败", e.getMessage()));
    }


}
