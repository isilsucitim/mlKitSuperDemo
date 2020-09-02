package com.huawei.mlkit.factory

import android.content.Context
import android.content.Intent
import com.huawei.mlkit.model.LandMarkModel

open class BaseMLKit : MLKit {

    override fun translate(text: String, getTranslatedTextResult: (translatedText: String) -> Unit) {
        println("translate")
    }

    override fun textRecognition(
        context: Context,
        imageData: Intent?,
        getTextRecognitionResult: (recognitionText: String) -> Unit
    ) {
        println("textRecognition")
    }

    override fun documentRecognition(imageData: Intent?, getRecognitionDocumentResult: (response: String) -> Unit) {
        println("documentRecognition")
    }

    override fun productVisualSearch(imageData: Intent?, getProductVisualSearchResult: (response: String) -> Unit) {
        println("productVisualSearch")
    }

    override fun objectDetection(context: Context, imageData: Intent?, getTextResponse: (text: String) -> Unit) {
        println("objectDetection")
    }

    override fun objectSegmentation(context: Context, imageData: Intent?, getTextResponse: (response: String) -> Unit) {
        println("objectSegmentation")
    }

    override fun imageDetection(context: Context, imageData: Intent?, getTextResponse: (response: String) -> Unit) {
        println("imageDetection")
    }

    override fun languageIdentification(text: String, getLanguageIdentificationResult: (text: String) -> Unit) {
        println("languageIdentification")
    }

    override fun landmarkRecognition(
        context: Context,
        imageData: Intent?,
        getLandmarkRecognition: (response: LandMarkModel) -> Unit
    ) {
        println("landmarkRecognition")
    }

    override fun scanBarcode(context: Context, imageData: Intent?, getScanBarcodeResult: (response: String) -> Unit) {
        println("scanBarcode")
    }

    override fun autoVisionEdge(
        context: Context,
        imageData: Intent?,
        getAutoVisionEdgeResult: (response: String) -> Unit
    ) {
        println("autoVisionEdge")
    }

}