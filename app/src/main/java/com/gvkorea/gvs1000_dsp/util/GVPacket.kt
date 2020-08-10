package com.gvkorea.gvs1000_dsp.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gvkorea.gvs1000_dsp.MainActivity.Companion.presetSavedList
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket

class GVPacket(val view: Fragment) {

    val protocol = Protocol()
    val CMD_PARA2_CH1 = '1'
    val CMD_PARA2_CH2 = '2'
    private val CHECKINTERVAL = 50L
    private val EQINTERVAL = 100L
    private val LISTINTERVAL = 20L
    private lateinit var tx_buff: ByteArray
    private lateinit var outputStream: OutputStream
    private var dataOutputStream: DataOutputStream? = null

    fun SendPacket_LevelmeterOutput(socket: Socket?, channel: Char, switch: Int) {
        if (socket != null) {
            try {
                tx_buff = protocol.packet_LevelmeterOutput(channel, switch)
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

    fun SendPacket_Preset_SetName(socket: Socket?, presetNo: Int, name: String) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_SetName(presetNo, name)
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

    fun SendPacket_Preset_Save(socket: Socket?, presetNo: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_Save(presetNo)
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

    fun SendPacket_Preset_Save_Start(socket: Socket?) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_preset_Start()
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

    fun SendPacket_Preset_Save_End(socket: Socket?) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_preset_End()
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

    fun SendPacket_Preset_Select(socket: Socket?, presetNo: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_Select(presetNo)
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
                Thread.sleep(50L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            Thread.currentThread()
            Thread.interrupted()
        } else {
            msg("TCP Socket 연결 안됨")
        }
    }

    fun SendPacket_Preset_Request_All(socket: Socket?) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_Request_All()
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
                Thread.sleep(50L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            Thread.currentThread()
            Thread.interrupted()
        } else {
            msg("TCP Socket 연결 안됨")
        }
    }

    fun SendPacket_Preset_Load(socket: Socket?, presetNo: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_Load(presetNo)
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

    fun SendPacket_Preset_GetName_All(socket: Socket?){
        for(i in 0..11){
            SendPacket_Preset_GetName(socket, i)
        }
    }

    fun SendPacket_Preset_Delete_All(socket: Socket?) {
        for(i in presetSavedList.indices){
            SendPacket_Preset_Delete(socket, i)
        }
    }

    fun SendPacket_Preset_Delete(socket: Socket?, presetNo: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_Delete(presetNo)
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

    fun SendPacket_Preset_GetName(socket: Socket?, presetNo: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_Preset_GetName(presetNo)
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
                Thread.sleep(LISTINTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            Thread.currentThread()
            Thread.interrupted()
        } else {
            msg("TCP Socket 연결 안됨")
        }
    }

    fun SendPacket_InputGEQ_Reset(socket: Socket?, channel: Char) {
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

    fun SendPacket_NoiseGenerator(socket: Socket?, channel: Char, noise: Int, gain: Float, switch: Int) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_NoiseGenerator(
                        channel,
                        noise,
                        gain,
                        switch
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

    fun SendPacket_EQ_All(socket: Socket?, channel: Char, eq: FloatArray) {
        if (socket != null) {

            try {
                tx_buff = protocol.packet_InputGEQ_All(channel, eq)
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
                Thread.sleep(EQINTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            Thread.currentThread()
            Thread.interrupted()
        } else {
            msg("TCP Socket 연결 안됨")
        }
    }

    fun savePreset(socket: Socket?, presetNo: Int, title: String) {
        SendPacket_Preset_Save_Start(socket)
        SendPacket_Preset_SetName(socket, presetNo, title)
        SendPacket_Preset_Save(socket, presetNo)
        SendPacket_Preset_Save_End(socket)
    }


}