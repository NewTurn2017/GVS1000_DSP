package com.gvkorea.gvs1000_dsp.util

import java.net.Socket

data class SpeakerInfo(var socket: Socket?, var channel: Char, var name: String)