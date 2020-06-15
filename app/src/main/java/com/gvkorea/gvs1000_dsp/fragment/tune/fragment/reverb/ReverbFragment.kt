package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.listener.TuneSpeakerItemSelectedListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener.ReverbButtonListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener.ReverbSpeakerItemSelectedListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter.ReverbPresenter
import kotlinx.android.synthetic.main.fragment_auto_tune.*
import kotlinx.android.synthetic.main.fragment_reverb.*

class ReverbFragment(val handler: Handler) : Fragment() {

    private lateinit var presenter: ReverbPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reverb, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ReverbPresenter(this, handler)
        initListener()
        presenter.initializerList()
    }

    private fun initListener() {
        btn_noiseClap.setOnClickListener(ReverbButtonListener(presenter))
        btn_testReset.setOnClickListener(ReverbButtonListener(presenter))
        sp_ReverbSpeakerList.onItemSelectedListener = ReverbSpeakerItemSelectedListener(presenter)
    }

    companion object {
        var reverbCount = 0
    }
}