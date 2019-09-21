package com.qing.androidkotlinutils.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ResourceUtils
import com.qing.androidkotlinutils.core.R
import com.qing.androidkotlinutils.core.ext.dismissLittleLoading
import com.qing.androidkotlinutils.core.ext.showLittleLoading
import com.qing.androidkotlinutils.core.helper.ViewHelper
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger

import org.json.JSONException
import org.json.JSONObject
import java.io.File
import kotlin.concurrent.thread

/**
 *
 * 类说明: 分享工具
 *
 * @author qing
 * @time 2019-09-21 16:29
 */
object ShareUtil {

    //本机安装的可分享图片的app
    private var installShareApps = arrayListOf<ResolveInfo>()
    //排序后的
    private val sortedSharePicAppPackageNames = arrayListOf<String>()
    //预设的分享app包名
    private var thirdShareApps = arrayListOf<String>()

    /**
     * 分享布局
     */
    fun getShareLayoutBitmap(
        activity: Activity, @LayoutRes layoutId: Int,
        action: (inflateView: View) -> Unit
    ): Bitmap? {
        val inflate = LayoutInflater.from(activity).inflate(layoutId, null)
        inflate?.let { view ->
            action(view)
            if (view.isLayoutRequested) {
                val widthSpec: Int
                val heightSpec: Int
                val layoutParams = view.layoutParams
                if (layoutParams != null) {
                    widthSpec = View.MeasureSpec.makeMeasureSpec(
                        layoutParams.width,
                        View.MeasureSpec.EXACTLY
                    )
                    heightSpec = View.MeasureSpec.makeMeasureSpec(
                        layoutParams.height,
                        View.MeasureSpec.EXACTLY
                    )
                } else {
                    widthSpec = View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                    )
                    heightSpec = View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                    )
                }
                view.measure(widthSpec, heightSpec)
                view.layout(
                    0, 0, view.measuredWidth,
                    view.measuredHeight
                )
            }

            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * 获取安装的可以分享图片的app
     */
    fun getSharePicAppList(context: Context?): ArrayList<String> {
        context?.let {
            //安装的app
            if (installShareApps.isEmpty()) {
                val sharePicApps = getSharePicApps(it)
                sharePicApps?.forEach { resolveinfo ->
                    resolveinfo.activityInfo?.packageName?.let {

                        if (!installShareApps.contains(resolveinfo)) {
                            installShareApps.add(resolveinfo)
                        }
                    }
                }
            }
            //app.json中的app
            if (thirdShareApps.isEmpty()) {
                val textByAssets = ResourceUtils.readAssets2String("apps.json")
                try {
                    val json = JSONObject(textByAssets)
                    val jsonList = json.getJSONArray("apps")
                    for (i in 0 until jsonList.length()) {
                        val element = jsonList.getString(i)
                        if (!TextUtils.isEmpty(element) && !thirdShareApps.contains(element)) {
                            thirdShareApps.add(element)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        thirdShareApps.reverse()
        //根据json排序
        if (sortedSharePicAppPackageNames.isNullOrEmpty()) {
            for (thirdShareAppPackageNames in thirdShareApps) {
                for (installShareApp in installShareApps) {
                    val packageName = installShareApp.activityInfo?.packageName ?: ""
                    if (thirdShareAppPackageNames == packageName && !sortedSharePicAppPackageNames.contains(
                            packageName
                        ) && !TextUtils.isEmpty(packageName)
                    ) {
                        sortedSharePicAppPackageNames.add(0, packageName)
                    }
                }
            }
        }
        for (installShareApp in installShareApps) {
            val packageName = installShareApp.activityInfo?.packageName ?: ""
            if (!sortedSharePicAppPackageNames.contains(packageName) && !TextUtils.isEmpty(
                    packageName
                )
            ) {
                sortedSharePicAppPackageNames.add(packageName)
            }
        }
        return sortedSharePicAppPackageNames
    }


    /**
     * 分享image
     * @param ctx Activity
     * @param bitmap Bitmap?
     * @param shareByApp String
     */
    fun shareImage(ctx: Activity, bitmap: Bitmap?, shareByApp: String) {
        if (bitmap == null) {
            return
        }
        ctx.showLittleLoading()
        try {
            // QTIP: 2018/12/27 MediaStore.Images.Media.insertImage 的图片是image/jpeg 不带透明通道
            thread(start = true) {
                //val imageUrl = MediaStore.Images.Media.insertImage(ctx.contentResolver, bitmap, getString(app_name), "")
                val filePath =
                    FileOpUtils.getShareDirectory() + File.separator + "Share" + System.currentTimeMillis() + ".png"
                QLogger.i("share image path=== $filePath")
                val file = File(filePath)
                if (BitmapUtils.saveBitmap(filePath, bitmap)) {
                    val intent = Intent(Intent.ACTION_SEND)
                    //intent.putExtra(Intent.EXTRA_SUBJECT, "")
                    //intent.putExtra(Intent.EXTRA_TEXT, ResUtils.getString(R.string.share))
                    intent.type = "image/*"
                    if (Build.VERSION.SDK_INT >= 24) {
                        // Provider分享
                        val uriForFile = FileProvider.getUriForFile(
                            ApplicationProxy.getApp(),
                            ApplicationProxy.getApp().packageName + ".FileProvider", file
                        )
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        intent.putExtra(Intent.EXTRA_STREAM, uriForFile)
                    } else {
                        // 普通分享
                        val uri = Uri.fromFile(file)
                        intent.putExtra(Intent.EXTRA_STREAM, uri)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    //指定app分享
                    if (!TextUtils.isEmpty(shareByApp)) {
                        intent.setPackage(shareByApp)
                    }
                    ctx.startActivity(intent)
                }
                bitmap.recycle()
                ctx.runOnUiThread {
                    ctx.dismissLittleLoading()
                }
            }


        } catch (e: Exception) {
            QLogger.e(e)
        }
    }


    /**
     * 分享文件
     * @param ctx Activity
     * @param file File?
     * @param shareByApp String
     */
    fun shareFile(ctx: Activity, file: File?, shareByApp: String) {
        if (file == null) {
            return
        }
        ctx.showLittleLoading()
        try {
            // QTIP: 2018/12/27 MediaStore.Images.Media.insertImage 的图片是image/jpeg 不带透明通道
            thread(start = true) {
                //val imageUrl = MediaStore.Images.Media.insertImage(ctx.contentResolver, bitmap, getString(app_name), "")
                val filePath =
                    FileOpUtils.getShareDirectory() + File.separator + "Share" + System.currentTimeMillis() + file?.extension
                QLogger.i("share file path=== $filePath")

                val intent = Intent(Intent.ACTION_SEND)
                //intent.putExtra(Intent.EXTRA_SUBJECT, "")
                //intent.putExtra(Intent.EXTRA_TEXT, ResUtils.getString(R.string.share))

                val fileExtensionFromUrl = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                        ?: ""
                ) ?: "file/*"
                intent.type = fileExtensionFromUrl
                if (Build.VERSION.SDK_INT >= 24) {
                    // Provider分享
                    val uriForFile = FileProvider.getUriForFile(
                        ApplicationProxy.getApp(),
                        ApplicationProxy.getApp().packageName + ".FileProvider", file
                    )
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_STREAM, uriForFile)
                } else {
                    // 普通分享
                    val uri = Uri.fromFile(file)
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                //指定app分享
                if (!TextUtils.isEmpty(shareByApp)) {
                    intent.setPackage(shareByApp)
                }
                ctx.startActivity(intent)
            }
            ctx.runOnUiThread {
                ctx.dismissLittleLoading()
            }


        } catch (e: Exception) {
            QLogger.e(e)
        }
    }


    /**
     * 分享一个指定xml布局 action 回调中对布局初始化
     * @param activity Activity
     * @param layoutId Int
     * @param shareByApp String
     * @param action Function1<[@kotlin.ParameterName] View, Unit>
     */
    fun shareLayout(
        activity: Activity, @LayoutRes layoutId: Int,
        shareByApp: String,
        action: (inflateView: View) -> Unit
    ) {

        val bitmap = ViewHelper.inflateLayoutAndGetBitmap(activity, layoutId, action)
        shareImage(activity, bitmap, shareByApp)
    }

    /**
     * 分享文本
     *
     * @param context
     * @param text
     */
    fun shareText(context: Context, subject: String? = null, text: String, url: String) {

        // QTIP: 2019-03-12 Facebook 分享文字 问题 https://stackoverflow.com/questions/34618514/share-text-via-intent-on-facebook-without-using-facebook-sdk/35102802
        //This will work
        //
        //Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        //sharingIntent.setType("text/plain");
        //sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://www.google.com");
        //startActivity(Intent.createChooser(sharingIntent, "Share via"));
        //This will not work.
        //
        //Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        //sharingIntent.setType("text/plain");
        //sharingIntent.putExtra(Intent.EXTRA_TEXT, "Share Text from App");
        //startActivity(Intent.createChooser(sharingIntent, "Share via"));


        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(Intent.EXTRA_TITLE, url)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject ?: ResUtils.getString(R.string.share))
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"

        val sendIntent = Intent.createChooser(intent, ResUtils.getString(R.string.share_by))
        sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            QLogger.e(e)
        }

    }

    private fun getSharePicApps(context: Context): MutableList<ResolveInfo>? {
        val packageManager = context.packageManager
        packageManager?.let {
            val intent = Intent(Intent.ACTION_SEND, null)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.type = "image/*"
            return it.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
        }
        return null
    }


}
