package com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.presenter

import android.R
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.ResetSettingFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedId
import com.gvkorea.gvs1000_dsp.util.GVPacket
import kotlinx.android.synthetic.main.fragment_reset_setting.*

class ResetSettingPresenter(val view: ResetSettingFragment) {

    val packet = GVPacket(view)

    var nameList = ArrayList<String>()

    fun initializeList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_resetSpeakerList)
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

    fun resetSettingsAll() {
        val spkID =view.sp_resetSpeakerList.selectedItem.toString()
        val index = view.sp_resetSpeakerList.selectedItemPosition
        val speakerInfo = spkList[index]
        AlertDialog.Builder(view.context)
            .setTitle("세팅 초기화")
            .setMessage("$spkID 의 모든 오디오 세팅을 초기화 하시겠습니까?")
            .setPositiveButton("확인"){dialog, _ ->
                dialog.dismiss()
                packet.SendPacket_Reset_Default(speakerInfo.socket, speakerInfo.channel)
                hideResetButton()
            }
            .setNegativeButton("취소"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun hideResetButton() {
        view.lay_reset.visibility = View.GONE
    }

    fun showSelectReset() {
        view.lay_reset.visibility = View.VISIBLE
    }

}