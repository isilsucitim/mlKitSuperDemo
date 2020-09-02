package com.huawei.mlkitproject

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_object_segmentation.*

class ObjectSegmentationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_segmentation)

        btn_take_photo.setOnClickListener {
            val intent = Intent()
            //intent.type = "image/*"
            intent.action = android.provider.MediaStore.ACTION_IMAGE_CAPTURE
            //intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PIC_IMAGE)
            //startActivityForResult(Intent.createChooser(intent, "xx"), PIC_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PIC_IMAGE) {
            mMLKitManager.getObjectSegmentation(this, data, getObjectSegmentationResult = {
                tv_result.text = it
            })
        }
    }
}