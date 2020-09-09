package com.example.kissmoney.contas.mensal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.mes.Mes

@Entity(tableName = "movimentacao_mensal_table", foreignKeys = arrayOf(
    ForeignKey(entity = Mes::class,
    parentColumns = arrayOf("mesId"),
    childColumns = arrayOf("mesId"),
    onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(entity = Conta::class,
        parentColumns = arrayOf("contaId"),
        childColumns = arrayOf("contaId"),
        onDelete = ForeignKey.CASCADE
    )
))

data class MovimentacaoMensal (
    @PrimaryKey(autoGenerate = true)
    var movimentacaoMensalId: Long = 0L,

    @ColumnInfo(name = "mesId")
    var mesId: Long,

    @ColumnInfo(name = "contaId")
    var contaId: Long,

    @ColumnInfo(name = "valor_inicial")
    val saldoInicial: Double = 0.00,

    @ColumnInfo(name = "valor_atual")
    val saldoAtualOuFinal: Double = 0.00,

    @ColumnInfo(name = "data_atualizacao")
    val dataAtualizacao: String
)