package com.example.kissmoney.compromissos.mensal

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CompromissoMensalDao {

    @Query("SELECT * FROM compromisso_mensal_table ORDER BY compromissoId DESC")
    fun getAll(): LiveData<List<CompromissoMensal>>

//    @Query("SELECT * FROM compromisso_mensal_table ORDER BY compromissoId DESC")
//    fun getCopromissosMensaisRecorrentesDoMes(mesId : Long): LiveData<List<CompromissoMensal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (compromissoMensal: CompromissoMensal): Long

    @Update
    fun update(compromissoMensal: CompromissoMensal)

    @Delete
    fun delete(compromissoMensal: CompromissoMensal)

    @Query("DELETE FROM compromisso_mensal_table WHERE compromissoId = :idCompromisso")
    fun deleteAllCompromissosMensaisDoCompromisso(idCompromisso: Long)

    @Query("UPDATE compromisso_mensal_table SET is_pago =:cheked WHERE compromissoMensalId  =:id ")
    fun setIsPago(id: Long, cheked: Boolean)
}