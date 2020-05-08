package com.gvkorea.gvs1000_dsp.fragment.music.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.music.presenter.MusicPresenter
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.PLAY_STATE_PLAYING
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentState

class MusicPlayerButtonListener(val view: MusicFragment, val presenter: MusicPresenter) : View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.play_button -> {
                if(sCurrentState == PLAY_STATE_PLAYING){
                    presenter.pause()
                }else{
                    presenter.resume()
                }
            }
            R.id.pre_button -> {
                presenter.playPrevious()
                presenter.initMusicUI()
            }
            R.id.next_button -> {
                presenter.playNext()
                presenter.initMusicUI()
            }
        }
    }

}