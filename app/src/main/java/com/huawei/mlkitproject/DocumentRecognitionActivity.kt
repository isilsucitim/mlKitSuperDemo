package com.huawei.mlkitproject

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_document_recognition.*

class DocumentRecognitionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_recognition)

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
            mMLKitManager.getDocumentRecognition(data, getDocumentRecognitionResult = {
                tv_result.text = it
            })
        }
    }
}