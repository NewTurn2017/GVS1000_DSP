package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.sInstance
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.arrList
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.chart
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.isRepeat
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.labelList
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.repeatCount
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.valuesArrays
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.util.GVAudioRecord
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.util.GVPath
import kotlinx.android.synthetic.main.fragment_reverb.*

class ReverbPresenter(val view: ReverbFragment, val handler: Handler) {
    var audioRecord: GVAudioRecord


    init {
        val path =  GVPath()
        path.checkDownloadFolder()
        audioRecord = GVAudioRecord(path, this)
    }


    fun playClap() {
        startRecord()
        handler.postDelayed({
            clapPlay()
        },200)
    }

    private fun startRecord() {
        msg("측정을 시작합니다.")
        audioRecord.startRecord()
        handler.postDelayed({
            stopRecord()
        }, 3000)
    }
    fun stopRecord() {
        msg("잠시만 기다려 주세요...")
        audioRecord.stopRecord()
    }

    fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun caculateRT60() {
        if(!Python.isStarted()){
            Python.start(AndroidPlatform(view.context))
        }
        Environment.getExternalStorageDirectory().absolutePath
                .toString() + "/" + sInstance.resources.getString(
                R.string.app_name) + "/"
        val py = Python.getInstance()
        val pyf = py.getModule("myscript")
        val wavPath = Environment.getExternalStorageDirectory().absolutePath + "/" + sInstance.resources.getString(R.string.app_name) + "/rt.wav"
        val graphPath = Environment.getExternalStorageDirectory().absolutePath + "/" + sInstance.resources.getString(R.string.app_name) + "/graph.png"
        val obj = pyf.callAttr("rt60", wavPath, graphPath)
        val arr = pyObjectToArray(obj.toString())
        view.iv_spectrogram.setImageResource(android.R.drawable.ic_delete)
        view.iv_spectrogram.setImageURI(Uri.parse(graphPath))
        view.iv_spectrogram.invalidate()
        drawLineChart(arr)
        val reverbTime_1khz = arr[3]
        view.tv_reverb.text = "RT60\n$reverbTime_1khz\n(sec)"
        prefSetting.setReverbTimePref(reverbTime_1khz.toString())
    }

    private fun drawLineChart(arr: FloatArray) {
        if(isRepeat){
            if(repeatCount == 0){
                arrList = ArrayList()
                valuesArrays = ArrayList()
                labelList = ArrayList()

            }
            repeatCount += 1
            arrList.add(arr)
            labelList.add("RT60($repeatCount)")
            chart.initGraphRepeat(arrList, labelList)

        }else{
            repeatCount = 0
            chart.initGraph(arr, "RT60(sec)", Color.BLUE)

        }
    }

    fun pyObjectToArray(objects: String): FloatArray{
        val arr2 = objects.split(" ").toMutableList()
        val rt60Arrays = FloatArray(arr2.size)
        for(i in arr2.indices){
            if(i== 0){
                rt60Arrays[i] = arr2[i].removePrefix("[").toFloat()
            }else if(i == 6){
                rt60Arrays[i] = arr2[i].removeSuffix("]").toFloat()
            }else{
                rt60Arrays[i] = arr2[i].toFloat()
            }
        }
        return rt60Arrays
    }

    fun clapPlay() {
        val afd = view.activity?.assets?.openFd("ir_clap.wav")
        val play = MediaPlayer()
        play.setDataSource(afd?.fileDescriptor, afd?.startOffset!!, afd.length)
        play.prepare()
        play.start()
    }
}