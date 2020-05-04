package com.gvkorea.gvs1000_dsp.fragment.eq.listener

import android.widget.SeekBar
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.fragment.eq.presenter.GEQPresenter
import kotlinx.android.synthetic.main.fragment_eq.*

class GEQSeekBarChangeListener(
    val view: GEQFragment,
    val presenter: GEQPresenter
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id){
            R.id.sb_input_eq1 -> presenter.geqControl(0, progress, view.tv_input_eq1, view.btn_input_eq1_bypass)
            R.id.sb_input_eq2 -> presenter.geqControl(1, progress, view.tv_input_eq2, view.btn_input_eq2_bypass)
            R.id.sb_input_eq3 -> presenter.geqControl(2, progress, view.tv_input_eq3, view.btn_input_eq3_bypass)
            R.id.sb_input_eq4 -> presenter.geqControl(3, progress, view.tv_input_eq4, view.btn_input_eq4_bypass)
            R.id.sb_input_eq5 -> presenter.geqControl(4, progress, view.tv_input_eq5, view.btn_input_eq5_bypass)
            R.id.sb_input_eq6 -> presenter.geqControl(5, progress, view.tv_input_eq6, view.btn_input_eq6_bypass)
            R.id.sb_input_eq7 -> presenter.geqControl(6, progress, view.tv_input_eq7, view.btn_input_eq7_bypass)
            R.id.sb_input_eq8 -> presenter.geqControl(7, progress, view.tv_input_eq8, view.btn_input_eq8_bypass)
            R.id.sb_input_eq9 -> presenter.geqControl(8, progress, view.tv_input_eq9, view.btn_input_eq9_bypass)
            R.id.sb_input_eq10 -> presenter.geqControl(9, progress, view.tv_input_eq10, view.btn_input_eq10_bypass)
            R.id.sb_input_eq11 -> presenter.geqControl(10, progress, view.tv_input_eq11, view.btn_input_eq11_bypass)
            R.id.sb_input_eq12 -> presenter.geqControl(11, progress, view.tv_input_eq12, view.btn_input_eq12_bypass)
            R.id.sb_input_eq13 -> presenter.geqControl(12, progress, view.tv_input_eq13, view.btn_input_eq13_bypass)
            R.id.sb_input_eq14 -> presenter.geqControl(13, progress, view.tv_input_eq14, view.btn_input_eq14_bypass)
            R.id.sb_input_eq15 -> presenter.geqControl(14, progress, view.tv_input_eq15, view.btn_input_eq15_bypass)
            R.id.sb_input_eq16 -> presenter.geqControl(15, progress, view.tv_input_eq16, view.btn_input_eq16_bypass)
            R.id.sb_input_eq17 -> presenter.geqControl(16, progress, view.tv_input_eq17, view.btn_input_eq17_bypass)
            R.id.sb_input_eq18 -> presenter.geqControl(17, progress, view.tv_input_eq18, view.btn_input_eq18_bypass)
            R.id.sb_input_eq19 -> presenter.geqControl(18, progress, view.tv_input_eq19, view.btn_input_eq19_bypass)
            R.id.sb_input_eq20 -> presenter.geqControl(19, progress, view.tv_input_eq20, view.btn_input_eq20_bypass)
            R.id.sb_input_eq21 -> presenter.geqControl(20, progress, view.tv_input_eq21, view.btn_input_eq21_bypass)
            R.id.sb_input_eq22 -> presenter.geqControl(21, progress, view.tv_input_eq22, view.btn_input_eq22_bypass)
            R.id.sb_input_eq23 -> presenter.geqControl(22, progress, view.tv_input_eq23, view.btn_input_eq23_bypass)
            R.id.sb_input_eq24 -> presenter.geqControl(23, progress, view.tv_input_eq24, view.btn_input_eq24_bypass)
            R.id.sb_input_eq25 -> presenter.geqControl(24, progress, view.tv_input_eq25, view.btn_input_eq25_bypass)
            R.id.sb_input_eq26 -> presenter.geqControl(25, progress, view.tv_input_eq26, view.btn_input_eq26_bypass)
            R.id.sb_input_eq27 -> presenter.geqControl(26, progress, view.tv_input_eq27, view.btn_input_eq27_bypass)
            R.id.sb_input_eq28 -> presenter.geqControl(27, progress, view.tv_input_eq28, view.btn_input_eq28_bypass)
            R.id.sb_input_eq29 -> presenter.geqControl(28, progress, view.tv_input_eq29, view.btn_input_eq29_bypass)
            R.id.sb_input_eq30 -> presenter.geqControl(29, progress, view.tv_input_eq30, view.btn_input_eq30_bypass)
            R.id.sb_input_eq31 -> presenter.geqControl(30, progress, view.tv_input_eq31, view.btn_input_eq31_bypass)

        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}