package com.hm.iou.lawyer.demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.iou.lawyer.business.lawyer.WorkBenchActivity
import com.hm.iou.tools.kt.startActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_lawyer.setOnClickListener {
            startActivity(Intent(this@MainActivity, WorkBenchActivity::class.java))
        }

        btn_lawyer_index.setOnClickListener {
            startActivity<TabActivity>()
        }

    }
}
