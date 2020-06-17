package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.presenter

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.CALIBRATION
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.prefSetting
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.CalibFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.audio.RecordAudioCalib.Companion.rmsValue

class CalibPresenter(val view: CalibFragment) {

    fun autoCalib() {
        prefSetting.loadCalib()
        CALIBRATION += (94 - rmsValue).toFloat()
        prefSetting.saveIsCalib(true)
        prefSetting.saveCalib()
        msg("설정이 완료되었습니다.")
    }

    fun calibReset() {

        AlertDialog.Builder(view.context!!)
                .setTitle("Calibration 초기화")
                .setMessage("Calibration을 초기화 하면 다시 설정해야 합니다.")
                .setPositiveButton(
                        android.R.string.yes
                ) { dialog, _ ->
                    dialog.dismiss()
                    CALIBRATION = 0f
                    prefSetting.saveIsCalib(false)
                    msg("CALIBRATION 값이 초기화 되었습니다.")
                }
                .setNegativeButton(
                        android.R.string.no
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_info)
                .show()
    }

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

}