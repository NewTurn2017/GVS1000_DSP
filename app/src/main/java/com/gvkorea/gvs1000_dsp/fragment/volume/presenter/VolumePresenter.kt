package com.gvkorea.gvs1000_dsp.fragment.volume.presenter

import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.muteArrays
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.volumeArrays
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.util.DSPMessage
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.SpeakerInfo
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_volume.*
import java.lang.Exception
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
    var isAllMute = true

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

    private fun mainButtonDisable() {
        msg(view.context!!.getString(R.string.waiting))
        val m = Message()
        m.what = DSPMessage.MSG_UI_UNTOUCH.value
        handler.sendMessage(m)
    }

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun mainButtonEnable() {
        val m = Message()
        m.what = DSPMessage.MSG_UI_TOUCH.value
        handler.sendMessage(m)
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
            mainButtonDisable()
            volumeArrays = ArrayList()
            muteArrays = ArrayList()

            for(i in 0 until spkList.size){
                packet.SendPacket_StatusRequest_general(packet.protocol.CMD_INPUT_GAIN, spkList[i].socket, spkList[i].channel)
                packet.SendPacket_StatusRequest_general(packet.protocol.CMD_INPUT_MUTE, spkList[i].socket, spkList[i].channel)
                try {
                    Thread.sleep(100)
                }catch (e: Exception){}
            }
            handler.postDelayed({
                updateVolumeUI(volumeArrays)
                updateMuteUI(muteArrays)
                mainButtonEnable()
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

    fun volumeReset() {
        val FIRST_ID = 1
        val LAST_ID = spkList.size
        val RESET_VOLUME = 0f
        for (i in FIRST_ID..LAST_ID) {
            val seekBar = view.activity?.findViewById<SeekBar>(
                    view.activity?.resources?.getIdentifier(
                            "sb_spk$i",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            seekBar?.progress = changeGainToProgress(RESET_VOLUME)
        }
    }

    fun muteAll() {
        muteAllButtonControl()
        val FIRST_ID = 1
        val LAST_ID = spkList.size
        for (i in FIRST_ID..LAST_ID) {
            val button = view.activity?.findViewById<QButton>(
                    view.activity?.resources?.getIdentifier(
                            "btn_spk${i}_mute",
                            "id",
                            view.activity?.packageName
                    )!!
            )
            mute(i-1, spkList[i-1], button!!)
        }
    }

    private fun muteAllButtonControl() {
        if(!isAllMute) {
            changeMuteAllButton()
            isAllMute = true

        }else {
            changeMuteAllButton()
            isAllMute = false
        }
    }

    private fun changeMuteAllButton() {
        Arrays.fill(isMuteArray, !isAllMute)
        changeButton(isAllMute, view.btn_allMute)
    }

    fun savePreset() {

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

    fun loadPreset() {
        TODO("Not yet implemented")
    }


}