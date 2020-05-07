package com.gvkorea.gvs1000_dsp.fragment.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment.Companion.songs
import java.util.*

class PlayerService : Service() {
    var mediaPlayer: MediaPlayer? = null

    private val mIBinder = LocalBinder()
    private var mTimer: Timer? = null

    inner class LocalBinder : Binder() {
        val service: PlayerService
            get() = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mIBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        scheduleUpdate()
        return Service.START_NOT_STICKY
    }

    fun playMusic() {

        mTimer?.cancel()
        mTimer = null
        mediaPlayer?.release()
        mediaPlayer = null

        if (songs.size> 0) {
            mediaPlayer = MediaPlayer()
            if (sIsPlayNew) {
                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(applicationContext, songs[sCurrentPlayIndex].uri)
                mediaPlayer!!.prepare()
                sIsPlayNew = false
            }
            sCurrentState = PLAY_STATE_PLAYING
            sCurrentPlayPosition = 0
            sTotalTime = mediaPlayer!!.duration
            mediaPlayer!!.seekTo(sCurrentPlayPosition)
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                playNext()
                val intent = Intent()
                intent.action = PLAY_NEXT
                sendBroadcast(intent)
            }
            val intent = Intent()
            intent.action = PLAY
            sendBroadcast(intent)
            scheduleUpdate()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
        sCurrentState = PLAY_STATE_PAUSE
        val intent = Intent()
        intent.action = PAUSE
        sendBroadcast(intent)
    }

    fun resumeMusic() {
        mediaPlayer?.start()
        sCurrentState = PLAY_STATE_PLAYING
        val intent = Intent()
        intent.action = RESUME
        sendBroadcast(intent)
    }


    fun playNext() {
        val nextIndex = sCurrentPlayIndex + 1
        if (nextIndex > songs.size - 1) {
            return
        }
        sCurrentPlayIndex = nextIndex
        sCurrentPlayPosition = 0
        sIsPlayNew = true
        playMusic()
    }


    fun playPrevious() {
        val previousIndex = sCurrentPlayIndex - 1
        if (previousIndex < 0) {
            return
        }
        sCurrentPlayIndex = previousIndex
        sCurrentPlayPosition = 0
        sIsPlayNew = true
        playMusic()

    }

    private fun scheduleUpdate() {
        mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                if (sCurrentState == PLAY_STATE_PLAYING) {
                    sCurrentPlayPosition = mediaPlayer?.currentPosition!!
                    val intent = Intent()
                    intent.action = UPDATE_PROGRESS
                    sendBroadcast(intent)
                }
            }
        }, 500, 1000)
    }

    companion object {
        const val PLAY = "PLAY"
        const val PAUSE = "PAUSE"
        const val RESUME = "RESUME"
        const val PLAY_NEXT = "PLAY_NEXT"
        const val PLAY_PREVIOUS = "PLAY_PREVIOUS"
        const val UPDATE_PROGRESS = "UPDATE_PROGRESS"
        var sCurrentPlayIndex = -1
        var sCurrentPlayPosition = 0
        var sIsPlayNew = true
        var sTotalTime = 0
        const val PLAY_STATE_PAUSE = 0
        const val PLAY_STATE_PLAYING = 1
        var sCurrentState = PLAY_STATE_PAUSE
        private var mediaPlayer: MediaPlayer? = null

        fun getMediaPlayer() : MediaPlayer? {
            return mediaPlayer
        }
    }
}
