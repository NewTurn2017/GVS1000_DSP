package com.gvkorea.gvs1000_dsp.fragment.eq.presenter

import android.R
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import kotlinx.android.synthetic.main.fragment_eq.*

class GEQPresenter(val view: GEQFragment) {

    val packet = GVPacket(view)
    var nameList = ArrayList<String>()


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
        val LAST_EQ = 30
        for(i in FIRST_EQ..LAST_EQ){
            val textView = view.activity?.findViewById<TextView>(
                view.activity?.resources?.getIdentifier(
                    "tv_input_eq$i",
                    "id",
                    view.activity?.packageName
                )!!
            )
            textView?.text = "0"
        }

        for(i in FIRST_EQ..LAST_EQ){
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
}