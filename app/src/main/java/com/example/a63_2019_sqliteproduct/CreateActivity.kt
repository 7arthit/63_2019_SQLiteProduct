package com.example.a63_2019_sqliteproduct

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.system.exitProcess

class CreateActivity : AppCompatActivity() {
    lateinit var nameCreate: EditText
    lateinit var priceCreate: EditText
    lateinit var productCreate: Button
    lateinit var photoCreate: ImageView
    lateinit var createPhoto: Button

    private lateinit var SqliteProduct: SqliteProduct
    private lateinit var storagePermisssios: Array<String>

    private var imageUri: Uri? = null
    private val pickImage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initView()

        SqliteProduct = SqliteProduct(this)

        storagePermisssios = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (!checkStoragePermission()) {
            requestStoragePermissions()
        }

        createPhoto.setOnClickListener {
            pickImage()
        }

        productCreate.setOnClickListener {
            addProduct()
        }
    }

    private fun pickImage() {
        if (!checkStoragePermission()) {
            requestStoragePermissions()
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            photoCreate.setImageURI(imageUri)
        }
    }

    private fun addProduct(){
        val p_name = nameCreate.text.toString()
        val p_price = priceCreate.text.toString()

        if(p_name.isEmpty()||p_price.isEmpty()) {
            Toast.makeText(this,"Input!!!!", Toast.LENGTH_LONG).show()
        } else {
            val std = ProductModel(p_name = p_name, p_price = p_price, p_photo = imageUri.toString())
            val status = SqliteProduct.InsertProduct(std)

            if(status>-1) {
                Toast.makeText(this,"Add", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@CreateActivity,MainActivity::class.java))
            } else {
                Toast.makeText(this,"no Save", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initView() {
        nameCreate = findViewById(R.id.ptNameCreate)
        priceCreate = findViewById(R.id.ptPriceCreate)
        productCreate = findViewById(R.id.btCreateProduct)
        createPhoto = findViewById(R.id.btCreatePhoto)
        photoCreate = findViewById(R.id.imgCreateProduct)
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermisssios, 101)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {

                    } else {
                        exitProcess(0)
                    }
                }
            }
        }
    }
}