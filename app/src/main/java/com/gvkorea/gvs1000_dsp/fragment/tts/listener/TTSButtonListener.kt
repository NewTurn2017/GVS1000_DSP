package com.gvkorea.gvs1000_dsp.fragment.tts.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import com.gvkorea.gvs1000_dsp.fragment.tts.presenter.TTSPresenter
import kotlinx.android.synthetic.main.fragment_tts.*

class TTSButtonListener(val view: TTSFragment, val presenter: TTSPresenter) : View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_selectLang -> {
                presenter.showLangDialog()
            }
            R.id.btn_resetPitch -> {
                view.sb_pitch.progress = 10
            }
            R.id.btn_resetRate -> {
                view.sb_rate.progress = 14
            }
            R.id.btn_text_clear -> {
                view.et_text.setText("")
                view.et_text.hint = view.context?.getString(R.string.stt_hint)
            }
            R.id.btn_text_save -> {
                presenter.saveText()
            }
            R.id.btn_text_load -> {
                presenter.loadText()
            }
        }
    }

}