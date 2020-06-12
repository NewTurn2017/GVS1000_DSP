package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.util

import android.content.Intent
import android.net.Uri
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.sInstance
import java.io.File
import java.io.IOException

class GVPath {


    private val mRecodeFilePath: String =
            android.os.Environment.getExternalStorageDirectory().absolutePath
                    .toString() + "/" + sInstance.resources.getString(
                    com.gvkorea.gvs1000_dsp.R.string.app_name) + "/"


    fun checkDownloadFolder() {
        val path = mRecodeFilePath
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        sInstance.sendBroadcast(intent)
    }

    fun getNewFilePath(): String? {
//        val path =
//            mRecodeFilePath + SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())
//                .toString() + ".pcm"

        val path = "${mRecodeFilePath}rt.pcm"
        val file = File(path)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        sInstance.sendBroadcast(intent)
        return path
    }

    fun getWavFilePath(): String? {
        val path = "${mRecodeFilePath}rt.wav"
        val file = File(path)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        sInstance.sendBroadcast(intent)
        return path
    }

    fun getRecodeFileArray(): Array<File?>? {
        val directory = File(mRecodeFilePath)
        return directory.listFiles()
    }
}