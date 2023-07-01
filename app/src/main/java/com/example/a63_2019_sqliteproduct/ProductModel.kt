package com.example.a63_2019_sqliteproduct

import java.util.*

class ProductModel(var p_id: Int = getAutoId(), var p_name: String = "", var p_price: String , var p_photo: String?)
{
    companion object
    {
        fun getAutoId():Int
        {
            var random = Random()
            return random.nextInt(100)
        }
    }
}