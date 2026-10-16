package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityThirdBinding
import java.io.File

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    private var currentPhotoPath: String? = null

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                displayPhoto()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString(getString(R.string.key_photo_path))
            if (currentPhotoPath != null) {
                displayPhoto()
            }
        }

        binding.btnTakePhoto.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (captureIntent.resolveActivity(packageManager) == null) {
            Toast.makeText(this, R.string.no_camera_app, Toast.LENGTH_SHORT).show()
            return
        }
        val photoDir = File(cacheDir, getString(R.string.photo_dir_name))
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
        val photoFile = File(photoDir, getString(R.string.photo_file_name, System.currentTimeMillis()))
        val photoUri: Uri = FileProvider.getUriForFile(
            this,
            getString(R.string.file_provider_authority, packageName),
            photoFile
        )
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        currentPhotoPath = photoFile.absolutePath
        takePhotoLauncher.launch(captureIntent)
    }

    private fun displayPhoto() {
        val path = currentPhotoPath ?: return
        val file = File(path)
        if (!file.exists()) {
            return
        }
        val maxDimension = resources.getInteger(R.integer.max_photo_dimension)
        val bitmap = decodeSampledBitmap(path, maxDimension) ?: return
        binding.ivPhoto.setImageBitmap(rotateBitmapByExif(bitmap, path))
    }

    private fun decodeSampledBitmap(path: String, maxDimension: Int): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        var sampleSize = 1
        while (bounds.outWidth / sampleSize > maxDimension * 2 ||
            bounds.outHeight / sampleSize > maxDimension * 2
        ) {
            sampleSize *= 2
        }
        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return BitmapFactory.decodeFile(path, options)
    }

    private fun rotateBitmapByExif(bitmap: Bitmap, path: String): Bitmap {
        val exif = ExifInterface(path)
        val degrees = when (
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        if (degrees == 0f) {
            return bitmap
        }
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.key_photo_path), currentPhotoPath)
    }
}
