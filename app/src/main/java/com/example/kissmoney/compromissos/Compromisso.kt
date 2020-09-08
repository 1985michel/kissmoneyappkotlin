package com.example.kissmoney.compromissos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kissmoney.mes.Mes


@Entity(tableName = "compromisso_table", foreignKeys = arrayOf(
    ForeignKey(entity = Mes::class,
        parentColumns = arrayOf("mesId"),
        childColumns = arrayOf("mesId"),
        onDelete = ForeignKey.CASCADE
    )
))
data class Compromisso (

    @PrimaryKey(autoGenerate = true)
    var compromissoId: Long = 0L,

    @ColumnInfo(name = "nome_compromisso")
    val nomeCompromisso: String,

    @ColumnInfo(name = "mesId")
    val mesId: Long
)