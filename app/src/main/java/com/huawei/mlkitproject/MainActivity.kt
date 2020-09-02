package com.huawei.mlkitproject

import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(view: View) {
        when (view.id) {
            R.id.btn_translator -> {
                startActivity(Intent(this, TranslatorActivity::class.java))
            }
            R.id.btn_text_recognition -> {
                startActivity(Intent(this, TextRecognitionActivity::class.java))
            }
            R.id.btn_document_recognition -> {
                startActivity(Intent(this, DocumentRecognitionActivity::class.java))
            }
            R.id.btn_product_visual_search -> {
                startActivity(Intent(this, ProductVisualSearchActivity::class.java))
            }
            R.id.btn_object_detection -> {
                startActivity(Intent(this, ObjectDetectionActivity::class.java))
            }
            R.id.btn_language_identification -> {
                startActivity(Intent(this, LanguageIdentificationActivity::class.java))
            }
            R.id.btn_landmark_recognition -> {
                startActivity(Intent(this, LandMarkRecognitionActivity::class.java))
            }
            R.id.btn_scan_barcode -> {
                startActivity(Intent(this, ScanBarCodeActivity::class.java))
            }
            R.id.btn_auto_vision_edge -> {
                startActivity(Intent(this, AutoVisionEdgeActivity::class.java))
            }
        }
    }
}