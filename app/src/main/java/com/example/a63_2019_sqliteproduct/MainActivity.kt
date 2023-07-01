package com.example.a63_2019_sqliteproduct

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnSearch: Button
    private lateinit var ptsProduct: EditText

    private lateinit var recyelerViwe: RecyclerView
    private lateinit var SqliteProduct: SqliteProduct

    private var adapter: ProductAdapter? = null
    private var pd:ProductModel? = null

    private lateinit var storagePermisssios: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViwe()
        initRecycleViwe()

        SqliteProduct = SqliteProduct(this)

        storagePermisssios = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (!checkStoragePermission()) {
            requestStoragePermissions()
        }

        btnSearch.setOnClickListener {
            searchProduct()
        }

        btnAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity,CreateActivity::class.java))
        }

        btnView.setOnClickListener {
            getProduct()
        }

        adapter?.setOnClickDetailItem {
            pd = it
            val detaildata = Intent(this@MainActivity,DetailActivity::class.java)
            detaildata.putExtra("p_id",it.p_id.toString())
            detaildata.putExtra("p_name",it.p_name)
            detaildata.putExtra("p_price",it.p_price)
            detaildata.putExtra("p_photo",it.p_photo)
            startActivity(detaildata)
        }

        adapter?.setOnClickUpdateItem {
            pd = it
            val updatedata = Intent(this@MainActivity,UpdateActivity::class.java)
            updatedata.putExtra("p_id",it.p_id.toString())
            updatedata.putExtra("p_name",it.p_name)
            updatedata.putExtra("p_price",it.p_price)
            updatedata.putExtra("p_photo",it.p_photo)
            startActivity(updatedata)
        }

        adapter?.setOnClickDeleteItem {
            deleteProduct(it.p_id)
        }
    }

    private fun searchProduct(){
        val textSearch = ptsProduct.text
        if (textSearch.isEmpty()){
            getProduct()
        }
        val lispd = SqliteProduct.SerachProduct(textSearch.toString())
        adapter?.addItem(lispd)
    }

    private fun deleteProduct(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog,_->
            SqliteProduct.DeleteProduct(id)
            getProduct()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog,_->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun getProduct() {
        val lispd = SqliteProduct.GetAllProduct()
        adapter?.addItem(lispd)
    }

    private fun initRecycleViwe() {
        recyelerViwe.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter()
        recyelerViwe.adapter = adapter
    }

    fun initViwe() {
        recyelerViwe = findViewById(R.id.recycleView)
        btnAdd = findViewById(R.id.btCreate)
        btnView = findViewById(R.id.btView)
        btnSearch = findViewById(R.id.btSearch)
        ptsProduct = findViewById(R.id.ptSearchProduct)
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermisssios, 101)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
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