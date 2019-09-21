package  com.qing.androidkotlinutils.core.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable

/**
 * drawable 2 bitmap
 * @receiver Drawable
 * @return Bitmap?
 */
fun  Drawable.toBitmap() :Bitmap?{
    when (val drawable =this) {
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