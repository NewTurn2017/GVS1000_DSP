package com.gvkorea.gvs1000_dsp.fragment.eq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.eq.listener.GEQButtonListener
import com.gvkorea.gvs1000_dsp.fragment.eq.listener.GEQBypassButtonListener
import com.gvkorea.gvs1000_dsp.fragment.eq.listener.GEQSeekBarChangeListener
import com.gvkorea.gvs1000_dsp.fragment.eq.presenter.GEQPresenter
import kotlinx.android.synthetic.main.fragment_eq.*


class GEQFragment : Fragment() {

    lateinit var presenter: GEQPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = GEQPresenter(this)
        initListener()
        presenter.initializeList()
    }

    private fun initListener() {
        btn_geqSpeakerSelect.setOnClickListener(GEQButtonListener(presenter))
        btn_geqReset.setOnClickListener(GEQButtonListener(presenter))
        btn_geqBypass.setOnClickListener(GEQButtonListener(presenter))
        addEQBypassButtonListener()
        addEQSeekBarListener()
    }

    private fun addEQBypassButtonListener() {
        val FIRST_EQ = 1
        val LAST_EQ = 30
        for(i in FIRST_EQ..LAST_EQ){
            val button = activity?.findViewById<Button>(
                activity?.resources?.getIdentifier(
                    "btn_input_eq${i}_bypass",
                    "id",
                    activity?.packageName
                )!!
            )
            button?.setOnClickListener(GEQBypassButtonListener(presenter))
        }
    }

    private fun addEQSeekBarListener() {
        val FIRST_EQ = 1
        val LAST_EQ = 30
        for(i in FIRST_EQ..LAST_EQ){
            val seekBar = activity?.findViewById<SeekBar>(
                activity?.resources?.getIdentifier(
                    "sb_input_eq$i",
                    "id",
                    activity?.packageName
                )!!
            )
            seekBar?.setOnSeekBarChangeListener(GEQSeekBarChangeListener(this, presenter))
        }
    }



}
