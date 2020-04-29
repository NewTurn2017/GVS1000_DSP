package com.gvkorea.gvs1000_dsp.util

import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import com.gvkorea.gvs1000_dsp.MainActivity
import java.io.IOException
import java.net.*
import java.util.*

class GVMainPacket(val view: MainActivity) {


    private lateinit var tx_buff: ByteArray
    private val uPort = 5000
    private val protocol = Protocol()


    fun SendPacket_Connect() {

        val str = getIPAddress()
        val ipAddr = str.split('.')
        val commandID = protocol.CMD_SET
        val para1 = protocol.CMD_UDP_TCP_SERVER_IFNO
        val para2 = protocol.CMD_UDP_TCP_SERVER_IFNO_PARA2
        val data0 = ipAddr[0].toInt()
        val data1 = ipAddr[1].toInt()
        val data2 = ipAddr[2].toInt()
        val data3 = ipAddr[3].toInt()


        Thread(Runnable {
            tx_buff = protocol.packet_Connect(commandID, para1, para2, data0, data1, data2, data3)

            val dSocket: DatagramSocket?
            try {
                dSocket = DatagramSocket()
                val dPacket = DatagramPacket(
                    tx_buff,
                    tx_buff.size,
                    InetAddress.getByName("192.168.$data2.255"),
                    uPort
                )

                dSocket.send(dPacket)
                dSocket.close()
            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()

        Thread.currentThread()
        Thread.interrupted()
    }

    fun getIPAddress(): String {
        val wifiMan = view.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val wifiInf = wifiMan.connectionInfo
        val ipAddress = wifiInf.ipAddress
        val ip = String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )
        return ip
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> =
                    Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: java.lang.Exception) {
        } // for now eat exceptions
        return ""
    }

}