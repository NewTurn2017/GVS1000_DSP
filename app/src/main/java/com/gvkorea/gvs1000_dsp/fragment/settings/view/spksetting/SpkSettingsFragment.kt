package com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.listener.SpkSettingsButtonListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.listener.SpkSettingsItemSelectedListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.presenter.SpkSettingsPresenter
import kotlinx.android.synthetic.main.fragment_spk_settings.*

class SpkSettingsFragment(val mHandler: Handler) : Fragment() {

    lateinit var presenter: SpkSettingsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spk_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SpkSettingsPresenter(this, mHandler)
        initListener()
        presenter.initializeList()


    }

    private fun initListener() {
        btn_settingsReset.setOnClickListener(SpkSettingsButtonListener(presenter))
        btn_settingsSave.setOnClickListener(SpkSettingsButtonListener(presenter))
        btn_settingsComplete.setOnClickListener(SpkSettingsButtonListener(presenter))
        sp_spkNo.onItemSelectedListener = SpkSettingsItemSelectedListener(presenter)
    }

    companion object {
        private val EMPTYLIST = "없음"

    }
}
