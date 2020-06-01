package com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.presenter

import android.R
import android.content.Context
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.otherClient
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.selectedClient
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.sockets
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk1Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk2Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk3Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk4Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk5Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk6Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk7Client
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spk8Client
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedClient
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listAvailableId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listOtherClientId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedId
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listAllClient
import com.gvkorea.gvs1000_dsp.util.GVSubPacket
import com.gvkorea.gvs1000_dsp.util.WaitingDialog
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_setid.*
import java.net.Socket

class SetIdPresenter(
    val view: SetIdFragment,
    private val mHandler: Handler, val activity: MainActivity

) {

    val gvPacket = GVSubPacket(view, mHandler)
    private var changedidNo = '1'

    fun resetId() {
        if (sockets.size > 0) {
            AlertDialog.Builder(view.context!!)
                .setTitle("Reset All Id")
                .setMessage("스피커 ID를 모두 초기화 하시겠습니까?\n확인을 누르면 재접속 합니다.")
                .setPositiveButton(
                    android.R.string.yes
                ) { dialog, _ ->
                    dialog.dismiss()
                    SendPacket_ResetAllId()
                    msg("모든 ID가 초기화 되었습니다.")
                    activity.reconnection()
                    mHandler.postDelayed({
                        initializeList()
                    }, 1100)
                }
                .setNegativeButton(
                    android.R.string.no
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_info)
                .show()
            prefSetting.saveIsSetting(false)
        } else {
            msg("접속되지 않았습니다.")
        }

    }

    fun checkId(socket: Socket?, button: QButton) {

        gvPacket.SendPacket_NoiseGenerator(socket, gvPacket.CMD_PARA2_CHA, gvPacket.PINK, -40f, 1)
        isButtonEnable(button, false)
        button.text = "체크 중...."
        mHandler.postDelayed({
            gvPacket.SendPacket_NoiseGenerator(socket, gvPacket.CMD_PARA2_CHA, gvPacket.PINK, -40f, 0)
            button.text = "체크"
            isButtonEnable(button, true)
        }, 1000)
    }

    fun loadNamedSocket(): Socket? {
        val index = view.sp_namedSpk.selectedItemPosition
        return listUsedClient[index]
    }

    fun loadnoNamedSocket(): Socket? {
        val index = view.sp_noNamedSpk.selectedItemPosition
        return otherClient[index]
    }


    private fun isButtonEnable(button: QButton, boolean: Boolean) {
        if (boolean) {
            button.isEnabled = true
            button.alpha = 1f
        } else {
            button.isEnabled = false
        }
    }

    fun setId() {
        if (otherClient.size == 0 && listAllClient.size == 0) {
            msg("선택된 스피커가 없습니다.")
        } else {
            setIdAvailable()
        }


    }

    private fun setIdAvailable() {
        val changedId = changedidNo
        gvPacket.SendPacket_SetID(selectedClient?.inetAddress?.hostAddress.toString(), changedId)
        activity.reconnection()
        mHandler.postDelayed({
            initializeList()
        }, 1100)
    }

    fun selectChangedID(position: Int) {
        changedidNo = listAvailableId[position].toCharArray()[0]
    }

    private fun SendPacket_ResetAllId() {
        for (i in listAllClient.indices) {
            if (listAllClient[i] != null) {
                selectedClient = listAllClient[i]
                gvPacket.SendPacket_SetID(selectedClient?.inetAddress?.hostAddress.toString(), '0')
            }
        }
    }

    fun selectOtherClient(position: Int) {
        if (otherClient.size > 0) {
            selectedClient = otherClient[position]
            isButtonEnable(view.btn_noNamedSpkCheck, true)
        } else {
            isButtonEnable(view.btn_noNamedSpkCheck, false)
        }
    }

    fun selectUsedClient(position: Int) {
        if (listUsedId.size > 0 && listUsedClient.size > 0) {
            selectedClient = listUsedClient[position]
            isButtonEnable(view.btn_namedSpkCheck, true)
        } else {
            isButtonEnable(view.btn_namedSpkCheck, false)
        }
    }

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun initializeList() {
        mHandler.post {
            WaitingDialog(view.context!!).create("잠시만 기다려 주세요..", 1200)

        }
        listOtherClientId = ArrayList()
        listAvailableId = ArrayList()
        listAllClient = ArrayList()
        listUsedId = ArrayList()
        initIdList()
        mHandler.postDelayed({
            registerAdapter(view.context!!, listOtherClientId, view.sp_noNamedSpk)
            registerAdapter(view.context!!, listAvailableId, view.sp_changedID)
            registerAdapter(view.context!!, listUsedId, view.sp_namedSpk)
        }, 1000)

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


    private fun initIdList() {
        noNamedList()
        availableIdList()
        namedList()
    }

    private fun namedList() {
        if (listUsedId.size == 0) {
            listUsedId.add("없음")
        }
    }

    private fun noNamedList() {

        if (otherClient.size > 0) {
            for (index in otherClient.indices) {
                listOtherClientId.add("미지정 DSP($index)")
            }
        } else {
            listOtherClientId.add("미지정 없음")
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

}