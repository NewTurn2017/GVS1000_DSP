package com.gvkorea.gvs1000_dsp.fragment.settings.view.sync

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.listener.SyncButtonListener
import com.gvkorea.gvs1000_dsp.fragment.settings.view.sync.presenter.SyncPresenter
import kotlinx.android.synthetic.main.fragment_sync.*

class SyncFragment : Fragment() {

    lateinit var presenter: SyncPresenter
    val handler = Handler()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SyncPresenter(this, handler)
        initListener()
        presenter.initializeList()
    }

    private fun initListener() {
        btn_syncSpeakerSelect.setOnClickListener(SyncButtonListener(presenter))
    }

}
