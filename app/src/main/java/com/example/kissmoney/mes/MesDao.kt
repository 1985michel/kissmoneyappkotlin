package com.example.kissmoney.mes

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MesDao {

    @Query("SELECT * FROM mes_table ORDER BY nome_mes DESC")
    fun getAll(): LiveData<List<Mes>>

    @Query("SELECT * FROM mes_table WHERE nome_mes LIKE :nome ")
    fun getPorNome(nome: String): Mes

    @Query("SELECT * FROM mes_table WHERE mesId LIKE :id ")
    fun getPorId(id: Long): Mes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mes: Mes): Long

    @Update
    fun update(mes: Mes)

    @Delete
    fun delete(mes: Mes)

}