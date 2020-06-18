package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.presenter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.storage.StorageReference
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.barChart
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.curModelPath
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.initialValues
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.isShowEQ
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.isShowTable
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.lineChart
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.noiseVolume
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.storageRef
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.targetValues
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.targetdB
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment.Companion.tuningCounter
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.ann.ANN_Closed
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.avgStart
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq10Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq11Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq12Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq13Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq14Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq15Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq16Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq17Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq18Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq19Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq1Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq20Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq21Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq22Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq23Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq24Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq25Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq26Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq27Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq28Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq29Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq2Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq30Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq31Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq3Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq4Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq5Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq6Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq7Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq8Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freq9Sum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.freqSum
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.isMeasure
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune.Companion.spldB
import com.gvkorea.gvs1000_dsp.util.*
import kotlinx.android.synthetic.main.dialog_target_volume.*
import kotlinx.android.synthetic.main.fragment_auto_tune.*
import kotlinx.android.synthetic.main.fragment_tune.*
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt


class AutoTunePresenter(val view: AutoTuneFragment, val helper: Helper, val mHandler: Handler) {

    var curEQ = IntArray(31)
    val packet = GVPacket(view)

    val NOISE_OFF = 3
    val PINK = 2

    val SWITCH_ON = 1
    val SWITCH_OFF = 0

    var nameList = ArrayList<String>()
    lateinit var pathReference: StorageReference

    private val hzArrays = arrayOf(
            "20", "25", "31.5", "40", "50", "63", "80", "100", "125", "160",
            "200", "250", "315", "400", "500", "630", "800", "1000", "1250", "1600",
            "2000", "2500", "3150", "4000", "5000", "6300", "8000", "10000", "12500", "16000", "20000"
    )

    private fun tuneStart() {

        mainButtonDisable()
        buttonDisable()
        tuningStart()

        // val model = loadModel().model
        //  val file = view.context?.getExternalFilesDir(null)?.absolutePath + "/gvkorea/${model}/optimized_frozen_closed_model_75.pb"
        //   val filePath = File(file)
        //  val fileSize = filePath.length()
        //   if (filePath.exists() && fileSize > 0) {
        //        curModelPath = file
        //        tuningStart()
        //     } else {
        //         pathReference = storageRef.child("models/${model}/optimized_frozen_closed_model_75.pb")
        //          saveModelFromFireBaseStoreage(pathReference, model)
        //    }

    }

    private fun tuningStart() {
        adjustVolumeStart()
        view.btn_tune_start.text = "튜닝 중.."
        view.btn_tune_stop.isEnabled = true
    }

