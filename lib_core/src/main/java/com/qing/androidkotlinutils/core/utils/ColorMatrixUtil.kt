package com.qing.androidkotlinutils.core.utils

import android.graphics.*
/**
 *
 * 类说明: 调整bitmap :亮度,饱和,对比
 *
 * @author qing
 * @time 2019-08-02 09:40
 */
object ColorMatrixUtil {

    fun imageoperation(src: Bitmap, hue: Float, saturation: Float, lum: Float): Bitmap {
        val output = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        //色调
        val hueMatrix = ColorMatrix()
        hueMatrix.setRotate(0, hue)
        hueMatrix.setRotate(1, hue)
        hueMatrix.setRotate(2, hue)

        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturation)

        val lumMatrix = ColorMatrix()
        lumMatrix.setScale(lum, lum, lum, 1f)

        val imageMatrix = ColorMatrix()
        //色调
        imageMatrix.setRotate(0, hue)
        imageMatrix.setRotate(1, hue)
        imageMatrix.setRotate(2, hue)
        //饱和
        imageMatrix.setSaturation(saturation)
        //亮度
        imageMatrix.setScale(lum, lum, lum, 1f)

        imageMatrix.postConcat(hueMatrix)
        //imageMatrix.postConcat(saturationMatrix)
        //imageMatrix.postConcat(lumMatrix)

        //通过画笔的setColorFilter进行设置
        mPaint.colorFilter = ColorMatrixColorFilter(imageMatrix)
        canvas.drawBitmap(src, 0f, 0f, mPaint)
        return output
    }


    /**
     * 对比
     * @param progress -50  - +50
     */
    fun adjustContrast(progress: Int): ColorMatrix {
        val matrix = ColorMatrix()
        val contrast = (((progress + 50) + 64) / 128f)
        val arrayOf: FloatArray = floatArrayOf(contrast, 0F, 0F, 0F, 0F, 0F,
            contrast, 0F, 0F, 0F,// 改变对比度
            0F, 0F, contrast, 0F, 0F, 0F, 0F, 0F, 1F, 0F)
        matrix.set(arrayOf)
        return matrix

    }

    /**
     * 亮度
     * @param progress -50  - +50
     */
    fun adjustLum(progress: Int): ColorMatrix {
        val matrix = ColorMatrix()
        val v = (progress + 50) * 2 / 100f
        matrix.setScale(v, v, v, 1f)
        return matrix

    }


    /**
     * 饱和度
     * @param progress -50  - +50
     */
    fun adjustSaturation(progress: Int): ColorMatrix {
        val matrix = ColorMatrix()
        val v = (progress + 50) * 2 / 100f
        matrix.setSaturation(v)
        return matrix

    }


    fun createBitmap(srcBitmap: Bitmap, matrixs: ArrayList<ColorMatrix>): Bitmap {

        val output = Bitmap.createBitmap(srcBitmap.width, srcBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val matrix = ColorMatrix()
        matrixs.forEach {
            matrix.postConcat(it)
        }
        mPaint.colorFilter = ColorMatrixColorFilter(matrix)
        canvas.drawBitmap(srcBitmap, 0f, 0f, mPaint)
        return output
    }
}