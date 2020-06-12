package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener.CheckChangeListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener.ReverbButtonListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter.ReverbPresenter
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.util.chart.ChartLayoutLineChartForRT
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
        initChartLayout()
    }

    private fun initChartLayout() {
        chart = ChartLayoutLineChartForRT(this.context!!, mLineChart)
        chart.initLineChartLayout()
    }

    private fun initListener() {
        btn_noiseClap.setOnClickListener(ReverbButtonListener(presenter))
        cb_repeat.setOnCheckedChangeListener(CheckChangeListener())
    }

    companion object {
        lateinit var chart: ChartLayoutLineChartForRT
        var isRepeat = false
        var repeatCount = 0
        lateinit var arrList: ArrayList<FloatArray?>
        lateinit var valuesArrays: ArrayList<ArrayList<Entry>>
        lateinit var labelList: ArrayList<String>
    }
}