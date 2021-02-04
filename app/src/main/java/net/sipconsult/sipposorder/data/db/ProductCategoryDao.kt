package net.sipconsult.sipposorder.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.sipconsult.sipposorder.data.models.ProductCategoryItem

@Dao
interface ProductCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(productCategories: List<ProductCategoryItem>)

    @Query("SELECT * FROM product_categories WHERE id = :productCategoryId")
    fun getProductCategory(productCategoryId: Int): LiveData<ProductCategoryItem>

    @get:Query("SELECT * FROM product_categories")
    val getProductCategories: LiveData<List<ProductCategoryItem>>

    @Query("DELETE FROM product_categories")
    fun deleteAll()
}