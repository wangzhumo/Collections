package com.wangzhumo.app.module.opengl.gles;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-09  21:19
 * <p>
 * Base class for stuff we like to draw.
 * 参考 {https://blog.piasy.com/2017/10/06/Open-gl-es-android-2-part-3/index.html}
 * 添加一个setTransformation的方法
 */
public class Drawable2d {

    private static final int SIZEOF_FLOAT = 4;


    /**
     * A "full" square, extending from -1 to +1 in both dimensions.  When the model/view/projection
     * matrix is identity, this will exactly cover the viewport.
     * <p>
     * The texture coordinates are Y-inverted relative to RECTANGLE.  (This seems to work out
     * right with external textures from SurfaceTexture.)
     */
    private static final float FULL_RECTANGLE_COORDS[] = {
            -1.0f, -1.0f,   // 0 bottom left
            1.0f, -1.0f,   // 1 bottom right
            -1.0f, 1.0f,   // 2 top left
            1.0f, 1.0f,   // 3 top right
    };


    private static final float FULL_RECTANGLE_TEX_COORDS[] = {
            0.0f, 0.0f,     // 0 bottom left
            1.0f, 0.0f,     // 1 bottom right
            0.0f, 1.0f,     // 2 top left
            1.0f, 1.0f      // 3 top right
    };


    private static final FloatBuffer FULL_RECTANGLE_BUF =
            GLUtils.createFloatBuffer(FULL_RECTANGLE_COORDS);
    private static final FloatBuffer FULL_RECTANGLE_TEX_BUF =
            GLUtils.createFloatBuffer(FULL_RECTANGLE_TEX_COORDS);

    private FloatBuffer mVertexArray;
    private FloatBuffer mTexCoordArray;

    private float[] verticesData;
    private float[] textureCoordsData;

    private int mVertexCount;
    private int mTexCoordCount;
    private int mVertexStride;
    private int mTexCoordStride;
    private int mCoordsPerVertex;

    public int vboId;
    public int fboId;
    public int fboTextureId;

    private Prefab mPrefab;

    /**
     * Enum values for constructor.
     */
    public enum Prefab {
        FULL_RECTANGLE
    }

    public Drawable2d(Prefab shape) {
        switch (shape) {
            case FULL_RECTANGLE:
                mVertexArray = FULL_RECTANGLE_BUF;
                mTexCoordArray = FULL_RECTANGLE_TEX_BUF;
                mCoordsPerVertex = 2;
                mVertexStride = mCoordsPerVertex * SIZEOF_FLOAT;
                mVertexCount = FULL_RECTANGLE_COORDS.length / mCoordsPerVertex;
                mTexCoordCount = FULL_RECTANGLE_TEX_COORDS.length / mCoordsPerVertex;
                break;
            default:
                throw new RuntimeException("Unknown shape " + shape);
        }
        mTexCoordStride = 2 * SIZEOF_FLOAT;
        mPrefab = shape;
    }


    /**
     * 设置变换效果
     *
     * @param transformation trans
     */
    public void setTransformation(Transformation transformation) {
        if (mPrefab != Prefab.FULL_RECTANGLE) {
            return;
        }

        //重新计算mVertexArray 与 mTexCoordArray
        verticesData = Arrays.copyOf(FULL_RECTANGLE_COORDS, FULL_RECTANGLE_COORDS.length);
        textureCoordsData = new float[8];

        if (transformation.cropRect != null) {
            //需要裁切
            resolveCrop(
                    transformation.cropRect.x,
                    transformation.cropRect.y,
                    transformation.cropRect.width,
                    transformation.cropRect.height);
        } else {
            resolveCrop(
                    Transformation.FULL_RECT.x,
                    Transformation.FULL_RECT.y,
                    Transformation.FULL_RECT.width,
                    Transformation.FULL_RECT.height);
        }

        resolveFlip(transformation.flip);
        resolveRotate(transformation.rotation);

        if (transformation.inputSize != null && transformation.outputSize != null) {
            resolveScale(
                    transformation.inputSize.width,
                    transformation.inputSize.height,
                    transformation.outputSize.width,
                    transformation.outputSize.height,
                    transformation.scaleType);
        }

        mVertexArray = GLUtils.createFloatBuffer(verticesData);
        mTexCoordArray = GLUtils.createFloatBuffer(textureCoordsData);
    }

    private void resolveCrop(float x, float y, float width, float height) {
        float minX = x;
        float minY = y;
        float maxX = minX + width;
        float maxY = minY + height;

        // left bottom
        textureCoordsData[0] = minX;
        textureCoordsData[1] = minY;
        // right bottom
        textureCoordsData[2] = maxX;
        textureCoordsData[3] = minY;
        // left top
        textureCoordsData[4] = minX;
        textureCoordsData[5] = maxY;
        // right top
        textureCoordsData[6] = maxX;
        textureCoordsData[7] = maxY;
    }

    private void resolveRotate(int rotation) {
        float x, y;
        switch (rotation) {
            case Transformation.ROTATION_90:
                x = textureCoordsData[0];
                y = textureCoordsData[1];
                textureCoordsData[0] = textureCoordsData[4];
                textureCoordsData[1] = textureCoordsData[5];
                textureCoordsData[4] = textureCoordsData[6];
                textureCoordsData[5] = textureCoordsData[7];
                textureCoordsData[6] = textureCoordsData[2];
                textureCoordsData[7] = textureCoordsData[3];
                textureCoordsData[2] = x;
                textureCoordsData[3] = y;
                break;
            case Transformation.ROTATION_180:
                swap(textureCoordsData, 0, 6);
                swap(textureCoordsData, 1, 7);
                swap(textureCoordsData, 2, 4);
                swap(textureCoordsData, 3, 5);
                break;
            case Transformation.ROTATION_270:
                x = textureCoordsData[0];
                y = textureCoordsData[1];
                textureCoordsData[0] = textureCoordsData[2];
                textureCoordsData[1] = textureCoordsData[3];
                textureCoordsData[2] = textureCoordsData[6];
                textureCoordsData[3] = textureCoordsData[7];
                textureCoordsData[6] = textureCoordsData[4];
                textureCoordsData[7] = textureCoordsData[5];
                textureCoordsData[4] = x;
                textureCoordsData[5] = y;
                break;
            case Transformation.ROTATION_0:
            default:
                break;
        }
    }

    private void resolveScale(int inputWidth, int inputHeight, int outputWidth, int outputHeight,
                              int scaleType) {
        if (scaleType == Transformation.SCALE_TYPE_FIT_XY) {
            // The default is FIT_XY
            return;
        }

        // Note: scale type need to be implemented by adjusting
        // the vertices (not textureCoords).
        if (inputWidth * outputHeight == inputHeight * outputWidth) {
            // Optional optimization: If input w/h aspect is the same as output's,
            // there is no need to adjust vertices at all.
            return;
        }

        float inputAspect = inputWidth / (float) inputHeight;
        float outputAspect = outputWidth / (float) outputHeight;

        if (scaleType == Transformation.SCALE_TYPE_CENTER_CROP) {
            if (inputAspect < outputAspect) {
                float heightRatio = outputAspect / inputAspect;
                verticesData[1] *= heightRatio;
                verticesData[3] *= heightRatio;
                verticesData[5] *= heightRatio;
                verticesData[7] *= heightRatio;
            } else {
                float widthRatio = inputAspect / outputAspect;
                verticesData[0] *= widthRatio;
                verticesData[2] *= widthRatio;
                verticesData[4] *= widthRatio;
                verticesData[6] *= widthRatio;
            }
        } else if (scaleType == Transformation.SCALE_TYPE_CENTER_INSIDE) {
            if (inputAspect < outputAspect) {
                float widthRatio = inputAspect / outputAspect;
                verticesData[0] *= widthRatio;
                verticesData[2] *= widthRatio;
                verticesData[4] *= widthRatio;
                verticesData[6] *= widthRatio;
            } else {
                float heightRatio = outputAspect / inputAspect;
                verticesData[1] *= heightRatio;
                verticesData[3] *= heightRatio;
                verticesData[5] *= heightRatio;
                verticesData[7] *= heightRatio;
            }
        }
    }

    private void resolveFlip(int flip) {
        switch (flip) {
            case Transformation.FLIP_HORIZONTAL:
                swap(textureCoordsData, 0, 2);
                swap(textureCoordsData, 4, 6);
                break;
            case Transformation.FLIP_VERTICAL:
                swap(textureCoordsData, 1, 5);
                swap(textureCoordsData, 3, 7);
                break;
            case Transformation.FLIP_HORIZONTAL_VERTICAL:
                swap(textureCoordsData, 0, 2);
                swap(textureCoordsData, 4, 6);

                swap(textureCoordsData, 1, 5);
                swap(textureCoordsData, 3, 7);
                break;
            case Transformation.FLIP_NONE:
            default:
                break;
        }
    }



    private void swap(float[] arr, int index1, int index2) {
        float temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }


    /**
     * Create VBO
     * @return vbo buffers id
     */
    public int createVboBuffer(){
        //获取
        int[] vbos = new int[1];
        GLES20.glGenBuffers(vbos.length, vbos, 0);

        //绑定,开始操作
        vboId = vbos[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);

        //分配内存
        GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                (getVertexLength() + getTexCoordLength()) * SIZEOF_FLOAT,
                null,
                GLES20.GL_STATIC_DRAW);

        //数据设置.
        //设置顶点数据
        GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                0,
                getVertexLength() * SIZEOF_FLOAT,
                getVertexArray());

        //设置纹理数据
        GLES20.glBufferSubData(
                GLES20.GL_ARRAY_BUFFER,
                getVertexLength() * SIZEOF_FLOAT,
                getTexCoordLength() * SIZEOF_FLOAT,
                getTexCoordArray());

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        return vboId;
    }


    /**
     * 创建一个FBO
     * @param width  纹理宽度
     * @param height  纹理高度
     * @return id
     */
    public int createFboBuffer(int width,int height){
        //获取
        int[] fboIds = new int[1];
        GLES20.glGenBuffers(fboIds.length, fboIds, 0);
        //绑定FBO
        fboId = fboIds[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);


        //创建一个纹理
        int[] textureArr = new int[1];
        GLES20.glGenTextures(textureArr.length, textureArr, 0);
        fboTextureId = textureArr[0];

        //绑定纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTextureId);
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT
        );
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT
        );
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
        );
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
        );

        //绑定纹理到FBO
        GLES20.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                fboTextureId,
                0
        );

        //要在glBindTexture绑定成功之后再设置
        //FBO需要自己管理在,这里我们给FBO的内存分配空间
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                width,
                height,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                null
        );

        //检查FBO与Texture是否绑定成功
        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            Log.d(EGLCore.TAG, "createFBO 绑定FBO和Texture失败");
        }

        //解绑纹理与FBO
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_RENDERBUFFER, 0);
        return fboId;
    }


    /**
     * Returns the array of vertices.
     * <p>
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    public FloatBuffer getVertexArray() {
        return mVertexArray;
    }

    /**
     * Returns the array of texture coordinates.
     * <p>
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    public FloatBuffer getTexCoordArray() {
        return mTexCoordArray;
    }

    /**
     * Returns the number of vertices stored in the vertex array.
     */
    public int getVertexCount() {
        return mVertexCount;
    }

    /**
     * Returns the length of vertices stored in the vertex array.
     */
    public int getVertexLength() {
        return FULL_RECTANGLE_COORDS.length;
    }

    /**
     * Returns the number of vertices stored in the vertex array.
     */
    public int getTexCoordCount() {
        return mTexCoordCount;
    }

    /**
     * Returns the length of vertices stored in the vertex array.
     */
    public int getTexCoordLength() {
        return FULL_RECTANGLE_TEX_COORDS.length;
    }

    /**
     * Returns the width, in bytes, of the data for each vertex.
     */
    public int getVertexStride() {
        return mVertexStride;
    }

    /**
     * Returns the width, in bytes, of the data for each texture coordinate.
     */
    public int getTexCoordStride() {
        return mTexCoordStride;
    }

    /**
     * Returns the number of position coordinates per vertex.  This will be 2 or 3.
     */
    public int getCoordsPerVertex() {
        return mCoordsPerVertex;
    }

    public int getSizeofFloat() {
        return SIZEOF_FLOAT;
    }
}
