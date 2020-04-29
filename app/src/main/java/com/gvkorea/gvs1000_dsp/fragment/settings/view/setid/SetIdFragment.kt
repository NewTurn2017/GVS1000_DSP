package com.gvkorea.gvs1000_dsp.fragment.settings.view.setid

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gvkorea.gvs1000_dsp.MainActivity

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.listener.SetIdButtonListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.listener.SetIdItemSelectedListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.presenter.SetIdPresenter
import kotlinx.android.synthetic.main.fragment_setid.*
import java.net.Socket


class SetIdFragment(val activity: MainActivity, val mHandler: Handler) : Fragment() {

    lateinit var presenter: SetIdPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SetIdPresenter(this, mHandler, activity)
        initListener()
        presenter.initializeList()
    }

    private fun initListener() {
        btn_resetId.setOnClickListener(SetIdButtonListener(presenter))
        btn_noNamedSpkCheck.setOnClickListener(SetIdButtonListener(presenter))
        btn_namedSpkCheck.setOnClickListener(SetIdButtonListener(presenter))
        btn_changeID.setOnClickListener(SetIdButtonListener(presenter))
        sp_noNamedSpk.onItemSelectedListener = SetIdItemSelectedListener(presenter)
        sp_namedSpk.onItemSelectedListener = SetIdItemSelectedListener(presenter)
        sp_changedID.onItemSelectedListener = SetIdItemSelectedListener(presenter)
    }

    companion object {

        //null 포함 모든 Socket list
        var listAllClient = ArrayList<Socket?>()

        //null 제외 Socket list(현재 지정된)
        var listUsedClient = ArrayList<Socket?>()
        //listUsedClient의 ID
        var listUsedId = ArrayList<String>()

        //미지정 DSP list
        var listOtherClientId = ArrayList<String>()
        //사용가능한 ID(미지정)
        var listAvailableId = ArrayList<String>()

    }

}
