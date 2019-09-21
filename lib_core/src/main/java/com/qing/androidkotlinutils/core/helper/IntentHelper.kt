package com.qing.androidkotlinutils.core.helper

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.blankj.utilcode.util.AppUtils
import com.qing.androidkotlinutils.core.proxy.ApplicationProxy
import com.qingyc.qlogger.QLogger
import java.util.*

/**
 *
 * 类说明: intent使用
 *
 * @author qing
 * @time 2019-09-21 14:54
 */
object IntentHelper {

    /**
     * 浏览器打开url
     * @param context Context
     * @param url String
     */
    fun openBrowser(context: Context, url: String) {
        try {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            urlIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(urlIntent)
        } catch (e: Exception) {
            QLogger.e(e)
        }

    }


    /**
     * 发送邮件
     * @param email
     */
    fun sendSupportEmail(email: String) {
        try {
            // 收集用户基本信息
            val locale = Locale.getDefault()

            val defaultMsg = """
                model: ${Build.MODEL}
                version: ${Build.VERSION.RELEASE}
                country: ${locale.country}
                language:${locale.language}
                packageName:${AppUtils.getAppPackageName()}
                
            """
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "${AppUtils.getAppName()} -v ${AppUtils.getAppVersionName()}"
            )
            intent.putExtra(Intent.EXTRA_TEXT, defaultMsg)
            ApplicationProxy.getApp().startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 打开应用详情页
     *
     * @param activity
     * @param requestCode
     * @param packageName
     */
    fun openAppDetails(
        activity: Activity,
        requestCode: Int = -1,
        packageName: String = activity.packageName
    ) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.fromParts("package", packageName, null)
            if (requestCode != -1) {
                activity.startActivityForResult(intent, requestCode)
            } else {
                activity.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 检测是否开启usage(查看应用统计信息)权限
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isUsagePermissionOpen(context: Context): Boolean {

        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }


    /**
     * 打开usage(应用统计信息)设置页
     *
     * @param activity Activity
     * @return Boolean
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun openUsageSettingActivity(activity: Activity): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            activity.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * 无障碍服务是否启动
     *
     * @return boolean
     */
    fun isAccessibilityServiceEnable(accessibilityService: String): Boolean {
        return try {
            val services = Settings.Secure.getString(
                ApplicationProxy.getApp().contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            services != null && services.contains(accessibilityService)
            true
        } catch (e: Throwable) {
            false
        }
    }


    /**
     * 进入AccessibilitySettings(辅助服务)页面
     *
     * @param act Activity
     * @return 是否启动成功
     */
    fun openAccessibilitySetting(act: Activity): Boolean {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            act.startActivity(intent)
        } catch (e: Exception) {
            return false
        }
        return true
    }


    /**
     * 检测通知是否开启
     */
    fun isNotificationEnable(context: Context): Boolean {
        val managerCompat = NotificationManagerCompat.from(context)
        return managerCompat.areNotificationsEnabled()
    }

    /**
     * 调整通知设置
     */
    fun openNotificationSettings(context: Context) {

        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
            val uri = Uri.fromParts("package", context.applicationContext.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        } catch (e: Exception) {
            QLogger.e(e)
        }

    }


    /**
     * 打开输入法设置页
     * @param activity Activity
     */
    fun openInputMethodSetting(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: Exception) {
            QLogger.e(e)
        }
    }


    const val REQUEST_PICK_IMAGE = 1234

    /**
     * 打开相册
     * @param activity Activity
     */
    fun openPhotoPicker(activity: Activity) {
        try {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            activity.startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE)
        } catch (e: Exception) {
        }
    }


    /**
     * 打开googlePlay应用订阅页
     * @param activity
     * @param sku 商品id
     * @param packageName
     *
     * https://developer.android.com/distribute/marketing-tools/linking-to-google-play?hl=zh-CN
     */
    fun openVendingSubscription(
        activity: Activity,
        sku: String,
        packageName: String = ApplicationProxy.getApp().packageName
    ) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://play.google.com/store/account/subscriptions?sku=$sku&package=$packageName")
                setPackage("com.android.vending")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: Exception) {
            QLogger.e(e)
        }
    }

    /**
     * googlePlay中打开app
     */
    fun openAppInPlayStore(a: Context, packageName: String) {

        try {
            // 打开市场
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "market://details?id=$packageName"
                )
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            a.startActivity(intent)
        } catch (e: Exception) {
            openBrowser(a, "https://play.google.com/store/apps/details?id=$packageName")
        }
    }


}


