package com.example.kissmoney.util

import java.util.*

fun getDataHojeString(): String {
    val cal = Calendar.getInstance()
    val year = cal[Calendar.YEAR]
    var month = cal[Calendar.MONTH]
    month += 1
    val day = cal[Calendar.DAY_OF_MONTH]
    var mes = if (month < 10) "0" + month.toString() else month
    var dia = if (day < 10) "0" + day.toString() else day

    return "${dia}/${mes}/${year}"
}

fun recebeDataRetornaMes(data: String): String {

    val mes = data.substring(3, 5)
    val ano = data.substring(6)

    return "$mes/$ano"
}