package com.example.a63_2019_sqliteproduct

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var list_pd:ArrayList<ProductModel> = ArrayList()

    private var onClickDetailItem:((ProductModel)->Unit)? = null

    private var onClickDeleteItem:((ProductModel)->Unit)? = null

    private var onClickUpdateItem:((ProductModel)->Unit)? = null

    fun addItem(items:ArrayList<ProductModel>){
        this.list_pd = items
        notifyDataSetChanged()
    }

    fun setOnClickDetailItem(callback:(ProductModel)->Unit){
        this.onClickDetailItem = callback
    }

    fun setOnClickDeleteItem(callback:(ProductModel)->Unit){
        this.onClickDeleteItem = callback
    }

    fun setOnClickUpdateItem(callback:(ProductModel)->Unit){
        this.onClickUpdateItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items,parent,false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val pd = list_pd[position]
        holder.bindView(pd)

        holder.itemView.setOnClickListener{onClickDetailItem?.invoke(pd)}
        holder.btUpdate.setOnClickListener{onClickUpdateItem?.invoke(pd)}
        holder.btDelete.setOnClickListener{onClickDeleteItem?.invoke(pd)}
    }

    override fun getItemCount(): Int {
        return list_pd.size
    }

    class ProductViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var p_id = view.findViewById<TextView>(R.id.tvId)
        private var p_name = view.findViewById<TextView>(R.id.tvName)
        private var p_price = view.findViewById<TextView>(R.id.tvPrice)
        private var p_photo = view.findViewById<ImageView>(R.id.imageView)

        var btUpdate = view.findViewById<Button>(R.id.btUpdate)
        var btDelete = view.findViewById<Button>(R.id.btDelete)

        fun bindView(pd:ProductModel){
            p_id.text = pd.p_id.toString()
            p_name.text = pd.p_name
            p_price.text = pd.p_price
            p_photo.setImageURI(Uri.parse(pd.p_photo))
        }
    }
}