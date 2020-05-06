package com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.presenter

import android.R
import android.content.Context
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.arrGEQ
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.arrPEQ
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.SyncFragment
import com.gvkorea.gvs1000_dsp.util.*
import kotlinx.android.synthetic.main.fragment_sync.*

class SyncPresenter(val view: SyncFragment, val handler: Handler) {



    val packet = GVPacket(view)
    var nameList = ArrayList<String>()

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

    private val protocol = Protocol()


    fun initializeList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_syncSpeakerList)
    }


    private fun makeNameList() {
        nameList = ArrayList()
        for(i in spkList.indices){
            nameList.add(spkList[i].name)
        }
    }

    private fun registerAdapter(
        context: Context,
        list: ArrayList<String>,
        spinner: Spinner
    ) {
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(com.gvkorea.gvs1000_dsp.R.layout.spinner_dropdown)
        adapter.notifyDataSetChanged()
        spinner.adapter = adapter

    }

    fun syncSettings() {
        resetGeqAndPeqArrays()
        view.tv_syncReceived.text = ""
        requestSyncAllAudioSettings()
    }



    private fun resetGeqAndPeqArrays(){
        arrPEQ = ArrayList()
        arrGEQ = ArrayList()
        val resetData_peq = PEQData( 100.0f, 0f, 0f, false)
        val resetData_geq = GEQData( 0f, false)
        for (i in 0..30){
            arrPEQ.add(resetData_peq)
            arrGEQ.add(resetData_geq)
        }
    }

    private fun requestSyncAllAudioSettings() {
        val index = view.sp_syncSpeakerList.selectedItemPosition
        val speakerInfo = spkList[index]
        packet.SendPacket_StatusRequest_All(speakerInfo.socket, speakerInfo.channel)
        WaitingDialog(view.context!!).create("동기화 중입니다..", 1000L)
        handler.postDelayed({
            appendtext(receivedData(speakerInfo))
        }, 1500L)
    }

    fun appendtext(msg: String) {
        view.tv_syncReceived.append(msg)
    }

    private fun receivedData(speakerInfo: SpeakerInfo): String {
        val spkNo = speakerInfo.name
        val spkFirmWareVersion = prefSetting.loadFirmware(loadIdFromName(spkNo))
        val source_gain = pref.getFloat(KEY_SOURCE_GAIN, -40f)
        val input_delay = pref.getInt(KEY_INPUT_DELAY, 0)
        val input_delay_bypass = pref.getBoolean(KEY_INPUT_DELAY_BYPASS, true)
        val input_mute = pref.getBoolean(KEY_INPUT_MUTE, false)
        val phase = pref.getBoolean(KEY_PHASE, false)
        val input_gain = pref.getFloat(KEY_INPUT_GAIN, 0f)
        val noise_gate_th = pref.getFloat(KEY_NOISE_GATE_TH, 0f)
        val noise_gate_re = pref.getFloat(KEY_NOISE_GATE_RE, 0f)
        val noise_gate_att = pref.getFloat(KEY_NOISE_GATE_ATT, 0f)
        val noise_gate_bypass = pref.getBoolean(KEY_NOISE_GATE_BYPASS, true)
        val peakcomp_th = pref.getFloat(KEY_PEAKCOMP_TH, 0f)
        val peakcomp_re = pref.getFloat(KEY_PEAKCOMP_RE, 0f)
        val peakcomp_hold = pref.getFloat(KEY_PEAKCOMP_HOLD, 0f)
        val peakcomp_bypass = pref.getBoolean(KEY_PEAKCOMP_BYPASS, true)
        val rmscomp_th = pref.getFloat(KEY_RMSCOMP_TH, 0f)
        val rmscomp_re = pref.getFloat(KEY_RMSCOMP_RE, 0f)
        val rmscomp_att = pref.getFloat(KEY_RMSCOMP_ATT, 0f)
        val rmscomp_ratio = pref.getInt(KEY_RMSCOMP_RATIO, 0)
        val rmscomp_knee = pref.getInt(KEY_RMSCOMP_KNEE, 0)
        val rmscomp_bypass = pref.getBoolean(KEY_RMSCOMP_BYPASS, true)
        val output_gain = pref.getFloat(KEY_OUTPUT_GAIN, 0f)
        val output_mute = pref.getBoolean(KEY_OUTPUT_MUTE, false)
        val limiter_th = pref.getFloat(KEY_LIMITER_TH, 0f)
        val limiter_re = pref.getFloat(KEY_LIMITER_RE, 0f)
        val limiter_att = pref.getFloat(KEY_LIMITER_ATT, 0f)
        val limiter_ratio = pref.getInt(KEY_LIMITER_RATIO, 0)
        val limiter_knee = pref.getInt(KEY_LIMITER_KNEE, 0)
        val limiter_bypass = pref.getBoolean(KEY_LIMITER_BYPASS, true)
        val output_bpf_filterlow_ch1 = pref.getString(KEY_OUT_BPF_FILTERLOW_CH1, "NONE")
        val output_bpf_filterhigh_ch1 = pref.getString(KEY_OUT_BPF_FILTERHIGH_CH1, "NONE")
        val output_bpf_freq_lc_ch1 = pref.getFloat(KEY_OUT_BPF_FREQ_LC_CH1, 35f)
        val output_bpf_freq_hc_ch1 = pref.getFloat(KEY_OUT_BPF_FREQ_HC_CH1, 16000f)
        val output_bpf_gain_ch1 = pref.getFloat(KEY_OUT_BPF_GAIN_CH1, 0f)
        val output_bpf_filterlow_ch2 = pref.getString(KEY_OUT_BPF_FILTERLOW_CH2, "NONE")
        val output_bpf_filterhigh_ch2 = pref.getString(KEY_OUT_BPF_FILTERHIGH_CH2, "NONE")
        val output_bpf_freq_lc_ch2 = pref.getFloat(KEY_OUT_BPF_FREQ_LC_CH2, 35f)
        val output_bpf_freq_hc_ch2 = pref.getFloat(KEY_OUT_BPF_FREQ_HC_CH2, 16000f)
        val output_bpf_gain_ch2 = pref.getFloat(KEY_OUT_BPF_GAIN_CH2, 0f)
        val output_bpf_bypass = pref.getBoolean(KEY_OUT_BPF_BYPASS, true)
        val reverb_bypass = pref.getBoolean(KEY_REVERB_BYPASS, true)
        val reverb_time = pref.getInt(KEY_REVERB_TIME, 0)
        val reverb_hp = pref.getInt(KEY_REVERB_HP, 0)
        val reverb_gain = pref.getInt(KEY_REVERB_GAIN, 0)
        val bridge = pref.getString(KEY_BRIDGE, "2-WAY MODE")
        var out_geq = ""
        val geq_bypass = pref.getBoolean(KEY_GEQ_BYPASS, false)
        var out_peq = ""
        val peq_bypass = pref.getBoolean(KEY_PEQ_BYPASS, false)


        for(i in 0 until arrPEQ.size){

            out_geq += "\tEQ${i+1} GAIN(${pref.getFloat("geq_gain_eq$i", 0f)}) " +
                    "B.P(${pref.getBoolean("geq_bypass_eq$i", false)})\n"

            out_peq += "\tPEQ EQ${i+1} FREQ(${pref.getFloat("peq_freq_eq$i", 100f)} " +
                    "GAIN(${pref.getFloat("peq_gain_eq$i", 0f)}) " +
                    "Q(${pref.getFloat("peq_q_eq$i", 0.7f)}) " +
                    "B.P(${pref.getBoolean("peq_bypass_eq$i", false)})\n"
        }
        val line = "----------------------------------------------------------" + "\n"
        val receivedData = "\t\t<현재 SPEAKER $spkNo 의 동기화 데이터>" + "\n\n" +
                "\tSPEAKER NAME : $spkNo" + "\n" +
                "\tSPEAKER FIRMWARE : $spkFirmWareVersion" + "\n" +
                line +
                "\tINPUT GAIN : $input_gain dB, INPUT MUTE : $input_mute" + "\n" +
                line +
                "\tINPUT DELAY : $input_delay ms, BYPASS : $input_delay_bypass" + "\n" +
                line +
                "\tNOISE GENERATOR GAIN : $source_gain dB" + "\n" +
                line +
                "\tNOISE GATE THRESHOLD : $noise_gate_th dB" + "\n" +
                "\tNOISE GATE RELEASE   : $noise_gate_re sec" + "\n" +
                "\tNOISE GATE ATTACK    : $noise_gate_att dB" + "\n" +
                "\tNOISE GATE BYPASS    : $noise_gate_bypass" + "\n" +
                line +
                "\tPHASE INVERTER : $phase" + "\n" +
                line +
                "\tINPUT PEAK COMP. THRESHOLD : $peakcomp_th dB" + "\n" +
                "\tINPUT PEAK COMP. RELEASE   : $peakcomp_re sec" + "\n" +
                "\tINPUT PEAK COMP. HOLD      : $peakcomp_hold ms" + "\n" +
                "\tINPUT PEAK COMP. BYPASS    : $peakcomp_bypass" + "\n" +
                line +
                "\tINPUT RMS COMP. THRESHOLD : $rmscomp_th dB" + "\n" +
                "\tINPUT RMS COMP. RELEASE   : $rmscomp_re sec" + "\n" +
                "\tINPUT RMS COMP. ATTACK    : $rmscomp_att ms" + "\n" +
                "\tINPUT RMS COMP. RATIO     : $rmscomp_ratio : 1" + "\n" +
                "\tINPUT RMS COMP. KNEE      : $rmscomp_knee %" + "\n" +
                "\tINPUT RMS COMP. BYPASS    : $rmscomp_bypass" + "\n" +
                line +
                "\tOUTPUT GAIN : $output_gain dB, OUTPUT MUTE : $output_mute" + "\n" +
                line +
                "\tOUTPUT LIMITER THRESHOLD : $limiter_th dB" + "\n" +
                "\tOUTPUT LIMITER RELEASE   : $limiter_re sec" + "\n" +
                "\tOUTPUT LIMITER ATTACK    : $limiter_att ms" + "\n" +
                "\tOUTPUT LIMITER RATIO     : $limiter_ratio : 1" + "\n" +
                "\tOUTPUT LIMITER KNEE      : $limiter_knee %" + "\n" +
                "\tOUTPUT LIMITER BYPASS    : $limiter_bypass" + "\n" +
                line +
                "\t<BPF CH1>" + "\n" +
                "\tOUTPUT BPF FILTER  LOW(HPF)  : $output_bpf_filterlow_ch1" + "\n" +
                "\tOUTPUT BPF FILTER HIGH(LPF)  : $output_bpf_filterhigh_ch1" + "\n" +
                "\tOUTPUT BPF FREQ.  LOW CUTOFF : $output_bpf_freq_lc_ch1 hz" + "\n" +
                "\tOUTPUT BPF FREQ. HIGH CUTOFF : $output_bpf_freq_hc_ch1 hz" + "\n" +
                "\tOUTPUT BPF GAIN              : $output_bpf_gain_ch1 dB" + "\n" +
                line +
                "\t<BPF CH2>" + "\n" +
                "\tOUTPUT BPF FILTER  LOW(HPF)  : $output_bpf_filterlow_ch2" + "\n" +
                "\tOUTPUT BPF FILTER HIGH(LPF)  : $output_bpf_filterhigh_ch2" + "\n" +
                "\tOUTPUT BPF FREQ.  LOW CUTOFF : $output_bpf_freq_lc_ch2 hz" + "\n" +
                "\tOUTPUT BPF FREQ. HIGH CUTOFF : $output_bpf_freq_hc_ch2 hz" + "\n" +
                "\tOUTPUT BPF GAIN              : $output_bpf_gain_ch2 dB" + "\n" +
                line +
                "\t<BPF BYPASS>" + "\n" +
                "\tOUTPUT BPF BYPASS            : $output_bpf_bypass" + "\n\n" +
                line +
                "\tREVERB BYPASS : $reverb_bypass" + "\n" +
                "\tREVERB TIME         : $reverb_time" + "\n" +
                "\tREVERB HP DAMPING   : $reverb_hp" + "\n" +
                "\tREVERB BASS GAIN    : $reverb_gain" + "\n" +
                line +
                "\tSETTING : $bridge" + "\n" +
                line +
                "\t<GEQ> : ALL BYPASS($geq_bypass)"+ "\n" +
                out_geq + "\n" +
                line +
                "\t<PEQ> : ALL BYPASS($peq_bypass)"+ "\n" +
                out_peq


        return receivedData
    }

    private fun loadIdFromName(spkNo: String): Int {
        val id : Int
        if(spkNo.contains("Main")){
            id = spkNo.substring(4,5).toInt()
        }else{
            id = spkNo.substring(3,4).toInt()
        }
        return id
    }
}