package com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.presenter

import android.R
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.SetModeFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.SpeakerInfo
import kotlinx.android.synthetic.main.fragment_set_mode.*

class SetModePresenter(val view: SetModeFragment) {

    val packet = GVPacket(view)

    val SETUP_2WAY = 0
    val SETUP_BRIDGE = 1
    var nameList = ArrayList<String>()

    fun initializeList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_setModeSpeakerList)
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

    fun showSelectMode() {
        view.lay_spkMode.visibility = View.VISIBLE
        loadModeAndSelect()
    }

    private fun loadModeAndSelect() {
        val spkName = view.sp_setModeSpeakerList.selectedItem.toString()
        val speakerId = loadIdFromName(spkName).toString()
        val settingData = prefSetting.loadSpeakerSettings(speakerId)
        selectMode(settingData.mode)
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

    private fun selectMode(mode: String) {
        when (mode) {
            "2-WAY" -> view.rb_setup_2way.isChecked = true
            "BRIDGE" -> view.rb_setup_bridge.isChecked = true
        }
    }

    fun applyModeAndHideSelect() {
        val spkName = view.sp_setModeSpeakerList.selectedItem.toString()
        val spkID = loadIdFromName(spkName).toString()
        val index = view.sp_setModeSpeakerList.selectedItemPosition
        val selectedMode =
            view.activity?.findViewById<RadioButton>(view.rg_setup.checkedRadioButtonId)?.text.toString()
        AlertDialog.Builder(view.context)
            .setTitle("SETUP MODE")
            .setMessage("변경값: 스피커 ID($spkID) -> $selectedMode \n확인을 누르면 변경됩니다.")
            .setPositiveButton(
                "확인"
            ) { dialog, _ ->
                SendPacket_Bridge(spkList[index], selectedMode)
                prefSetting.updateSpeakerSetMode(spkID, selectedMode)
                hideSelectMode()
                dialog.dismiss()
            }
            .setNegativeButton(
                "취소"
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .show()


    }

    private fun SendPacket_Bridge(
        speakerInfo: SpeakerInfo,
        selected: String
    ) {
        when (selected) {
            "2-WAY" -> packet.SendPacket_Bridge(speakerInfo.socket, SETUP_2WAY)
            "BRIDGE" -> packet.SendPacket_Bridge(speakerInfo.socket, SETUP_BRIDGE)
        }
    }

    private fun hideSelectMode() {
        view.lay_spkMode.visibility = View.GONE
    }
}