package com.huawei.mlkitproject

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_language_identification.*

class LanguageIdentificationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_identification)

        btn_get_language_identification.setOnClickListener {
            val text = et_input.text.toString()
            mMLKitManager.getLanguageIdentification(text, getLanguageIdentificationResult = {
                tv_result.text = it
            })
        }
    }

}