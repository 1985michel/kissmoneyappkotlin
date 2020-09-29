package com.example.kissmoney.ganhos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ganho_table")
data class Ganho (

    @PrimaryKey(autoGenerate = true)
    var ganhoId: Long = 0L,

    @ColumnInfo(name = "nome_ganho")
    var nomeGanho: String,

    @ColumnInfo(name = "is_renda_passiva")
    var isRendaPassiva: Boolean = false,

    @ColumnInfo(name = "is_recorrente")
    var isRecorrente: Boolean = false
)