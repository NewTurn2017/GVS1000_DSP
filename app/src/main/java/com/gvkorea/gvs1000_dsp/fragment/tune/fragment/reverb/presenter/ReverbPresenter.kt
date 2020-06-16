package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.sInstance
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.reverbCount
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.util.GVAudioRecord
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.util.GVPath
import com.gvkorea.gvs1000_dsp.util.DSPMessage
import com.gvkorea.gvs1000_dsp.util.GVPacket
import kotlinx.android.synthetic.main.fragment_reverb.*
import kotlinx.android.synthetic.main.fragment_tune.*

class ReverbPresenter(val view: ReverbFragment, val handler: Handler) {
    var audioRecord: GVAudioRecord
    var rt60Arrays = ArrayList<Float>(5)
    val gvPacket = GVPacket(view)
    var nameList = ArrayList<String>()
    var play: MediaPlayer? = null

    init {
        val path =  GVPath()
        path.checkDownloadFolder()
        audioRecord = GVAudioRecord(path, this)
    }


    fun noiseClap() {

        startRecord()
        handler.postDelayed({
            clapPlay()
        },500)
        handler.postDelayed({
            play?.stop()
            play?.reset()
            if(play != null){
                play?.release()
                play = null
            }
        }, 2600)

    }

    private fun startRecord() {
        msg("측정을 시작합니다.")
        audioRecord.startRecord()
        handler.postDelayed({
            stopRecord()
        }, 4000)

    }
    fun stopRecord() {
        msg("잠시만 기다려 주세요...")
        audioRecord.stopRecord()
    }


    fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun caculateRT60() {
        reverbCount++
        if(!Python.isStarted()){
            Python.start(AndroidPlatform(view.context))
        }
        Environment.getExternalStorageDirectory().absolutePath
                .toString() + "/" + MainActivity.sInstance.resources.getString(
                R.string.app_name) + "/"
        val py = Python.getInstance()
        val pyf = py.getModule("reverberation")
        val wavPath = Environment.getExternalStorageDirectory().absolutePath + "/" + sInstance.resources.getString(R.string.app_name) + "/rt.wav"
        val graphPath = Environment.getExternalStorageDirectory().absolutePath + "/" + sInstance.resources.getString(R.string.app_name) + "/graph.png"
        val obj = pyf.callAttr("rt60", wavPath, graphPath)
        val arr = pyObjectToArray(obj.toString())
        view.iv_spectrogram.setImageResource(android.R.drawable.ic_delete)
        view.iv_spectrogram.setImageURI(Uri.parse(graphPath))
        view.iv_spectrogram.invalidate()
//        drawLineChart(arr)
        val reverbTime_500hz = arr[0]
        view.tv_reverb.text = "RT60: $reverbTime_500hz (sec)"
        rt60Arrays.add(reverbTime_500hz)
        var testResult = ""
        for(i in rt60Arrays.indices){
            testResult += "${i+1}회차 결과: ${rt60Arrays[i]} sec\n"
        }
        if(reverbCount < 5){
            view.tv_Reverb_result.text = testResult
            noiseClap()
        }else{
            val saveReverb = String.format("%.2f", rt60Arrays.average())
            testResult += "평균 : $saveReverb sec (저장됨)"
            view.tv_Reverb_result.text = testResult
            prefSetting.setReverbTimePref(view.sp_ReverbSpeakerList.selectedItem.toString(), saveReverb)
            rt60Arrays = ArrayList()
            impulseButtonEnable()
            mainButtonEnable()
            buttonEnable()

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
        play = MediaPlayer.create(view.context, R.raw.ir_clap)
        play?.start()
    }

    fun testReset() {
        view.tv_Reverb_result.text = "측정 결과"
        reverbCount = 0
        rt60Arrays = ArrayList()
        handler.removeMessages(0)
        impulseButtonEnable()
        buttonEnable()
        mainButtonEnable()
        eqReset()
    }

    fun eqReset() {

        val index = view.sp_ReverbSpeakerList.selectedItemPosition
        val socket = spkList[index].socket
        val channel = spkList[index].channel
        gvPacket.SendPacket_InputGEQ_Reset(socket, channel)

    }

    fun initializerList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_ReverbSpeakerList)
    }

    private fun makeNameList() {
        nameList = ArrayList()
        for (i in spkList.indices) {
            nameList.add(spkList[i].name)
        }
    }
    private fun registerAdapter(
            context: Context,
            list: ArrayList<String>,
            spinner: Spinner
    ) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        adapter.notifyDataSetChanged()
        spinner.adapter = adapter

    }

    fun updateDisplay() {
        val selectedItem = view.sp_ReverbSpeakerList.selectedItem.toString()
        val reverbTime =  "스피커 : $selectedItem 잔향시간: ${prefSetting.getReverbTimePref(selectedItem)}"
        view.tv_Reverb_result.text = reverbTime
    }

    fun impulseButtonDisenable() {
        view.btn_noiseClap.isEnabled = false
    }

    fun impulseButtonEnable(){
        view.btn_noiseClap.isEnabled = true
        view.btn_noiseClap.alpha = 1f
    }

    fun mainButtonDisable() {
        msg(view.context!!.getString(R.string.waiting))
        val m = Message()
        m.what = DSPMessage.MSG_UI_UNTOUCH.value
        handler.sendMessage(m)
    }

    fun mainButtonEnable() {
        val m = Message()
        m.what = DSPMessage.MSG_UI_TOUCH.value
        handler.sendMessage(m)
    }
    fun buttonEnable() {
        view.parentFragment?.btn_calibration?.isEnabled = true
        view.parentFragment?.btn_calibration?.alpha = 1f
        view.parentFragment?.btn_autoTune?.isEnabled = true
        view.parentFragment?.btn_autoTune?.alpha = 1f
        view.parentFragment?.btn_reverb?.isEnabled = true
        view.parentFragment?.btn_reverb?.alpha = 1f
        view.parentFragment?.btn_evalueation?.isEnabled = true
        view.parentFragment?.btn_evalueation?.alpha = 1f
    }

    fun buttonDisable() {
        view.parentFragment?.btn_calibration?.isEnabled = false
        view.parentFragment?.btn_autoTune?.isEnabled = false
        view.parentFragment?.btn_reverb?.isEnabled = false
        view.parentFragment?.btn_evalueation?.isEnabled = false
    }


}