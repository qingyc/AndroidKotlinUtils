package com.qing.androidkotlinutils.core.helper

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger
import java.io.File

/**
 * 扫描Helper
 */
object ScanHelper {

    /**
     * 扫描媒体文件(加入MediaStore)
     *
     * @param ctx      上下文
     * @param filepath 文件路径
     */
    fun scanFile(ctx: Context, filepath: String) {
        val paths = arrayOf(filepath)
        MediaScannerConnection.scanFile(ctx, paths, null) { path, uri ->

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
            ApplicationProxy.getApp().sendBroadcast(mediaScanIntent)
            QLogger.i("path:$path uri:$uri")

        }
    }


    /**
     * 扫描文件列表(加入MediaStore)
     *
     * @param ctx       上下文
     * @param filePaths 文件路径列表
     */
    fun scanFileList(ctx: Context, filePaths: Array<String>) {
        MediaScannerConnection.scanFile(ctx, filePaths, null) { path, uri ->
            QLogger.i("path:$path uri:$uri")
        }
    }


    /**
     * 扫描图片文件(加入MediaStore)
     *
     * @param ctx 上下文
     */
    fun scanImage(ctx: Context, filepath: String) {
        val paths = arrayOf(filepath)
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        MediaScannerConnection.scanFile(ctx, paths, mimeTypes) { path, uri ->
            QLogger.i("path:$path uri:$uri")
        }
    }

    /**
     * 立即刷新文件(相册图片图片)
     */
    fun refreshFileNow(filepath: String) {
        try {//下次打开相册才会刷新
            scanImage(ApplicationProxy.getApp(), filepath)
            //马上刷新到相册 部分手机无效
            val file = File(filepath)
            //MediaStore.Images.Media.insertImage(ApplicationProxy.getApp().contentResolver, filepath, file.name, file.name)

            if (Build.VERSION.SDK_INT >= 24) {
                // Provider分享
                val uriForFile = FileProvider.getUriForFile(
                    ApplicationProxy.getApp().applicationContext,
                    ApplicationProxy.getApp().applicationContext.packageName + ".FileProvider", file
                )
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriForFile)
                ApplicationProxy.getApp().sendBroadcast(mediaScanIntent)

            } else {
                // 普通分享
                val contentUri = Uri.fromFile(file)
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri)
                ApplicationProxy.getApp().sendBroadcast(mediaScanIntent)
            }


        } catch (e: Exception) {
            QLogger.e(e)
        }
    }

    //删除系统缩略图
    fun refreshImageDelete(imagePath: String) {
        ApplicationProxy.getApp().contentResolver.delete(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.DATA + "=?",
            arrayOf(imagePath)
        )
    }

}
