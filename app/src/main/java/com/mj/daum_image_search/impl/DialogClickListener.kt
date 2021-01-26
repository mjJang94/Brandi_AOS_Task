package com.mj.daum_image_search.impl

import android.content.DialogInterface

interface DialogClickListener {

    fun positiveClick(dialog: DialogInterface)
    fun negetiveClick(dialog: DialogInterface)
}