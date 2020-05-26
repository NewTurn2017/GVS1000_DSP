package com.gvkorea.gvs1000_dsp.fragment.eq.presenter

import android.R
import android.content.Context
import android.os.Handler
import android.widget.*
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.SpeakerInfo
import com.gvkorea.gvs1000_dsp.util.WaitingDialog
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_eq.*
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

class GEQPresenter(val view: GEQFragment, val handler: Handler) {

    val packet = GVPacket(view)
    var nameList = ArrayList<String>()
    val isEachBypassArray = BooleanArray(31)
    val SWITCH_ON = 1
    val SWITCH_OFF = 0
    val EQ_GAIN_ZERO = 0f
    var isGEQBypass = false
    val KEY_GEQ_BYPASS = "geq_bypass"

    val frequencyArrays = floatArrayOf(
            20f, 25f, 31.5f, 40f, 50f, 63f, 80f, 100f, 125f, 160f,
            200f, 250f, 315f, 400f, 500f, 630f, 800f, 1000f, 1250f, 1600f,
            2000f, 2500f, 3150f, 4000f, 5000f, 6300f, 8000f, 10000f, 12500f, 16000f, 20000f
    )

    init {
        Arrays.fill(isEachBypassArray, false)
    }

    fun initializeList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_eqSpeakerList)
    }

    private fun makeNameList() {
        nameList = ArrayList()
        for (i in MainActivity.spkList.indices) {
            nameList.add(MainActivity.spkList[i].name)
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

    fun loadGEQ() {
        Arrays.fill(isEachBypassArray, false)
        handler.post {
            WaitingDialog(view.context!!).create("Loading...", 1000)
        }
        loadGEQData()

        handler.postDelayed({
            updateUI()
        }, 500)
    }

    private fun loadGEQData() {
        val spk = readSpeakerInfo()
        val cmdGEQAll = packet.protocol.CMD_INPUT_GEQ_ALL
        val cmdGEQBypass = packet.protocol.CMD_EQ_BYPASS
        packet.SendPacket_StatusRequest_general(cmdGEQAll, spk.socket, spk.channel)
        packet.SendPacket_StatusRequest_general(cmdGEQBypass, spk.socket, spk.channel)
    }

    private fun updateUI() {
        val geq_bypass = pref.getBoolean(KEY_GEQ_BYPASS, false)
        changeBypassButton(geq_bypass)
        updateEQGainUI()
        updateEQBypassUI()
    }

    private fun changeBypassButton(isBypass: Boolean) {
        if (isBypass) {
            changeButton(true, view.btn_geqBypass)
            isGEQBypass = true
        } else {
            changeButton(false, view.btn_geqBypass)
            isGEQBypass = false
        }
    }

    private fun updateEQGainUI() {
        val FIRST_EQ = 1
        val LAST_EQ = 31
        for (i in FIRST_EQ..LAST_EQ) {
            val seekBar = view.activity?.findViewById<SeekBar>(
                    view.activity?.resources?.getIdentifier(
                            "sb_input_eq${i}",
                            "id",
                            view.activity?.packageName
                    )!!
            )

            seekBar?.progress = changeEQGainValueToEQProgress(pref.getFloat("geq_gain_eq${i - 1}", 0f))
        }


    }

    private fun updateEQBypassUI() {
        val FIRST_EQ = 1
        val LAST_EQ = 31
        for (i in FIRST_EQ..LAST_EQ) {

            val button = view.activity?.findViewById<QButton>(
                    view.activity?.resources?.getIdentifier(
                            "btn_input_eq${i}_bypass",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            changeEachBypassButton(i - 1, pref.getBoolean("geq_bypass_eq${i - 1}", false), button)
        }


    }


    private fun changeEachBypassButton(no: Int, isBypass: Boolean, button: QButton?) {
        if (isBypass) {
            changeButton(true, button)
            isEachBypassArray[no] = true
        } else {
            changeButton(false, button)
            isEachBypassArray[no] = false
        }
    }


    fun eqReset() {
        resetGEQUI()
        val spk = readSpeakerInfo()
        packet.SendPacket_GEQ_Reset(spk.socket, spk.channel)
    }

    private fun resetGEQUI() {
        val FIRST_EQ = 1
        val LAST_EQ = 31
        for (i in FIRST_EQ..LAST_EQ) {
            val textView = view.activity?.findViewById<TextView>(
                    view.activity?.resources?.getIdentifier(
                            "tv_input_eq$i",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            val seekBar = view.activity?.findViewById<SeekBar>(
                    view.activity?.resources?.getIdentifier(
                            "sb_input_eq$i",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            val button = view.activity?.findViewById<QButton>(
                    view.activity?.resources?.getIdentifier(
                            "btn_input_eq${i}_bypass",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            textView?.text = "0"
            seekBar?.progress = 30
            changeEachBypassButton(i - 1, true, button)
        }
    }

    fun geqControl(no: Int, progress: Int, textView: TextView, bypassButton: QButton) {
        val textValue = changeEQProgressToEQGainValue(progress)
        textView.text = textValue.toString()
        val gain = changeEQProgressToEQGainValue(progress)
        val spk = readSpeakerInfo()
        packet.SendPacket_InputGEQ(spk.socket, spk.channel, frequencyArrays[no], gain, SWITCH_ON)

        if (isEachBypassArray[no]) {
            getEachBypassUIControl(no, bypassButton)
        }

    }

    private fun getEachBypassUIControl(no: Int, bypassButton: QButton) {
        changeButton(false, bypassButton)
        isEachBypassArray[no] = false
    }

    fun geqAllBypass() {
        val spk = readSpeakerInfo()
        val bypass: Int
        if (!isGEQBypass) {
            changeButton(true, view.btn_geqBypass)
            isGEQBypass = true
            bypass = SWITCH_ON
        } else {
            changeButton(false, view.btn_geqBypass)
            isGEQBypass = false
            bypass = SWITCH_OFF
        }

        packet.SendPacket_InputGEQBypass(spk.socket, spk.channel, bypass)
    }


    private fun changeButton(isBypass: Boolean, qButton: QButton?) {
        if (isBypass) {
            qButton?.setBackgroundColor(
                    ContextCompat.getColor(
                            view.context!!,
                            R.color.holo_red_light
                    )
            )
        } else {
            qButton?.setBackgroundColor(
                    ContextCompat.getColor(
                            view.context!!,
                            R.color.darker_gray
                    )
            )
        }
    }

    fun geqEachBypass(
            no: Int,
            bypassButton: QButton,
            seekBar: VerticalSeekBar
    ) {
        val spk = readSpeakerInfo()
        val freq = frequencyArrays[no]
        val gain = changeEQProgressToEQGainValue(seekBar.progress)
        applyEachBypass(spk.socket, spk.channel, freq, bypassButton, gain, no)
    }


    private fun applyEachBypass(
            socket: Socket?,
            channel: Char,
            freq: Float,
            bypassButton: QButton,
            gain: Float,
            no: Int
    ) {
        if (!isEachBypassArray[no]) {
            changeButton(true, bypassButton)
            isEachBypassArray[no] = true
            packet.SendPacket_InputGEQ(socket, channel, freq, EQ_GAIN_ZERO, SWITCH_OFF)
        } else {
            changeButton(false, bypassButton)
            isEachBypassArray[no] = false
            packet.SendPacket_InputGEQ(socket, channel, freq, gain, SWITCH_ON)
        }
    }

    private fun changeEQProgressToEQGainValue(progress: Int): Float {
        return (progress / 2 - 15).toFloat()
    }

    private fun changeEQGainValueToEQProgress(gain: Float): Int {
        return (2 * (gain + 15)).toInt()
    }

    private fun readSpeakerInfo(): SpeakerInfo {
        val index = view.sp_eqSpeakerList.selectedItemPosition
        return spkList[index]
    }

    fun savePreset() {
        val title = "PRESET01"
        val presetNo = 0
        val spk = readSpeakerInfo()
        packet.savePreset(spk.socket, presetNo, title)
        msg("저장됨")
    }

    fun loadPreset() {
        val spk = readSpeakerInfo()
        packet.SendPacket_Preset_Load(spk.socket, 0)
        handler.postDelayed({
            loadGEQ()
            msg("로드됨")
        }, 300)
    }

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }


}