package com.gvkorea.gvs1000_dsp.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.CALIBRATION
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.isCalib
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.pref


class PrefSetting {
    private var editor = pref.edit()
    private val SPEAKER_1_SETTING = "SPEAKER_1_SETTING"
    private val SPEAKER_2_SETTING = "SPEAKER_2_SETTING"
    private val SPEAKER_3_SETTING = "SPEAKER_3_SETTING"
    private val SPEAKER_4_SETTING = "SPEAKER_4_SETTING"
    private val SPEAKER_5_SETTING = "SPEAKER_5_SETTING"
    private val SPEAKER_6_SETTING = "SPEAKER_6_SETTING"
    private val SPEAKER_7_SETTING = "SPEAKER_7_SETTING"
    private val SPEAKER_8_SETTING = "SPEAKER_8_SETTING"
    private lateinit var settingData: SettingData
    private val gson = Gson()
    private var defaultSetting: String = ""
    private val EMPTYLIST = "없음"
    private val NONSETTING = "미지정"
    private val ISSETTING = "ISSETTING"
    val NOISE_VOLUME = "noiseVolume"
    val REVERB_TIME = "reverbTime"



    init {
        defaultSetting = gson.toJson(initializeSettingData())
    }

    fun updateSpeakerSetMode(id: String, updateMode: String) {
        settingData = loadSpeakerSettings(id)
        saveSpeakerSettings(id, settingData.model, updateMode, settingData.name)
    }

    fun saveSpeakerSettings(id: String, model: String, mode: String, name: String){
        settingData = SettingData(model, mode, name)
        val json = gson.toJson(settingData)
        val key = loadKey(id.toInt())
        editor.putString(key, json)
        editor.apply()
    }

    fun resetSpeakerSetting(id: String){
        settingData = initializeSettingData()
        val json = gson.toJson(settingData)
        val key = loadKey(id.toInt())
        editor.putString(key, json)
        editor.apply()
    }

    private fun loadKey(id: Int): String {
        var selectId = ""
        when(id) {
            1 -> selectId =  SPEAKER_1_SETTING
            2 -> selectId =  SPEAKER_2_SETTING
            3 -> selectId =  SPEAKER_3_SETTING
            4 -> selectId =  SPEAKER_4_SETTING
            5 -> selectId =  SPEAKER_5_SETTING
            6 -> selectId =  SPEAKER_6_SETTING
            7 -> selectId =  SPEAKER_7_SETTING
            8 -> selectId =  SPEAKER_8_SETTING
        }
        return selectId
    }

    fun loadSpeakerSettings(id: String): SettingData {
        if(id != EMPTYLIST){
            val json = pref.getString(loadKey(id.toInt()), defaultSetting)
            val type = object : TypeToken<SettingData>() {}.type
            settingData = gson.fromJson(json, type)
        }else{
            settingData = initializeSettingData()
        }

        return settingData
    }

    private fun initializeSettingData(): SettingData{
       return SettingData(NONSETTING, NONSETTING, NONSETTING)
    }

    fun isSetting(): Boolean {
       return loadIsSetting()
    }

    private fun loadIsSetting(): Boolean {
        return pref.getBoolean(ISSETTING, false)
    }

    fun saveIsSetting(boolean: Boolean) {
        editor.putBoolean(ISSETTING, boolean)
        editor.apply()
    }

    fun saveFirmware(spkNo: Int, firmwareVersion: String) {
        editor.putString("firmware$spkNo", firmwareVersion)
        editor.apply()
    }

    fun loadFirmware(spkNo: Int): String?{
        return pref.getString("firmware$spkNo", "No Saved")
    }

    fun loadCalibMic(): String {
        return pref.getString("selectedMic", "iSEMic725TR-3511903-freefield.csv")!!
    }

    fun loadCalib() {
        CALIBRATION = pref.getFloat("calibration", 0f)
    }

    fun saveIsCalib(isCalib: Boolean) {
        editor.putBoolean("isCalib", isCalib)
        editor.apply()
    }

    fun loadIsCalib(): Boolean{
        return pref.getBoolean("isCalib", false)
    }

    fun saveNoiseVolumePref(noiseLevel: Float) {
        editor.putFloat(NOISE_VOLUME, noiseLevel)
        editor.apply()
    }

    fun getNoiseVolumePref():Float{
        return pref.getFloat(NOISE_VOLUME, -40f)
    }

    fun setReverbTimePref(value: String){
        val editor = pref.edit()
        editor.putString(REVERB_TIME, value)
        editor.apply()
    }

    fun getReverbTimePref(): String?{
        return pref.getString(REVERB_TIME, "Not measured yet")
    }


}