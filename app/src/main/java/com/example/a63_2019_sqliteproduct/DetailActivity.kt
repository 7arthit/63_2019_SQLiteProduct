package com.example.a63_2019_sqliteproduct

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    lateinit var nameDetail: TextView
    lateinit var priceDetail: TextView
    lateinit var photoDetail: ImageView

    private var p_id: String? = null
    private var p_name: String? = null
    private var p_price: String? = null
    private var p_photo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initView()
        initViewModel()
    }

    private fun initView() {
        nameDetail = findViewById(R.id.tvNameDetail)
        priceDetail = findViewById(R.id.tvPriceDetail)
        photoDetail = findViewById(R.id.imgDetailProduct)
    }

    fun initViewModel(){
        this.p_id = intent.getStringExtra("p_id")
        this.p_name = intent.getStringExtra("p_name")
        this.p_price = intent.getStringExtra("p_price")
        this.p_photo = intent.getStringExtra("p_photo")

        nameDetail.setText(this.p_name.toString())
        priceDetail.setText(this.p_price.toString())
        photoDetail.setImageURI(Uri.parse(this.p_photo))
    }
}