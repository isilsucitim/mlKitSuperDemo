package com.huawei.mlkitproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.huawei.mlkit.manager.MLKitManager

open class BaseActivity : AppCompatActivity() {

    val PIC_IMAGE = 12345

    lateinit var mMLKitManager: MLKitManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mMLKitManager = MLKitManager(this)
    }

}