package com.gvkorea.gvs1000_dsp.fragment.eq.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.fragment.eq.presenter.GEQPresenter
import kotlinx.android.synthetic.main.fragment_eq.*

class GEQBypassButtonListener(val view: GEQFragment, val presenter: GEQPresenter) : View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_input_eq1_bypass -> presenter.geqEachBypass(0, view.btn_input_eq1_bypass, view.sb_input_eq1)
            R.id.btn_input_eq2_bypass -> presenter.geqEachBypass(1, view.btn_input_eq2_bypass, view.sb_input_eq2)
            R.id.btn_input_eq3_bypass -> presenter.geqEachBypass(2, view.btn_input_eq3_bypass, view.sb_input_eq3)
            R.id.btn_input_eq4_bypass -> presenter.geqEachBypass(3, view.btn_input_eq4_bypass, view.sb_input_eq4)
            R.id.btn_input_eq5_bypass -> presenter.geqEachBypass(4, view.btn_input_eq5_bypass, view.sb_input_eq5)
            R.id.btn_input_eq6_bypass -> presenter.geqEachBypass(5, view.btn_input_eq6_bypass, view.sb_input_eq6)
            R.id.btn_input_eq7_bypass -> presenter.geqEachBypass(6, view.btn_input_eq7_bypass, view.sb_input_eq7)
            R.id.btn_input_eq8_bypass -> presenter.geqEachBypass(7, view.btn_input_eq8_bypass, view.sb_input_eq8)
            R.id.btn_input_eq9_bypass -> presenter.geqEachBypass(8, view.btn_input_eq9_bypass, view.sb_input_eq9)
            R.id.btn_input_eq10_bypass -> presenter.geqEachBypass(9, view.btn_input_eq10_bypass, view.sb_input_eq10)
            R.id.btn_input_eq11_bypass -> presenter.geqEachBypass(10, view.btn_input_eq11_bypass, view.sb_input_eq11)
            R.id.btn_input_eq12_bypass -> presenter.geqEachBypass(11, view.btn_input_eq12_bypass, view.sb_input_eq12)
            R.id.btn_input_eq13_bypass -> presenter.geqEachBypass(12, view.btn_input_eq13_bypass, view.sb_input_eq13)
            R.id.btn_input_eq14_bypass -> presenter.geqEachBypass(13, view.btn_input_eq14_bypass, view.sb_input_eq14)
            R.id.btn_input_eq15_bypass -> presenter.geqEachBypass(14, view.btn_input_eq15_bypass, view.sb_input_eq15)
            R.id.btn_input_eq16_bypass -> presenter.geqEachBypass(15, view.btn_input_eq16_bypass, view.sb_input_eq16)
            R.id.btn_input_eq17_bypass -> presenter.geqEachBypass(16, view.btn_input_eq17_bypass, view.sb_input_eq17)
            R.id.btn_input_eq18_bypass -> presenter.geqEachBypass(17, view.btn_input_eq18_bypass, view.sb_input_eq18)
            R.id.btn_input_eq19_bypass -> presenter.geqEachBypass(18, view.btn_input_eq19_bypass, view.sb_input_eq19)
            R.id.btn_input_eq20_bypass -> presenter.geqEachBypass(19, view.btn_input_eq20_bypass, view.sb_input_eq20)
            R.id.btn_input_eq21_bypass -> presenter.geqEachBypass(20, view.btn_input_eq21_bypass, view.sb_input_eq21)
            R.id.btn_input_eq22_bypass -> presenter.geqEachBypass(21, view.btn_input_eq22_bypass, view.sb_input_eq22)
            R.id.btn_input_eq23_bypass -> presenter.geqEachBypass(22, view.btn_input_eq23_bypass, view.sb_input_eq23)
            R.id.btn_input_eq24_bypass -> presenter.geqEachBypass(23, view.btn_input_eq24_bypass, view.sb_input_eq24)
            R.id.btn_input_eq25_bypass -> presenter.geqEachBypass(24, view.btn_input_eq25_bypass, view.sb_input_eq25)
            R.id.btn_input_eq26_bypass -> presenter.geqEachBypass(25, view.btn_input_eq26_bypass, view.sb_input_eq26)
            R.id.btn_input_eq27_bypass -> presenter.geqEachBypass(26, view.btn_input_eq27_bypass, view.sb_input_eq27)
            R.id.btn_input_eq28_bypass -> presenter.geqEachBypass(27, view.btn_input_eq28_bypass, view.sb_input_eq28)
            R.id.btn_input_eq29_bypass -> presenter.geqEachBypass(28, view.btn_input_eq29_bypass, view.sb_input_eq29)
            R.id.btn_input_eq30_bypass -> presenter.geqEachBypass(29, view.btn_input_eq30_bypass, view.sb_input_eq30)
            R.id.btn_input_eq31_bypass -> presenter.geqEachBypass(30, view.btn_input_eq31_bypass, view.sb_input_eq31)
        }
    }
}