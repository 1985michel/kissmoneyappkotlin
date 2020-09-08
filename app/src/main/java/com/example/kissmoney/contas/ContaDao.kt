package com.example.kissmoney.contas

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContaDao {

    @Query("Select * FROM conta_table ORDER BY nome_conta ASC")
    fun getAll(): LiveData<List<Conta>>

    @Query("Select * FROM conta_table WHERE contaId = :id")
    fun getById(id: Long): Conta

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(conta: Conta) : Long

    @Update
    fun update(conta: Conta)

    @Delete
    fun delete(conta: Conta)

    @Query("DELETE FROM conta_table")
    fun deleteAll()
}