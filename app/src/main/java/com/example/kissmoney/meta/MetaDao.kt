package com.example.kissmoney.meta

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MetaDao {

    @Query("Select * FROM meta_table ORDER BY nome_meta ASC")
    fun getAll(): LiveData<List<Meta>>

    @Query("Select * FROM meta_table WHERE metaId = :id")
    fun getById(id: Long): Meta

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meta: Meta) : Long

    @Update
    fun update(meta: Meta)

    @Delete
    fun delete(meta: Meta)

    @Query("DELETE FROM meta_table")
    fun deleteAll()
}