package com.huawei.mlkit.factory

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import com.huawei.hmf.tasks.Task
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.classification.MLImageClassificationAnalyzer
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.document.MLDocumentAnalyzer
import com.huawei.hms.mlsdk.document.MLDocumentSetting
import com.huawei.hms.mlsdk.langdetect.MLLangDetectorFactory
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetector
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzerSetting
import com.huawei.hms.mlsdk.productvisionsearch.cloud.MLRemoteProductVisionSearchAnalyzerSetting
import com.huawei.hms.mlsdk.text.MLRemoteTextSetting
import com.huawei.hms.mlsdk.text.MLTextAnalyzer
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting
import com.huawei.mlkit.model.LandMarkModel
import java.io.IOException
import java.util.*

class HuaweiMLKitImpl : BaseMLKit() {

    override fun translate(text: String, getTranslatedTextResult: (translatedText: String) -> Unit) {
        super.translate(text, getTranslatedTextResult)
        val sourceLangCode = "en"
        val targetLangCode = "zh"

        val setting =
            MLRemoteTranslateSetting.Factory() // Set the source language code. The ISO 639-1 standard is used.
                // This parameter is optional. If this parameter is not set, the system automatically detects the language.
                .setSourceLangCode(sourceLangCode) // Set the target language code. The ISO 639-1 standard is used.
                .setTargetLangCode(targetLangCode)
                .create()

        val mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting)

        val task = mlRemoteTranslator?.asyncTranslate(text)

