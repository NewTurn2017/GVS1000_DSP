package com.gvkorea.gvs1000_dsp.util

enum class DSPMessage(val value: Int) {
    MSG_RSV(1) ,
    MSG_INFO(2),
    MSG_QUIT(3),
    MSG_CONN(4),
    MSG_SOCK(5),
    MSG_UI_UNTOUCH(6),
    MSG_UI_TOUCH(7)
}