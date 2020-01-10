package com.wangzhumo.app.module.opengl.gles;

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
    private int mVertexStride;
    private int mTexCoordStride;
    private int mCoordsPerVertex;

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
}
