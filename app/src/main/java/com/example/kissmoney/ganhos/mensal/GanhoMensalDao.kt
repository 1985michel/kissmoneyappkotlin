package com.example.kissmoney.ganhos.mensal

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GanhoMensalDao {

    @Query("SELECT * FROM ganho_mensal_table ORDER BY ganhoId DESC")
    fun getAll(): LiveData<List<GanhoMensal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ganhoMensal: GanhoMensal): Long

    @Update
    fun update(ganhoMensal: GanhoMensal)

    @Delete
    fun delete(ganhoMensal: GanhoMensal)

    @Query("DELETE FROM ganho_mensal_table WHERE ganhoId = :idGanho")
    fun deleteAllGanhosMensaisDoGanho(idGanho: Long)

    @Query("UPDATE ganho_mensal_table SET is_recebido =:cheked WHERE ganhoMensalId  =:id ")
    fun setIsRecebido(id: Long, cheked: Boolean)
}