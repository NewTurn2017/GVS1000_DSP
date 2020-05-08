package com.gvkorea.gvs1000_dsp.fragment.music.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment.Companion.songs
import java.net.URI
import java.net.URI.create

class MusicListAdapter(val view: MusicFragment) : BaseAdapter() {


    private class ViewHolder(row: View?) {
        var tvTitle: TextView? = null
        var tvArtist: TextView? = null
        var ivAlbumArt: ImageView? = null

        init {
            this.tvTitle = row?.findViewById(R.id.tv_row_musicTitle)
            this.tvArtist = row?.findViewById(R.id.tv_row_musicArtist)
            this.ivAlbumArt = row?.findViewById(R.id.iv_row_albumArt)
        }
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val v: View?
        val viewHolder: ViewHolder

        if(mSelectedIndex == p0){
            p1?.setBackgroundColor(Color.BLUE)
        }else{
            p1?.setBackgroundColor(Color.TRANSPARENT)
        }


        if (p1 == null) {
            val inflater = view.activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.music_list_row, null)
            viewHolder = ViewHolder(v)
            v?.tag = viewHolder
        } else {
            v = p1
            viewHolder = v.tag as ViewHolder
        }

        var curSong = songs[p0]
        viewHolder.tvTitle?.text = curSong.title
        viewHolder.tvArtist?.text = curSong.artist
        val uri = Uri.parse("content://media/external/audio/albumart/3")
        val uri2 = Uri.parse("content://media/external/audio/albumart/8")
        val uri3 = Uri.parse("content://media/external/audio/albumart/10")

        if(curSong.albumArt == uri || curSong.albumArt == uri2 || curSong.albumArt == uri3){
            viewHolder.ivAlbumArt?.setImageDrawable(ContextCompat.getDrawable(view.context!!, R.drawable.noimage))
        }else{
            viewHolder.ivAlbumArt?.setImageURI(curSong.albumArt)
        }



        return v as View
    }

    override fun getItem(p0: Int): Any {
        return songs[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return songs.size
    }


    companion object {
        var mSelectedIndex = -1
    }

}