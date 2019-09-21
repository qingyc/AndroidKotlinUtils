package com.qing.androidkotlinutils.core.utils

import android.Manifest
import android.graphics.Bitmap
import com.blankj.utilcode.util.PermissionUtils
import com.qing.androidkotlinutils.core.R
import com.qing.androidkotlinutils.core.base.BaseActivity
import com.qing.androidkotlinutils.core.ext.dismissLittleLoading
import com.qing.androidkotlinutils.core.ext.safeClose
import com.qing.androidkotlinutils.core.ui.dialog.CommonDialog
import com.qingyc.qlogger.QLogger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * 类说明: 文件保存
 *
 * @author qing
 * @time 2019-07-18 18:52
 */
object StorageUtil {

    fun saveWithRequestStoragePermission(activity: BaseActivity, bitmap: Bitmap?, path: String = FileOpUtils.getNoMediaPictureDirectory(), fileSuffix:String=FileOpUtils.PNG_FILE_SUFFIX, action: (success: Boolean, outputFilePath: String, error: Throwable?) -> Any) {
        if (bitmap == null) {
            action.invoke(false, "", null)
            activity?.dismissLittleLoading()
            return
        }
        var disposable: Disposable? = null
        disposable = RxPermissions(activity)
                .requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .compose(activity.bindToLifecycle())
                .subscribe {
                    it?.let {
                        when {
                            it.granted -> {
                                bitmap2File(bitmap, path,fileSuffix , action)
                                disposable?.dispose()
                            }
                            //权限被拒
                            it.shouldShowRequestPermissionRationale -> {
                                disposable?.dispose()
                                activity?.dismissLittleLoading()
                            }
                            //永久被拒
                            else -> {
                                val dialog = CommonDialog.newInstance(ResUtils.getString(R.string.tip_storage_open), ResUtils.getString(R.string.allow))
                                dialog.setOnClickListener(object : CommonDialog.OnclickListener {
                                    override fun onclick(isConfirm: Boolean) {
                                        if (isConfirm) {
                                            PermissionUtils.launchAppDetailsSettings()
                                        }
                                    }
                                })
                                dialog.show(activity.supportFragmentManager, "")
                                disposable?.dispose()
                                activity?.dismissLittleLoading()

                            }
                        }
                    }
                }

    }

    fun bitmap2File(bitmap: Bitmap, path: String = FileOpUtils.getNoMediaPictureDirectory(), fileSuffix:String=FileOpUtils.PNG_FILE_SUFFIX, action: (success: Boolean, outputFilePath: String, error: Throwable?) -> Any) {
        //文件保存的时间
        val date = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date(System.currentTimeMillis()))
        //val fileName = date + FileOpUtils.PNG_FILE_SUFFIX
        val fileName = date + fileSuffix
        val outputFilePath = path + File.separator + fileName
        val outputFile = File(outputFilePath)

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(outputFile)
            val compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            if (compress) {
                fos?.flush()
            }
            //刷新相册
            ScanHelper.refreshFileNow(outputFilePath)
            action.invoke(true, outputFilePath, null)
        } catch (e: Exception) {
            action.invoke(false, outputFilePath, e)
            QLogger.e(e)
            fos.safeClose()
        }
    }
}

