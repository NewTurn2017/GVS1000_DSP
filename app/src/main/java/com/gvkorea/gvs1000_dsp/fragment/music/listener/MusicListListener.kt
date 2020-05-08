package com.gvkorea.gvs1000_dsp.fragment.music.listener

import android.view.View
import android.widget.AdapterView
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.music.adapter.MusicListAdapter
import com.gvkorea.gvs1000_dsp.fragment.music.adapter.MusicListAdapter.Companion.mSelectedIndex
import com.gvkorea.gvs1000_dsp.fragment.music.presenter.MusicPresenter
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentPlayIndex
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentPlayPosition
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sIsPlayNew
import kotlinx.android.synthetic.main.fragment_music.*

class MusicListListener(val view: MusicFragment, val presenter: MusicPresenter, val adapter: MusicListAdapter): AdapterView.OnItemClickListener{
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        view.pre_button.visibility = View.VISIBLE
        view.play_button.visibility = View.VISIBLE
        view.next_button.visibility = View.VISIBLE
        sIsPlayNew = true
        sCurrentPlayIndex = p2
        sCurrentPlayPosition = 0
        presenter.play()
        presenter.initMusicUI()
        p1?.isSelected = true
        mSelectedIndex = p2
        adapter.notifyDataSetChanged()

    }

}