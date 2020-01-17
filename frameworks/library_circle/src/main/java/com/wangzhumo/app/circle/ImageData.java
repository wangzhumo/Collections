package com.wangzhumo.app.circle;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-16  19:17
 */
public class ImageData implements ICircleData {

    private String url;

    public ImageData(String url) {
        this.url = url;
    }

    @Override
    public String getImageUrl() {
        return url;
    }

    @Override
    public int getResource() {
        return 0;
    }
}
