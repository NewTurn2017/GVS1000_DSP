package com.gvkorea.gvs1000_dsp.fragment.volume

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.spkList
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.volume.listener.MuteButtonListener
import com.gvkorea.gvs1000_dsp.fragment.volume.listener.VolumeSeekBarChangeListener
import com.gvkorea.gvs1000_dsp.fragment.volume.presenter.VolumePresenter
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_volume.*

class VolumeFragment : Fragment() {

    lateinit var presenter: VolumePresenter
    val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter = VolumePresenter(this, handler)
        initListener()
        presenter.loadVolume()
    }

    private fun initListener() {
        addVolumeSeekBarListener()
        addMuteButtonListener()
    }


    private fun initView() {
        val numOfSpeaker = spkList.size
        makeLayout(numOfSpeaker)
        applyName()

    }

    private fun applyName() {

        for (i in spkList.indices) {
            setName(i)
        }
    }

    private fun setName(i: Int) {
        when (i) {
            0 -> tv_spk1_name.text = spkList[i].name
            1 -> tv_spk2_name.text = spkList[i].name
            2 -> tv_spk3_name.text = spkList[i].name
            3 -> tv_spk4_name.text = spkList[i].name
            4 -> tv_spk5_name.text = spkList[i].name
            5 -> tv_spk6_name.text = spkList[i].name
            6 -> tv_spk7_name.text = spkList[i].name
            7 -> tv_spk8_name.text = spkList[i].name
        }
    }

    private fun makeLayout(numOfSpeaker: Int) {

        val layoutList = arrayListOf(
            lay_spk1, lay_spk2, lay_spk3, lay_spk4
            , lay_spk5, lay_spk6, lay_spk7, lay_spk8
        )
        if (numOfSpeaker != layoutList.size) {
            for (i in numOfSpeaker until layoutList.size) {
                hideLayout(layoutList[i])
            }
        }
    }

    fun hideLayout(layout: ViewGroup) {
        val foregroundTran =
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.trans_disenable))

        layout.foreground = foregroundTran

        for (i in 0 until layout.childCount) {
            val child: View? = layout.getChildAt(i)
            child?.isEnabled = false
            if (child is ViewGroup)
                hideLayout(child)
        }
    }

    private fun addVolumeSeekBarListener() {
        val FIRST_ID = 1
        val LAST_ID = 8
        for (i in FIRST_ID..LAST_ID) {
            val seekBar = activity?.findViewById<SeekBar>(
                activity?.resources?.getIdentifier(
                    "sb_spk$i",
                    "id",
                    activity?.packageName
                )!!
            )
            seekBar?.setOnSeekBarChangeListener(VolumeSeekBarChangeListener(this, presenter))
        }
    }

    private fun addMuteButtonListener() {
        val FIRST_ID = 1
        val LAST_ID = 8
        for (i in FIRST_ID..LAST_ID) {
            val button = activity?.findViewById<QButton>(
                activity?.resources?.getIdentifier(
                    "btn_spk${i}_mute",
                    "id",
                    activity?.packageName
                )!!
            )
            button?.setOnClickListener(MuteButtonListener(this, presenter))
        }
    }


}
