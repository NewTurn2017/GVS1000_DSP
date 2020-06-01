package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.audio.RecordAudioRta
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.chart.ChartLayoutLineChartForAverage
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.listener.ButtonListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.listener.EvalcheckChangeListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.listener.SpinnerItemSeletedListener
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.presenter.EvalPresenter
import com.gvkorea.gvs1000_dsp.util.PrefSetting
import kotlinx.android.synthetic.main.fragment_eval.*

class EvalFragment : Fragment() {

    lateinit var presenter: EvalPresenter
    val handler = Handler()
    lateinit var recordAudioRta: RecordAudioRta

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eval, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = EvalPresenter(this, handler)
        presenter.initTableList()
        initListener()
        init_ChartLayout()
        presenter.initializeList()
        updateNoiseVolume()

    }

    private fun updateNoiseVolume(){
        val noiseVolume = "NOISE VOLUME : ${prefSetting.getNoiseVolumePref()} dB"
        tv_noiseVolume.text = noiseVolume
    }

    private fun initListener() {
        cb_eval_repeat.setOnCheckedChangeListener(EvalcheckChangeListener())
        btn_noiseOn.setOnClickListener(ButtonListener(presenter))
        btn_noiseOff.setOnClickListener(ButtonListener(presenter))
        btn_measureAvg.setOnClickListener(ButtonListener(presenter))
        btn_save.setOnClickListener(ButtonListener(presenter))
        sp_avgTime.onItemSelectedListener = SpinnerItemSeletedListener(this)
        val arrayAdapter = ArrayAdapter(context!!, R.layout.spinner_item ,resources.getStringArray(R.array.avgTime_array) )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_avgTime.adapter = arrayAdapter

    }

    private fun init_ChartLayout() {
        //lineChart for Average
        chart = ChartLayoutLineChartForAverage(view?.context!!, mChart)
        chart.initLineChartLayout(100f, 20f)
        chart.initGraph(null, "Average", Color.RED)

    }

    fun recordTaskStart() {
        handler.postDelayed({
            if (!isStarted) {
                isStarted = true
                mChart.clear()
                recordAudioRta = RecordAudioRta(this.view!!)
                recordAudioRta.execute()
            }
        }, 100)
    }

    fun recordTaskStop() {
        if (isStarted) {
            isStarted = false
            recordAudioRta.cancel(true)
        }
    }

    override fun onStart() {
        super.onStart()
        recordTaskStart()
    }

    override fun onStop() {
        super.onStop()
        recordTaskStop()
    }

    companion object {
        var averageCount = 1
        var isStarted = false
        lateinit var chart: ChartLayoutLineChartForAverage
        var averageTime = 1

        lateinit var arrEvalList: ArrayList<ArrayList<String>>
        lateinit var valuesEvalArrays: ArrayList<ArrayList<Entry>>
        lateinit var labelEvalList: ArrayList<String>
        var repeatEvalCount = 0
        var isEvalRepeat = false

    }

}
