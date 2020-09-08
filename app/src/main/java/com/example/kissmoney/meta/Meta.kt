package com.example.kissmoney.meta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meta_table")
data class Meta (

    @PrimaryKey(autoGenerate = true)
    var metaId: Long = 0L,

    @ColumnInfo(name = "nome_meta")
    var nomeMeta: String,

    @ColumnInfo(name = "valor")
    var valor: Double = 0.00,

    @ColumnInfo(name = "is_alcancada")
    var isAlcancada: Boolean = false,

    @ColumnInfo(name = "data_limite")
    var dataLimite: String, //para gasto e investimento, esta data pode ser setada automaticamente no último dia do mês

    @ColumnInfo(name = "observacao")
    var observacao: String

)