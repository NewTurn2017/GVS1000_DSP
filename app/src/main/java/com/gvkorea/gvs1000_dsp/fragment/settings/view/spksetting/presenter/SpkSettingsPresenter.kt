package com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.presenter

import android.R
import android.content.Context
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk1Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk2Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk3Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk4Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk5Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk6Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk7Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk8Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R.layout
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listAllClient
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listAvailableId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedClient
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.SpkSettingsFragment
import com.gvkorea.gvs1000_dsp.util.SettingData
import com.gvkorea.gvs1000_dsp.util.SpeakerInfo
import com.gvkorea.gvs1000_dsp.util.WaitingDialog
import kotlinx.android.synthetic.main.fragment_spk_settings.*

class SpkSettingsPresenter(val view: SpkSettingsFragment, val mHandler: Handler) {

    val MODEL_GVA200 = "GVA-200"
    val MODEL_GVA300 = "GVA-300"
    val MODEL_GVA500 = "GVA-500"
    val MODEL_GVA700 = "GVA-700"
    val MODEL_GVA900 = "GVA-900"
    val MODEL_GVS200A = "GVS-200A"
    val MODEL_GVS500A = "GVS-500A"
    val MODEL_GVS700A = "GVS-700A"
    val MODEL_GVS200B = "GVS-200B"
    val MODEL_GVS200BA = "GVS-200BA"
    val MODEL_GVS400B = "GVS-400B"
    val MODEL_GVS500B = "GVS-500B"
    val MODEL_GVS500BA = "GVS-500BA"
    val MODEL_GVAS50 = "GVAS-50"
    val MODEL_GVS200 = "GVS-200"
    val MODEL_GVS300 = "GVS-300"
    val MODEL_GVS400 = "GVS-400"
    val MODEL_GVS500 = "GVS-500"
    val MODEL_GVS700 = "GVS-700"

    val MODE_2WAY = "2-WAY"
    val MODE_BRIDGE = "BRIDGE"
    val NAME_MAIN = "Main"
    val NAME_SUB = "Sub"


    private val listModel = arrayListOf(
        MODEL_GVA200, MODEL_GVA300, MODEL_GVA500,
        MODEL_GVA700, MODEL_GVA900, MODEL_GVS200A,
        MODEL_GVS500A, MODEL_GVS700A, MODEL_GVS200B,
        MODEL_GVS200BA, MODEL_GVS400B, MODEL_GVS500B,
        MODEL_GVS500BA, MODEL_GVAS50, MODEL_GVS200,
        MODEL_GVS300, MODEL_GVS400, MODEL_GVS500, MODEL_GVS700
    )
    private val listMode = arrayListOf(MODE_2WAY, MODE_BRIDGE)
    private val listName = arrayListOf(NAME_MAIN, NAME_SUB)
    private val EMPTYLIST = "없음"
    private val NONSETTING = "미지정"
    val CMD_PARA2_CH1 = '1'
    val CMD_PARA2_CH2 = '2'
    val CMD_PARA2_CHA = 'A'

    fun initializeList() {
        WaitingDialog(view.context!!).create("잠시만 기다려 주세요..", 1200)
        listAvailableId = ArrayList()
        listAllClient = ArrayList()
        listUsedId = ArrayList()
        initIdList()
        mHandler.postDelayed({
            registerAdapter(view.context!!, listUsedId, view.sp_spkNo)
            registerAdapter(view.context!!, listModel, view.sp_spkModel)
            registerAdapter(view.context!!, listMode, view.sp_spkMode)
            registerAdapter(view.context!!, listName, view.sp_spkName)
        }, 1000)
    }

    private fun registerAdapter(
        context: Context,
        list: ArrayList<String>,
        spinner: Spinner
    ) {
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(layout.spinner_dropdown)
        adapter.notifyDataSetChanged()
        spinner.adapter = adapter
    }

    private fun initIdList() {
        availableIdList()
        namedList()
    }

    private fun namedList() {
        if (listUsedId.size == 0) {
            listUsedId.add(EMPTYLIST)
        }
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
            if (avalableClient != null) listUsedClient.add(avalableClient)
        }
    }

    fun saveSettings() {
        if (listUsedId.size > 0 && listUsedId[0] != EMPTYLIST) {
            val spkNo = view.sp_spkNo.selectedItem.toString()
            val spkModel = view.sp_spkModel.selectedItem.toString()
            val spkMode = view.sp_spkMode.selectedItem.toString()
            val spkName = view.sp_spkName.selectedItem.toString()
            prefSetting.saveSpeakerSettings(spkNo, spkModel, spkMode, spkName)
            msg("저장되었습니다.")
            refreshSetting(spkNo)
        } else {
            msg("접속 후 저장바랍니다.")
        }

    }

    private fun refreshSetting(id: String) {
        val settingData = loadSetting(id)
        view.tv_spkModel.text = settingData.model
        view.tv_spkMode.text = settingData.mode
        view.tv_spkName.text = settingData.name
    }

    private fun loadSetting(id: String): SettingData {
        return prefSetting.loadSpeakerSettings(id)
    }

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun loadSavedData(position: Int) {
        refreshSetting(listUsedId[position])
    }

    fun settingComplete() {
        if (isSettingComplete()) {
            speakerIdSetting()
            msg("설정이 완료되었습니다.")
            prefSetting.saveIsSetting(true)
        }
    }

    private fun isSettingComplete(): Boolean {
        val isComplete: Boolean
        if (listUsedId.size > 0 && listUsedId[0] == EMPTYLIST) {
            msg("접속을 확인하세요. 연결된 스피커가 없습니다.")
            isComplete = false
        } else {
            isComplete = isConfirmSettingComplete()
        }
        return isComplete
    }

    private fun isConfirmSettingComplete(): Boolean {
        var isConfirm = false
        for (id in listUsedId) {
            val data = loadSetting(id)
            isConfirm =
                (data.model != NONSETTING) && (data.mode != NONSETTING) && (data.name != NONSETTING)
        }
        if (isConfirm == false) {
            msg("미설정 부분이 있습니다. 확인 바랍니다.")
        }

        return isConfirm
    }

    private fun speakerIdSetting() {
        spkList = ArrayList()
        for (socketId in listUsedId) {
            val id = socketId
            val idPosition = socketId.toInt() - 1
            if (isMode2Way(id)) {
                val CH1_NAME = loadSetting(socketId).name + "$id-1"
                val CH2_NAME = loadSetting(socketId).name + "$id-2"
                spkList.add(SpeakerInfo(listAllClient[idPosition], CMD_PARA2_CH1, CH1_NAME))
                spkList.add(SpeakerInfo(listAllClient[idPosition], CMD_PARA2_CH2, CH2_NAME))
            } else {
                val CH1_NAME = loadSetting(socketId).name + id
                spkList.add(SpeakerInfo(listAllClient[idPosition], CMD_PARA2_CH1, CH1_NAME))
            }

        }
    }

    private fun isMode2Way(id: String): Boolean {
        return loadSetting(id).mode == MODE_2WAY
    }


    fun resetSettings() {
        if (listUsedId.size > 0 && listUsedId[0] == EMPTYLIST) {
            for (id in listUsedId) {
                prefSetting.resetSpeakerSetting(id)
                refreshSetting(id)
            }
            prefSetting.saveIsSetting(false)
        }

    }
}