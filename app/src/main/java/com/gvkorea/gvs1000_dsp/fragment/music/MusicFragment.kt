package com.gvkorea.gvs1000_dsp.fragment.music

import android.content.*
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gvkorea.gvs1000_dsp.MainActivity
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.mContentResolver

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.music.adapter.MusicListAdapter
import com.gvkorea.gvs1000_dsp.fragment.music.adapter.MusicListAdapter.Companion.mSelectedIndex
import com.gvkorea.gvs1000_dsp.fragment.music.listener.MusicListListener
import com.gvkorea.gvs1000_dsp.fragment.music.listener.MusicPlayerButtonListener
import com.gvkorea.gvs1000_dsp.fragment.music.listener.MusicPlayerVolumeSeekBarListener
import com.gvkorea.gvs1000_dsp.fragment.music.listener.MusicSeekBarListener
import com.gvkorea.gvs1000_dsp.fragment.music.presenter.MusicPresenter
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService
import com.mtechviral.mplaylib.MusicFinder
import kotlinx.android.synthetic.main.fragment_music.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MusicFragment : Fragment() {

    private var handler = Handler()
    private lateinit var mMusicAdapter: MusicListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPlayer()
        handler.postDelayed({

            mMusicAdapter = MusicListAdapter(this)
            lv_musiclist.adapter = mMusicAdapter
            lv_musiclist.setSelection(mSelectedIndex)
            presenter = MusicPresenter(this, mPlayerService, mMusicAdapter)

            if(mPlayerService != null){
                presenter.initMusicUI()
            }
            presenter.updatePlayButton()

            val startPlayIntent = Intent(context, PlayerService::class.java)
            activity?.startService(startPlayIntent)
            init_Listener()

        }, 50)
    }

    private fun createPlayer() {
        val songsJob = CoroutineScope(Dispatchers.Default).async {
            val songFinder = MusicFinder(MainActivity.mContentResolver)
            songFinder.prepare()
            songFinder.allSongs
        }
        CoroutineScope(Dispatchers.Main).launch {
            val mSongs = songsJob.await()
            songs = mSongs

        }
    }


    private fun init_Listener() {
        play_button.setOnClickListener(MusicPlayerButtonListener(this, presenter))
        pre_button.setOnClickListener(MusicPlayerButtonListener(this, presenter))
        next_button.setOnClickListener(MusicPlayerButtonListener(this, presenter))
        sb_music_seekBar.setOnSeekBarChangeListener(MusicSeekBarListener(this, presenter, mPlayerService))
        val audioManager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        sb_mpVolume.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        sb_mpVolume.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        sb_mpVolume.setOnSeekBarChangeListener(MusicPlayerVolumeSeekBarListener(this, audioManager))
        lv_musiclist.onItemClickListener = MusicListListener(this, presenter, mMusicAdapter)
    }

    companion object {
        lateinit var songs: MutableList<MusicFinder.Song>
        lateinit var presenter: MusicPresenter
        var mPlayerService: PlayerService? = null

        val mServiceConnection = object : ServiceConnection {

            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                val binder = p1 as PlayerService.LocalBinder
                mPlayerService = binder.service
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                mPlayerService = null
            }
        }

        val mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {

                when (p1?.action) {
                    PlayerService.PLAY -> {
                        presenter.updatePlayButton()
                        presenter.updateList()
                        presenter.updateProgress()
                    }
                    PlayerService.PAUSE -> {
                        presenter.updatePlayButton()
                        presenter.updateList()
                    }
                    PlayerService.RESUME -> {
                        presenter.updatePlayButton()
                        presenter.updateList()
                    }
                    PlayerService.UPDATE_PROGRESS -> {
                        if(p0 == MusicFragment.presenter.view.context){
                            presenter.updateProgress()
                            presenter.mMusicAdapter.notifyDataSetChanged()
                        }
                    }
                    PlayerService.PLAY_NEXT -> {
                        presenter.initMusicUI()
                        presenter.mMusicAdapter.notifyDataSetChanged()

                    }
                    PlayerService.PLAY_PREVIOUS -> {
                        presenter.initMusicUI()
                        presenter.mMusicAdapter.notifyDataSetChanged()

                    }
                }
            }

        }
    }

}
