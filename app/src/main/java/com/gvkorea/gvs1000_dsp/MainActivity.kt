package com.gvkorea.gvs1000_dsp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.fragment.listener.MenuButtonListener
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment.Companion.mBroadcastReceiver
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment.Companion.mPlayerService
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment.Companion.mServiceConnection
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService
import com.gvkorea.gvs1000_dsp.fragment.settings.SettingsFragment
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import com.gvkorea.gvs1000_dsp.fragment.tune.TuneFragment
import com.gvkorea.gvs1000_dsp.presenter.MainPresenter
import com.gvkorea.gvs1000_dsp.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tune.*
import java.io.DataInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    var isButtonEnable = false

    private var permission_list = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE
    )

    private var isBind: Boolean = false

    var mFlag = false
    private var mHandlerBackPress: Handler? = Handler {
        if (it.what == 0) {
            mFlag = false
        }
        return@Handler true
    }

    private val SERVERPORT = 5001
    private val threadList = ArrayList<ClientThread>()
    private val lock: ReentrantLock = ReentrantLock()
    private lateinit var thread: ServerThread
    private var loop: Boolean = false
    private var isConnect = false
    private var isResigterWifi = false

    val mHandler = @SuppressLint("HandlerLeak") object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                DSPMessage.MSG_RSV.value -> {
                    presenter.appendText(msg)
                }
                DSPMessage.MSG_INFO.value -> {
                    presenter.ipInfoText(msg)
                }
                DSPMessage.MSG_QUIT.value -> {
                    presenter.appendTextQuit(msg)
                }
                DSPMessage.MSG_CONN.value -> {
                    presenter.appendText(msg)
                }
                DSPMessage.MSG_SOCK.value -> {
                    presenter.arrangeSockets(msg)
                }
                DSPMessage.MSG_UI_UNTOUCH.value -> {
                    presenter.buttonDisable()
                }
                DSPMessage.MSG_UI_TOUCH.value -> {
                    presenter.buttonenable()
                }
            }
            super.handleMessage(msg)
        }
    }

    //fragment

    val mainFragment: VolumeFragment by lazy { VolumeFragment(mHandler) }
    val geqFragment: GEQFragment by lazy { GEQFragment(mHandler) }
    val tuneFragment: TuneFragment by lazy { TuneFragment(mHandler) }
    val musicFragment: MusicFragment by lazy { MusicFragment() }
    val ttsFragment: TTSFragment by lazy { TTSFragment() }
    val settingsFragment: SettingsFragment by lazy { SettingsFragment(this, mHandler) }
    lateinit var mSleepLock: PowerManager.WakeLock
    lateinit var wifiManager: WifiManager
    private lateinit var connection: WifiInfo
    lateinit var presenter: MainPresenter
    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        setContentView(R.layout.activity_main)

        checkPermission()
        sleepLock()
        sInstance = this

        pref = applicationContext.getSharedPreferences(PREF_SETUP_KEY, Context.MODE_PRIVATE)
        prefSetting = PrefSetting()
        presenter = MainPresenter(this, mHandler)
        initListener()
        buttonDisable()
        mContentResolver = contentResolver
        selectedMicName = prefSetting.loadCalibMic()
