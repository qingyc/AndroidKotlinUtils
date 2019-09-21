package com.qing.androidkotlinutils.core.utils


import android.os.Environment
import com.qing.androidkotlinutils.core.R
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import java.io.File

/**
 *
 * 类说明: 手机文件路径配置
 *
 * @author qing
 * @time 2019-07-18 18:17
 */
object FileOpUtils {


    private const val SAVE_LOG_FOLDER = "/log"

    //应用文件根目录
    private val APP_ROOT_FOLDER = "/${ResUtils.getString(R.string.app_name).replace(" ", "")}"

    //录音文件目录
    private const val SAVE_AUDIO_FOLDER = "/audio"
    //拍照文件保存的目录
    private const val SAVE_IMAGE_CAMERA_FOLDER = "/camera"
    //图片裁剪的临时目录
    private const val SAVE_IMAGE_TEMP_FOLDER = "/.temp"
    //用户下载缓存文件
    private const val SAVE_USER_CACHE_FOLDER = "/.cache"
    //附件的保存目录
    private const val SAVE_DOWNLOAD_FOLDER = "/download"
    private const val SAVE_FILE_FOLDER = "/file"

    //分享目录
    private const val SHARE_TEMP_FOLDER = "/share"
    //图片
    private const val PICTURES_HIED_FOLDER = "/.hidePic"
    private const val PICTURES_PUBLIC_FOLDER = "/pictures"


    //默认图片的前缀及后缀
    const val JPEG_FILE_PREFIX = "IMG_"
    const val JPEG_FILE_SUFFIX = ".jpg"
    const val PNG_FILE_SUFFIX = ".png"

    /**
     * sd卡的根目录
     */
    private val sSdRootPath: String = Environment.getExternalStorageDirectory().path
    /**
     * 手机的内部缓存根目录
     */
    private val sInnerDataRootPath = ApplicationProxy.getApp().cacheDir.path
    /**
     * 手机的内置的file目录
     */
    private val sInnerFileRootPath = ApplicationProxy.getApp().filesDir.path


    val MIME_MapTable = arrayOf(
            //{后缀名，MIME类型}
            arrayOf(".3gp", "video/3gpp"), arrayOf(".apk", "application/vnd.android.package-archive"), arrayOf(".asf", "video/x-ms-asf"), arrayOf(".avi", "video/x-msvideo"), arrayOf(".bin", "application/octet-stream"), arrayOf(".bmp", "image/bmp"), arrayOf(".c", "text/plain"), arrayOf(".class", "application/octet-stream"), arrayOf(".conf", "text/plain"), arrayOf(".cpp", "text/plain"), arrayOf(".doc", "application/msword"), arrayOf(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"), arrayOf(".xls", "application/vnd.ms-excel"), arrayOf(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), arrayOf(".exe", "application/octet-stream"), arrayOf(".gif", "image/gif"), arrayOf(".gtar", "application/x-gtar"), arrayOf(".gz", "application/x-gzip"), arrayOf(".h", "text/plain"), arrayOf(".htm", "text/html"), arrayOf(".html", "text/html"), arrayOf(".jar", "application/java-archive"), arrayOf(".java", "text/plain"), arrayOf(".jpeg", "image/jpeg"), arrayOf(".jpg", "image/jpeg"), arrayOf(".js", "application/x-javascript"), arrayOf(".log", "text/plain"), arrayOf(".m3u", "audio/x-mpegurl"), arrayOf(".m4a", "audio/mp4a-latm"), arrayOf(".m4b", "audio/mp4a-latm"), arrayOf(".m4p", "audio/mp4a-latm"), arrayOf(".m4u", "video/vnd.mpegurl"), arrayOf(".m4v", "video/x-m4v"), arrayOf(".mov", "video/quicktime"), arrayOf(".mp2", "audio/x-mpeg"), arrayOf(".mp3", "audio/x-mpeg"), arrayOf(".mp4", "video/mp4"), arrayOf(".mpc", "application/vnd.mpohun.certificate"), arrayOf(".mpe", "video/mpeg"), arrayOf(".mpeg", "video/mpeg"), arrayOf(".mpg", "video/mpeg"), arrayOf(".mpg4", "video/mp4"), arrayOf(".mpga", "audio/mpeg"), arrayOf(".msg", "application/vnd.ms-outlook"), arrayOf(".ogg", "audio/ogg"), arrayOf(".pdf", "application/pdf"), arrayOf(".png", "image/png"), arrayOf(".pps", "application/vnd.ms-powerpoint"), arrayOf(".ppt", "application/vnd.ms-powerpoint"), arrayOf(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"), arrayOf(".prop", "text/plain"), arrayOf(".rc", "text/plain"), arrayOf(".rmvb", "audio/x-pn-realaudio"), arrayOf(".rtf", "application/rtf"), arrayOf(".sh", "text/plain"), arrayOf(".tar", "application/x-tar"), arrayOf(".tgz", "application/x-compressed"), arrayOf(".txt", "text/plain"), arrayOf(".wav", "audio/x-wav"), arrayOf(".wma", "audio/x-ms-wma"), arrayOf(".wmv", "audio/x-ms-wmv"), arrayOf(".wps", "application/vnd.ms-works"), arrayOf(".xml", "text/plain"), arrayOf(".z", "application/x-compress"), arrayOf(".zip", "application/x-zip-compressed"), arrayOf("", "*/*"))


    /**
     * 获取存储的根目录
     *
     * @return
     */
    private val rootDirectory: String
        get() = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            sSdRootPath + APP_ROOT_FOLDER
        else
            sInnerDataRootPath + APP_ROOT_FOLDER

    /**
     * 获取应用内部的文件目录
     */
    fun appInnerDirectory(): String {
        val path = sInnerFileRootPath + APP_ROOT_FOLDER
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return path

    }


    /**
     * 通用图片保存目录(不显示到相册)
     *
     * 有的手机是 文件夹有 ".nomedia " 子文件夹,此文件夹不显示,有的手机即使有 ".nomedia " 系统还是会读取到其中的图片
     */
    fun getNoMediaPictureDirectory(hidePic: Boolean = false): String {
        //var path = rootDirectory + PICTURES_HIED_FOLDER
        // if (hidePic) {
        val path = "$rootDirectory$PICTURES_HIED_FOLDER/.nomedia"
        // }
        val folder = File(path)
        if (!folder.exists()) {
            //https://developer.android.com/guide/topics/data/data-storage.html?hl=zh-cn
            folder.mkdirs()
        }
        return path
    }

    /**
     *
     */
    fun getPublicPictureDirectory(): String {
        val path = rootDirectory + PICTURES_PUBLIC_FOLDER
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return path
    }

    /**
     * 分享目录
     */
    fun getShareDirectory(): String {
        val path = rootDirectory + SHARE_TEMP_FOLDER
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return path
    }

    /**
     * 下载目录
     */
    fun getDownloadDirectory(): String {
        val path = rootDirectory + SAVE_DOWNLOAD_FOLDER
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return path
    }

    /**
     * file目录
     */
    fun getFileDir(): String {
        val path = rootDirectory + SAVE_FILE_FOLDER
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return path
    }

    /**
     * 相册目录
     */
    fun getSysDcimDirectory(): String {
        val dcimPath = "$sSdRootPath/DCIM/${ResUtils.getString(R.string.app_name).replace(" ", "")}"
        File(dcimPath).mkdirs()
        return dcimPath
    }

}
