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

class UpdateActivity : AppCompatActivity() {
    lateinit var nameUpdate: EditText
    lateinit var priceUpdate: EditText
    lateinit var photoUpdate: ImageView
    lateinit var productUpdate: Button
    lateinit var updatePhoto: Button

    private lateinit var SqliteProduct: SqliteProduct
    private var adapter: ProductAdapter? = null

    private var p_id:String? = null
    private var p_name:String? = null
    private var p_price:String? = null
    private var p_photo:String? = null

    private lateinit var storagePermisssios: Array<String>

    private var imageUri: Uri? = null
    private val pickImage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        initView()
        initViewModel()

        SqliteProduct = SqliteProduct(this)

        storagePermisssios = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (!checkStoragePermission()) {
            requestStoragePermissions()
        }

        productUpdate.setOnClickListener {
            updateProduct()
        }

        updatePhoto.setOnClickListener {
            pickImage()
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
            photoUpdate.setImageURI(imageUri)
        }
    }

    private fun updateProduct(){
        val name = nameUpdate.text.toString()
        val price = priceUpdate.text.toString()

        if(name == this.p_name && price == this.p_price) {
            return
        }

        val pd = ProductModel(p_id= this.p_id!!.toInt(),p_name= name,p_price = price, p_photo = imageUri.toString() )
        println(pd)
        val status = SqliteProduct.UpdateProduct(pd)

        if(status>-1) {
            Toast.makeText(this,"Update", Toast.LENGTH_LONG).show()
            getProduct()
            startActivity(Intent(this@UpdateActivity,MainActivity::class.java))
        } else {
            Toast.makeText(this,"no Update", Toast.LENGTH_LONG).show()
        }
    }

    private fun getProduct() {
        val lispd = SqliteProduct.GetAllProduct()
        adapter?.addItem(lispd)
    }

    private fun initView() {
        nameUpdate=findViewById(R.id.ptNameUpdate)
        priceUpdate=findViewById(R.id.ptPriceUpdate)
        productUpdate=findViewById(R.id.btUpdateProduct)
        updatePhoto=findViewById(R.id.btUpdatePhoto)
        photoUpdate=findViewById(R.id.imgUpdateProduct)
    }

    fun initViewModel(){
        this.p_id = intent.getStringExtra("p_id")
        this.p_name = intent.getStringExtra("p_name")
        this.p_price = intent.getStringExtra("p_price")
        this.p_photo = intent.getStringExtra("p_photo")

        nameUpdate.setText(this.p_name.toString())
        priceUpdate.setText(this.p_price.toString())
        photoUpdate.setImageURI(Uri.parse(this.p_photo))
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