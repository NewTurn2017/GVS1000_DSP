package com.gvkorea.gvs1000_dsp.util

enum class DSPFilterType(val value: Int) {
    DSP_FILTER_PEAKING(0),
    DSP_FILTER_SHELF_LOW(1),
    DSP_FILTER_SHELF_HIGH(2),
    DSP_FILTER_LO_PASS(3),
    DSP_FILTER_HI_PASS(4),
    DSP_FILTER_ALL_PASS(5),
    DSP_FILTER_LR12(112),
    DSP_FILTER_LR24(113),
    DSP_FILTER_LR36(114),
    DSP_FILTER_LR48(115),
    DSP_FILTER_BW12(128),
    DSP_FILTER_BW24(129),
    DSP_FILTER_BW36(130),
    DSP_FILTER_BW48(131),
    DSP_FILTER_BS12(144),
    DSP_FILTER_BS24(145),
    DSP_FILTER_BS36(146),
    DSP_FILTER_BS48(147),
    DSP_FILTER_NONE(255),

}