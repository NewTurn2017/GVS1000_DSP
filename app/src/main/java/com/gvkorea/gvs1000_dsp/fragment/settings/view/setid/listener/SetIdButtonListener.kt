package com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.presenter.SetIdPresenter
import kotlinx.android.synthetic.main.fragment_setid.*

class SetIdButtonListener(val presenter: SetIdPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btn_resetId -> presenter.resetId()
            R.id.btn_noNamedSpkCheck -> presenter.checkId(presenter.loadnoNamedSocket(), presenter.view.btn_noNamedSpkCheck)
            R.id.btn_namedSpkCheck -> presenter.checkId(presenter.loadNamedSocket(), presenter.view.btn_namedSpkCheck)
            R.id.btn_changeID -> presenter.setId()
        }
    }
}