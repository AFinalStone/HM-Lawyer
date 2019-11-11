package com.hm.iou.lawyer.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.iou.lawyer.business.lawyer.workbench.WorkBenchActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_lawyer.setOnClickListener {
            startActivity(Intent(this@MainActivity, WorkBenchActivity::class.java))
        }
    }
}
