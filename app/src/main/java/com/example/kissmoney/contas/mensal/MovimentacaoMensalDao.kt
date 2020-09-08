package com.example.kissmoney.contas.mensal

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovimentacaoMensalDao {

    @Query("SELECT * FROM movimentacao_mensal_table ORDER BY contaId DESC")
    fun getAll(): LiveData<List<MovimentacaoMensal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movimentacaoMensal: MovimentacaoMensal): Long

    @Update
    fun update(movimentacaoMensal: MovimentacaoMensal)

    @Delete
    fun delete(movimentacaoMensal: MovimentacaoMensal)

    @Query("DELETE FROM movimentacao_mensal_table WHERE contaId = :idConta")
    fun deleteAllMovimentacoesDaConta(idConta: Long)
}