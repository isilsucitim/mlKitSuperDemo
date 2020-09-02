package com.huawei.mlkitproject

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.huawei.mlkit.utils.CheckServiceAvailable
import com.huawei.mlkit.utils.DistributeType
import kotlinx.android.synthetic.main.activity_scan_bar_code.*

class ScanBarCodeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_bar_code)
        btn_take_photo.setOnClickListener {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_CAMERA
                )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            requestCode == REQUEST_CODE_CAMERA) {
            if (CheckServiceAvailable.getAvailableService(this) == DistributeType.HUAWEI_SERVICES) {
                ScanUtil.startScan(
                    this,
                    REQUEST_CODE_SCAN_ONE,
                    HmsScanAnalyzerOptions.Creator().create()
                )
            } else {
                val intent = Intent()
                //intent.type = "image/*"
                intent.action = android.provider.MediaStore.ACTION_IMAGE_CAPTURE
//                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, REQUEST_CODE_CAMERA)
                //startActivityForResult(Intent.createChooser(intent, "xx"), PIC_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            mMLKitManager.getScanBarcode(this, data, getScanBarcodeResult = {
                tv_result.text = it
            })
        }
    }

    companion object {
        const val REQUEST_CODE_CAMERA = 1111
        const val REQUEST_CODE_SCAN_ONE = 0X01
    }
}