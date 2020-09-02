package com.huawei.mlkitproject

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_translator.*

class TranslatorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        btn_translate.setOnClickListener {
            val text = et_input.text.toString()
            mMLKitManager.getTranslate(text, getTranslatedTextResult = {
                println("Response - $it")
                tv_result.text = it
            })
        }
    }

}