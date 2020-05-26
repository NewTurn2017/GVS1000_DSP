package com.gvkorea.gvs1000_dsp.fragment.settings.view.preset

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.listener.PresetButtonListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.preset.presenter.PresetPresenter
import kotlinx.android.synthetic.main.fragment_preset.*


class PresetFragment : Fragment() {

    lateinit var presenter: PresetPresenter
    val handler = Handler()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = PresetPresenter(this, handler)
        initListener()
        presenter.initializeList()
    }

    private fun initListener() {
        btn_presetSave.setOnClickListener(PresetButtonListener(presenter))
        btn_presetLoad.setOnClickListener(PresetButtonListener(presenter))
        btn_presetDelete.setOnClickListener(PresetButtonListener(presenter))
        btn_presetRequest.setOnClickListener(PresetButtonListener(presenter))
    }

}
