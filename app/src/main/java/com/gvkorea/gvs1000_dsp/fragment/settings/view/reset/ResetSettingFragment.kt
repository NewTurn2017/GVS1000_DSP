package com.gvkorea.gvs1000_dsp.fragment.settings.view.reset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.listener.ResetSettingButtonListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.presenter.ResetSettingPresenter
import kotlinx.android.synthetic.main.fragment_reset_setting.*


class ResetSettingFragment : Fragment() {

    lateinit var presenter: ResetSettingPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ResetSettingPresenter(this)
        initlistener()
        presenter.initializeList()

    }

    private fun initlistener() {
        btn_resetSpeakerSelect.setOnClickListener(ResetSettingButtonListener(presenter))
        btn_resetSettingAll.setOnClickListener(ResetSettingButtonListener(presenter))
    }

}
