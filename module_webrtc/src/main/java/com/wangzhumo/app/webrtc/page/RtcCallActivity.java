package com.wangzhumo.app.webrtc.page;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzhumo.app.base.IRoute;
import com.wangzhumo.app.origin.BaseActivity;
import com.wangzhumo.app.webrtc.R;
import com.wangzhumo.app.webrtc.func.CameraVideoCapturer;
import com.wangzhumo.app.webrtc.func.StreamFormatConfig;
import com.wangzhumo.app.webrtc.func.CallState;
import com.wangzhumo.app.webrtc.func.VideoFormat;
import org.webrtc.*;


/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:15
 * <p>
 * 实现通话的地方
 */
@Route(path = IRoute.WEBRTC_CALL)
public class RtcCallActivity extends BaseActivity {

    private static final String TAG = "RtcCall";

    //Views
    public TextView mLocalName;
    public SurfaceView mLocalSurface;
    public TextView mRemoteName;
    public SurfaceView mRemoteSurface;
    public TextView mLogTextview;


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
    private SurfaceTextureHelper mSurfaceTureHelper;
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
        mLocalSurface = findViewById(R.id.local_surface);
        mRemoteName = findViewById(R.id.remote_name);
        mRemoteSurface = findViewById(R.id.remote_surface);
        mLogTextview = findViewById(R.id.log_textview);

        //初始化一些变量
        mRootEglBase = EglBase.create();

        /*本地的Renderer*/
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

        /*PeerConnect*/
        //PeerConnectionFactory
        PeerConnectionFactory peerConnectionFactory = createPeerFactory(mRootEglBase);
        //打开日志
        Logging.enableLogToDebugOutput(Logging.Severity.LS_VERBOSE);

        /*捕捉*/
        mVideoCapturer = CameraVideoCapturer.createCapturer(RtcCallActivity.this);
        //创建视屏源
        mSurfaceTureHelper = SurfaceTextureHelper.create("CaptureThread", mRootEglBase.getEglBaseContext());
        VideoSource videoSource = peerConnectionFactory.createVideoSource(false);
        //初始化捕捉
        mVideoCapturer.initialize(mSurfaceTureHelper, getApplicationContext(), videoSource.getCapturerObserver());

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
     * @param context ctx
     * @return PeerConnectionFactory
     */
    private PeerConnectionFactory createPeerFactory(EglBase context){
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

}
