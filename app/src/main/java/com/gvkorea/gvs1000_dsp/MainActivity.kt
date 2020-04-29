package com.gvkorea.gvs1000_dsp

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.fragment.listener.MenuButtonListener
import com.gvkorea.gvs1000_dsp.fragment.volume.VolumeFragment
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.settings.SettingsFragment
import com.gvkorea.gvs1000_dsp.presenter.MainPresenter
import com.gvkorea.gvs1000_dsp.util.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var isButtonEnable = false
    private var permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE
    )

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
            }
            super.handleMessage(msg)
        }
    }

    //fragment

    val mainFragment: VolumeFragment by lazy { VolumeFragment() }
    val GEQFragment: GEQFragment by lazy { GEQFragment() }
    val musicFragment: MusicFragment by lazy { MusicFragment() }
    val settingsFragment: SettingsFragment by lazy { SettingsFragment(this, mHandler) }


    lateinit var mSleepLock: PowerManager.WakeLock
    lateinit var wifiManager: WifiManager
    private lateinit var connection: WifiInfo
    lateinit var presenter: MainPresenter

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        setContentView(R.layout.activity_main)
        checkPermission()
        sleepLock()
        pref = applicationContext.getSharedPreferences(PREF_SETUP_KEY, Context.MODE_PRIVATE)
        prefSetting = PrefSetting()
        presenter = MainPresenter(this, mHandler)
        initListener()
        buttonDisable()
    }

    private fun buttonDisable() {
        btn_volumePannel.isEnabled = false
        btn_eqPannel.isEnabled = false
        btn_musicPlayer.isEnabled = false
        btn_settings.isEnabled = false
        isButtonEnable = false
    }

    fun buttonEnable() {
        if(!isButtonEnable){
            btn_volumePannel.isEnabled = true
            btn_volumePannel.alpha = 1f
            btn_eqPannel.isEnabled = true
            btn_eqPannel.alpha = 1f
            btn_musicPlayer.isEnabled = true
            btn_musicPlayer.alpha = 1f
            btn_settings.isEnabled = true
            btn_settings.alpha = 1f
            isButtonEnable = true
        }


    }

    private fun initListener() {
        iv_logo.setOnClickListener(MenuButtonListener(presenter))
        btn_connect.setOnClickListener(this)
        btn_volumePannel.setOnClickListener(MenuButtonListener(presenter))
        btn_eqPannel.setOnClickListener(MenuButtonListener(presenter))
        btn_musicPlayer.setOnClickListener(MenuButtonListener(presenter))
        btn_settings.setOnClickListener(MenuButtonListener(presenter))
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

    fun reconnection(){
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
        }
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
                            if (spkNo.length > 1) {
                                curSpkNo = "0"
                            } else {
                                curSpkNo = spkNo
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
        val server: ServerSocket = ServerSocket(port)
        private val pool: ExecutorService
        private val poolSize = 8

        init {
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

    }

    override fun onBackPressed() {
        if (!mFlag) {
            Toast.makeText(
                applicationContext, "\'Back\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT
            ).show()
            mFlag = true
            mHandlerBackPress?.sendEmptyMessageDelayed(0, (1000 * 2).toLong())
        } else {
            super.onBackPressed()
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
    }

}
