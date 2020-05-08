package com.gvkorea.gvs1000_dsp.presenter

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.arrGEQ
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.arrPEQ
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.muteArrays
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.otherClient
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.otherClientNo
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.sockets
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk1Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk2Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk3Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk4Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk5Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk6Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk7Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk8Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.volumeArrays
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listAllClient
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listAvailableId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedId
import com.gvkorea.gvs1000_dsp.util.*
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.activity_main.*
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainPresenter(val view: MainActivity, val handler: Handler) {

    val infoTextLineLimit = 200
    val gvPacket = GVMainPacket(view)
    private var isShowAdminBar = false
    val CMD_PARA2_CH1 = '1'
    val CMD_PARA2_CH2 = '2'
    val CMD_PARA2_CHA = 'A'
    val KEY_SOURCE_GAIN = "source_gain"
    val KEY_INPUT_GAIN = "input_gain"
    val KEY_INPUT_MUTE = "input_mute"
    val KEY_INPUT_DELAY = "input_delay"
    val KEY_INPUT_DELAY_BYPASS = "input_delay_bypass"
    val KEY_NOISE_GATE_TH = "noise_gate_th"
    val KEY_NOISE_GATE_RE = "noise_gate_re"
    val KEY_NOISE_GATE_ATT = "noise_gate_att"
    val KEY_NOISE_GATE_BYPASS = "noise_gate_bypass"
    val KEY_PHASE = "phase"
    val KEY_PEAKCOMP_TH = "peakcomp_th"
    val KEY_PEAKCOMP_RE = "peakcomp_re"
    val KEY_PEAKCOMP_HOLD = "peakcomp_hold"
    val KEY_PEAKCOMP_BYPASS = "peakcomp_bypass"
    val KEY_RMSCOMP_TH = "rmscomp_th"
    val KEY_RMSCOMP_RE = "rmscomp_re"
    val KEY_RMSCOMP_ATT = "rmscomp_att"
    val KEY_RMSCOMP_RATIO = "rmscomp_ratio"
    val KEY_RMSCOMP_KNEE = "rmscomp_knee"
    val KEY_RMSCOMP_BYPASS = "rmscomp_bypass"
    val KEY_OUTPUT_GAIN = "output_gain"
    val KEY_OUTPUT_MUTE = "output_mute"
    val KEY_LIMITER_TH = "limiter_th"
    val KEY_LIMITER_RE = "limiter_re"
    val KEY_LIMITER_ATT = "limiter_att"
    val KEY_LIMITER_RATIO = "limiter_ratio"
    val KEY_LIMITER_KNEE = "limiter_knee"
    val KEY_LIMITER_BYPASS = "limiter_bypass"
    val KEY_OUT_BPF_FILTERLOW_CH1 = "out_bpf_filterlow_ch1"
    val KEY_OUT_BPF_FILTERHIGH_CH1 = "out_bpf_filterhigh_ch1"
    val KEY_OUT_BPF_FREQ_LC_CH1 = "out_bpf_freq_lc_ch1"
    val KEY_OUT_BPF_FREQ_HC_CH1 = "out_bpf_freq_hc_ch1"
    val KEY_OUT_BPF_GAIN_CH1 = "out_bpf_gain_ch1"
    val KEY_OUT_BPF_FILTERLOW_CH2 = "out_bpf_filterlow_ch2"
    val KEY_OUT_BPF_FILTERHIGH_CH2 = "out_bpf_filterhigh_ch2"
    val KEY_OUT_BPF_FREQ_LC_CH2 = "out_bpf_freq_lc_ch2"
    val KEY_OUT_BPF_FREQ_HC_CH2 = "out_bpf_freq_hc_ch2"
    val KEY_OUT_BPF_GAIN_CH2 = "out_bpf_gain_ch2"
    val KEY_OUT_BPF_BYPASS = "out_bpf_bypass"
    val KEY_REVERB_BYPASS = "reverb_bypass"
    val KEY_REVERB_TIME = "reverb_time"
    val KEY_REVERB_HP = "reverb_hp"
    val KEY_REVERB_GAIN = "reverb_gain"
    val KEY_BRIDGE = "bridge"
    val KEY_GEQ_BYPASS = "geq_bypass"
    val KEY_PEQ_BYPASS = "peq_bypass"


    fun selectPannel(panel: GVSPanel) {
        resetButtonColor()
        when (panel) {
            GVSPanel.VOLUME -> replacePanel(view.mainFragment, view.btn_volumePannel)
            GVSPanel.EQ -> replacePanel(view.GEQFragment, view.btn_eqPannel)
            GVSPanel.MUSIC -> replacePanel(view.musicFragment, view.btn_musicPlayer)
            GVSPanel.SETTINGS -> replacePanel(view.settingsFragment, view.btn_settings)
        }
    }

    private fun replacePanel(fragment: Fragment, button: QButton) {
        view.replace(R.id.container, fragment)
        setButtonColor(button, android.R.color.holo_red_light)
    }

    fun setButtonColor(button: QButton, color: Int) {
        button.setTextColor(ContextCompat.getColor(view.applicationContext, color))
    }

    private fun resetButtonColor() {
        setButtonColor(view.btn_volumePannel, android.R.color.white)
        setButtonColor(view.btn_eqPannel, android.R.color.white)
        setButtonColor(view.btn_musicPlayer, android.R.color.white)
        setButtonColor(view.btn_settings, android.R.color.white)
    }

    fun appendText(msg: Message) {
        view.tv_received.append("${++MainActivity.no}. " + msg.obj as String + "(${getTime()})\n")
        view.scrollView_info.post {
            view.scrollView_info.fullScroll(View.FOCUS_DOWN)
        }
        if (MainActivity.no % infoTextLineLimit == 0) {
            view.tv_received.text = ""
        }
        if(!view.isButtonEnable){
            view.buttonEnable()
        }



    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val date = SimpleDateFormat("hh:mm:ss")
        return date.format(Date(System.currentTimeMillis()))
    }

    fun appendTextQuit(msg: Message) {
        view.tv_received.append("${++MainActivity.no}. " + msg.obj as String + "(${getTime()})\n")
        view.scrollView_info.post {
            view.scrollView_info.fullScroll(View.FOCUS_DOWN)
        }
        if (MainActivity.no % infoTextLineLimit == 0) {
            view.tv_received.text = ""
        }

    }

    fun hexToAscii(infoArray: List<String>): String {

        if (infoArray.size > 23) {
            val hexStr = findDeviceName(infoArray)
            val output = StringBuilder("")
            var i = 0
            while (i < hexStr.length) {
                val str = hexStr.substring(i, i + 2)
                output.append(str.toInt(16).toChar())
                i += 2
            }
            return output.toString()
        }
        return "0"
    }

    fun findDeviceName(infoArray: List<String>): String {
        var hexStr = ""
        for (i in 25..infoArray.size) {
            if (infoArray[i] != "0x00") {
                hexStr += infoArray[i].substring(2, 4)
            } else {
                break
            }
        }
        return hexStr
    }

    fun ipInfoText(msg: Message) {
        view.tv_ip_info.text = (msg.obj as String)
    }

    fun sync(msg: String) {

        if(arrGEQ.size == 0){
            resetGEQArray()
        }
        if(arrPEQ.size == 0){
            resetPEQArray()
        }
        received_sourceGain(msg)
        received_inputGain(msg)
        received_inputMute(msg)
        received_noiseGate(msg)
        received_phase(msg)
        received_inputDelay(msg)
        received_peakComp(msg)
        received_rmsComp(msg)
        received_outputGain(msg)
        received_outputMute(msg)
        received_limiter(msg)
        received_bpf_ch1(msg)
        received_bpf_ch2(msg)
        received_reverb(msg)
        received_bridge(msg)
        received_geq(msg)
        received_peq(msg)
        received_geq_bypass(msg)
        received_peq_bypass(msg)
    }

    private fun resetGEQArray(){
        arrGEQ = ArrayList()
        val resetData_geq = GEQData( 0f, false)
        for (i in 0..30){
            arrGEQ.add(resetData_geq)
        }
    }

    private fun resetPEQArray(){
        arrPEQ = ArrayList()
        val resetData_peq= PEQData( 100.0f, 0f, 0f, false)
        for (i in 0..30){
            arrPEQ.add(resetData_peq)
        }
    }

    fun received_sourceGain(receivedData: String) {

        if (receivedData.startsWith("0x0a 0x52 0x68")) {
            val arr = receivedData.split(" ")
            if (arr.size == 11) {
                val arr1 = arr[5].substring(2) + arr[6].substring(2) +
                        arr[7].substring(2) + arr[+8].substring(2)
                val sourceGain = hexStringToFloat(arr1)

                setupPrefFloat(KEY_SOURCE_GAIN, sourceGain)
            }

        }
    }

    fun hexStringToFloat(s: String): Float {
        val b = ByteArray(s.length / 2)
        for (i in b.indices) {
            val index = i * 2
            val v = s.substring(index, index + 2).toInt(16)
            b[i] = v.toByte()
        }
        return ByteBuffer.wrap(b).float
    }

    fun received_inputMute(receivedData: String) {

        if (receivedData.startsWith("0x05 0x52 0x74")) {
            val arr = receivedData.split(" ")
            if (arr.size == 6) {

                val arr1 = arr[4].substring(2).toInt()
                val isMute = arr1 != 0

                setupPrefBoolean(KEY_INPUT_MUTE, isMute)
                if(muteArrays == null) {
                    muteArrays = ArrayList()
                }else{
                    muteArrays?.add(isMute)
                }
            }

        }
    }

    fun received_inputDelay(receivedData: String) {

        if (receivedData.startsWith("0x09 0x52 0x64")) {
            val arr = receivedData.split(" ")
            if (arr.size == 10) {
                val arr1 = arr[5].substring(2) + arr[4].substring(2)
                val bypass = arr[8].substring(3).toInt() != 1
                val delayValue = Integer.parseInt(arr1, 16)

                setupPrefInt(KEY_INPUT_DELAY, delayValue)
                setupPrefBoolean(KEY_INPUT_DELAY_BYPASS, bypass)
            }
        }

    }

    private fun received_geq(receivedData: String) {
        if (receivedData.startsWith("0x9f 0x52 0x69")) {
            val arr = receivedData.split(" ")
            var arr1: String
            var arr2: Boolean
            var stepCounter = 1
            if (arr.size == 160) {
                for (i in 4 until 159 step 5) {
                    arr1 = arr[i].substring(2) + arr[i+1].substring(2) + arr[i+2].substring(2) + arr[i+3].substring(2)
                    arr2 = arr[i+4].substring(2).toInt() != 1
                    val gain = hexStringToFloat(arr1)
                    val bypass = arr2
                    arrGEQ[i-4*stepCounter] = GEQData(gain, bypass)
                    setupPrefFloat("geq_gain_eq${i-4*stepCounter}", arrGEQ[i-4*stepCounter].gain)
                    setupPrefBoolean("geq_bypass_eq${i-4*stepCounter}", arrGEQ[i-4*stepCounter].bypass)
                    stepCounter++
                }
            }
        }
    }

    private fun received_geq_bypass(receivedData: String){
        if (receivedData.startsWith("0x06 0x52 0x79")){
            val arr = receivedData.split(" ")
            if (arr.size == 7) {
                val arr1 = arr[4].substring(2).toInt() != 0
                val bypass = arr1

                setupPrefBoolean(KEY_GEQ_BYPASS, bypass)
            }
        }
    }

    private fun received_peq(receivedData: String) {
        if (receivedData.startsWith("0x17 0x52 0x50")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = ""
            var arr5 = false
            if (arr.size == 24) {
                for (i in arr.indices) {
                    if (i == 4) {
                        arr1 = arr[i].substring(2)
                    }
                    if (i in 6..9) {
                        arr2 += arr[i].substring(2)
                    }
                    if (i in 10..13) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i in 14..17) {
                        arr4 += arr[i].substring(2)
                    }
                    if (i == 22) {
                        arr5 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val filterNo = Integer.parseInt(arr1, 16)
            val freq = hexStringToFloat(arr2)
            val gain = hexStringToFloat(arr3)
            val qfactor = hexStringToFloat(arr4)
            val bypass = arr5

            arrPEQ[filterNo] = PEQData(freq, gain, qfactor, bypass)
            setupPrefFloat("peq_freq_eq$filterNo", arrPEQ[filterNo].freq)
            setupPrefFloat("peq_gain_eq$filterNo", arrPEQ[filterNo].gain)
            setupPrefFloat("peq_q_eq$filterNo", arrPEQ[filterNo].qfactor)
            setupPrefBoolean("peq_bypass_eq$filterNo", arrPEQ[filterNo].bypass)
        }
    }

    private fun received_peq_bypass(receivedData: String){
        if (receivedData.startsWith("0x06 0x52 0x79")){
            val arr = receivedData.split(" ")
            if (arr.size == 7) {
                val arr1 = arr[5].substring(2).toInt() != 0
                val bypass = arr1

                setupPrefBoolean(KEY_PEQ_BYPASS, bypass)
            }
        }
    }

    fun received_bridge(receivedData: String) {
        if (receivedData.startsWith("0x06 0x52 0x62")) {
            val arr = receivedData.split(" ")
            if (arr.size == 7) {
                val arr1 = arr[4].substring(2).toInt()
                val bridge: String
                bridge = if (arr1 == 0) {
                    "2-WAY MODE"
                } else {
                    "BRIDGE MODE"
                }

                setupPrefString(KEY_BRIDGE, bridge)
            }

        }
    }

    fun received_reverb(receivedData: String) {
        if (receivedData.startsWith("0x08 0x52 0x56")) {
            val arr = receivedData.split(" ")
            if (arr.size == 9) {
                val arr1 = arr[4]
                val arr2 = arr[5]
                val arr3 = arr[6]
                val arr4 = arr[7]

                val bypass = arr1.substring(2).toInt() != 1
                val reverbTime = arr2.substring(2).toInt()
                val hpDamping = arr3.substring(2).toInt()
                val gain = arr4.substring(2).toInt()

                setupPrefBoolean(KEY_REVERB_BYPASS, bypass)
                setupPrefInt(KEY_REVERB_TIME, reverbTime)
                setupPrefInt(KEY_REVERB_HP, hpDamping)
                setupPrefInt(KEY_REVERB_GAIN, gain)
            }

        }
    }

    fun received_bpf_ch1(receivedData: String) {
        if (receivedData.startsWith("0x13 0x52 0x58 0x31")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = ""
            var arr5 = ""
            var arr6 = false
            if (arr.size == 20) {
                for (i in arr.indices) {
                    if (i == 4) {
                        arr1 = arr[i].substring(2)
                    }
                    if (i == 5) {
                        arr2 = arr[i].substring(2)
                    }
                    if (i in 6..9) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i in 10..13) {
                        arr4 += arr[i].substring(2)
                    }
                    if (i in 14..17) {
                        arr5 += arr[i].substring(2)
                    }
                    if (i == 18) {
                        arr6 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val filterLow = intToFilterType(Integer.parseInt(arr1, 16))
            val filterHigh = intToFilterType(Integer.parseInt(arr2, 16))
            val freqLowCutoff = hexStringToFloat(arr3)
            val freqHighCutoff = hexStringToFloat(arr4)
            val gain = hexStringToFloat(arr5)
            val bypass = arr6

            setupPrefString(KEY_OUT_BPF_FILTERLOW_CH1, filterLow)
            setupPrefString(KEY_OUT_BPF_FILTERHIGH_CH1, filterHigh)
            setupPrefFloat(KEY_OUT_BPF_FREQ_LC_CH1, freqLowCutoff)
            setupPrefFloat(KEY_OUT_BPF_FREQ_HC_CH1, freqHighCutoff)
            setupPrefFloat(KEY_OUT_BPF_GAIN_CH1, gain)
            setupPrefBoolean(KEY_OUT_BPF_BYPASS, bypass)

        }
    }

    fun received_bpf_ch2(receivedData: String) {
        if (receivedData.startsWith("0x13 0x52 0x58 0x32")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = ""
            var arr5 = ""
            var arr6 = false
            if (arr.size == 20) {
                for (i in arr.indices) {
                    if (i == 4) {
                        arr1 = arr[i].substring(2)
                    }
                    if (i == 5) {
                        arr2 = arr[i].substring(2)
                    }
                    if (i in 6..9) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i in 10..13) {
                        arr4 += arr[i].substring(2)
                    }
                    if (i in 14..17) {
                        arr5 += arr[i].substring(2)
                    }
                    if (i == 18) {
                        arr6 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val filterLow = intToFilterType(Integer.parseInt(arr1, 16))
            val filterHigh = intToFilterType(Integer.parseInt(arr2, 16))
            val freqLowCutoff = hexStringToFloat(arr3)
            val freqHighCutoff = hexStringToFloat(arr4)
            val gain = hexStringToFloat(arr5)
            val bypass = arr6

            setupPrefString(KEY_OUT_BPF_FILTERLOW_CH2, filterLow)
            setupPrefString(KEY_OUT_BPF_FILTERHIGH_CH2, filterHigh)
            setupPrefFloat(KEY_OUT_BPF_FREQ_LC_CH2, freqLowCutoff)
            setupPrefFloat(KEY_OUT_BPF_FREQ_HC_CH2, freqHighCutoff)
            setupPrefFloat(KEY_OUT_BPF_GAIN_CH2, gain)
            setupPrefBoolean(KEY_OUT_BPF_BYPASS, bypass)

        }
    }

    private fun intToFilterType(value: Int): String {
        var filterType = ""
        when (value) {
            DSPFilterType.DSP_FILTER_LR12.value -> filterType = "LR12"
            DSPFilterType.DSP_FILTER_LR24.value -> filterType = "LR24"
            DSPFilterType.DSP_FILTER_LR36.value -> filterType = "LR36"
            DSPFilterType.DSP_FILTER_LR48.value -> filterType = "LR48"
            DSPFilterType.DSP_FILTER_BW12.value -> filterType = "BW12"
            DSPFilterType.DSP_FILTER_BW24.value -> filterType = "BW24"
            DSPFilterType.DSP_FILTER_BW36.value -> filterType = "BW36"
            DSPFilterType.DSP_FILTER_BW48.value -> filterType = "BW48"
            DSPFilterType.DSP_FILTER_BS12.value -> filterType = "BS12"
            DSPFilterType.DSP_FILTER_BS24.value -> filterType = "BS24"
            DSPFilterType.DSP_FILTER_BS36.value -> filterType = "BS36"
            DSPFilterType.DSP_FILTER_BS48.value -> filterType = "BS48"
            DSPFilterType.DSP_FILTER_NONE.value -> filterType = "NONE"
        }
        return filterType
    }


    fun received_limiter(receivedData: String) {
        if (receivedData.startsWith("0x13 0x52 0x4c")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = ""
            var arr5 = ""
            var arr6 = false
            if (arr.size == 20) {
                for (i in arr.indices) {
                    if (i in 4..7) {
                        arr1 += arr[i].substring(2)
                    }
                    if (i in 8..11) {
                        arr2 += arr[i].substring(2)
                    }
                    if (i in 12..15) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i == 16) {
                        arr4 = arr[i].substring(2)
                    }
                    if (i == 17) {
                        arr5 = arr[i].substring(2)
                    }
                    if (i == 18) {
                        arr6 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val th = hexStringToFloat(arr1)
            val re = hexStringToFloat(arr2)
            val att = hexStringToFloat(arr3)
            val ratio = Integer.parseInt(arr4, 16)
            val knee = Integer.parseInt(arr5, 16)
            val bypass = arr6

            setupPrefFloat(KEY_LIMITER_TH, th)
            setupPrefFloat(KEY_LIMITER_RE, re)
            setupPrefFloat(KEY_LIMITER_ATT, att)
            setupPrefInt(KEY_LIMITER_RATIO, ratio)
            setupPrefInt(KEY_LIMITER_KNEE, knee)
            setupPrefBoolean(KEY_LIMITER_BYPASS, bypass)

        }
    }

    fun received_outputMute(receivedData: String) {

        if (receivedData.startsWith("0x05 0x52 0x54")) {
            val arr = receivedData.split(" ")
            if (arr.size == 6) {
                val arr1 = arr[4].substring(2).toInt()
                val isMute = arr1 != 0
                setupPrefBoolean(KEY_OUTPUT_MUTE, isMute)
            }

        }
    }

    fun received_outputGain(receivedData: String) {
        if (receivedData.startsWith("0x08 0x52 0x47")) {
            val arr = receivedData.split(" ")
            if (arr.size == 9) {
                val arr1 = arr[4].substring(2) + arr[5].substring(2) +
                        arr[6].substring(2) + arr[7].substring(2)
                val outputgain = hexStringToFloat(arr1)
                setupPrefFloat(KEY_OUTPUT_GAIN, outputgain)
            }

        }
    }

    fun received_rmsComp(receivedData: String) {
        if (receivedData.startsWith("0x13 0x52 0x72")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = ""
            var arr5 = ""
            var arr6 = false
            if (arr.size == 20) {
                for (i in arr.indices) {
                    if (i in 4..7) {
                        arr1 += arr[i].substring(2)
                    }
                    if (i in 8..11) {
                        arr2 += arr[i].substring(2)
                    }
                    if (i in 12..15) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i == 16) {
                        arr4 = arr[i].substring(2)
                    }
                    if (i == 17) {
                        arr5 = arr[i].substring(2)
                    }
                    if (i == 18) {
                        arr6 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val th = hexStringToFloat(arr1)
            val re = hexStringToFloat(arr2)
            val att = hexStringToFloat(arr3)
            val ratio = Integer.parseInt(arr4, 16)
            val knee = Integer.parseInt(arr5, 16)
            val bypass = arr6

            setupPrefFloat(KEY_RMSCOMP_TH, th)
            setupPrefFloat(KEY_RMSCOMP_RE, re)
            setupPrefFloat(KEY_RMSCOMP_ATT, att)
            setupPrefInt(KEY_RMSCOMP_RATIO, ratio)
            setupPrefInt(KEY_RMSCOMP_KNEE, knee)
            setupPrefBoolean(KEY_RMSCOMP_BYPASS, bypass)

        }
    }

    fun received_peakComp(receivedData: String) {
        if (receivedData.startsWith("0x11 0x52 0x6b")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = false
            if (arr.size == 18) {
                for (i in arr.indices) {
                    if (i in 4..7) {
                        arr1 += arr[i].substring(2)
                    }
                    if (i in 8..11) {
                        arr2 += arr[i].substring(2)
                    }
                    if (i in 12..15) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i == 16) {
                        arr4 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val th = hexStringToFloat(arr1)
            val re = hexStringToFloat(arr2)
            val hold = hexStringToFloat(arr3)
            val bypass = arr4

            setupPrefFloat(KEY_PEAKCOMP_TH, th)
            setupPrefFloat(KEY_PEAKCOMP_RE, re)
            setupPrefFloat(KEY_PEAKCOMP_HOLD, hold)
            setupPrefBoolean(KEY_PEAKCOMP_BYPASS, bypass)

        }
    }

    fun received_noiseGate(receivedData: String) {
        if (receivedData.startsWith("0x11 0x52 0x63")) {
            val arr = receivedData.split(" ")
            var arr1 = ""
            var arr2 = ""
            var arr3 = ""
            var arr4 = false
            if (arr.size == 18) {
                for (i in arr.indices) {
                    if (i in 4..7) {
                        arr1 += arr[i].substring(2)
                    }
                    if (i in 8..11) {
                        arr2 += arr[i].substring(2)
                    }
                    if (i in 12..15) {
                        arr3 += arr[i].substring(2)
                    }
                    if (i == 16) {
                        arr4 = arr[i].substring(2).toInt() != 1
                    }
                }
            }
            val th = hexStringToFloat(arr1)
            val re = hexStringToFloat(arr2)
            val att = hexStringToFloat(arr3)
            val bypass = arr4

            setupPrefFloat(KEY_NOISE_GATE_TH, th)
            setupPrefFloat(KEY_NOISE_GATE_RE, re)
            setupPrefFloat(KEY_NOISE_GATE_ATT, att)
            setupPrefBoolean(KEY_NOISE_GATE_BYPASS, bypass)
        }
    }

    fun received_inputGain(receivedData: String) {
        if (receivedData.startsWith("0x08 0x52 0x67")) {
            val arr = receivedData.split(" ")
            if (arr.size == 9) {
                val arr1 = arr[4].substring(2) + arr[5].substring(2) +
                        arr[6].substring(2) + arr[7].substring(2)
                val inputgain = hexStringToFloat(arr1)

                setupPrefFloat(KEY_INPUT_GAIN, inputgain)
                if(volumeArrays == null) {
                    volumeArrays = ArrayList()
                }else{
                    volumeArrays?.add(inputgain)
                }
            }

        }
    }

    fun received_phase(receivedData: String) {
        if (receivedData.startsWith("0x05 0x52 0x76")) {
            val arr = receivedData.split(" ")
            if (arr.size == 6) {
                val phase = arr[4].substring(2).toInt() != 0

                setupPrefBoolean(KEY_PHASE, phase)

            }

        }
    }

    fun setupPrefString(key: String, value: String) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setupPrefFloat(key: String, value: Float) {
        val editor = pref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun setupPrefInt(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setupPrefBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun arrangeSockets(msg: Message) {
//        setupPrefInt("speaker${msg.arg1}", msg.arg1)
        when (msg.arg1) {

            1 -> {
                spk1Client = msg.obj as Socket

                if (spk1Client != null) {
//                    view.btn_spk1.setImageResource(R.drawable.spk1)
                }
            }
            2 -> {
                spk2Client = msg.obj as Socket
                if (spk2Client != null) {
//                    view.btn_spk2.setImageResource(R.drawable.spk2)
                }
            }
            3 -> {
                spk3Client = msg.obj as Socket
                if (spk3Client != null) {
//                    view.btn_spk3.setImageResource(R.drawable.spk3)
                }
            }
            4 -> {
                spk4Client = msg.obj as Socket
                if (spk4Client != null) {
//                    view.btn_spk4.setImageResource(R.drawable.spk4)
                }
            }
            5 -> {
                spk5Client = msg.obj as Socket
                if (spk5Client != null) {
//                    view.btn_spk5.setImageResource(R.drawable.spk5)
                }
            }
            6 -> {
                spk6Client = msg.obj as Socket
                if (spk6Client != null) {
//                    view.btn_spk6.setImageResource(R.drawable.spk6)
                }
            }
            7 -> {
                spk7Client = msg.obj as Socket
                if (spk7Client != null) {
//                    view.btn_spk7.setImageResource(R.drawable.spk7)
                }
            }
            8 -> {
                spk8Client = msg.obj as Socket
                if (spk8Client != null) {
//                    view.btn_spk8.setImageResource(R.drawable.spk8)
                }
            }

            else -> {
                otherClient.add(msg.obj as Socket)
                otherClientNo++
                val m = Message()
                m.what = DSPMessage.MSG_RSV.value
                m.obj = "SPK ID가 지정되지 않은 DSP 발견 \n 갯수: $otherClientNo 개"
                handler.sendMessage(m)
                Toast.makeText(view, "ID가 없는 DSP 발견! SET ID 실행요청", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun connect() {
        gvPacket.SendPacket_Connect()

        handler.postDelayed({
            makeSocketsList()
            initailizeSpeakerList()
        }, 700L)
    }


    private fun makeSocketsList() {
        sockets = ArrayList()

        sockets.add(spk1Client)
        sockets.add(spk2Client)
        sockets.add(spk3Client)
        sockets.add(spk4Client)
        sockets.add(spk5Client)
        sockets.add(spk6Client)
        sockets.add(spk7Client)
        sockets.add(spk8Client)
    }

    fun getIPAddress(): String {
        val wifiMan = view.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val wifiInf = wifiMan.connectionInfo
        val ipAddress = wifiInf.ipAddress
        val ip = String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )
        return ip
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> =
                    Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: java.lang.Exception) {
        } // for now eat exceptions
        return ""
    }

    fun initailizeSpeakerList() {
        if (prefSetting.isSetting()) {
            WaitingDialog(view).create("잠시만 기다려 주세요..", 1200)
            listAvailableId = ArrayList()
            listAllClient = ArrayList()
            listUsedId = ArrayList()
            initIdList()
            speakerIdSetting()
            selectPannel(GVSPanel.VOLUME)
        } else {
            val builder = AlertDialog.Builder(view)
            builder.setTitle("스피커 미설정")
            builder.setMessage("스피커 설정화면으로 이동합니다.")
            builder.setPositiveButton("이동") { dialog: DialogInterface, _: Int ->
                selectPannel(GVSPanel.SETTINGS)
                dialog.dismiss()
            }
            builder.setIcon(android.R.drawable.ic_dialog_info)
            builder.show()
        }

    }

    private fun speakerIdSetting() {
        spkList = ArrayList()
        for(socketId in listUsedId){
            val id = socketId
            val idPosition = socketId.toInt() - 1
            if(isMode2Way(id)){
                val CH1_NAME = loadSetting(socketId).name + "$id-1"
                val CH2_NAME = loadSetting(socketId).name + "$id-2"
                spkList.add(SpeakerInfo(listAllClient[idPosition], CMD_PARA2_CH1, CH1_NAME))
                spkList.add(SpeakerInfo(listAllClient[idPosition], CMD_PARA2_CH2, CH2_NAME))
            }else{
                val CH1_NAME = loadSetting(socketId).name + id
                spkList.add(SpeakerInfo(listAllClient[idPosition], CMD_PARA2_CH1, CH1_NAME))
            }

        }
    }

    val MODE_2WAY = "2-WAY"

    private fun isMode2Way(id: String): Boolean {
        return loadSetting(id).mode == MODE_2WAY
    }

    private fun loadSetting(id: String): SettingData {
        return prefSetting.loadSpeakerSettings(id)
    }

    private fun initIdList() {
        availableIdList()
    }

    private fun availableIdList() {
        connectedSocketList()
        for (index in listAllClient.indices) {
            if (listAllClient[index] == null) {
                listAvailableId.add("${index + 1}")
            } else {
                listUsedId.add("${index + 1}")
            }
        }

    }

    private fun connectedSocketList() {
        listAllClient.add(spk1Client)
        listAllClient.add(spk2Client)
        listAllClient.add(spk3Client)
        listAllClient.add(spk4Client)
        listAllClient.add(spk5Client)
        listAllClient.add(spk6Client)
        listAllClient.add(spk7Client)
        listAllClient.add(spk8Client)
        for (avalableClient in listAllClient) {
            if (avalableClient != null) SetIdFragment.listUsedClient.add(avalableClient)
        }
    }

    fun showHideAdminBar() {
        if(!isShowAdminBar){
            view.lay_admin.visibility = View.VISIBLE
            isShowAdminBar = true
        }else{
            view.lay_admin.visibility = View.GONE
            isShowAdminBar = false
        }
    }

    fun saveFirmwareVersion(spkNo: Int, majorVersion: Int, minorVersion: Int) {
        val firmwareVersion = "V$majorVersion.$minorVersion"
        prefSetting.saveFirmware(spkNo, firmwareVersion)
    }

    fun hexStringToInt(hexString: String): Int {
        return hexString.substring(2, 4).toInt()
    }

}