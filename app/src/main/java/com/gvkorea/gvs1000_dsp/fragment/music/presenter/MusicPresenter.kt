package com.gvkorea.gvs1000_dsp.fragment.music.presenter

import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment.Companion.songs
import com.gvkorea.gvs1000_dsp.fragment.music.adapter.MusicListAdapter
import com.gvkorea.gvs1000_dsp.fragment.music.adapter.MusicListAdapter.Companion.mSelectedIndex
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.PLAY_STATE_PAUSE
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.PLAY_STATE_PLAYING
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentPlayIndex
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentPlayPosition
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentState
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sTotalTime
import kotlinx.android.synthetic.main.fragment_music.*

class MusicPresenter(val view: MusicFragment, private val service: PlayerService?, val mMusicAdapter: MusicListAdapter) {


    fun initMusicUI() {
        val mediaPlayer = service?.mediaPlayer

        if (mediaPlayer != null && (sCurrentState == PLAY_STATE_PLAYING || sCurrentState == PLAY_STATE_PAUSE)) {
            view.sb_music_seekBar.progress = sCurrentPlayPosition
            view.tv_music_totalTime.text = formatTime(mediaPlayer.duration)
            view.tv_current_play_time.text = formatTime(sCurrentPlayPosition)
            view.sb_music_seekBar.max = mediaPlayer.duration
            val musicTitle = songs[sCurrentPlayIndex].title
            val musicArtist = songs[sCurrentPlayIndex].artist
            view.tv_musicTitle.text = musicTitle
            view.tv_musicArtist.text = musicArtist
            val uri = Uri.parse("content://media/external/audio/albumart/3")
            val uri2 = Uri.parse("content://media/external/audio/albumart/8")
            val uri3 = Uri.parse("content://media/external/audio/albumart/10")

            if(songs[sCurrentPlayIndex].albumArt == uri || songs[sCurrentPlayIndex].albumArt == uri2 || songs[sCurrentPlayIndex].albumArt == uri3){
                view.iv_album.setImageDrawable(ContextCompat.getDrawable(view.context!!, R.drawable.noimage))
            }else{
                view.iv_album.setImageURI(songs[sCurrentPlayIndex].albumArt)
            }


        } else {
            view.sb_music_seekBar.progress = 0
            view.tv_music_totalTime.text = "00:00"
            view.tv_current_play_time.text = "00:00"
            view.iv_album.setImageResource(R.drawable.noimage)
        }
        updatePlayButton()
        updateList()

    }


    fun updatePlayButton() {
        if (sCurrentState == PLAY_STATE_PLAYING) {
            view.play_button.setImageResource(R.drawable.selector_pause_button)
        } else {
            view.play_button.setImageResource(R.drawable.selector_play_button)
        }
    }

    fun updateList() {
        if (sCurrentPlayIndex > -1) {
            mSelectedIndex = sCurrentPlayIndex
            mMusicAdapter.notifyDataSetChanged()
        }
    }

    fun updateProgress() {
        view.sb_music_seekBar.progress = sCurrentPlayPosition
        view.tv_music_totalTime.text = formatTime(sTotalTime)
        view.tv_current_play_time.text = formatTime(sCurrentPlayPosition)
        view.sb_music_seekBar.max = sTotalTime
    }


    private fun formatTime(time: Int): String {
        val mTime = time / 1000
        val hour: Int
        val minute: Int
        val second: Int
        if (mTime > 3600) {
            hour = mTime / 3600
            minute = mTime % 3600 / 60
            second = mTime % 3600 % 60
            return String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            minute = mTime / 60
            second = mTime % 60
            return String.format("%02d:%02d", minute, second)
        }
    }

    fun pause() {
        service?.pauseMusic()
    }

    fun resume() {
        service?.resumeMusic()
    }

    fun playPrevious() {
        service?.playPrevious()
    }

    fun playNext() {
        service?.playNext()
    }

    fun play() {

        service?.playMusic()
    }

}