package com.gvkorea.gvs1000_dsp.util

import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.selectedClient
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket

class GVSubPacket(val view: Fragment, val mHandler: Handler) {

    private val protocol = Protocol()
    val CMD_PARA2_CH1 = '1'
    val CMD_PARA2_CH2 = '2'
    val CMD_PARA2_CHA = 'A'
    private val CHECKINTERVAL = 50L
    val PINK = 2
    private lateinit var tx_buff: ByteArray
    private lateinit var outputStream: OutputStream
    private var dataOutputStream: DataOutputStream? = null



    fun SendPacket_NoiseGenerator(
            socket: Socket?, para2: Char, data0: Int, data1: Float, data5: Int
    ) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_NoiseGenerator(
                    para2,
                    data0,
                    data1,
                    data5
                )
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {
                try {
                    dataOutputStream?.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream?.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()

            try {
                Thread.sleep(CHECKINTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            Thread.currentThread()
            Thread.interrupted()
        } else {
            msg("TCP Socket 연결 안됨")
        }
    }

    fun SendPacket_SetID(ip: String, id: Char) {
        if (selectedClient != null) {

            try {
                tx_buff = protocol.packet_SetID(ip, id)
                outputStream = selectedClient!!.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
               msg("ID변경이 원할하지 않습니다. 다시한번 시도해주세요.")
            }
            Thread {

                try {
                    dataOutputStream?.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream?.flush()
                } catch (e: IOException) {
                    msg("ID변경이 원할하지 않습니다. 다시한번 시도해주세요.")
                }
            }.start()

            try {
                Thread.sleep(50L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            Thread.currentThread()
            Thread.interrupted()

            val m = Message()
            m.what = DSPMessage.MSG_RSV.value
            m.obj = "Device Name : $id 적용되었습니다."
            mHandler.sendMessage(m)

        } else {
            msg("미지정 SPEAKER가 없습니다.")
        }
    }

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

}