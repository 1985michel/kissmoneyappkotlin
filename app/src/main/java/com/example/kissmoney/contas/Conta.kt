package com.example.kissmoney.contas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.kissmoney.mes.Mes


@Entity(tableName = "conta_table")

data class Conta (

    @PrimaryKey(autoGenerate = true)
    var contaId: Long = 0L,

    @ColumnInfo(name = "nome_conta")
    var nomeConta: String,

    @ColumnInfo(name = "tipo_conta")
    var tipoConta: String,

    @ColumnInfo(name = "is_encerrada")
    var isEncerrada: Boolean = false

)