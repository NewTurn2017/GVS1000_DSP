package com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.listener

import android.view.View
import android.widget.AdapterView
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setid.presenter.SetIdPresenter

class SetIdItemSelectedListener(val presenter: SetIdPresenter) :
    AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sp_noNamedSpk -> presenter.selectOtherClient(position)
            R.id.sp_namedSpk -> presenter.selectUsedClient(position)
            R.id.sp_changedID -> presenter.selectChangedID(position)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}