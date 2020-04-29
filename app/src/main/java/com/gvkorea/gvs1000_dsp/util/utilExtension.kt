package com.gvkorea.gvs1000_dsp.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replace(@IdRes frameID: Int, fragment: Fragment, tag:String? = null){
    val transaction = supportFragmentManager.beginTransaction()
    transaction.detach(fragment)
    transaction.attach(fragment)
    transaction.replace(frameID, fragment, tag).commit()
}

fun Fragment.replaceChild(@IdRes frameID: Int, fragment: Fragment, tag:String? = null){
    childFragmentManager.beginTransaction().replace(frameID, fragment, tag).commit()
}