/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wangzhumo.app.module.media.opengl.gles

import android.opengl.Matrix

/**
 * Base class for a 2d object.  Includes position, scale, rotation, and flat-shaded color.
 */
class Sprite2d(private val mDrawable: Drawable2d) {
    /**
     * Returns the color.
     *
     *
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    val color: FloatArray
    private var mTextureId: Int
    private var mAngle = 0f
    /**
     * Returns the sprite scale along the X axis.
     */
    var scaleX = 0f
        private set
    /**
     * Returns the sprite scale along the Y axis.
     */
    var scaleY = 0f
        private set
    /**
     * Returns the position on the X axis.
     */
    var positionX = 0f
        private set
    /**
     * Returns the position on the Y axis.
     */
    var positionY = 0f
        private set
    private val mModelViewMatrix: FloatArray
    private var mMatrixReady: Boolean
    private val mScratchMatrix = FloatArray(16)
    /**
     * Re-computes mModelViewMatrix, based on the current values for rotation, scale, and
     * translation.
     */
    private fun recomputeMatrix() {
        val modelView = mModelViewMatrix
        Matrix.setIdentityM(modelView, 0)
        Matrix.translateM(modelView, 0, positionX, positionY, 0.0f)
        if (mAngle != 0.0f) {
            Matrix.rotateM(modelView, 0, mAngle, 0.0f, 0.0f, 1.0f)
        }
        Matrix.scaleM(modelView, 0, scaleX, scaleY, 1.0f)
        mMatrixReady = true
    }

    /**
     * Sets the sprite scale (size).
     */
    fun setScale(scaleX: Float, scaleY: Float) {
        this.scaleX = scaleX
        this.scaleY = scaleY
        mMatrixReady = false
    }

    /**
     * Gets the sprite rotation angle, in degrees.
     */// Normalize.  We're not expecting it to be way off, so just iterate.
    /**
     * Sets the sprite rotation angle, in degrees.  Sprite will rotate counter-clockwise.
     */
    var rotation: Float
        get() = mAngle
        set(angle) { // Normalize.  We're not expecting it to be way off, so just iterate.
            var angle = angle
            while (angle >= 360.0f) {
                angle -= 360.0f
            }
            while (angle <= -360.0f) {
                angle += 360.0f
            }
            mAngle = angle
            mMatrixReady = false
        }

    /**
     * Sets the sprite position.
     */
    fun setPosition(posX: Float, posY: Float) {
        positionX = posX
        positionY = posY
        mMatrixReady = false
    }

    /**
     * Returns the model-view matrix.
     *
     *
     * To avoid allocations, this returns internal state.  The caller must not modify it.
     */
    val modelViewMatrix: FloatArray
        get() {
            if (!mMatrixReady) {
                recomputeMatrix()
            }
            return mModelViewMatrix
        }

    /**
     * Sets color to use for flat-shaded rendering.  Has no effect on textured rendering.
     */
    fun setColor(red: Float, green: Float, blue: Float) {
        color[0] = red
        color[1] = green
        color[2] = blue
    }

    /**
     * Sets texture to use for textured rendering.  Has no effect on flat-shaded rendering.
     */
    fun setTexture(textureId: Int) {
        mTextureId = textureId
    }

    /**
     * Draws the rectangle with the supplied program and projection matrix.
     */
//    fun draw(
//        program: FlatShadedProgram?,
//        projectionMatrix: FloatArray?
//    ) { // Compute model/view/projection matrix.
//        Matrix.multiplyMM(
//            mScratchMatrix,
//            0,
//            projectionMatrix,
//            0,
//            modelViewMatrix,
//            0
//        )
//                program.draw(mScratchMatrix, mColor, mDrawable.getVertexArray(), 0,
//                mDrawable.getVertexCount(), mDrawable.getCoordsPerVertex(),
//                mDrawable.getVertexStride());
//    }

    /**
     * Draws the rectangle with the supplied program and projection matrix.
     */
    fun draw(
        program: Texture2dProgram,
        projectionMatrix: FloatArray?
    ) { // Compute model/view/projection matrix.
        Matrix.multiplyMM(
            mScratchMatrix,
            0,
            projectionMatrix,
            0,
            modelViewMatrix,
            0
        )
        program.draw(
            mScratchMatrix,
            mDrawable.vertexArray!!,
            0,
            mDrawable.vertexCount,
            mDrawable.coordsPerVertex,
            mDrawable.vertexStride,
            GLUtils.IDENTITY_MATRIX,
            mDrawable.texCoordArray!!,
            mTextureId, mDrawable.texCoordStride
        )
    }

    override fun toString(): String {
        return "[Sprite2d pos=" + positionX + "," + positionY +
                " scale=" + scaleX + "," + scaleY + " angle=" + mAngle +
                " color={" + color[0] + "," + color[1] + "," + color[2] +
                "} drawable=" + mDrawable + "]"
    }

    companion object {
        private const val TAG = "OpenGL Record"
    }

    init {
        color = FloatArray(4)
        color[3] = 1.0f
        mTextureId = -1
        mModelViewMatrix = FloatArray(16)
        mMatrixReady = false
    }
}