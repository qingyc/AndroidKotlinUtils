package com.qing.androidkotlinutils.core.helper

import android.content.Context
import android.content.pm.PackageManager

/**
 * 
 * 类说明: 清单帮助
 *
 * @author qing
 * @time 2019-08-13 15:06
 */
object ManifestHelper {

    /**
     * Check if the app requests a specific permission in the manifest.
     *
     * @param permissionName the permission to check
     * @return true -> the permission in requested in manifest, false -> not.
     */
    fun hasPermissionInManifest(
            context: Context, permissionName: String
    ): Boolean {
        val packageName = context.packageName
        try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val declaredPermissions = packageInfo.requestedPermissions
            if (declaredPermissions != null && declaredPermissions.isNotEmpty()) {
                for (p in declaredPermissions) {
                    if (p.equals(permissionName, ignoreCase = true)) {
                        return true
                    }
                }
            }
            // } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: Exception) {
        }

        return false
    }

    /**
     * 获取Application中的metadata参数
     *
     * @param ctx          Context
     * @param key          metadata的name属性
     * @param defaultValue 默认值，获取失败或者未配置metadata返回时候
     * @return Value
     */
    fun getMetadata(ctx: Context, key: String, defaultValue: Any): Any {
        try {
            val info = ctx.packageManager
                    .getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
            val value = info.metaData.get(key)
            return value ?: defaultValue
        } catch (e: Exception) {
            //
        }

        return defaultValue
    }
}