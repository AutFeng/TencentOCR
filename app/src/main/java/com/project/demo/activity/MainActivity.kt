package com.project.demo.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.project.demo.R
import com.project.demo.utils.ImageUtil
import com.project.demo.utils.TencentOCRService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnRecognize: Button
    private lateinit var radioFront: RadioButton
    private lateinit var progressBar: ProgressBar

    private var selectedImageUri: Uri? = null

    // 图片选择器
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageView.setImageURI(it)
            btnRecognize.isEnabled = true
        }
    }

    // 权限请求
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openImagePicker()
        } else {
            Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnRecognize = findViewById(R.id.btnRecognize)
        radioFront = findViewById(R.id.radioFront)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        btnSelectImage.setOnClickListener {
            checkPermissionAndPickImage()
        }

        btnRecognize.setOnClickListener {
            recognizeIDCard()
        }
    }

    private fun checkPermissionAndPickImage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13及以上使用新的权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            // Android 13以下使用旧的权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    private fun recognizeIDCard() {
        val uri = selectedImageUri ?: return

        progressBar.isVisible = true
        btnRecognize.isEnabled = false

        lifecycleScope.launch {
            try {
                // 将图片转换为Base64
                val imageBase64 = ImageUtil.uriToBase64(this@MainActivity, uri)

                // 确定正反面
                val cardSide = if (radioFront.isChecked) "FRONT" else "BACK"

                // 调用OCR识别
                val response = TencentOCRService.recognizeIDCard(imageBase64, cardSide)

                // 解析结果
                val result = TencentOCRService.parseIDCardResult(response)

                // 跳转到结果页面
                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("result", HashMap(result))
                startActivity(intent)

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "识别失败: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.isVisible = false
                btnRecognize.isEnabled = true
            }
        }
    }
}
