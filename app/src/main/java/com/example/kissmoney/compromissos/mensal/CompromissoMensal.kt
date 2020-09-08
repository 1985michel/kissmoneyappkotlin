package com.example.kissmoney.compromissos.mensal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kissmoney.compromissos.Compromisso
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.mes.Mes


@Entity(tableName = "compromisso_mensal_table", foreignKeys = arrayOf(
    ForeignKey(entity = Mes::class,
        parentColumns = arrayOf("mesId"),
        childColumns = arrayOf("mesId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(entity = Compromisso::class,
        parentColumns = arrayOf("compromissoId"),
        childColumns = arrayOf("compromissoId"),
        onDelete = ForeignKey.CASCADE
    )
))
data class CompromissoMensal (

    @PrimaryKey(autoGenerate = true)
    var compromissoMensalId: Long = 0L,

    @ColumnInfo(name = "mesId")
    val mesId: Long,

    @ColumnInfo(name = "compromissoId")
    val compromissoId: Long,

    @ColumnInfo(name = "valor")
    val valor: Double = 0.00,

    @ColumnInfo(name = "data_vencimento")
    val dataVencimento: String,

    @ColumnInfo(name = "is_pago")
    var isPago: Boolean = false,

    @ColumnInfo(name = "is_recorrente")
    var isRecorrente: Boolean = false

)