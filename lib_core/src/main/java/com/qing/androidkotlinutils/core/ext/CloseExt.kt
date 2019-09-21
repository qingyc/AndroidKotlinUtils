package  com.qing.androidkotlinutils.core.ext

import java.io.Closeable


fun Closeable?.safeClose() {
    try {
        this?.close()
    } catch (e: Exception) {
    }
}