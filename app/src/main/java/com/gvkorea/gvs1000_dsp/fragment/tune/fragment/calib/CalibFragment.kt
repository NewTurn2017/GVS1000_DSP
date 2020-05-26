package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.audio.RecordAudioCalib
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.audio.RecordAudioCalib.Companion.started
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.chart.ChartLayoutBarChart
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.listener.CalibListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.presenter.CalibPresenter
import kotlinx.android.synthetic.main.fragment_caib.*


class CalibFragment : Fragment() {

    lateinit var recordTaskCalib : RecordAudioCalib
    var handler = Handler()
    lateinit var presenter : CalibPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caib, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = CalibPresenter(this)
        init_ChartLayout(view)
        init_Listener()
    }

    private fun init_ChartLayout(view: View) {
        val chartLayout = ChartLayoutBarChart(view.context, barChart_calib)
        chartLayout.initBarChartLayout(120f, 20f)
    }

    private fun init_Listener(){
        btn_autoCalib.setOnClickListener(CalibListener(presenter))
        btn_calibReset.setOnClickListener(CalibListener(presenter))
    }

    override fun onStop() {
        super.onStop()
        recordTaskStop()
    }

    override fun onStart() {
        super.onStart()
        recordTaskStart()

    }

    private fun recordTaskStart() {
        handler.postDelayed({
            if(!started){
                started = true
                barChart_calib.clear()
                recordTaskCalib = RecordAudioCalib(this, barChart_calib)
                recordTaskCalib.execute()
            }
        }, 100)
    }


    private fun recordTaskStop() {
        if(started){
            started = false
            recordTaskCalib.cancel(true)
        }
    }

}
