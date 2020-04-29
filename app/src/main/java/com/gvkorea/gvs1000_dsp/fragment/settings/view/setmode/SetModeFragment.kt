package com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.listener.SetModeButtonListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.presenter.SetModePresenter
import kotlinx.android.synthetic.main.fragment_set_mode.*


class SetModeFragment : Fragment() {

    lateinit var presenter: SetModePresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_mode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = SetModePresenter(this)
        initListener()
        presenter.initializeList()
    }

    private fun initListener() {
        btn_setModeSpeakerSelect.setOnClickListener(SetModeButtonListener(presenter))
        btn_saveMode.setOnClickListener(SetModeButtonListener(presenter))
    }

}
