package  com.qing.androidkotlinutils.core.ext

import android.graphics.Bitmap
import com.qing.androidkotlinutils.core.utils.BitmapUtils


/**
 * 回收
 */
fun Bitmap?.safeRecycle() {
    if (this?.isRecycled == false) {
        this.recycle()
    }
}

/**
 * 指定最大宽高->缩放图片
 */
fun Bitmap.scaleBitmapInMaxWidthOrHeight(maxWidth: Int, maxHeight: Int): Bitmap? {
    return BitmapUtils.scaleBitmapInMaxWidthOrHeight(this, maxWidth, maxHeight)
}

/**
 * 指定最小宽高->缩放图片
 */
fun Bitmap.scaleBitmapInMinWidthOrHeight(maxWidth: Int, maxHeight: Int): Bitmap? {
    return BitmapUtils.scaleBitmapInMinWidthOrHeight(this, maxWidth, maxHeight)
}

/**
 * 剪裁图片
 */
fun Bitmap.trimBitmap(leftTrim: Int, topTrim: Int, rightTrim: Int, bottomTrim: Int): Bitmap {
    val width = this.width
    val height = this.height
    return Bitmap.createBitmap(this, leftTrim, topTrim, width - leftTrim - rightTrim, height - topTrim - bottomTrim)
}

/**
 * 复制
 */
fun Bitmap.copyBitmap(left: Int, top: Int, width: Int, height: Int): Bitmap {
    return Bitmap.createBitmap(this, left, top, width, height)
}