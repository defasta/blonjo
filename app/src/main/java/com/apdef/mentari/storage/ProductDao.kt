package com.apdef.mentari.storage

import androidx.room.*
import com.apdef.mentari.models.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM product")
    fun getProduct(): List<Product>

    @Query("SELECT * from product WHERE id= :id")
    fun getProductById(id: String?): List<Product>

    @Transaction
    fun insertOrUpdate(product: Product) {
        val itemsFromDB: List<Product> = getProductById(product.id)
        if (itemsFromDB.isEmpty()) {
            insert(product)
        } else {
            update(product)
        }
    }

   @Query("SELECT * FROM product WHERE count > 0")
   fun getCart(): List<Product>

//
//    @Query("SELECT * FROM product WHERE cat == :cat")
//   fun getProductByCat(cat: Int?): List<Sembako>
}