    private fun saveModelFromFireBaseStorage(pathReference: StorageReference, model: String) {

        try {
            val folderPath = File(view.context?.getExternalFilesDir(null)?.absolutePath + "/gvkorea/${model}")
            val file = File(folderPath, "optimized_frozen_closed_model_75.pb")
            val filePath = view.context?.getExternalFilesDir(null)?.absolutePath + "/gvkorea/${model}/optimized_frozen_closed_model_75.pb"
            curModelPath = filePath
            try {
                if (!folderPath.exists()) {
                    folderPath.mkdir()
                }
                file.createNewFile()

                val fileDownloadTask = pathReference.getFile(file)

                helper.initProgressDialog(view.context!!)
                helper.mProgressDialog?.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                    fileDownloadTask.cancel()
                }
                helper.mProgressDialog?.show()

                fileDownloadTask.addOnSuccessListener {
                    helper.dismissProgressDialog()
                    msg("모델 다운로드 성공하였습니다")
                    tuningStart()

                }.addOnFailureListener {
                    helper.dismissProgressDialog()
                    msg("모델 다운로드 실패하였습니다. ${it.message}")
                    mainButtonEnable()
                    buttonEnable()
                }.addOnProgressListener {
                    val progress = ((100 * it.bytesTransferred) / it.totalByteCount).toInt()
                    helper.setProgress(progress)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun mainButtonDisable() {
        msg(view.context!!.getString(R.string.waiting))
        val m = Message()
        m.what = DSPMessage.MSG_UI_UNTOUCH.value
        mHandler.sendMessage(m)
    }

    private fun mainButtonEnable() {
        val m = Message()
        m.what = DSPMessage.MSG_UI_TOUCH.value
        mHandler.sendMessage(m)
    }
    private fun buttonEnable() {
        view.parentFragment?.btn_calibration?.isEnabled = true
        view.parentFragment?.btn_calibration?.alpha = 1f
        view.parentFragment?.btn_autoTune?.isEnabled = true
        view.parentFragment?.btn_autoTune?.alpha = 1f
        view.parentFragment?.btn_reverb?.isEnabled = true
        view.parentFragment?.btn_reverb?.alpha = 1f
        view.parentFragment?.btn_evalueation?.isEnabled = true
        view.parentFragment?.btn_evalueation?.alpha = 1f
    }

    private fun buttonDisable() {
        view.parentFragment?.btn_calibration?.isEnabled = false
        view.parentFragment?.btn_autoTune?.isEnabled = false
        view.parentFragment?.btn_reverb?.isEnabled = false
        view.parentFragment?.btn_evalueation?.isEnabled = false
    }

    fun adjustVolumeStart() {
        val calib = prefSetting.loadIsCalib()
        if (calib) {
            msg("음압 셋팅 중입니다...")
            view.btn_tune_start.text = "진행 중..."
            view.btn_tune_start.isEnabled = false
            noiseVolume = -35
            mHandler.postDelayed({
                eqReset()
            }, 200)
            mHandler.postDelayed({
                NoiseVolumeControl(noiseVolume)
            }, 500)
        } else {
            msg("마이크 캘리브레이션이 되지 않았습니다. 캘리브레이션 후 튜닝바랍니다.")
        }
    }

    fun eqReset() {
        val spk = readSpeakerInfo()
        packet.SendPacket_InputGEQ_Reset(spk.socket, spk.channel)

    }

    private fun NoiseVolumeControl(progress: Int) {

        noise(PINK, progress)
        mHandler.postDelayed({
            if (spldB.toInt() < targetdB) {
                noiseVolume++
                if (noiseVolume < 10) {
                    NoiseVolumeControl(noiseVolume)
                } else {
                    mainButtonEnable()
                    buttonEnable()
                    msg("마이크 캘리브레이션을 확인 바랍니다.(초기화 -> 캘리브레이션)")
                    view.btn_tune_start.text = "시작"
                    mHandler.removeMessages(0)
                    noise(NOISE_OFF, noiseVolume)
                }
            } else {
                prefSetting.saveNoiseVolumePref(noiseVolume.toFloat())
                msg("읍압 셋팅이 완료되었습니다.")
//                noise(NOISE_OFF, noiseVolume)
                view.btn_tune_start.isEnabled = true
                view.btn_tune_start.alpha = 1f
                autoTuning()
            }
        }, 500)
    }

    fun noise(noise: Int, progress: Int) {

        val spk = readSpeakerInfo()
        val gain = progress.toFloat()
        if (noise != NOISE_OFF) {
            packet.SendPacket_NoiseGenerator(spk.socket, spk.channel, noise, gain, SWITCH_ON)
        } else {
            packet.SendPacket_NoiseGenerator(spk.socket, spk.channel, PINK, gain, SWITCH_OFF)
        }
    }

    fun setTargetVolumeDialog() {
        val innerView = View.inflate(view.context, R.layout.dialog_target_volume, null)
        val dialog = Dialog(view.context!!)
        dialog.setContentView(innerView)
        dialog.setCancelable(false)
        dialog.btn_dialog_tune_start.setOnClickListener {
            targetdB = dialog.sp_target_volume.selectedItem.toString().toDouble()
            view.init_lineChart()
            tuneStart()
            dialog.dismiss()
        }
        dialog.btn_dialog_tune_stop.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun autoTuning() {
        msg("서버로부터 모델을 다운로드 중입니다...")
        curEQReset()
        average()
//        handler.postDelayed({
//            val open = ANN_Open(view.activity?.assets!!, targetValues!!)
//            val eqValues = open.getControlEQ_Open()
//            val eqFloats = floatToInt(eqValues)
//            val spk = readSpeakerInfo()
//            curEQ = eqFloats
//            val eqFloatArrays = changeEQValues(curEQ)
//            if(loadModel().model == "GVS-200A"){
//                packet.SendPacket_EQ_All(spk.socket, packet.CMD_PARA2_CH1, eqFloatArrays)
//                packet.SendPacket_EQ_All(spk.socket, packet.CMD_PARA2_CH2, eqFloatArrays)
//            }else{
//                packet.SendPacket_EQ_All(spk.socket, spk.channel, eqFloatArrays)
//            }
//
//        }, 1500)

//        handler.postDelayed({
////            average()
//        }, 2000)

        mHandler.postDelayed({
            initialValues = FloatArray(31)

            for(i in initialValues!!.indices){
                initialValues!![i] = freqSum[i].toFloat()
            }
            lineChart.drawGraph(freqSum, "현재 측정값(dB)", Color.RED)
        }, 2300)
        mHandler.postDelayed({
            tuningCounter = 0
            ANN_ClosedLoop_repeat()
        }, 2500)
    }

    private fun curEQReset() {
        for (i in curEQ.indices) {
            curEQ[i] = 30
        }
    }

    fun average() {
        measure(true)
//        WaitingDialog(view.context!!).create("평균 측정 중입니다..", 1000)
        mHandler.postDelayed({
            measure(false)
        }, 1600)
        mHandler.postDelayed({
//            for (i in freqSum.indices) {
//                if (i < 6) {
//                    targetValues!![i] = freqSum[i].toFloat()
//                }
//                if(i == 30){
//                    targetValues!![i] = freqSum[i].toFloat()
//                }
//            }

            if(initialValues != null){
                lineChart.drawGraph(freqSum, "현재 측정값(dB)", Color.RED)
                barChart.initGraph(changeEQValues(curEQ))
                updateTableList()
            }

//            msg("측정 완료")
        }, 1800)
    }

    private fun floatToInt(results: FloatArray): IntArray {
        val resultsToInt = IntArray(31)
        for (i in results.indices) {
            if (results[i] >= 60) {
                resultsToInt[i] = 60
            } else if (results[i] <= 0) {
                resultsToInt[i] = 0
            } else {
                resultsToInt[i] = results[i].roundToInt()
            }
        }
        return resultsToInt
    }

    private fun changeEQValues(eqValues: IntArray): FloatArray {
        val eqFloatArray = FloatArray(eqValues.size)
        for (i in eqValues.indices) {
            eqFloatArray[i] = eqValues[i] * 0.5f - 15
        }
        return eqFloatArray
    }

    private fun ANN_ClosedLoop_repeat() {
        ANN_ClosedLoop()
        var isCompleted = true
        tuningCounter++
        val diff = FloatArray(31)
        var count = 0
        mHandler.postDelayed({
            average()
        }, 200)
        mHandler.postDelayed({

            for (i in freqSum.indices) {
                diff[i] = freqSum[i].toFloat() - targetValues!![i]
            }
            for (i in diff.indices) {
                if (diff[i] > 2 || diff[i] < -2) {
                    count++
                }
            }
            if (count > 0) {
                isCompleted = false
            }

        }, 2100)

        mHandler.postDelayed({
            if (isCompleted) {
                msg("튜닝이 완료되었습니다.")
                tuneStop()
                mainButtonEnable()
                buttonEnable()
                savePreset()
            } else if (tuningCounter > 20) {
                msg("튜닝이 완료되지 않았습니다. 다시 진행바랍니다.")
                tuneStop()
                mainButtonEnable()
                buttonEnable()
            } else {
                msg("오차 범위 초과 갯수: $count " + "\n" +
                        "${tuningCounter}번 반복튜닝 중..")
                ANN_ClosedLoop_repeat()
            }
        }, 2300)
    }

    private fun savePreset() {
        val title = "PRESET01"
        val presetNo = 0
        val spk = readSpeakerInfo()
        packet.savePreset(spk.socket, presetNo, title)
    }


    private fun ANN_ClosedLoop() {
        val curRms = convertStringToFloat(freqSum)
        val closed = ANN_Closed(curEQ, curRms, targetValues!!, view.activity?.assets!!)
        curEQ = closed.getControlEQ_Closed()
        val spk = readSpeakerInfo()
        val eqFloatArrays = changeEQValues(curEQ)

        if (loadModel().model == "GVS-200A") {
            packet.SendPacket_EQ_All(spk.socket, packet.CMD_PARA2_CH1, eqFloatArrays)
            packet.SendPacket_EQ_All(spk.socket, packet.CMD_PARA2_CH2, eqFloatArrays)
        } else {
            packet.SendPacket_EQ_All(spk.socket, spk.channel, eqFloatArrays)
        }
    }

    private fun convertStringToFloat(freqSum: java.util.ArrayList<String>): FloatArray {
        val curRms = FloatArray(31)
        for (i in freqSum.indices) {
            curRms[i] = freqSum[i].toFloat()
        }
        return curRms
    }

    fun measure(isStart: Boolean) {
        if (isStart) {
            mHandler.postDelayed({
                freq1Sum = ArrayList()
                freq2Sum = ArrayList()
                freq3Sum = ArrayList()
                freq4Sum = ArrayList()
                freq5Sum = ArrayList()
                freq6Sum = ArrayList()
                freq7Sum = ArrayList()
                freq8Sum = ArrayList()
                freq9Sum = ArrayList()
                freq10Sum = ArrayList()
                freq11Sum = ArrayList()
                freq12Sum = ArrayList()
                freq13Sum = ArrayList()
                freq14Sum = ArrayList()
                freq15Sum = ArrayList()
                freq16Sum = ArrayList()
                freq17Sum = ArrayList()
                freq18Sum = ArrayList()
                freq19Sum = ArrayList()
                freq20Sum = ArrayList()
                freq21Sum = ArrayList()
                freq22Sum = ArrayList()
                freq23Sum = ArrayList()
                freq24Sum = ArrayList()
                freq25Sum = ArrayList()
                freq26Sum = ArrayList()
                freq27Sum = ArrayList()
                freq28Sum = ArrayList()
                freq29Sum = ArrayList()
                freq30Sum = ArrayList()
                freq31Sum = ArrayList()
                freqSum = ArrayList()
                avgStart = true
                isMeasure = true
            }, 100)
        } else {
            avgStart = true
            isMeasure = false
        }
    }

    private fun updateTableList() {
        if (freqSum.size > 0) {
            var freq = "Freq\n"
            var target = "Target\n"
            val diff = "Diff\n"
            var curRms = "result\n"
            val builder = SpannableStringBuilder(diff)
            for (i in 0 until 31) {
                freq += hzArrays[i] + "\n"
                target += targetValues!![i].toString() + "\n"
                curRms += freqSum[i] + "\n"
                val diffFloat = targetValues!![i] - freqSum[i].toFloat()
                if (diffFloat > 2 || diffFloat < -2) {
                    val str = "${String.format("%.2f", diffFloat)}\n"
                    val temp = SpannableStringBuilder(str)
                    temp.setSpan(
                            ForegroundColorSpan(Color.RED),
                            0,
                            str.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.append(temp)
                } else {
                    builder.append("${String.format("%.2f", diffFloat)}\n")
                }
            }

            view.tv_tune_curFreq.text = freq
            view.tv_tune_diff.text = builder
            view.tv_tune_target.text = target
            view.tv_tune_result.text = curRms
        } else {
            msg("데이터가 없습니다.")
        }
    }

    fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun tuneStop() {
        noise(NOISE_OFF, noiseVolume)
        mHandler.removeMessages(0)
        view.btn_tune_start.text = "시작"
        view.btn_tune_start.isEnabled = true
        view.btn_tune_start.alpha = 1f

        mainButtonEnable()
        buttonEnable()
    }

    fun showTable() {
        if (isShowTable) {
            view.btn_showTable.text = view.context!!.getString(R.string.show_table)
            view.sc_table.visibility = View.GONE
            isShowTable = false
        } else {
            view.btn_showTable.text = view.context!!.getString(R.string.close_table)
            view.sc_table.visibility = View.VISIBLE
            isShowTable = true
        }
    }

    fun showEQ() {
        if (isShowEQ) {
            view.btn_showEQ.text = view.context!!.getString(R.string.show_eq)
            view.chart_tune_bar.visibility = View.GONE
            isShowEQ = false
        } else {
            view.btn_showEQ.text = view.context!!.getString(R.string.close_eq)
            view.chart_tune_bar.visibility = View.VISIBLE
            isShowEQ = true
        }
    }

    fun setTarget(targetdB: Int): FloatArray? {
        val target = targetdB.toFloat()
        val targetdBs = FloatArray(31)
        targetdBs[0] = target - 30
        targetdBs[1] = target - 31
        targetdBs[2] = target - 29
        targetdBs[3] = target - 21
        targetdBs[4] = target - 11
        targetdBs[5] = target - 13
        targetdBs[6] = target - 8
        targetdBs[7] = target
        targetdBs[8] = target
        targetdBs[9] = target
        targetdBs[10] = target
        targetdBs[11] = target
        targetdBs[12] = target
        targetdBs[13] = target
        targetdBs[14] = target
        targetdBs[15] = target
        targetdBs[16] = target
        targetdBs[17] = target
        targetdBs[18] = target
        targetdBs[19] = target
        targetdBs[20] = target
        targetdBs[21] = target
        targetdBs[22] = target
        targetdBs[23] = target
        targetdBs[24] = target
        targetdBs[25] = target
        targetdBs[26] = target
        targetdBs[27] = target
        targetdBs[28] = target
        targetdBs[29] = target
        targetdBs[30] = target
        return targetdBs
    }

    fun initializerList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_TuneSpeakerList)
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

    fun updateModel() {
        val spkName = view.sp_TuneSpeakerList.selectedItem.toString()
        val settingData = loadModel()
        val reverbTime = prefSetting.getReverbTimePref(spkName)

        val data = "MODEL: ${settingData.model}\nNAME: ${spkName}\nREVERB_TIME: $reverbTime"
        view.tv_tuneModel.text = data

    }

    fun loadModel(): SettingData {
        val spkName = view.sp_TuneSpeakerList.selectedItem.toString()
        val speakerId = loadIdFromName(spkName).toString()
        return prefSetting.loadSpeakerSettings(speakerId)
    }

    private fun loadIdFromName(spkNo: String): Int {
        val id: Int
        if (spkNo.contains("Main")) {
            id = spkNo.substring(4, 5).toInt()
        } else {
            id = spkNo.substring(3, 4).toInt()
        }
        return id
    }

    private fun readSpeakerInfo(): SpeakerInfo {
        val index = view.sp_TuneSpeakerList.selectedItemPosition
        return SpeakerInfo(spkList[index].socket, spkList[index].channel, spkList[index].name)
    }


}