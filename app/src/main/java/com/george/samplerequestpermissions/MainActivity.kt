package com.george.samplerequestpermissions

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.george.samplerequestpermissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding

    val FINE_LOCATION_RQ = 101
    val CAMERA_RQ = 102


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnRequestLocation.setOnClickListener {
            checkForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", FINE_LOCATION_RQ)
        }

        mBinding.btnRequestCamera.setOnClickListener {
            checkForPermissions(android.Manifest.permission.CAMERA,"camera", CAMERA_RQ)
        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> {
                    showDialog(permission, name, requestCode)
                }
                else -> {
                    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                }
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val build = AlertDialog.Builder(this)

        build.apply {
            setMessage("Permission to access your $name is requested to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission),requestCode)
            }
        }
        val dialog = build.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext,"$name permission refused!!", Toast.LENGTH_SHORT)
            } else {
                Toast.makeText(applicationContext,"$name permission granted!!", Toast.LENGTH_SHORT)
            }
        }

        when (requestCode) {
            FINE_LOCATION_RQ -> innerCheck("location")
            CAMERA_RQ -> innerCheck("camera")
        }
    }
}