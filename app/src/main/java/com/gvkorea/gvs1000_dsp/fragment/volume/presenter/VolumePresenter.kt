package com.gvkorea.gvs1000_dsp.fragment.volume.presenter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.SpeakerInfo
import com.manojbhadane.QButton
import java.net.Socket
import java.util.*

class VolumePresenter(val view: VolumeFragment) {

    val packet = GVPacket(view)

    val isMuteArray = BooleanArray(8)
    val SWITCH_ON = 1
    val SWITCH_OFF = 0

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

    private fun changeButton(isSelected: Boolean, qButton: QButton) {
        if (isSelected) {
            qButton.setBackgroundColor(
                ContextCompat.getColor(
                    view.context!!,
                    android.R.color.holo_red_light
                )
            )
        } else {
            qButton.setBackgroundColor(
                ContextCompat.getColor(
                    view.context!!,
                    android.R.color.darker_gray
                )
            )
        }
    }


}