package com.gvkorea.gvs1000_dsp.fragment.tts.presenter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import com.gvkorea.gvs1000_dsp.fragment.tts.listener.TTSProgressListener
import kotlinx.android.synthetic.main.fragment_tts.*
import java.util.*
import kotlin.collections.ArrayList

class TTSPresenter(val view: TTSFragment, val am: AudioManager, val pref: SharedPreferences) : TextToSpeech.OnInitListener {

    lateinit var tts: TextToSpeech
    private lateinit var languages: ArrayList<Locale>
    private var isInit: Boolean = false
    private var isSupport: Boolean = false
    private lateinit var textSet: Set<String>
    private lateinit var textSet_pitch: Set<String>
    private lateinit var textSet_rate: Set<String>

    override fun onInit(p0: Int) {

        if (p0 == TextToSpeech.SUCCESS) {
            isInit = true
            ToastShort(view.context!!.getString(R.string.tts_no_text))
            tts.setOnUtteranceProgressListener(TTSProgressListener(view))
        } else {
            ToastShort(view.context!!.getString(R.string.tts_reset_fail))
        }
    }

    fun init_setup() {
        view.sb_ttsVolume.max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        view.sb_ttsVolume.progress = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        view.sb_pitch.max = 20
        view.sb_pitch.progress = pref.getInt("pitch", 10)
        view.sb_rate.max = 20
        view.sb_rate.progress = pref.getInt("rate", 14)
        view.et_text.setText(pref.getString("text", ""))
        val langText = "언어 : " + pref.getString("lang", "언어선택")
        view.btn_selectLang.text = langText
        initData()
        view.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        textSet = pref.getStringSet("savedtext", HashSet<String>())!!
        textSet_pitch = pref.getStringSet("savedtext_pitch", HashSet<String>())!!
        textSet_rate = pref.getStringSet("savedtext_rate", HashSet<String>())!!
        isSupport = pref.getBoolean("isSupport", false)
    }

    private fun initData() {
        tts = TextToSpeech(view.context, this)
        languages = ArrayList()
        languages.add(Locale.KOREA)
        languages.add(Locale.US)

    }

    fun savePreference() {
        val editor = pref.edit()
        editor.putInt("pitch", view.sb_pitch.progress)
        editor.putInt("rate", view.sb_rate.progress)
        editor.putString("text", view.et_text.text.toString())
        editor.apply()
    }


    private fun saveLang(lang: String) {
        val editor = pref.edit()
        editor.putString("lang", lang)
        editor.putBoolean("isSupport", isSupport)
        editor.apply()
    }

