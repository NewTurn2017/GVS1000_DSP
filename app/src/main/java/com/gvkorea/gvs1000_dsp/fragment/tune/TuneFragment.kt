package com.gvkorea.gvs1000_dsp.fragment.tune

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.AutoTuneFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.CalibFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.listener.TuneSelectPanelListener
import com.gvkorea.gvs1000_dsp.fragment.tune.presenter.TunePresenter
import kotlinx.android.synthetic.main.fragment_tune.*

class TuneFragment : Fragment() {

    lateinit var presenter: TunePresenter
    val handler = Handler()

    val calibFragment: CalibFragment by lazy { CalibFragment() }
    val autoTuneFragment: AutoTuneFragment by lazy { AutoTuneFragment() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tune, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = TunePresenter(this, handler)
        initListener()
    }

    private fun initListener() {
        btn_calibration.setOnClickListener(TuneSelectPanelListener(presenter))
        btn_autoTune.setOnClickListener(TuneSelectPanelListener(presenter))
    }

}

