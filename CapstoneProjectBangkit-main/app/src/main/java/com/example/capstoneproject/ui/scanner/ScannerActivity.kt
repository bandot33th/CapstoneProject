package com.example.capstoneproject.ui.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityScannerBinding
import com.example.capstoneproject.ui.ViewModelFactory
import com.example.capstoneproject.ui.main.MainActivity
import com.example.capstoneproject.ui.profile.ProfileActivity
import com.example.capstoneproject.ui.profile.ProfileViewModel
import com.example.capstoneproject.util.getImageUri
import com.example.capstoneproject.util.reduceFileImage
import com.example.capstoneproject.util.uriToFile
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class ScannerActivity : AppCompatActivity() {
    private val CAMERA_PERMISSION_REQUEST_CODE = 101

    private lateinit var binding: ActivityScannerBinding

    private val scannerViewModel by viewModels<ScannerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var username: String? = null

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.scanTrashButton.setOnClickListener{uploadImage()}

        binding.navView.setOnNavigationItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home-> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_scan->{
                    startActivity(Intent(this, ScannerActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    private fun uploadImage(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)


            scannerViewModel.getSession().observe(this){
                username = it
            }
            username?.let { scannerViewModel.predict(multipartBody, it) }
            scannerViewModel.getPredictResultResponse().observe(this){
                showToast(it)
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }
    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                showToast("Permission not granted")
            }
        }
    }
    private fun startCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the CAMERA permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // You have the CAMERA permission, you can open the camera here
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imagePickerView.setImageURI(it)
        }
    }
}