    fun showLangDialog() {
        val builder = AlertDialog.Builder(view.context!!)
        val arrayLang = arrayOf(view.context!!.getString(R.string.korean), view.context!!.getString(R.string.english))
        builder.setTitle(view.context!!.getString(R.string.tts_select_lang))
        builder.setSingleChoiceItems(arrayLang, -1) { dialog: DialogInterface, i: Int ->
            val available = tts.isLanguageAvailable(languages[i])
            if (available < 0) {
                ToastShort(view.context!!.getString(R.string.tts_not_support_lang))
                isSupport = false
            } else {
                val langText = "언어 : ${arrayLang[i]}"
                view.btn_selectLang.text = langText
                isSupport = true
                saveLang(arrayLang[i])
            }
            dialog.dismiss()

        }

        builder.setNeutralButton("취소") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun ToastShort(msg: String) {
        val toast = Toast.makeText(view.context, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun saveText() {

        val builder = AlertDialog.Builder(view.context!!)
        builder.setTitle(view.context!!.getString(R.string.tts_save_text))
        builder.setMessage(view.et_text.text.toString())
        builder.setPositiveButton("저장") { dialog: DialogInterface, _ ->
            val textList = ArrayList(textSet)
            val textList_pitch = ArrayList(textSet_pitch)
            val textList_rate = ArrayList(textSet_rate)
            textList.add(view.et_text.text.toString())
            textList_pitch.add(view.sb_pitch.progress.toString())
            textList_rate.add(view.sb_rate.progress.toString())
            textSet = textList.toHashSet()
            textSet_pitch = textList_pitch.toHashSet()
            textSet_rate = textList_rate.toHashSet()
            val editor = pref.edit()
            editor.putStringSet("savedtext", textSet)
            editor.putStringSet("savedtext_pitch", textSet_pitch)
            editor.putStringSet("savedtext_rate", textSet_rate)

            editor.apply()
            ToastShort(view.context!!.getString(R.string.saved))
            dialog.dismiss()
        }

        builder.setNeutralButton("취소") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val dialog = builder.create()
        if (view.et_text.text.toString() == "") {
            ToastShort(view.context!!.getString(R.string.tts_save_after_textinput))
        } else {
            dialog.show()

        }

    }

    fun loadText() {
        val builder = AlertDialog.Builder(view.context!!)
        textSet = pref.getStringSet("savedtext", HashSet<String>())!!
        textSet_pitch = pref.getStringSet("savedtext_pitch", HashSet<String>())!!
        textSet_rate = pref.getStringSet("savedtext_rate", HashSet<String>())!!
        val arrayText = textSet.toTypedArray()
        val arrayText_pitch = textSet_pitch.toTypedArray()
        val arrayText_rate = textSet_rate.toTypedArray()
        builder.setTitle(view.context!!.getString(R.string.tts_select_text))
        builder.setSingleChoiceItems(arrayText, -1) { dialog: DialogInterface, i: Int ->

            dialog.dismiss()
            view.et_text.setText(arrayText[i])
            view.sb_pitch.progress = arrayText_pitch[i].toInt()
            view.sb_rate.progress = arrayText_rate[i].toInt()
        }
        builder.setNeutralButton("취소") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        builder.setPositiveButton("삭제") { dialog, _ ->
            dialog.dismiss()
            deleteText()

        }

        val dialog = builder.create()
        if (textSet.isEmpty()) {
            ToastShort(view.context!!.getString(R.string.tts_not_saved_text))
        } else {
            dialog.show()
        }

    }

    private fun deleteText() {
        val builder = AlertDialog.Builder(view.context!!)
        textSet = pref.getStringSet("savedtext", HashSet<String>())!!
        textSet_pitch = pref.getStringSet("savedtext_pitch", HashSet<String>())!!
        textSet_rate = pref.getStringSet("savedtext_rate", HashSet<String>())!!
        val arrayText = textSet.toTypedArray()
        val booleanArray = makeBooleanArray(arrayText)
        builder.setTitle(view.context!!.getString(R.string.tts_select_deletetext))
        builder.setMultiChoiceItems(arrayText, booleanArray) { _, i, b ->
            booleanArray[i] = b
        }
        builder.setPositiveButton("삭제") { dialog: DialogInterface, _ ->
            val textList = ArrayList(textSet)
            val textList_pitch = ArrayList(textSet_pitch)
            val textList_rate = ArrayList(textSet_rate)


            for (j in textList.size - 1 downTo 0) {
                if (booleanArray[j]) {
                    textList.removeAt(j)
                    textList_pitch.removeAt(j)
                    textList_rate.removeAt(j)

                }
            }
            textSet = textList.toHashSet()
            textSet_pitch = textList_pitch.toHashSet()
            textSet_rate = textList_rate.toHashSet()
            val editor = pref.edit()
            editor.putStringSet("savedtext", textSet)
            editor.putStringSet("savedtext_pitch", textSet_pitch)
            editor.putStringSet("savedtext_rate", textSet_rate)
            editor.apply()
            dialog.dismiss()
            loadText()

        }

        builder.setNeutralButton("취소") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()


    }

    private fun makeBooleanArray(arrayText: Array<String>): BooleanArray {
        val booleanArray = BooleanArray(arrayText.size)
        for (i in arrayText.indices) {
            booleanArray[i] = false
        }
        return booleanArray
    }

    fun tts() {
        if (!isInit) {
            ToastShort(view.context!!.getString(R.string.tts_reset_fail))
        } else if (view.btn_selectLang.text == view.context!!.getString(R.string.select_language)) {
            ToastShort(view.context!!.getString(R.string.tts_select_lang))
        } else if (!isSupport) {
            val builder = AlertDialog.Builder(view.context!!)
            builder.setTitle(view.context!!.getString(R.string.tts_setting_info))
            val msg = "본 설정 안내는 기기마다 다를 수 있습니다\n" +
                    "1. 설정 - 일반(언어 및 입력) - 글자 읽어주기(tts 출력)\n" +
                    "2. Google tts 엔진 - 현재 설정된 언어 확인\n" +
                    "3. 원하는 언어는 설치"
            builder.setMessage(msg)

            builder.setPositiveButton(view.context!!.getString(R.string.tts_go_setting)) { dialog: DialogInterface, _ ->
                dialog.dismiss()
                view.activity?.startActivity(Intent(Settings.ACTION_SETTINGS))
            }
            builder.setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            if (TextUtils.isEmpty(view.et_text.text.toString())) {
                ToastShort(view.context!!.getString(R.string.tts_no_text))
            } else {
                val seletedlang: Int = if (pref.getString("lang", null) == view.context!!.getString(R.string.korean)) {
                    0
                } else {
                    1
                }
                val lang = languages[seletedlang]
                tts.language = lang
                tts.setPitch(view.sb_pitch.progress / 10f)
                tts.setSpeechRate(view.sb_rate.progress / 10.0f)
                tts.speak(view.et_text.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "speak")

            }
        }
    }
}