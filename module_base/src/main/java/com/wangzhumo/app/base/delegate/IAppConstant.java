package com.wangzhumo.app.base.delegate;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2019-09-28  17:50
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({IAppConstant.WEBRTC,IAppConstant.MEDIA})
public @interface IAppConstant {
    String BASE = "app_base";
    String MEDIA = "app_media";
    String WEBRTC = "app_webrtc";
}
