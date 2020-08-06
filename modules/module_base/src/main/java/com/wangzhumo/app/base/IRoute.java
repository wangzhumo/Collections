package com.wangzhumo.app.base;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-04-20  13:02
 *
 * 所有Module的路径
 */
public interface IRoute {

    String WEBRTC_MAIN = "/webrtc/activity/main";

    String WEBRTC_CALL = "/webrtc/activity/call";

    String APP_MAIN = "/main/activity/main";

    String MEDIA_MAIN = "/media/activity/main";

    String MEDIA_TASK_1 = "/media/activity/task_1";
    String MEDIA_TASK_2 = "/media/activity/task_2";
    String MEDIA_TASK_3 = "/media/activity/task_3";
    String MEDIA_TASK_4 = "/media/activity/task_4";

    interface MEDIA{
        String CAMERA_SHOW = "/media/camera";
        String CAMERA_RECORD = "/media/record";

    }

    String OPENGL_LIST = "/opengl/main";
    interface OPENGL{
        String JUST_SHOW = "/opengl/justshow";
        String CUSTOM_GL_SURFACE = "/opengl/customgl";
        String IMAGE_TEXTURE = "/opengl/image";
        String IMAGE_TEXTURE_FBO = "/opengl/image_fbo";
        String IMAGE_TEXTURE_MATRIX = "/opengl/matrix";
        String IMAGE_TEXTURE_MULTI = "/opengl/multi";
    }

    interface CPPGLES{
        String CPP_GLES = "/cpp_gles";

    }


    String FRAME_ACTIVITY = "/activity/framelayout";


}