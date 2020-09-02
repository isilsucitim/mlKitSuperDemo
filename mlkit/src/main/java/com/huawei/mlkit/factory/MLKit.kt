package com.huawei.mlkit.factory

import android.content.Context
import android.content.Intent
import com.huawei.mlkit.model.LandMarkModel

interface MLKit {

    fun translate(text: String, getTranslatedTextResult: (response: String) -> Unit)

    fun objectDetection(context: Context, imageData: Intent?, getTextResponse: (response: String) -> Unit)

    fun objectSegmentation(context: Context, imageData: Intent?, getObjectSegmentationResult: (response: String) -> Unit)

    fun imageDetection(context: Context, imageData: Intent?, getImageDetectionResult: (response: String) -> Unit)

    fun languageIdentification(text: String, getLanguageIdentificationResult: (response: String) -> Unit)

    fun landmarkRecognition(
        context: Context,
        imageData: Intent?,
        getLandmarkRecognition: (response: LandMarkModel) -> Unit
    )

    fun scanBarcode(context: Context, imageData: Intent?, getScanBarcodeResult: (response: String) -> Unit)

    fun autoVisionEdge(context: Context, imageData: Intent?, getAutoVisionEdgeResult: (response: String) -> Unit)

    fun textRecognition(
        context: Context,
        imageData: Intent?,
        getTextRecognitionResult: (response: String) -> Unit
    )

    fun documentRecognition(
        imageData: Intent?,
        getRecognitionDocumentResult: (response: String) -> Unit
    )

    fun productVisualSearch(imageData: Intent?, getProductVisualSearchResult: (response: String) -> Unit)
}