//        loadVersionAndUpdate()

        mHandler.postDelayed({
            connectDSP()
//            remoteConfigInit()
//            checkGooglePlayServices()
//            showUpdate()

        }, 500)
    }

    private fun loadVersionAndUpdate() {
        tv_version.text = "Version: v${getAppVersion(this)}"
    }

    private fun remoteConfigInit() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val configSettings =
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    private fun checkGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)

        if (status != ConnectionResult.SUCCESS) {
            val dialog = googleApiAvailability.getErrorDialog(this, status, -1)
            dialog.setOnDismissListener { finish() }
            dialog.show()

            googleApiAvailability.showErrorNotification(this, status)
        }
    }

    private fun showUpdate() {

        val cacheExpiration = 3000L
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, OnCompleteListener {
                    if (it.isSuccessful) {
                        mFirebaseRemoteConfig.activate()
                    }
                    showUpdateDialog()

                })

    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("앱 업데이트 알림")
        val versionInfo = checkVersion()

        val update = loadDate()
        builder.setMessage(" 현재 버전: ${versionInfo[1]} \n 최신 버전: ${versionInfo[0]} \n 업데이트 날짜: $update")
        if (versionInfo[0] == versionInfo[1]) {
            builder.setPositiveButton("확인") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }

        } else {
            builder.setPositiveButton("업데이트 바로가기") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                val marketLaunch = Intent(
                        Intent.ACTION_VIEW
                )
                marketLaunch.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                startActivity(marketLaunch)
                finish()

            }
        }
        if (versionInfo[0] != versionInfo[1]) {
            val dialog = builder.create()
            dialog.show()
        }

    }


    private fun loadDate(): String? {

        val latestUpdate = mFirebaseRemoteConfig.getString("latest_update")
        return latestUpdate
    }

    private fun checkVersion(): Array<String> {

        val latestVersion = mFirebaseRemoteConfig.getString("latest_version")
        val currentVersion = getAppVersion(this)

        return arrayOf(latestVersion, currentVersion)
    }

    private fun getAppVersion(context: Context): String {
        var result = ""

        try {
            result = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("getAppversion", e.message!!)
        }

        return result
    }

    private fun buttonDisable() {
        btn_volumePannel.isEnabled = false
        btn_eqPannel.isEnabled = false
        btn_tunePanel.isEnabled = false
        btn_musicPlayer.isEnabled = false
        btn_tts.isEnabled = false
        btn_settings.isEnabled = false
        isButtonEnable = false
    }

    fun buttonEnable() {
        if (!isButtonEnable) {
            btn_volumePannel.isEnabled = true
            btn_volumePannel.alpha = 1f
            btn_eqPannel.isEnabled = true
            btn_eqPannel.alpha = 1f
            btn_tunePanel.isEnabled = true
            btn_tunePanel.alpha = 1f
            btn_musicPlayer.isEnabled = true
            btn_musicPlayer.alpha = 1f
            btn_tts.isEnabled = true
            btn_tts.alpha = 1f
            btn_settings.isEnabled = true
            btn_settings.alpha = 1f
            isButtonEnable = true
        }
    }

    private fun initListener() {
        iv_logo.setOnLongClickListener(this)
        btn_connect.setOnClickListener(this)
        btn_volumePannel.setOnClickListener(MenuButtonListener(presenter))
        btn_eqPannel.setOnClickListener(MenuButtonListener(presenter))
        btn_tunePanel.setOnClickListener(MenuButtonListener(presenter))
        btn_musicPlayer.setOnClickListener(MenuButtonListener(presenter))
        btn_tts.setOnClickListener(MenuButtonListener(presenter))
        btn_settings.setOnClickListener(MenuButtonListener(presenter))
        btn_finish.setOnClickListener(MenuButtonListener(presenter))
    }


    private fun checkPermission() {

        for (permission: String in permission_list) {

            val chk = checkCallingOrSelfPermission(permission)

            if (chk == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permission_list, 0)
                break
            }
        }

    }

    @SuppressLint("InvalidWakeLockTag")
    private fun sleepLock() {
        val power = getSystemService(Context.POWER_SERVICE) as PowerManager
        mSleepLock = power.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NonSleepActivity")
        mSleepLock.acquire(10 * 60 * 1000L /*10 minutes*/)
    }


    override fun onClick(v: View?) {
        connectDSP()
    }


    private fun connectDSP() {
        otherClientNo = 0
        if (isConnect) {
            disconnect()
            connectButtonState(isConnect)
            buttonDisable()
            isConnect = false
        } else {
            openTCPServer()
            connectButtonState(isConnect)
            isConnect = true
        }

    }

    fun reconnection() {
        connectDSP()
        mHandler.postDelayed({
            connectDSP()
        }, 700)
    }

    private fun connectButtonState(isConnect: Boolean) {
        if (isConnect) {
            btn_connect.text = "접속"
            btn_connect.setStrokeColor(
                    ContextCompat.getColor(
                            this,
                            android.R.color.holo_green_dark
                    )
            )
        } else {
            btn_connect.text = "해제"
            btn_connect.setStrokeColor(Color.RED)
        }
    }

    private fun openTCPServer() {
        try {
            thread = ServerThread(SERVERPORT)
            thread.start()
            val m = Message()
            m.what = DSPMessage.MSG_RSV.value
            m.obj = "서버가 시작되었습니다."
            mHandler.sendMessage(m)
            presenter.connect()
        } catch (e: IOException) {
            val m = Message()
            m.what = DSPMessage.MSG_RSV.value
            m.obj = "Server Thread를 시작하지 못했습니다.$e"
            mHandler.sendMessage(m)
            presenter.msg("이미 작동 중인 서버가 있어서 재 시작합니다.")
            restartApp()
        }
    }

    private fun restartApp(){
        val packageManager = applicationContext.packageManager
        val intent = packageManager.getLaunchIntentForPackage(applicationContext.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        applicationContext.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    private fun disconnect() {

        val t = Thread(Runnable {
            if (loop) {
                thread.shutdown()
                thread.server.close()
                thread.join()

            }
            val m = Message()
            m.what = DSPMessage.MSG_QUIT.value
            m.obj = "서버를 종료합니다."
            mHandler.sendMessage(m)
            clearSocket()
        })
        t.start()
        try {
            t.join()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun clearSocket() {
        spk1Client = null
        spk2Client = null
        spk3Client = null
        spk4Client = null
        spk5Client = null
        spk6Client = null
        spk7Client = null
        spk8Client = null
        otherClient = ArrayList()
    }

    inner class ClientThread(private val sock: Socket) : Thread() {

        private val inetaddr: InetAddress = sock.inetAddress

        var isStarted = false
        var din: DataInputStream? = null
        var receivedData = ""
        var rxByteList = ArrayList<Byte>()
        var curSpkNo = ""

        init {
            val m = Message()
            m.what = DSPMessage.MSG_RSV.value
            m.obj = inetaddr.hostAddress + "로부터 접속하였습니다."
            mHandler.sendMessage(m)
            isButtonEnable = true
        }

        override fun run() {
            try {
                isStarted = true
                din = DataInputStream(sock.getInputStream())
                while (isStarted) {
                    val lengthByte = ByteArray(1)
                    rxByteList = ArrayList()

                    do {
                        din!!.read(lengthByte, 0, 1)
                        rxByteList.add(lengthByte[0])
                        val x = Integer.parseInt(String.format("%02x", rxByteList[0]), 16)
                    } while (rxByteList.size - 1 != x)

                    for (i in rxByteList.indices) {
                        if (i < rxByteList.size - 1) {
                            receivedData += String.format("0x%02x ", rxByteList[i])

                        } else {
                            receivedData += String.format("0x%02x", rxByteList[i])
                        }
                    }
                    val msg = receivedData
                    receivedData = ""
                    if (msg.isNotEmpty() && !msg.startsWith("0x00")) {

                        if (msg.contains("0x52 0x43 0x64 0x00")) {
                            val infoArray = msg.split(" ")
                            val spkNo = presenter.hexToAscii(infoArray)
                            val firmwareMajorVersion = presenter.hexStringToInt(infoArray[23])
                            val firmwareMinorVersion = presenter.hexStringToInt(infoArray[24])
                            if (spkNo.length > 1) {
                                curSpkNo = "0"
                            } else {
                                curSpkNo = spkNo
                                presenter.saveFirmwareVersion(spkNo.toInt(), firmwareMajorVersion, firmwareMinorVersion)
                            }
                        }

                        val m = Message()
                        m.what = DSPMessage.MSG_RSV.value
                        m.obj = "$msg(ID: $curSpkNo)"
                        mHandler.sendMessage(m)

                    }


                    if (msg.contains("0x52 0x43 0x64 0x00")) {
                        val infoArray = msg.split(" ")
                        val spkNo = presenter.hexToAscii(infoArray)
                        if (spkNo.length > 1) {
                            val m = Message()
                            m.what = DSPMessage.MSG_SOCK.value
                            m.arg1 = 0
                            m.obj = sock
                            mHandler.sendMessage(m)
                        } else {
                            val m = Message()
                            m.what = DSPMessage.MSG_SOCK.value
                            m.arg1 = spkNo.toInt()
                            m.obj = sock
                            mHandler.sendMessage(m)
                        }
                    }

                    if (msg.contains("0x0f 0x52 0x45")) {
                        val arr = msg.split(" ")
                        var presetNameHex = ""
                        if (arr.size == 16) {
                            for (i in 5..12) {
                                presetNameHex += arr[i].substring(2)
                            }
                            presetSavedList.add(hexToString(presetNameHex))
                        }
                    }
                    presenter.sync(msg)
                }
            } catch (e: InterruptedException) {

            } catch (e: Exception) {
            } finally {
                val m3 = Message()
                m3.what = DSPMessage.MSG_RSV.value
                m3.obj = inetaddr.hostAddress + "와의 접속이 종료되었습니다."
                mHandler.sendMessage(m3)

                try {
                    lock.lock()
                    threadList.remove(this)
                    lock.unlock()
                    sock.close()
                } catch (e: Exception) {
                }
            }
        }

        fun quit() {
            try {
                sock.close()
            } catch (e: IOException) {
            }
        }

        private fun hexToString(presetNameHex: String): String {
            val output = StringBuilder("")
            var i = 0
            while (i < presetNameHex.length) {
                val str: String = presetNameHex.substring(i, i + 2)
                output.append(str.toInt(16).toChar())
                i += 2
            }
            return output.toString()

        }
    }

    inner class ServerThread @Throws(IOException::class) constructor(port: Int) : Thread() {
        //        val server: ServerSocket = ServerSocket(port)
        val server: ServerSocket = ServerSocket()
        private val pool: ExecutorService
        private val poolSize = 8

        init {
            server.reuseAddress = true
            server.bind(InetSocketAddress(port))
            pool = Executors.newFixedThreadPool(poolSize)
            loop = true
        }

        override fun run() {
            while (loop) {
                try {
                    val thread = ClientThread(server.accept())
                    lock.lock()
                    threadList.add(thread)
                    lock.unlock()
                    pool.execute(thread)
                } catch (e: InterruptedException) {
                    //                    e.printStackTrace()
                } catch (e: IOException) {
                    val m = mHandler.obtainMessage()
                    m.what = DSPMessage.MSG_QUIT.value
                    m.obj = "서버를 중지합니다."
                    mHandler.sendMessage(m)
                    pool.shutdown()
                    break
                }
            }

            try {
                server.close()
            } catch (e: java.lang.Exception) {
            }
        }

        fun shutdown() {
            pool.shutdown()
            try {
                if (!pool.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                    pool.shutdownNow()
                }
            } catch (ie: InterruptedException) {
                pool.shutdownNow()
            }

            clearList()
            loop = false

        }

        private fun clearList() {
            if (threadList.isNotEmpty()) {
                lock.lock()
                for (index in threadList) {
                    index.quit()
                }
                lock.unlock()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (mPlayerService == null) {
            val bindIntent = Intent(this, PlayerService::class.java)
            bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
            isBind = true
        }

        val screenIntent = Intent(this, PlayerService::class.java)
        val screenSender = PendingIntent.getBroadcast(this, 0, screenIntent, PendingIntent.FLAG_NO_CREATE)
        if (screenSender == null) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(PlayerService.PLAY)
            intentFilter.addAction(PlayerService.PAUSE)
            intentFilter.addAction(PlayerService.RESUME)
            intentFilter.addAction(PlayerService.UPDATE_PROGRESS)
            intentFilter.addAction(PlayerService.PLAY_NEXT)
            intentFilter.addAction(PlayerService.PLAY_PREVIOUS)
            registerReceiver(mBroadcastReceiver, intentFilter)
        }
    }

    val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wifiStateExtra =
                    intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)

            when (wifiStateExtra) {
                WifiManager.WIFI_STATE_ENABLED -> {

                    wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

                    connection = wifiManager.connectionInfo

                    val hostaddr = presenter.getIPAddress()
                    val wifiInfo = "WIFI 연결됨\n SSID: ${connection.ssid}\n IP: $hostaddr"
                    tv_ip_info.text = wifiInfo
                    if (isResigterWifi) {
                        wifi_refresh()
                    }
                    onStart()


                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    tv_ip_info.text = "WIFI 연결안됨"
                    disconnect()
                }
            }
        }
    }

    fun wifi_refresh() {
        unregisterReceiver(wifiStateReceiver)
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)

    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)

        isResigterWifi = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loop) {
            val t = Thread(Runnable {
                thread.shutdown()
                thread.join()
            })
            t.start()
            t.join()
        }

        unregisterReceiver(wifiStateReceiver)

        if (isBind) {
            unbindService(mServiceConnection)
        }
        unregisterReceiver(mBroadcastReceiver)

        if (mSleepLock.isHeld) {

            mSleepLock.release()
        }
    }

    override fun onBackPressed() {
        if (!mFlag) {
            Toast.makeText(
                    applicationContext, "\'Back\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT
            ).show()
            mFlag = true
            mHandlerBackPress?.sendEmptyMessageDelayed(0, (1000 * 2).toLong())
        } else {
            presenter.finish()
        }
    }

    companion object {
        var no: Int = 0

        var selectedClient: Socket? = null
        var spk1Client: Socket? = null
        var spk2Client: Socket? = null
        var spk3Client: Socket? = null
        var spk4Client: Socket? = null
        var spk5Client: Socket? = null
        var spk6Client: Socket? = null
        var spk7Client: Socket? = null
        var spk8Client: Socket? = null
        var otherClient: ArrayList<Socket?> = ArrayList()
        var otherClientNo = 0
        var presetSavedList = ArrayList<String>()
        lateinit var pref: SharedPreferences
        lateinit var prefSetting: PrefSetting
        val PREF_SETUP_KEY = "Settings"
        var selectedSpkNo = 0
        var sockets = ArrayList<Socket?>()
        var spkList = ArrayList<SpeakerInfo>()

        var arrPEQ = ArrayList<PEQData>(31)
        var arrGEQ = ArrayList<GEQData>(31)
        var volumeArrays: ArrayList<Float>? = null
        var muteArrays: ArrayList<Boolean>? = null

        lateinit var mContentResolver: ContentResolver
        lateinit var selectedMicName: String
        var CALIBRATION = 0F
        var isCalib = false
        lateinit var sInstance: MainActivity

    }

    override fun onLongClick(v: View?): Boolean {
        presenter.showHideAdminBar()
        return true
    }

}
