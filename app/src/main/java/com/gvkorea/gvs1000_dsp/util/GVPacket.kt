package com.gvkorea.gvs1000_dsp.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket

class GVPacket(val view: Fragment) {

    val protocol = Protocol()
    val CMD_PARA2_CH1 = '1'
    val CMD_PARA2_CH2 = '2'
    val CMD_PARA2_CHA = 'A'
    private val CHECKINTERVAL = 50L
    private lateinit var tx_buff: ByteArray
    private lateinit var outputStream: OutputStream
    private lateinit var dataOutputStream: DataOutputStream

    fun SendPacket_InputGain(socket: Socket?, channel: Char, gain: Float) {
        if (socket != null) {
            try {
                tx_buff = protocol.packet_InputGain(channel, gain)
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {

                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    private fun msg(msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun SendPacket_InputMute(socket: Socket?, channel: Char, switch: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_InputMute(channel, switch)
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {

                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    fun SendPacket_Bridge(socket: Socket?, setupBridge: Int) {
        if (socket != null) {
            try {
                tx_buff = protocol.packet_Bridge(setupBridge)
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {
                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    fun SendPacket_Reset_Default(socket: Socket?, channel: Char) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Reset_Default(channel)
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {

                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    fun SendPacket_StatusRequest_All(socket: Socket?, channel: Char) {
        SendPacket_StatusRequest_general(protocol.CMD_NOISE_GENERATOR, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_GAIN, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_MUTE, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_NOISE_GATE, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_PHASE_INVERTER, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_DELAY, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_PEAK_COMP, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_RMS_COMP, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_OUTPUT_GAIN, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_OUTPUT_MUTE, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_OUTPUT_LIMITER, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_OUTPUT_BPF, socket, CMD_PARA2_CH1)
        SendPacket_StatusRequest_general(protocol.CMD_OUTPUT_BPF, socket, CMD_PARA2_CH2)
        SendPacket_StatusRequest_general(protocol.CMD_REVERB, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_BRIDGE, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_OUTPUT_PEQ_ALL, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_INPUT_GEQ_ALL, socket, channel)
        SendPacket_StatusRequest_general(protocol.CMD_EQ_BYPASS, socket, channel)
    }

    fun SendPacket_StatusRequest_general(cmd: Char, socket: Socket?, channel: Char) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_StatusRequest_Common(
                    cmd, channel
                )
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {

                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    fun SendPacket_GEQ_Reset(socket: Socket?, channel: Char) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_input_EQ_Reset(channel)
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {
                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    fun SendPacket_InputGEQ(
        socket: Socket?,
        channel: Char,
        freq: Float,
        gain: Float,
        switch: Int
    ) {

        if (socket != null) {

            try {
                tx_buff = protocol.packet_InputGEQ(channel, freq, gain, switch)
                outputStream = socket!!.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {
                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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

    fun SendPacket_InputGEQBypass(socket: Socket?, channel: Char, switch: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_input_GEQ_Bypass(
                    channel,
                    switch
                )
                outputStream = socket.getOutputStream()
                dataOutputStream = DataOutputStream(outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Thread {

                try {
                    dataOutputStream.write(tx_buff, 0, tx_buff.size)
                    dataOutputStream.flush()
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


}