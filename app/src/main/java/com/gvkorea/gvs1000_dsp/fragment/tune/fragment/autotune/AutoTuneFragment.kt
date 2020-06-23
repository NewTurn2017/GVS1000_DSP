package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gvkorea.gvs1000_dsp.MainActivity

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.audio.RecordAudioTune
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.chart.ChartLayoutBarChartForEQGraph
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.chart.ChartLayoutLineChartForTune
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.listener.TuneButtonListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.listener.TuneSpeakerItemSelectedListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.presenter.AutoTunePresenter
import com.gvkorea.gvs1000_dsp.fragment.tune.presenter.TunePresenter
import com.gvkorea.gvs1000_dsp.util.Helper
import kotlinx.android.synthetic.main.fragment_auto_tune.*

class AutoTuneFragment(val mHandler: Handler) : Fragment() {


    lateinit var presenter: AutoTunePresenter
    lateinit var recordAudioTune: RecordAudioTune
    val helper = Helper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auto_tune, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = AutoTunePresenter(this, helper, mHandler)
        presenter.initializerList()
        initListener()
        init_ChartLayout()
//        connectFireBaseStorage()

    }

    private fun connectFireBaseStorage() {
        //gs://gvs1000-dsp.appspot.com/models/gvs-500
        val storage = FirebaseStorage.getInstance("gs://gvs1000-dsp.appspot.com")
        storageRef = storage.reference
    }

    private fun initListener() {
        btn_tune_start.setOnClickListener(TuneButtonListener(presenter))
        btn_tune_stop.setOnClickListener(TuneButtonListener(presenter))
        btn_showTable.setOnClickListener(TuneButtonListener(presenter))
        btn_showEQ.setOnClickListener(TuneButtonListener(presenter))
        btn_tuneNoise.setOnClickListener(TuneButtonListener(presenter))
        btn_avgRepeat.setOnClickListener(TuneButtonListener(presenter))
        sp_TuneSpeakerList.onItemSelectedListener = TuneSpeakerItemSelectedListener(presenter)

    }

    private fun init_ChartLayout() {
        init_lineChart()
        init_barChart()
    }

    fun init_lineChart() {
        lineChart = ChartLayoutLineChartForTune(this.context!!, chart_tune_line)
        targetValues = presenter.setTarget(targetdB.toInt()-15)
        lineChart.initLineChartLayout(100f, 20f)
        lineChart.initGraph(targetValues, "Target(dB)", Color.argb(120, 0, 0, 255))
    }

    fun init_barChart(){
        barChart = ChartLayoutBarChartForEQGraph(this.context!!, chart_tune_bar)
        barChart.initLineChartLayout(15f, -15f)
        val curEQ = FloatArray(31)
        barChart.initGraph(curEQ)
    }

    override fun onStart() {
        super.onStart()
        recordTaskStart()
    }

    override fun onStop() {
        super.onStop()
        recordTaskStop()
    }

    fun recordTaskStart() {
        mHandler.postDelayed({
            if (!isStarted) {
                isStarted = true
                recordAudioTune = RecordAudioTune(this.view!!)
                recordAudioTune.execute()
            }
        }, 100)
    }

    fun recordTaskStop() {
        if (isStarted) {
            isStarted = false
            recordAudioTune.cancel(true)
        }
    }

    override fun onDetach() {
        super.onDetach()
        helper.dismissProgressDialog()
        helper.dismissDialog()
    }

    companion object {
        var isStarted = false
        var noiseVolume = -40
        var targetdB: Double = 85.0
        lateinit var lineChart: ChartLayoutLineChartForTune
        lateinit var barChart: ChartLayoutBarChartForEQGraph
        var targetValues:FloatArray? = null
        var initialValues:FloatArray? = null
        var isShowTable = false
        var isShowEQ = false

        lateinit var storageRef: StorageReference
        var curModelPath = ""
        var tuningCounter = 0

    }
}
