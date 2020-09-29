package com.example.kissmoney.ganhos.mensal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.ganhos.Ganho
import com.example.kissmoney.mes.Mes

@Entity(tableName = "ganho_mensal_table", foreignKeys = arrayOf(
    ForeignKey(entity = Mes::class,
        parentColumns = arrayOf("mesId"),
        childColumns = arrayOf("mesId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(entity = Ganho::class,
        parentColumns = arrayOf("ganhoId"),
        childColumns = arrayOf("ganhoId"),
        onDelete = ForeignKey.CASCADE
    )
))
data class GanhoMensal (

    @PrimaryKey(autoGenerate = true)
    var ganhoMensalId: Long = 0L,

    @ColumnInfo(name = "mesId")
    var mesId: Long,

    @ColumnInfo(name = "ganhoId")
    var ganhoId: Long,

    @ColumnInfo(name = "valor")
    val valor: Double = 0.00,

    @ColumnInfo(name = "data_recebimento")
    var dataRecebimento: String,

    @ColumnInfo(name = "is_recebido")
    var isRecebido: Boolean = false,

    @ColumnInfo(name = "observacao")
    val observacao: String


)