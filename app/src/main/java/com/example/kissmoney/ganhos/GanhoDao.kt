package com.example.kissmoney.ganhos

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GanhoDao {

    @Query("SELECT * FROM ganho_table ORDER BY nome_ganho ASC")
    fun getAll(): LiveData<List<Ganho>>

    @Query("SELECT * FROM ganho_table WHERE ganhoId = :id")
    fun getById(id: Long): Ganho

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ganho: Ganho) : Long

    @Update
    fun update(ganho: Ganho)

    @Delete
    fun delete(ganho: Ganho)

    @Query("DELETE FROM ganho_table")
    fun deleteAll()
}