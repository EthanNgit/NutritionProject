package com.example.nutritionproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SearchScanActivity : AppCompatActivity(), View.OnClickListener {

    private val cameraPermission = Manifest.permission.CAMERA;

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private lateinit var backBtn : ImageView
    private lateinit var previewView : PreviewView
    private lateinit var infoCard : RelativeLayout
    private lateinit var infoSearchBtn : CardView
    private lateinit var infoAddBtn : CardView
    private lateinit var infoBackBtn : ImageView
    private lateinit var upcText: TextView

    private var latestUpcId : String = ""

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_scan)

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        CustomUIMethods.hideKeyboard(this);

        backBtn = findViewById(R.id.backButton)
        previewView = findViewById(R.id.previewView)
        infoCard = findViewById(R.id.barcodeFoundLayout)
        infoSearchBtn = findViewById(R.id.searchBtn)
        infoAddBtn = findViewById(R.id.addBtn)
        infoBackBtn = findViewById(R.id.backInfoButton)
        upcText = findViewById(R.id.upcidTextLabel)

        infoBackBtn.setOnClickListener(this)
        infoSearchBtn.setOnClickListener(this)
        infoAddBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)

        cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
            processCameraProvider = cameraProviderFuture.get()
            bindCameraPreview()
            bindInputAnalyzer()
            }, ContextCompat.getMainExecutor(this)
        )

        requestCameraAndStartScanner()
    }

    override fun onClick(v: View?) {
        val id = v?.id

        if (id == backBtn.id) {
            finish()
        }

        if (id == infoBackBtn.id) {
            infoCard.visibility = View.GONE
            //Resume Scanning
        } else if (id == infoSearchBtn.id) {
            //Open search activity and start search
            SearchActivity.setPassedUpcId(latestUpcId);
            finish();

        } else if (id == infoAddBtn.id) {
            //Open Add form

            //Go To add item menu (send with upcid)
            var intent = Intent(this@SearchScanActivity, AddFoodItemActivity::class.java);
            intent.putExtra("upcid", latestUpcId)
            startActivity(intent)
            finish()

        }

    }



    private fun bindCameraPreview() {
        cameraPreview = Preview.Builder()
            .setTargetRotation(previewView.display.rotation)
            .build()
        cameraPreview.setSurfaceProvider(previewView.surfaceProvider)
        processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview)
    }

    private fun bindInputAnalyzer() {
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_UPC_A)
                .build()
        )

        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(previewView.display.rotation)
            .build()

        val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(cameraExecutor) {imageProxy ->
            processImageProxy(barcodeScanner, imageProxy)
        }

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if(barcodes.isNotEmpty()) {
                    onScan?.invoke(barcodes)
                    onScan = null

                    latestUpcId = barcodes[0].rawValue.toString()
                    upcText.text = "Upc Id: " + latestUpcId

                    infoCard.visibility = View.VISIBLE

                    //Pause Scanning
                }
            }.addOnFailureListener{
                it.printStackTrace()
            }.addOnCompleteListener{
                imageProxy.close()
            }

    }

    private fun requestCameraAndStartScanner() {
        if (isPermissionGranted(cameraPermission)) {
            //start scanner
        } else {
            requestCameraPermission();
        }
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                cameraPermissionRequest {
                    openPermissionSetting()
                }
            }
            else -> {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }

    companion object {
        private var onScan: ((barcodes: List<Barcode>) -> Unit)? = null
        fun startScanner(context : Context, onScan: (barcodes: List<Barcode>)-> Unit) {
            this.onScan = onScan
            Intent(context, SearchScanActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }

}