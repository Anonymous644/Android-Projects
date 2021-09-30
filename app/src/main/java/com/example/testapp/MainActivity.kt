package com.example.testapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.testapp.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {

    // View Binding Variable
    private lateinit var binding: ActivityMainBinding

    // Custom ActivityResultContract For UCrop
    private val uCropContract = object: ActivityResultContract<List<Uri>, Uri?>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            // UCrop with Square Aspect Ratio and Max Result Size of 800x800 (Optional)
            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(800, 800)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {

            // If User Didn't Crop the Image i.e Pressed Back
            if (intent == null)
                return null

            return UCrop.getOutput(intent!!)!!
        }
    }

    // Register GetContent() for Result
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->

        // If User Didn't Select Any Image, Return
        if(uri == null)
            return@registerForActivityResult

        val inputUri = uri
        val outputUri = File(filesDir, "croppedImage.jpg").toUri()

        val listUri = listOf<Uri>(inputUri, outputUri)
        cropImage.launch(listUri)
    }

    // Register UCrop for Result
    private val cropImage = registerForActivityResult(uCropContract){ uri ->
        binding.ivMainActivityImage.setImageURI(uri)
    }

    // Main Activity onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainActivitySelectImage.setOnClickListener {
            getContent.launch("image/*")
        }

    }
}