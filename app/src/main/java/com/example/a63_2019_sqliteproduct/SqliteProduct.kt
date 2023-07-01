package com.example.a63_2019_sqliteproduct

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteProduct(context: Context):
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION)
{
    companion object{
        private const val DATABASE_NAME = "productlist.db"
        private const val DATABASE_VERSION = 1
        private const val TBL_PRODUCTLIST = "tbl_productlist"
        private const val P_ID = "p_id"
        private const val P_NAME = "p_name"
        private const val P_PRICE = "p_price"
        private const val P_PHOTO = "p_photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblProduct=("CREATE TABLE " + TBL_PRODUCTLIST +"("
                + P_ID +" INTEGER PRIMARY KEY,"
                + P_NAME +" TEXT,"
                + P_PRICE +" TEXT,"
                + P_PHOTO +" TEXT NULL"+")")
        db?.execSQL(createTblProduct)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_PRODUCTLIST")
        onCreate(db)
    }

    fun InsertProduct(pd:ProductModel):Long {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(P_ID,pd.p_id)
        contentValue.put(P_NAME,pd.p_name)
        contentValue.put(P_PRICE,pd.p_price)
        contentValue.put(P_PHOTO,pd.p_photo)

        val succes = db.insert(TBL_PRODUCTLIST,null,contentValue)
        db.close()
        return succes
    }

    fun GetAllProduct():ArrayList<ProductModel> {
        val list_pd: ArrayList<ProductModel> = ArrayList()

        val selectQuery = "SELECT * FROM $TBL_PRODUCTLIST"

        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        } catch (e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var p_id:Int
        var p_name:String
        var p_price:String
        var p_photo:String?

        if(cursor.moveToFirst()) {
            do {
                p_id = cursor.getInt(cursor.getColumnIndexOrThrow("p_id"))
                p_name = cursor.getString(cursor.getColumnIndexOrThrow("p_name"))
                p_price = cursor.getString(cursor.getColumnIndexOrThrow("p_price"))
                p_photo = cursor.getString(cursor.getColumnIndexOrThrow("p_photo"))
                val pd = ProductModel(p_id = p_id, p_name = p_name, p_price = p_price, p_photo= p_photo)
                list_pd.add(pd)
            } while(cursor.moveToNext())
        }
        return list_pd
    }

    fun UpdateProduct(pd:ProductModel):Int {
        val db = this.writableDatabase
        val contentValue= ContentValues()

        contentValue.put(P_ID,pd.p_id)
        contentValue.put(P_NAME,pd.p_name)
        contentValue.put(P_PRICE,pd.p_price)
        contentValue.put(P_PHOTO,pd.p_photo)

        val succes = db.update(TBL_PRODUCTLIST,contentValue,"p_id="+pd.p_id,null)
        db.close()
        return succes
    }

    fun DeleteProduct(id:Int):Int {
        val db = this.writableDatabase

        var success = db.delete(TBL_PRODUCTLIST,"p_id=$id",null)
        db.close()
        return success
    }

    fun SerachProduct(txt:String):ArrayList<ProductModel> {
        val list_pd: ArrayList<ProductModel> = ArrayList()

        val selectQuery = "SELECT * FROM $TBL_PRODUCTLIST WHERE $P_NAME LIKE '%$txt%'"

        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,null)
        } catch (e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var p_id:Int
        var p_name:String
        var p_price:String
        var p_photo:String

        if(cursor.moveToFirst()) {
            do {
                p_id = cursor.getInt(cursor.getColumnIndexOrThrow("p_id"))
                p_name = cursor.getString(cursor.getColumnIndexOrThrow("p_name"))
                p_price = cursor.getString(cursor.getColumnIndexOrThrow("p_price"))
                p_photo = cursor.getString(cursor.getColumnIndexOrThrow("p_photo"))
                val pd = ProductModel(p_id = p_id, p_name = p_name, p_price = p_price, p_photo= p_photo)
                list_pd.add(pd)
            } while(cursor.moveToNext())
        }
        return list_pd
    }
}