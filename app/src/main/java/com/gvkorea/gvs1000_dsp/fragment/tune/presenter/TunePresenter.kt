package com.gvkorea.gvs1000_dsp.fragment.tune.presenter

import android.content.Context
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.TuneFragment
import com.gvkorea.gvs1000_dsp.util.GVPacket
import com.gvkorea.gvs1000_dsp.util.GVSTunePanel
import com.gvkorea.gvs1000_dsp.util.replaceChild
import com.manojbhadane.QButton
import kotlinx.android.synthetic.main.fragment_tune.*

class TunePresenter(val view: TuneFragment, val handler: Handler) {
    val packet = GVPacket(view)

    fun selectPanel(panel: GVSTunePanel) {
        resetButtonColor()
        when(panel){
            GVSTunePanel.CALIB -> replacePanel(view.calibFragment, view.btn_calibration)
            GVSTunePanel.AUTO_TUNE -> replacePanel(view.autoTuneFragment, view.btn_autoTune)
            GVSTunePanel.EVAL -> replacePanel(view.evalFragment, view.btn_evalueation)
        }
    }

    private fun resetButtonColor() {
        setButtonColor(view.btn_calibration, R.color.white)
        setButtonColor(view.btn_autoTune, R.color.white)
    }

    private fun replacePanel(fragment: Fragment, button: QButton) {
        view.replaceChild(R.id.container_tune, fragment)
        setButtonColor(button, android.R.color.holo_red_light)
    }

    private fun setButtonColor(button: QButton, color: Int) {
        button.setTextColor(ContextCompat.getColor(view.context!!, color))
    }
}