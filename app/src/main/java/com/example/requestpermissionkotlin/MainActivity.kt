package com.example.requestpermissionkotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.requestpermissionkotlin.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        // If Permission Is Granted
        if(it){
            // Do Whatever Required the Permission
            binding.tvMainActivityText.text = "Permission Granted"

        // If Permission Not Granted
        }else{

            // If User Did Not Select "Never Again" When Permission was denied
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                // Show Dialog To Show Importance of Permission
                MaterialAlertDialogBuilder(this)
                    .setTitle("Permission Required")
                    .setMessage("This Permission Is Required For Proper Working Of The App")
                    .setNegativeButton("No"){ d , _ ->
                        d.dismiss()
                    }.setPositiveButton("Ask Permission Again"){ d, _ ->
                        requestStoragePermission()
                        d.dismiss()
                    }

            // In-case User Selected Not To Ask Permission Again
            }else{
                // Show Snackbar to Take To App Settings
                Snackbar.make(binding.root, "Permission Required For App Functionality", Snackbar.LENGTH_LONG)
                    .setAction("Settings"){
                        val intent = Intent()
                        intent.data = Uri.fromParts("package", packageName, null)
                        intent.action = Uri.decode(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                        startActivity(intent)
                    }.show()
            }

        }
    }

    // Function To Request Permission
    private fun requestStoragePermission(){
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMainActivityText.text = "Permission Not Granted"

        binding.btnMainActivityRequest.setOnClickListener {
            // If Permission Is Already Granted
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // Do Whatever Required the Permission
                binding.tvMainActivityText.text = "Permission Granted"

            // Permission Is Not Granted Already
            }else{
                // Ask Permission
                requestStoragePermission()
            }
        }
    }
}