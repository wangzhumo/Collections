package com.wangzhumo.app.mdeia.gles;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-07  12:03
 */
public interface IGLRenderer {

    void onSurfaceCreate();

    void onSurfaceChange(int width,int height);

    void drawFrame();

}
