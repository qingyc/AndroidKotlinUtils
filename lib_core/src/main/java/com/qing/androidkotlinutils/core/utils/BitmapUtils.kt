package com.qing.androidkotlinutils.core.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.StateListDrawable
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.util.StateSet
import com.qing.androidkotlinutils.core.ext.toBitmap
import com.qing.androidkotlinutils.core.ext.toInputStream
import com.qingyc.qlogger.QLogger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import kotlin.math.ceil
import kotlin.math.max

/**
 *
 * 类说明: bitmap
 *
 * @author qing
 * @time 2019/3/13 14:57
 */
object BitmapUtils {


    /**
     * 非等比例缩放图片
     *
     * @param rawBitmap 原图片
     * @param newWidth  目标宽度
     * @param newHeight 目标高度
     * @return 新图片
     */
    fun scaleBitmap(rawBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        //获取这个图片的宽和高
        val width = rawBitmap.width
        val height = rawBitmap.height
        //计算缩放率，新尺寸除原始尺寸
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 创建操作图片用的matrix对象
        val matrix = Matrix()
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight)
        // 创建新的图片
        return Bitmap.createBitmap(
            rawBitmap, 0, 0,
            width, height, matrix, true
        )
    }

    /**
     * 缩放图片(不超过设定的宽高)
     */
    fun scaleBitmapInMaxWidthOrHeight(rawBitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = rawBitmap.width
        val height = rawBitmap.height
        val scaleWidth = maxWidth.toFloat() / width
        val scaleHeight = maxHeight.toFloat() / height
        val scale = minOf(scaleHeight, scaleWidth)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(rawBitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * 缩放图片(不小于设定的宽高)
     */
    fun scaleBitmapInMinWidthOrHeight(rawBitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = rawBitmap.width
        val height = rawBitmap.height
        val scaleWidth = maxWidth.toFloat() / width
        val scaleHeight = maxHeight.toFloat() / height
        val scale = maxOf(scaleHeight, scaleWidth)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(rawBitmap, 0, 0, width, height, matrix, true)
    }


    /**
     * 等比例缩放图片
     *
     * @param bitmap 原图片
     * @param scale  缩放因子
     * @return 新图片
     */
    fun scaleBitmap(bitmap: Bitmap, scale: Float): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) return null
        when (drawable) {
            is BitmapDrawable -> return drawable.bitmap
            is NinePatchDrawable -> {
                val bitmap = Bitmap
                    .createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        if (drawable.opacity != PixelFormat.OPAQUE)
                            Bitmap.Config.ARGB_8888
                        else
                            Bitmap.Config.RGB_565
                    )
                val canvas = Canvas(bitmap)
                drawable.setBounds(
                    0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                drawable.draw(canvas)
                return bitmap
            }
            else -> return null
        }
    }

    /**
     * bitmap转Drawable
     *
     * @param bitmap Bitmap
     * @return Drawable
     */
    fun bitmap2Drawable(res: Resources, bitmap: Bitmap): Drawable? {
        return BitmapDrawable(res, bitmap)
    }

    /**
     * bitmap转NinePatchDrawable
     *
     * @param bitmap Bitmap
     * @return Drawable
     */
    fun bitmap2NinePatchDrawable(res: Resources, bitmap: Bitmap): NinePatchDrawable {
        val patch = NinePatch(bitmap, bitmap.ninePatchChunk, null)
        return NinePatchDrawable(res, patch)
    }

    /**
     * 反转Bitmap
     *
     * @param bitmap 原图片
     * @param horizontal  水平
     * @return 新图片
     */
    fun invertBitmapX(bitmap: Bitmap, horizontal: Boolean = false): Bitmap {
        val matrix = Matrix()
        if (horizontal) {
            matrix.setScale(-1f, 1f)
        } else {
            matrix.setScale(1f, -1f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    /**
     * 保存图片
     *
     * @param savePath 保存路径
     * @param bitmap   Bitmap
     */
    fun saveBitmap(savePath: String, bitmap: Bitmap?): Boolean {

        var result = false
        if (bitmap == null) {
            return false
        }
        var out: FileOutputStream? = null
        try {
            val file = File(savePath)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return result
    }


    /**
     * Bitmap转 InputStream
     *
     * @param bitmap Bitmap
     * @return InputStream
     */
    fun bitmapToInputStream(bitmap: Bitmap?): InputStream? {
        if (bitmap == null) return null
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        val buff = output.toByteArray()
        return ByteArrayInputStream(buff)
    }


    /**
     * 回收垃圾 recycle
     *
     * @throws
     */
    fun recycle(bitmap: Bitmap?) {
        var bitmap = bitmap
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled) {
            // 回收并且置为null
            bitmap.recycle()
            bitmap = null
        }
        System.gc()
    }

    /**
     * 获取指定路径下的图片的指定大小的缩略图 getImageThumbnail
     *
     * @return Bitmap
     * @throws
     */
    fun getImageThumbnail(imagePath: String, width: Int, height: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options)
        options.inJustDecodeBounds = false // 设为 false
        // 计算缩放比
        val h = options.outHeight
        val w = options.outWidth
        val beWidth = w / width
        val beHeight = h / height
        var be = 1
        if (beWidth < beHeight) {
            be = beWidth
        } else {
            be = beHeight
        }
        if (be <= 0) {
            be = 1
        }
        options.inSampleSize = be
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options)
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(
            bitmap,
            width,
            height,
            ThumbnailUtils.OPTIONS_RECYCLE_INPUT
        )
        return bitmap
    }

    fun saveBitmap(dirpath: String, filename: String, bitmap: Bitmap, isDelete: Boolean) {
        val dir = File(dirpath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file = File(dirpath, filename)
        if (isDelete) {
            if (file.exists()) {
                file.delete()
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }

    fun makeRootDirectory(filePath: String) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
        } catch (e: Exception) {

        }

    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree

    }

    /**
     * 旋转图片一定角度
     * rotaingImageView
     *
     * @return Bitmap
     * @throws
     */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap {
        // 旋转图片 动作
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    fun toRoundCorner(bitmap: Bitmap, pixels: Int): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    /**
     * 将图片转化为圆形头像
     *
     * @throws
     * @Title: toRoundBitmap
     */
    fun toRoundBitmap(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val roundPx: Float
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float
        val dst_left: Float
        val dst_top: Float
        val dst_right: Float
        val dst_bottom: Float
        if (width <= height) {
            roundPx = (width / 2).toFloat()

            left = 0f
            top = 0f
            right = width.toFloat()
            bottom = width.toFloat()

            height = width

            dst_left = 0f
            dst_top = 0f
            dst_right = width.toFloat()
            dst_bottom = width.toFloat()
        } else {
            roundPx = (height / 2).toFloat()

            val clip = ((width - height) / 2).toFloat()

            left = clip
            right = width - clip
            top = 0f
            bottom = height.toFloat()
            width = height

            dst_left = 0f
            dst_top = 0f
            dst_right = height.toFloat()
            dst_bottom = height.toFloat()
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val src = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        val dst = Rect(dst_left.toInt(), dst_top.toInt(), dst_right.toInt(), dst_bottom.toInt())
        val rectF = RectF(dst)

        paint.isAntiAlias = true// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0) // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(
            rectF,
            roundPx,
            roundPx,
            paint
        )// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.xfermode =
            PorterDuffXfermode(PorterDuff.Mode.SRC_IN)// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint) // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output
    }

    fun simpleCompressImage(path: String, newPath: String): String {
        val bitmap = BitmapFactory.decodeFile(path)
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(newPath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        recycle(bitmap)
        return newPath
    }


    /**
     * 返回半透明的bitmap
     *
     * @param map
     * @return
     */
    fun getTranslucentBitmap(map: Bitmap, translate: Float): Bitmap {
        val pressed_map = Bitmap.createBitmap(map.width, map.height, Bitmap.Config.ARGB_8888)
        val pressed_canvas = Canvas(pressed_map)
        val p = Paint()
        p.colorFilter = ColorMatrixColorFilter(getContrastTranslateOnly(translate))
        pressed_canvas.drawBitmap(map, 0f, 0f, p)
        return pressed_map
    }

    /**
     * 改变ColorMatrix透明度值
     *
     * @param contrast
     * @return
     */
    fun getContrastTranslateOnly(contrast: Float): ColorMatrix {
        val cm = ColorMatrix()
        val scale = contrast + 1f
        val translate = (-.5f * scale + .5f) * 255f
        cm.set(
            floatArrayOf(
                1f,
                0f,
                0f,
                0f,
                translate,
                0f,
                1f,
                0f,
                0f,
                translate,
                0f,
                0f,
                1f,
                0f,
                translate,
                0f,
                0f,
                0f,
                1f,
                0f
            )
        )
        return cm
    }


    fun compressImage(path: String, newPath: String): String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        var inSampleSize = 1
        val maxSize = 1000//1M
        QLogger.d("outWidth=" + options.outWidth + " outHeight=" + options.outHeight)
        if (options.outWidth > maxSize || options.outHeight > maxSize) {
            val widthScale = Math.ceil(options.outWidth * 1.0 / maxSize).toInt()
            val heightScale = Math.ceil(options.outHeight * 1.0 / maxSize).toInt()
            inSampleSize = Math.max(widthScale, heightScale)
        }
        QLogger.d("inSampleSize=$inSampleSize")
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        val bitmap = BitmapFactory.decodeFile(path, options)
        val w = bitmap.width
        val h = bitmap.height
        var newW = w
        var newH = h
        if (w > maxSize || h > maxSize) {
            if (w > h) {
                newW = maxSize
                newH = (newW.toDouble() * h.toDouble() * 1.0 / w).toInt()
            } else {
                newH = maxSize
                newW = (newH.toDouble() * w.toDouble() * 1.0 / h).toInt()
            }
        }
        val newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false)
        //recycle(bitmap);
        QLogger.d("newBitmap setWidth=" + newBitmap.width + " h=" + newBitmap.height)

        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(newPath)
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        recycle(newBitmap)
        recycle(bitmap)
        return newPath
    }


    /**
     * 获得bitmap点击效果的StateListDrawable <br></br>
     * 获得半透明的点击效果(点击状态为半透明)
     *
     * @param bm
     * @param bmTransate 0.0-1.0之间的值
     * @return
     */
    fun getDrawableList(bm: Bitmap, bmTransate: Float): Drawable {
        val pressed_bm = getTranslucentBitmap(bm, bmTransate)
        val mDrawable = StateListDrawable()
        mDrawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            BitmapDrawable(Resources.getSystem(), pressed_bm)
        )
        mDrawable.addState(
            intArrayOf(android.R.attr.state_focused),
            BitmapDrawable(Resources.getSystem(), pressed_bm)
        )
        mDrawable.addState(StateSet.WILD_CARD, BitmapDrawable(Resources.getSystem(), bm))
        return mDrawable
    }

    /**
     * 获得bitmap点击效果的StateListDrawable <br></br>
     * 获得相反的半透明点击效果(普通状态为半透明)
     *
     * @param bm
     * @param bmTransate 0.0-1.0之间的值O
     * @return
     */
    fun getDrawableListReverse(bm: Bitmap, bmTransate: Float): Drawable {
        val pressed_bm = getTranslucentBitmap(bm, bmTransate)

        val mDrawable = StateListDrawable()
        mDrawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            BitmapDrawable(Resources.getSystem(), bm)
        )
        mDrawable.addState(
            intArrayOf(android.R.attr.state_focused),
            BitmapDrawable(Resources.getSystem(), bm)
        )
        mDrawable.addState(StateSet.WILD_CARD, BitmapDrawable(Resources.getSystem(), pressed_bm))

        return mDrawable
    }


    /**
     *
     * 类说明: 异步获取bitmap
     *
     * @author qing
     * @time 2019-06-05 22:12
     */
    fun decodeBitmapByUri(
        uri: Uri?,
        action: (bitmap: Bitmap?, error: Throwable?) -> Unit
    ): Disposable? {
        if (uri == null) {
            action(null, null)
            return null
        }
        return Observable.create<Bitmap?> { emitter ->
            var options = BitmapFactory.Options()
            //获取图片大小
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(uri.toInputStream(), null, options)

            val maxSize = 1080
            var inSampleSize = 1  //qtip 缩放图片
            if (options.outWidth > maxSize || options.outHeight > maxSize) {
                val widthScale = ceil(options.outWidth * 1.0 / maxSize).toInt()
                val heightScale = ceil(options.outHeight * 1.0 / maxSize).toInt()
                inSampleSize = max(widthScale, heightScale)
            }

            if (inSampleSize < 1) {
                inSampleSize = 1
            }
            //获取图片
            options = BitmapFactory.Options()
            options.inJustDecodeBounds = false
            options.inSampleSize = inSampleSize
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            options.inPurgeable = true
            options.inTempStorage = ByteArray(32 * 1024)
            val bitmap = BitmapFactory.decodeStream(uri.toInputStream(), null, options)
            bitmap?.let {
                if (!emitter.isDisposed) {
                    emitter.onNext(bitmap)
                    emitter.onComplete()
                }
            }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                action(it, null)
            }, {
                action(null, it)
            })
    }


    /**
     * @param src 原始图
     * @param watermark 水印
     */
    fun createWaterMaskBitmap(src: Bitmap?, watermark: Bitmap): Bitmap? {
        src?.let {
            val width = src.width
            val height = src.height
            //创建一个bitmap
            val newb =
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)// 创建一个新的和SRC长度宽度一样的位图
            //将该图片作为画布
            val canvas = Canvas(newb)
            //在画布 0，0坐标上开始绘制原始图片
            canvas.drawBitmap(src, 0F, 0F, null)
            //在画布上绘制水印图片
            //水印的位置 下中
            val left = (it.width - watermark.width) / 2
            val top = it.height - watermark.height
            canvas.drawBitmap(watermark, left.toFloat(), top.toFloat(), null)
            // 保存
            canvas.save()
            // 存储
            canvas.restore()
            return newb
        } ?: let {
            return null
        }

    }


    /**
     *
     * 类说明: 异步获取bitmap
     *
     * @author qing
     * @time 2019-06-05 22:12
     */
    fun decodeBitmapByFileAysn(
        path: String,
        action: (bitmap: Bitmap?, error: Throwable?) -> Any
    ): Disposable? {

        return Observable.create<Bitmap?> { emitter ->
            val options = BitmapFactory.Options()
            //获取图片大小
            options.inJustDecodeBounds = true

            BitmapFactory.decodeFile(path, options)

            val maxSize = 1080
            var inSampleSize = 1  //qtip 缩放图片
            if (options.outWidth > maxSize || options.outHeight > maxSize) {
                val widthScale = ceil(options.outWidth * 1.0 / maxSize).toInt()
                val heightScale = ceil(options.outHeight * 1.0 / maxSize).toInt()
                inSampleSize = max(widthScale, heightScale)
            }

            if (inSampleSize < 1) {
                inSampleSize = 1
            }
            //获取图片
            options.inJustDecodeBounds = false
            options.inSampleSize = inSampleSize
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            options.inPurgeable = true
            options.inTempStorage = ByteArray(32 * 1024)
            val bitmap = BitmapFactory.decodeFile(path, options)
            bitmap?.let {
                if (!emitter.isDisposed) {
                    emitter.onNext(bitmap)
                    emitter.onComplete()
                }
            }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                action(it, null)
            }, {
                action(null, it)
            })
    }


    /**
     *
     * 类说明: 异步获取bitmap
     *
     * @author qing
     * @time 2019-06-05 22:12
     */
    fun decodeBitmapByAssetsAsyn(
        path: String,
        action: (bitmap: Bitmap?, error: Throwable?) -> Any
    ): Disposable? {

        return Observable.create<Bitmap?> { emitter ->
            val bitmap = ResUtils.readBitmapFromAssets(path)
            bitmap?.let {
                if (!emitter.isDisposed) {
                    emitter.onNext(bitmap)
                    emitter.onComplete()
                }
            }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                action(it, null)
            }, {
                action(null, it)
            })
    }

    /**
     * activity截屏
     * @param activity Activity
     * @return Bitmap?
     */
    fun shotActivity(activity: Activity): Bitmap? {
        val decorView = activity.window.decorView
        return decorView.toBitmap()
    }


}
