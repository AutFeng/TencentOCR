package com.project.demo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.demo.R

class ResultActivity : AppCompatActivity() {

    private lateinit var resultContainer: LinearLayout
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
        displayResult()
        setupListeners()
    }

    private fun initViews() {
        resultContainer = findViewById(R.id.resultContainer)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun displayResult() {
        @Suppress("UNCHECKED_CAST")
        val result = intent.getSerializableExtra("result") as? HashMap<String, String>

        if (result == null || result.isEmpty()) {
            addResultItem("错误", "未获取到识别结果")
            return
        }

        if (result.containsKey("error")) {
            addResultItem("错误", result["error"] ?: "未知错误")
            return
        }

        // 显示所有识别结果
        result.forEach { (key, value) ->
            addResultItem(key, value)
        }
    }

    private fun addResultItem(label: String, value: String) {
        // 创建标签
        val labelView = TextView(this).apply {
            text = "$label："
            textSize = 14f
            setTextColor(getColor(com.google.android.material.R.color.material_on_surface_emphasis_medium))
            setPadding(0, 12, 0, 4)
        }

        // 创建值
        val valueView = TextView(this).apply {
            text = value
            textSize = 16f
            setTextColor(getColor(com.google.android.material.R.color.material_on_surface_emphasis_high_type))
            setPadding(0, 0, 0, 16)
        }

        resultContainer.addView(labelView)
        resultContainer.addView(valueView)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }
}
