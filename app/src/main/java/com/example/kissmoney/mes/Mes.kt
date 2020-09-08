package com.example.kissmoney.mes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "mes_table")
data class Mes(

    @PrimaryKey(autoGenerate = true)
    var mesId: Long = 0L,

    @ColumnInfo (name = "nome_mes")
    var nomeMes : String
)