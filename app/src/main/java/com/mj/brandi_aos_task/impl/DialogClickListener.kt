package com.mj.brandi_aos_task.impl

import android.content.DialogInterface

interface DialogClickListener {

    fun positiveClick(dialog: DialogInterface)
    fun negetiveClick(dialog: DialogInterface)
}