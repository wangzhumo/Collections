package com.wangzhumo.app.module.media.publisher.encodec;

import com.wangzhumo.app.mdeia.gles.IGLRenderer;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020/3/19  6:21 PM
 */
public class RecordMediaCodec extends BaseMediaCodec {

    private IGLRenderer iglRenderer;
    private int textureId;


    public RecordMediaCodec(int textureId) {
        this.textureId = textureId;
        //创建一个Renderer

        setRenderer(iglRenderer);
        setRenderMode(BaseMediaCodec.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public int getTextureId() {
        return textureId;
    }
}
