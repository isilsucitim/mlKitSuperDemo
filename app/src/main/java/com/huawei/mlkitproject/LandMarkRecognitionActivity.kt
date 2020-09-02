package com.huawei.mlkitproject

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_land_mark_recognition.*

class LandMarkRecognitionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_land_mark_recognition)

        btn_take_photo.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "xx"), PIC_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PIC_IMAGE) {
            mMLKitManager.getLandmarkRecognition(this, data, getLandmarkRecognition = {
                tv_result.text = it.responseText
            })
        }
    }
}