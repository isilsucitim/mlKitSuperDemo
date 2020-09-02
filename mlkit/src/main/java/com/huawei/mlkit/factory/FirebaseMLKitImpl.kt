package com.huawei.mlkit.factory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.text.Html
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmarkAnalyzerSetting
import com.huawei.mlkit.model.LandMarkModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class FirebaseMLKitImpl : BaseMLKit() {

    override fun translate(text: String, getTranslatedTextResult: (translatedText: String) -> Unit) {
        super.translate(text, getTranslatedTextResult)
        // Create an English-German translator:
        val options: FirebaseTranslatorOptions = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.DE)
            .build()

        val translator: FirebaseTranslator =
            FirebaseNaturalLanguage.getInstance().getTranslator(options)

        val conditions: FirebaseModelDownloadConditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator
                    .translate(text)
                    .addOnSuccessListener { translatedText -> // Translation successful.
                        getTranslatedTextResult(translatedText)
                    }
                    .addOnFailureListener {
                        getTranslatedTextResult("Fail-1")
                    }
            }
            .addOnFailureListener { // Model couldn’t be downloaded or other internal error.
                getTranslatedTextResult("Fail-11")
            }
    }

    override fun textRecognition(
        context: Context,
        imageData: Intent?,
        getTextRecognitionResult: (recognitionText: String) -> Unit
    ) {
        super.textRecognition(context, imageData, getTextRecognitionResult)

        try {
            val image = FirebaseVisionImage.fromFilePath(context, imageData?.data!!)
            val textRecognizer: FirebaseVisionTextRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

            textRecognizer.processImage(image)
                .addOnSuccessListener { result -> // Task completed successfully
                    // ...
                    // resultTv.setText(result.getText());
                    val resultText = result.text
                    val stringBuilder: StringBuilder = StringBuilder()
                    for (block in result.textBlocks) {
                        val blockText = block.text
                        val blockConfidence = block.confidence
                        val blockLanguages = block.recognizedLanguages
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox

                        for (line in block.lines) {
                            val lineText = line.text
                            val lineConfidence = line.confidence
                            val lineLanguages =
                                line.recognizedLanguages
                            val lineCornerPoints = line.cornerPoints
                            val lineFrame = line.boundingBox

                            for (element in line.elements) {
                                stringBuilder.append(
                                    """
                                    ${element.text}
                                    
                                    """.trimIndent()
                                )
                                val elementText = element.text
                                val elementConfidence = element.confidence
                                val elementLanguages =
                                    element.recognizedLanguages
                                val elementCornerPoints =
                                    element.cornerPoints
                                val elementFrame = element.boundingBox
                            }
                            stringBuilder.append("\n\n")
                        }
                        stringBuilder.append("\n\n")
                    }

                    getTextRecognitionResult(stringBuilder.toString())
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                    getTextRecognitionResult("Fail-3-1")
                }
        } catch (e: Exception) {
            getTextRecognitionResult("Fail-3-2")
        }
    }

    override fun productVisualSearch(imageData: Intent?, getProductVisualSearchResult: (response: String) -> Unit) {
        super.productVisualSearch(imageData, getProductVisualSearchResult)
        //
    }

    override fun documentRecognition(imageData: Intent?, getRecognitionDocumentResult: (response: String) -> Unit) {
        super.documentRecognition(imageData, getRecognitionDocumentResult)
        //
    }

    override fun objectDetection(context: Context, imageData: Intent?, getTextResponse: (text: String) -> Unit) {
        super.objectDetection(context, imageData, getTextResponse)

        try {
            val image = FirebaseVisionImage.fromFilePath(context, imageData?.data!!)
            val labeler = FirebaseVision.getInstance()
                .onDeviceImageLabeler
            labeler.processImage(image)
                .addOnSuccessListener { labels -> // Task completed successfully
                    // ...
                    var text = ""
                    for (label in labels) {
                        text += "${label.text}\n"
                    }
                    getTextResponse(text)
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                    println("it $it")
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun objectSegmentation(context: Context, imageData: Intent?, getTextResponse: (response: String) -> Unit) {
        super.objectSegmentation(context, imageData, getTextResponse)
        //
    }

    override fun imageDetection(context: Context, imageData: Intent?, getTextResponse: (response: String) -> Unit) {
        super.imageDetection(context, imageData, getTextResponse)
        //
    }

    override fun languageIdentification(text: String, getLanguageIdentificationResult: (text: String) -> Unit) {
        super.languageIdentification(text, getLanguageIdentificationResult)
        val languageIdentifier =
            FirebaseNaturalLanguage.getInstance().languageIdentification

        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                getLanguageIdentificationResult(languageCode)
            }
            .addOnFailureListener {
                getLanguageIdentificationResult("Fail-4-1")
            }
    }

    override fun landmarkRecognition(
        context: Context,
        imageData: Intent?,
        getLandmarkRecognition: (response: LandMarkModel) -> Unit
    ) {
        super.landmarkRecognition(context, imageData, getLandmarkRecognition)

        val settings = MLRemoteLandmarkAnalyzerSetting.Factory()
            .setLargestNumOfReturns(1)
            .setPatternType(MLRemoteLandmarkAnalyzerSetting.STEADY_PATTERN)
            .create()
        val analyzer = MLAnalyzerFactory.getInstance()
            .getRemoteLandmarkAnalyzer(settings)
        // Create an MLFrame by using android.graphics.Bitmap. Recommended image size: large than 640*640.
     // val imageBitmap = imageData?.extras!!["data"] as Bitmap?

        val imageUri = imageData?.data as Uri
        val imageBitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        //val imageBitmap = imageData?.extras?.get("data") as? Bitmap

        if (imageBitmap != null) {
            val mlFrame = MLFrame.Creator()
                .setBitmap(Objects.requireNonNull(imageBitmap))
                .create()

            val landMarkModel = LandMarkModel()

            val task = analyzer!!.asyncAnalyseFrame(mlFrame)

            task.addOnSuccessListener { landmarkResults ->
                val landmark: MLRemoteLandmark = landmarkResults[0]
                landMarkModel.responseText = getSuccessText(landmark)
                getLandmarkRecognition(landMarkModel)
                // Processing logic for recognition success.

            }.addOnFailureListener {
                // Processing logic for recognition failure
                landMarkModel.responseText = getFailureText()
                getLandmarkRecognition(landMarkModel)
            }
        }
    }

    override fun scanBarcode(context: Context, imageData: Intent?, getScanBarcodeResult: (response: String) -> Unit) {
        super.scanBarcode(context, imageData, getScanBarcodeResult)
        val image: FirebaseVisionImage
        try {
            image = FirebaseVisionImage.fromFilePath(
                context,
                Objects.requireNonNull(imageData?.data!!)
            )

            //  FirebaseVisionBarcodeDetectorOptions options =
            //          new FirebaseVisionBarcodeDetectorOptions.Builder()
            //                  .setBarcodeFormats(
            //                          FirebaseVisionBarcode.FORMAT_QR_CODE,
            //                          FirebaseVisionBarcode.FORMAT_AZTEC)
            //                  .build();
            val detector = FirebaseVision.getInstance()
                .visionBarcodeDetector
            val result =
                detector.detectInImage(image)
                    .addOnSuccessListener { barcodes -> // Task completed successfully
                        // ...
                        for (barcode in barcodes) {
                            val bounds = barcode.boundingBox
                            val corners = barcode.cornerPoints
                            val rawValue = barcode.rawValue
                            val valueType = barcode.valueType
                            when (valueType) {
                                FirebaseVisionBarcode.TYPE_WIFI -> {
                                    val ssid =
                                        Objects.requireNonNull(barcode.wifi)!!.ssid
                                    val password = barcode.wifi!!.password
                                    val type = barcode.wifi!!.encryptionType
                                    val response = "$ssid  $password  $type"
                                    getScanBarcodeResult(response)
                                    //resultTv.setText("$ssid  $password  $type")
                                }
                                FirebaseVisionBarcode.TYPE_URL -> {
                                    val title =
                                        Objects.requireNonNull(barcode.url)!!.title
                                    val url = barcode.url!!.url
                                    val response = "$title  $url"
                                    getScanBarcodeResult(response)
                                    //resultTv.setText("$title  $url")
                                }
                                FirebaseVisionBarcode.TYPE_PHONE -> {
                                    val phone =
                                        Objects.requireNonNull(barcode.phone)!!.number
                                    getScanBarcodeResult(phone ?: "")
                                    //resultTv.setText(phone)
                                }
                                FirebaseVisionBarcode.TYPE_EMAIL -> {
                                    val email = barcode.email!!.address
                                    val subject = barcode.email!!.subject
                                    val email_body = barcode.email!!.body
                                    val response = """
                                            $email
                                            $subject
                                            $email_body
                                            """.trimIndent()
                                    getScanBarcodeResult(response)
                                    //resultTv.setText("""$email$subject$email_body""".trimIndent())
                                }
                            }
                        }
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                        getScanBarcodeResult("Fail-6-1")
                    }
        } catch (e: IOException) {
            e.printStackTrace()
            getScanBarcodeResult("Fail-6-2")
        }
    }

    override fun autoVisionEdge(
        context: Context,
        imageData: Intent?,
        getAutoVisionEdgeResult: (response: String) -> Unit
    ) {

        val labeler: FirebaseVisionImageLabeler? = null

        val localModel: AutoMLImageLabelerLocalModel = AutoMLImageLabelerLocalModel.Builder()
            .setAssetFilePath("modelfiles/manifest.json") // or .setAbsoluteFilePath(absolute file path to manifest file)
            .build()
        try {
//            val options = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
//                .setConfidenceThreshold(0.0f)
//                .setLocalModelName("LOCAL_MODEL_NAME")
//                .setRemoteModelName("REMOTE_MODEL_NAME")
//                .build()
//
//            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options)
        } catch (e: FirebaseMLException) {
            e.printStackTrace()
        }

        try {
            val image = FirebaseVisionImage.fromFilePath(context, Objects.requireNonNull(imageData?.data!!))
            labeler?.processImage(image)
                ?.addOnSuccessListener { labels ->
                    // Task completed successfully
// ...
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                    }
                }
                ?.addOnFailureListener {
                    // Task failed with an exception
// ...
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getSuccessText(landmark: MLRemoteLandmark): String {
        val text: String

        if (landmark.landmark.contains("retCode") ||
            landmark.landmark.contains("retMsg") ||
            landmark.landmark.contains(
                "fail"
            )
        ) {
            text = "The landmark was not recognized."
        } else {
            var longitude = 0.0
            var latitude = 0.0
            var possibility = ""
            var landmarkName = ""
            var result = StringBuilder()
            if (landmark.landmark != null) {
                result = StringBuilder("Landmark information\n" + landmark.landmark)
                landmarkName = landmark.landmark
            }
            if (landmark.positionInfos != null) {
                for (coordinate in landmark.positionInfos) { //           setText(Html.fromHtml("<b>" + myText + "</b>");
                    result.append("\nLatitude: ").append(coordinate.lat)
                    result.append("\nLongitude: ").append(coordinate.lng)
                    result.append("\nPossibility: %")
                        .append(DecimalFormat("##.##").format(landmark.possibility * 100.toDouble()))
                    longitude = coordinate.lng
                    latitude = coordinate.lat
                    possibility = DecimalFormat("##.##")
                        .format(landmark.possibility * 100.toDouble())
                }
            }
            text =
                Html.fromHtml("<big><b>Landmark Information</b></big> <br><big><b>$landmarkName</b></big><br><b>Latitude: </b>$latitude<br><b>Longitude: </b>$longitude<br><b>Possibility: </b>%$possibility")
                    .toString()
        }

        return text
    }

    private fun getFailureText() = "Failure"

}