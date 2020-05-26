package com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.presenter

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.presetSavedList
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.PresetFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment.Companion.listUsedId
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.WaitingDialog
import kotlinx.android.synthetic.main.dialog_preset_delete.*
import kotlinx.android.synthetic.main.dialog_preset_load.*
import kotlinx.android.synthetic.main.dialog_preset_request.*
import kotlinx.android.synthetic.main.dialog_preset_save.*
import kotlinx.android.synthetic.main.fragment_preset.*
import java.net.Socket

class PresetPresenter(val view: PresetFragment, val handler: Handler) {
    val packet = GVPacket(view)
    var nameList = ArrayList<String>()

    fun initializeList() {
        makeNameList()
        registerAdapter(view.context!!, nameList, view.sp_presetSpeakerList)
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
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(com.gvkorea.gvs1000_dsp.R.layout.spinner_dropdown)
        adapter.notifyDataSetChanged()
        spinner.adapter = adapter
    }

    fun presetSaveDialog() {
        val innerView = View.inflate(view.context, R.layout.dialog_preset_save, null)
        val dialog = Dialog(view.context!!)
        dialog.setContentView(innerView)
        dialog.setCancelable(false)
        dialog.btn_presetSaveApply.setOnClickListener {
            val title = dialog.sp_presetSaveList.selectedItem.toString()
            val presetNo = title.substring(6).toInt() - 1
            presetSave(selectSocket(), presetNo, title)
            msg("저장 완료")
            dialog.dismiss()
        }
        dialog.btn_presetSaveCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun presetSave(socket: Socket?, presetNo: Int, title: String) {
        packet.savePreset(socket, presetNo, title)
    }

    fun makeSavedListLoadDialog(){
        presetSavedList = ArrayList()

        packet.SendPacket_Preset_GetName_All(selectSocket())
        handler.post {
            WaitingDialog(view.context!!).create("프리셋 리스트 생성 중입니다...", 1000L)
        }
        handler.postDelayed({
            presetLoadDialog()
        }, 1000L)
    }

    fun presetLoadDialog() {

        val innerView = View.inflate(view.context, R.layout.dialog_preset_load, null)
        val dialog = Dialog(view.context!!)
        dialog.setContentView(innerView)
        dialog.setCancelable(false)
        if(presetSavedList.size == 0){
            presetSavedList.add("저장된 프리셋 없음")
        }
        registerAdapter(view.context!!, presetSavedList, dialog.sp_presetLoadList)
        dialog.btn_presetLoadApply.setOnClickListener {
            if(presetSavedList[0] != "저장된 프리셋 없음"){
                val title = dialog.sp_presetLoadList.selectedItem.toString()
                val presetNo = title.substring(6).toInt() - 1
                presetLoad(presetNo)
                msg("로드됨")
            }else{
                msg("저장된 프리셋이 없습니다.")
            }

            dialog.dismiss()
        }
        dialog.btn_presetLoadCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun presetLoad(presetNo: Int) {
        packet.SendPacket_Preset_Load(selectSocket(), presetNo)
    }

    fun makeSavedListRequestDialog() {
        presetSavedList = ArrayList()
        packet.SendPacket_Preset_GetName_All(selectSocket())
        handler.post {
            WaitingDialog(view.context!!).create("프리셋 리스트 생성 중입니다...", 1000L)
        }
        handler.postDelayed({
            presetRequestDialog()
        }, 1000L)
    }

    private fun presetRequestDialog() {
        val innerView = View.inflate(view.context, R.layout.dialog_preset_request, null)
        val dialog = Dialog(view.context!!)
        dialog.setContentView(innerView)
        dialog.setCancelable(false)
        dialog.btn_presetRequestConfirm.isEnabled = false
        if(presetSavedList.size == 0){
            presetSavedList.add("저장된 프리셋 없음")
        }
        registerAdapter(view.context!!, presetSavedList, dialog.sp_presetRequestList)
        dialog.btn_presetSelect.setOnClickListener {
            if(presetSavedList[0] != "저장된 프리셋 없음"){
                val title = dialog.sp_presetRequestList.selectedItem.toString()
                val presetNo = title.substring(6).toInt() - 1
                dialog.btn_presetRequestConfirm.isEnabled = true
                dialog.btn_presetRequestConfirm.alpha = 1f
                presetSelect(presetNo)
            }else{
                msg("저장된 프리셋이 없습니다.")
            }
        }

        dialog.btn_presetRequestConfirm.setOnClickListener {
            presetRequestAll()
        }

        dialog.btn_presetRequestCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun presetSelect(presetNo: Int) {
        packet.SendPacket_Preset_Select(selectSocket(), presetNo)
    }

    private fun presetRequestAll() {
        packet.SendPacket_Preset_Request_All(selectSocket())
    }

    fun makeSavedListDeleteDialog(){
        presetSavedList = ArrayList()
        packet.SendPacket_Preset_GetName_All(selectSocket())
        handler.post {
            WaitingDialog(view.context!!).create("프리셋 리스트 생성 중입니다...", 1000L)
        }
        handler.postDelayed({
            presetDeleteDialog()
        }, 1000L)
    }

    fun presetDeleteDialog() {
        val innerView = View.inflate(view.context, R.layout.dialog_preset_delete, null)
        val dialog = Dialog(view.context!!)
        dialog.setContentView(innerView)
        dialog.setCancelable(false)

        if(presetSavedList.size == 0){
            presetSavedList.add("저장된 프리셋 없음")
        }
        registerAdapter(view.context!!, presetSavedList, dialog.sp_presetDeleteList)

        dialog.btn_presetDeleteAll.setOnClickListener {


            if(presetSavedList[0] != "저장된 프리셋 없음"){
                presetDeleteAll(dialog.context)
            }else{
                msg("저장된 프리셋이 없습니다.")
            }
            dialog.dismiss()
        }

        dialog.btn_presetDeleteApply.setOnClickListener {

            if(presetSavedList[0] != "저장된 프리셋 없음"){
                val title = dialog.sp_presetDeleteList.selectedItem.toString()
                val presetNo = title.substring(6).toInt() - 1
                presetDelete(selectSocket(), presetNo)
            }else{
                msg("저장된 프리셋이 없습니다.")
            }

            dialog.dismiss()
        }

        dialog.btn_presetDeleteCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun presetDelete(socket: Socket?, presetNo: Int) {
        packet.SendPacket_Preset_Delete(socket, presetNo)
    }

    private fun presetDeleteAll(context: Context) {
        AlertDialog.Builder(context)
                .setTitle("모든 프리셋 설정 삭제")
                .setMessage("모든 프리셋 설정을 삭제 하시겠습니까?")
                .setPositiveButton(
                        android.R.string.yes
                ) { dialog, _ ->
                    dialog.dismiss()
                    packet.SendPacket_Preset_Delete_All(selectSocket())
                    Toast.makeText(view.context, "초기화 되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(
                        android.R.string.no
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_info)
                .show()
    }

    private fun selectSocket(): Socket? {
        val index = view.sp_presetSpeakerList.selectedItemPosition
        return spkList[index].socket
    }



    fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }
}