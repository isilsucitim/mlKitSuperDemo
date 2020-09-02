package com.huawei.mlkit.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.huawei.mlkit.factory.MLKit
import com.huawei.mlkit.factory.MLKitFactory
import com.huawei.mlkit.model.LandMarkModel
import com.huawei.mlkit.utils.CheckServiceAvailable

typealias TypeAliasString = (response: String) -> Unit
typealias TypeAliasLandMark = (response: LandMarkModel) -> Unit

class MLKitManager(activity: Activity) {

    private var mlKit: MLKit? = null

    init {
        mlKit = MLKitFactory.getMLKitFactory(CheckServiceAvailable.getAvailableService(activity))
    }

    fun getTranslate(text: String, getTranslatedTextResult: TypeAliasString) {
        mlKit?.translate(text, getTranslatedTextResult)
    }

    fun getTextRecognition(
        context: Context,
        imageData: Intent?,
        getTextRecognitionResult: TypeAliasString
    ) {
        mlKit?.textRecognition(context, imageData, getTextRecognitionResult)
    }

    fun getObjectDetection(context: Context, imageData: Intent?, getTextResponse: TypeAliasString) {
        mlKit?.objectDetection(context, imageData, getTextResponse)
    }

    fun getImageDetection(context: Context, imageData: Intent?, getImageDetectionResult: TypeAliasString) {
        mlKit?.imageDetection(context, imageData, getImageDetectionResult)
    }

    fun getObjectSegmentation(context: Context, imageData: Intent?, getObjectSegmentationResult: TypeAliasString) {
        mlKit?.objectSegmentation(context, imageData, getObjectSegmentationResult)
    }

    fun getLanguageIdentification(text: String, getLanguageIdentificationResult: TypeAliasString) {
        mlKit?.languageIdentification(text, getLanguageIdentificationResult)
    }

    fun getLandmarkRecognition(
        context: Context,
        imageData: Intent?,
        getLandmarkRecognition: TypeAliasLandMark
    ) {
        mlKit?.landmarkRecognition(context, imageData, getLandmarkRecognition)
    }

    fun getScanBarcode(context: Context, imageData: Intent?, getScanBarcodeResult: TypeAliasString) {
        mlKit?.scanBarcode(context, imageData, getScanBarcodeResult)
    }

    fun getAutoVisionEdge(context: Context, imageData: Intent?, getAutoVisionEdgeResult: TypeAliasString) {
        mlKit?.autoVisionEdge(context, imageData, getAutoVisionEdgeResult)
    }

    fun getDocumentRecognition(
        imageData: Intent?,
        getDocumentRecognitionResult: TypeAliasString
    ) {
        mlKit?.productVisualSearch(imageData, getDocumentRecognitionResult)
    }

    fun getProductVisualSearch(imageData: Intent?, getProductVisualSearchResult: TypeAliasString) {
        mlKit?.productVisualSearch(imageData, getProductVisualSearchResult)
    }
}