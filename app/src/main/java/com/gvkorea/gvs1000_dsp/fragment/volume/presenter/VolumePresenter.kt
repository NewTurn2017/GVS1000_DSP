package com.gvkorea.gvs1000_dsp.fragment.volume.presenter

import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.muteArrays
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.volumeArrays
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.SpeakerInfo
import com.gvkorea.gvs1000_dsp.util.WaitingDialog
import com.manojbhadane.QButton
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

class VolumePresenter(val view: VolumeFragment, val handler: Handler) {

    val packet = GVPacket(view)
    var isMuteArray = BooleanArray(spkList.size)
    val SWITCH_ON = 1
    val SWITCH_OFF = 0
    val KEY_INPUT_GAIN = "input_gain"
    val KEY_INPUT_MUTE = "input_mute"

    init {
        Arrays.fill(isMuteArray, false)
    }

    fun volumeControl(
        speakerInfo: SpeakerInfo,
        progress: Int,
        textView: TextView
    ) {

        changeVolumeValue(progress, textView)
        sendVolumeControl(speakerInfo.socket, speakerInfo.channel, progress)

    }

    private fun sendVolumeControl(
        socket: Socket?,
        channel: Char,
        progress: Int
    ) {
        val gain = (progress - 40).toFloat()
        packet.SendPacket_InputGain(socket, channel, gain)
    }

    private fun changeVolumeValue(progress: Int, textView: TextView) {
        val value = "${progress - 40} dB"
        textView.text = value
    }

    fun mute(index: Int, speakerInfo: SpeakerInfo, qButton: QButton) {
        if(!isMuteArray[index]){
            changeButton(true, qButton)
            isMuteArray[index] = true
            packet.SendPacket_InputMute(speakerInfo.socket, speakerInfo.channel, SWITCH_ON)
        }else {
            changeButton(false, qButton)
            isMuteArray[index] = false
            packet.SendPacket_InputMute(speakerInfo.socket, speakerInfo.channel, SWITCH_OFF)
        }
    }

    private fun changeButton(isSelected: Boolean, qButton: QButton?) {
        if (isSelected) {
            qButton?.setBackgroundColor(
                ContextCompat.getColor(
                    view.context!!,
                    android.R.color.holo_red_light
                )
            )
        } else {
            qButton?.setBackgroundColor(
                ContextCompat.getColor(
                    view.context!!,
                    android.R.color.darker_gray
                )
            )
        }
    }



    fun loadVolume() {
        if(spkList.size > 0){
            volumeArrays = ArrayList()
            muteArrays = ArrayList()
            for(i in 0 until spkList.size){
                packet.SendPacket_StatusRequest_general(packet.protocol.CMD_INPUT_GAIN, spkList[i].socket, spkList[i].channel)
                packet.SendPacket_StatusRequest_general(packet.protocol.CMD_INPUT_MUTE, spkList[i].socket, spkList[i].channel)
            }
            WaitingDialog(view.context!!).create("Loading...", 1200)
            handler.postDelayed({
                updateVolumeUI(volumeArrays)
                updateMuteUI(muteArrays)
            }, 1000)

        }

    }

    private fun updateVolumeUI(volumeArrays: ArrayList<Float>?) {
        val FIRST_ID = 1
        val LAST_ID = volumeArrays?.size!!
        for (i in FIRST_ID..LAST_ID) {
            val seekBar = view.activity?.findViewById<SeekBar>(
                    view.activity?.resources?.getIdentifier(
                            "sb_spk$i",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            seekBar?.progress = changeGainToProgress(volumeArrays[i-1])
        }
    }

    private fun updateMuteUI(muteArray: ArrayList<Boolean>?) {
        val FIRST_ID = 1
        val LAST_ID = muteArray?.size!!
        for (i in FIRST_ID..LAST_ID) {
            val button = view.activity?.findViewById<QButton>(
                    view.activity?.resources?.getIdentifier(
                            "btn_spk${i}_mute",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            changeMuteButton(i-1, muteArray[i-1], button)
        }
    }

    private fun changeMuteButton(no: Int, isMute: Boolean, button: QButton?){
        changeButton(isMute, button)
        isMuteArray[no] = isMute
    }


    private fun changeGainToProgress(gain: Float): Int {
        return (gain + 40).toInt()
    }


}