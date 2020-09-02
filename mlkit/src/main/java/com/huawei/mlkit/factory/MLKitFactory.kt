package com.huawei.mlkit.factory

import com.huawei.mlkit.utils.DistributeType

object MLKitFactory {

    fun getMLKitFactory(type: DistributeType) = if (type == DistributeType.HUAWEI_SERVICES) {
        HuaweiMLKitImpl()
    } else {
        FirebaseMLKitImpl()
    }

}