        task?.addOnSuccessListener { translatedText ->
            getTranslatedTextResult(translatedText)

        }?.addOnFailureListener { e ->
            getTranslatedTextResult("Fail-2")
            println("${e.message}")
            println("${e.cause?.message}")
            println("${e.localizedMessage}")
            e.stackTrace.forEach {
                println("${it.fileName}")
                println("${it.className}")
                println("${it.methodName}")
            }
        }
    }

    override fun textRecognition(
        context: Context,
        imageData: Intent?,
        getTextRecognitionResult: (recognitionText: String) -> Unit
    ) {
        super.textRecognition(context, imageData, getTextRecognitionResult)

        val textAnalyzer: MLTextAnalyzer? = MLAnalyzerFactory.getInstance().localTextAnalyzer

        val bitmapImage = imageData?.extras!!["data"] as Bitmap?
        val mlFrame = MLFrame.Creator().setBitmap(Objects.requireNonNull(bitmapImage)).create()
        val task = textAnalyzer?.asyncAnalyseFrame(mlFrame)

        task?.addOnSuccessListener { mlTextResults ->
            val text = mlTextResults.stringValue
            textAnalyzer.stop()
            Log.i("analyzeText", "analyzeText success: $text")
            getTextRecognitionResult(text)

        }?.addOnFailureListener { e ->
            Log.e("analyzeText", "analyzeText Fail: " + e.message)
            getTextRecognitionResult("Fail-4-1")
        }
    }

    override fun objectDetection(context: Context, imageData: Intent?, getTextResponse: (text: String) -> Unit) {
        super.objectDetection(context, imageData, getTextResponse)
        // Use MLObjectAnalyzerSetting.TYPE_PICTURE for static image detection.
        val setting = MLObjectAnalyzerSetting.Factory()
            .setAnalyzerType(MLObjectAnalyzerSetting.TYPE_PICTURE)
            .allowMultiResults()
            .allowClassification()
            .create()

        val analyzer = MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(setting)

        // Create an MLFrame object using the bitmap, which is the image data in bitmap format.
        val bitmapImage = imageData?.extras!!["data"] as Bitmap?
        val frame = MLFrame.fromBitmap(bitmapImage)

        // Create a task to process the result returned by the object detector.
        val task = analyzer.asyncAnalyseFrame(frame)
        // Asynchronously process the result returned by the object detector.
        task.addOnSuccessListener {
            // Detection success.
            Log.d("MLKit", "staticObjectDetection: " + it[0].typeIdentity.toString())
            getTextResponse(it[0].typeIdentity.toString())
        }.addOnFailureListener {
            // Detection failure.
            getTextResponse("Faill-6")
        }
    }

    override fun objectSegmentation(
        context: Context,
        imageData: Intent?,
        getObjectSegmentationResult: (response: String) -> Unit
    ) {
        super.objectSegmentation(context, imageData, getObjectSegmentationResult)
        val analyzer = MLAnalyzerFactory.getInstance().imageSegmentationAnalyzer

        val bitmapImage = imageData?.extras!!["data"] as Bitmap?
        val frame = MLFrame.fromBitmap(bitmapImage)

        // Create a task to process the result returned by the image segmentation analyzer.
        val task = analyzer.asyncAnalyseFrame(frame)
        // Asynchronously process the result returned by the image segmentation analyzer.
        task.addOnSuccessListener {
            // Detection success.
            Log.d("MLKit", "staticObjectSegmentation: " + it.getOriginal().toString())
            getObjectSegmentationResult(it.getOriginal().toString())
        }.addOnFailureListener {
            // Detection failure.
            getObjectSegmentationResult("Faill-7")
        }
    }

    override fun imageDetection(
        context: Context,
        imageData: Intent?,
        getImageDetectionResult: (response: String) -> Unit
    ) {
        super.imageDetection(context, imageData, getImageDetectionResult)
        val analyzer: MLImageClassificationAnalyzer =
            MLAnalyzerFactory.getInstance().localImageClassificationAnalyzer

        val bitmapImage = imageData?.extras!!["data"] as Bitmap?
        val frame = MLFrame.fromBitmap(bitmapImage)

        val task = analyzer.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            // Recognition success.
            Log.d("MLKit", "staticImageDetection: " + it[0].name.toString())
            getImageDetectionResult(it[0].name.toString())

        }.addOnFailureListener {
            // Recognition failure.
            getImageDetectionResult("Faill-8")
        }

        try {
            analyzer.stop()
        } catch (e: IOException) {
            Log.d("MLKit", "staticImageDetection: ${e.message.toString()}")
        }
    }

    override fun languageIdentification(text: String, getLanguageIdentificationResult: (text: String) -> Unit) {
        super.languageIdentification(text, getLanguageIdentificationResult)
        val mlRemoteLangDetect: MLRemoteLangDetector = MLLangDetectorFactory.getInstance()
            .remoteLangDetector
        val firstBestDetectTask: Task<String> =
            mlRemoteLangDetect.firstBestDetect(text)
        firstBestDetectTask.addOnSuccessListener {
            // Processing logic for detection success.
            getLanguageIdentificationResult(it)
        }.addOnFailureListener {
            // Processing logic for detection failure.
            getLanguageIdentificationResult("Fail-4-1")
        }
    }

    override fun landmarkRecognition(
        context: Context,
        imageData: Intent?,
        getLandmarkRecognition: (response: LandMarkModel) -> Unit
    ) {
        super.landmarkRecognition(context, imageData, getLandmarkRecognition)

        val btm: Bitmap
        try {
            btm = MediaStore.Images.Media.getBitmap(context.contentResolver, imageData?.data)
            val mutBtm = btm.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(mutBtm)
            try {
                val image = FirebaseVisionImage.fromFilePath(
                    context,
                    Objects.requireNonNull(imageData?.data!!)
                )
                val options = FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                    .build()
                val detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options)
                val result =
                    detector.detectInImage(image)
                        .addOnSuccessListener { faces ->
                            val landMarkModel = LandMarkModel()
                            // Task completed successfully
                            // ...
                            for (face in faces) {
                                val bounds = face.boundingBox
                                val p = Paint()
                                p.color = Color.YELLOW
                                p.style = Paint.Style.STROKE
                                canvas.drawRect(bounds, p)

                                landMarkModel.responseBitmap = mutBtm
                                getLandmarkRecognition(landMarkModel)
                                //imageView.setImageBitmap(mutBtm)

                                val rotY =
                                    face.headEulerAngleY // Head is rotated to the right rotY degrees
                                val rotZ =
                                    face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                                // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                // nose available):
                                val leftEar =
                                    face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
                                if (leftEar != null) {
                                    val leftEarPos = leftEar.position
                                    val rect = Rect(
                                        (leftEarPos.x - 20).toInt(),
                                        (leftEarPos.y - 20).toInt(),
                                        (leftEarPos.x + 20).toInt(),
                                        (leftEarPos.y + 20).toInt()
                                    )
                                    canvas.drawRect(rect, p)
                                    //
                                    landMarkModel.responseBitmap = mutBtm
                                    getLandmarkRecognition(landMarkModel)
                                    //imageView.setImageBitmap(mutBtm)
                                }

                                // If contour detection was enabled:
                                val leftEyeContour =
                                    face.getContour(FirebaseVisionFaceContour.LEFT_EYE).points
                                val upperLipBottomContour =
                                    face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).points

                                // If classification was enabled:
                                val p2 = Paint()
                                p2.color = Color.BLACK
                                p2.textSize = 16f
                                if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                    val smileProb = face.smilingProbability
                                    if (smileProb > 0.5) {
                                        canvas.drawText(
                                            "Smiling for now :D",
                                            bounds.exactCenterX(),
                                            bounds.exactCenterY(),
                                            p2
                                        )
                                        //
                                        landMarkModel.responseBitmap = mutBtm
                                        getLandmarkRecognition(landMarkModel)
                                        //imageView.setImageBitmap(mutBtm)
                                    } else {
                                        canvas.drawText(
                                            "Not Smiling finally :D",
                                            bounds.exactCenterX(),
                                            bounds.exactCenterY(),
                                            p2
                                        )
                                        //
                                        landMarkModel.responseBitmap = mutBtm
                                        getLandmarkRecognition(landMarkModel)
                                        //imageView.setImageBitmap(mutBtm)
                                    }
                                }
                                if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                    val rightEyeOpenProb = face.rightEyeOpenProbability
                                }

                                // If face tracking was enabled:
                                if (face.trackingId != FirebaseVisionFace.INVALID_ID) {
                                    val id = face.trackingId
                                }
                            }
                        }
                        .addOnFailureListener {
                            // Task failed with an exception
                            // ...
                        }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun scanBarcode(context: Context, imageData: Intent?, getScanBarcodeResult: (response: String) -> Unit) {
        super.scanBarcode(context, imageData, getScanBarcodeResult)
        val hmsScan: HmsScan? = imageData?.getParcelableExtra(ScanUtil.RESULT)
        getScanBarcodeResult("${hmsScan?.getOriginalValue()}")
    }

    override fun autoVisionEdge(
        context: Context,
        imageData: Intent?,
        getAutoVisionEdgeResult: (response: String) -> Unit
    ) {
        //
    }

    override fun documentRecognition(imageData: Intent?, getRecognitionDocumentResult: (response: String) -> Unit) {
        super.documentRecognition(imageData, getRecognitionDocumentResult)
        val languageList = listOf("zh", "en")
        val setting = MLDocumentSetting.Factory()
            .setLanguageList(languageList)
            .setBorderType(MLRemoteTextSetting.ARC)
            .create()

        val documentAnalyzer: MLDocumentAnalyzer = MLAnalyzerFactory.getInstance().getRemoteDocumentAnalyzer(setting)
        val bitmapImage = imageData?.extras!!["data"] as Bitmap?
        val mlFrame = MLFrame.Creator().setBitmap(Objects.requireNonNull(bitmapImage)).create()
        val task = documentAnalyzer.asyncAnalyseFrame(mlFrame)
        task.addOnSuccessListener { mlDocumentResult ->
            val document = mlDocumentResult.stringValue
            Log.i("analyzeDocument", "analyzeDocument success: $document")
            getRecognitionDocumentResult(document)
            documentAnalyzer.close()
        }.addOnFailureListener { e ->
            Log.e("analyzeDocument", "analyzeDocument Fail: " + e.localizedMessage)
            getRecognitionDocumentResult("Fail-4-1")
        }
    }

    override fun productVisualSearch(imageData: Intent?, getProductVisualSearchResult: (response: String) -> Unit) {
        val settings = MLRemoteProductVisionSearchAnalyzerSetting.Factory()
            .setLargestNumOfReturns(5)
            .create()

        val productVisionSearchAnalyzer = MLAnalyzerFactory.getInstance().getRemoteProductVisionSearchAnalyzer(settings)

        val bitmapImage = imageData?.extras!!["data"] as Bitmap?
        val mlFrame = MLFrame.Creator().setBitmap(Objects.requireNonNull(bitmapImage)).create()
        val task = productVisionSearchAnalyzer!!.asyncAnalyseFrame(mlFrame)

        task.addOnSuccessListener { mlSearchResult ->
            val list = mlSearchResult.get(0).productList.size
            Log.i("visualSearch", "visualSearch success: $list")
            getProductVisualSearchResult("visualSearch success: $list")
        }.addOnFailureListener { e ->
            val mlException = e as MLException
            Log.e("visualSearch", "visualSearch Fail: " + mlException.errCode)
            getProductVisualSearchResult("Fail-5")
        }
    }

}