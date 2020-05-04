package com.gvkorea.gvs1000_dsp.fragment.eq.presenter

import android.R
import android.content.Context
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_eq.*
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

class GEQPresenter(val view: GEQFragment) {

    val packet = GVPacket(view)
    var nameList = ArrayList<String>()
    val isEachBypassArray = BooleanArray(31)
    val SWITCH_ON = 1
    val SWITCH_OFF = 0
    val EQ_GAIN_ZERO = 0f
    var isGEQBypass = false

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

    fun showGEQ() {
        view.lay_eq.visibility = View.VISIBLE
        //todo: load EQ in spkId
    }

    fun eqReset() {
        resetGEQUI()
        val index = view.sp_eqSpeakerList.selectedItemPosition
        val speakerInfo = spkList[index]
        packet.SendPacket_GEQ_Reset(speakerInfo.socket, speakerInfo.channel)
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
            textView?.text = "0"
        }

        for (i in FIRST_EQ..LAST_EQ) {
            val seekBar = view.activity?.findViewById<SeekBar>(
                view.activity?.resources?.getIdentifier(
                    "sb_input_eq$i",
                    "id",
                    view.activity?.packageName
                )!!
            )
            seekBar?.progress = 30
        }
    }

    fun geqControl(no: Int, progress: Int, textView: TextView, bypassButton: QButton) {
        val index = view.sp_eqSpeakerList.selectedItemPosition
        val textValue = changeEQGainValue(progress)
        textView.text = textValue.toString()
        val gain = changeEQGainValue(progress)
        val socket = spkList[index].socket
        val channel = spkList[index].channel
        packet.SendPacket_InputGEQ(socket, channel, frequencyArrays[no], gain, SWITCH_ON)

        if (isEachBypassArray[no]) {
            getEachBypassUIControl(no, bypassButton)
        }

    }

    private fun getEachBypassUIControl(no: Int, bypassButton: QButton) {
        changeButton(false, bypassButton)
        isEachBypassArray[no] = false
    }

    fun geqAllBypass() {
        val index = view.sp_eqSpeakerList.selectedItemPosition
        val socket = spkList[index].socket
        val channel = spkList[index].channel
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

        packet.SendPacket_InputGEQBypass(socket, channel, bypass)
    }

    private fun changeButton(isBypass: Boolean, qButton: QButton) {
        if (isBypass) {
            qButton.setBackgroundColor(
                ContextCompat.getColor(
                    view.context!!,
                    R.color.holo_red_light
                )
            )
        } else {
            qButton.setBackgroundColor(
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
        val index = view.sp_eqSpeakerList.selectedItemPosition
        val socket = spkList[index].socket
        val channel = spkList[index].channel
        val freq = frequencyArrays[no]
        val gain = changeEQGainValue(seekBar.progress)


        applyEachBypass(socket, channel, freq, bypassButton, gain, no)
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

    private fun changeEQGainValue(value: Int): Float {
        return (value / 2 - 15).toFloat()
    }
}