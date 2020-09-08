package com.example.kissmoney.compromissos

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CompromissoDao {

    @Query("SELECT * FROM compromisso_table ORDER BY nome_compromisso DESC")
    fun getAll(): LiveData<List<Compromisso>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(compromisso: Compromisso) : Long

    @Update
    fun update(compromisso: Compromisso)

    @Delete
    fun delete(compromisso: Compromisso)

}