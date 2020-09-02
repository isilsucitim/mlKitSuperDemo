package com.huawei.mlkit.utils

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability

object CheckServiceAvailable {

    fun getAvailableService(context: Context) = when {
        com.huawei.hms.api.ConnectionResult.SUCCESS == HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(
                context
            ) -> DistributeType.HUAWEI_SERVICES
        ConnectionResult.SUCCESS == GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(
                context
            ) -> DistributeType.FIREBASE_SERVICES
        else -> DistributeType.HUAWEI_SERVICES
    }

}


