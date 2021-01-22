package com.mj.brandi_aos_task.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mj.brandi_aos_task.R
import com.mj.brandi_aos_task.common.Constant

class FullScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

//        intent.putExtra(Constant.IMAGE_URL, data.image_url)
//        intent.putExtra(Constant.DISPLAY_SITENAME, data.display_sitename ?: "")
//        intent.putExtra(Constant.DATETIME, data.datetime ?: "")
    }
}