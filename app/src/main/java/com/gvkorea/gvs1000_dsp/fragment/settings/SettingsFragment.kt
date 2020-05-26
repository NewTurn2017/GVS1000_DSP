package com.gvkorea.gvs1000_dsp.fragment.settings

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gvkorea.gvs1000_dsp.MainActivity

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.listener.SettingsSelectPanelListener
import com.gvkorea.gvs1000_dsp.fragment.settings.presenter.SettingsPresenter
import com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.PresetFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.ResetSettingFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.SetIdFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.SetModeFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.SpkSettingsFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.SyncFragment
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment(val activity: MainActivity, val mHandler: Handler) : Fragment() {

    val setidFragment: SetIdFragment by lazy { SetIdFragment(activity, mHandler) }
    val resetSettingFragment: ResetSettingFragment by lazy { ResetSettingFragment() }
    val setModeFragment: SetModeFragment by lazy { SetModeFragment() }
    val spkSettingsFragment: SpkSettingsFragment by lazy { SpkSettingsFragment(mHandler) }
    val syncFragment: SyncFragment by lazy { SyncFragment() }
    val presetFragment: PresetFragment by lazy { PresetFragment() }

    lateinit var presenter: SettingsPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter  = SettingsPresenter(this)
        initListener()


    }

    private fun initListener() {
        btn_ID_setting.setOnClickListener(SettingsSelectPanelListener(presenter))
        btn_resetSettings.setOnClickListener(SettingsSelectPanelListener(presenter))
        btn_setMode.setOnClickListener(SettingsSelectPanelListener(presenter))
        btn_sync.setOnClickListener(SettingsSelectPanelListener(presenter))
        btn_spk_setting.setOnClickListener(SettingsSelectPanelListener(presenter))
        btn_preset.setOnClickListener(SettingsSelectPanelListener(presenter))
    }

}
