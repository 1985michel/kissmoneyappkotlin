package com.example.kissmoney.util

import com.example.kissmoney.mes.NomesMeses
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

fun verificaStatusVencimento(data: String) : Int {

    //retorna -1 para vencido
    //retorna 0 para vencendo
    //retorna 1 para futuro
    val cal = Calendar.getInstance()
    val year = cal[Calendar.YEAR]
    var month = cal[Calendar.MONTH]
    month += 1
    val day = cal[Calendar.DAY_OF_MONTH]
    var mes = if (month < 10) "0" + month.toString() else month
    var dia = if (day < 10) "0" + day.toString() else day

    var diaVencimento = data.substring(0,2)
    var mesVencimento = data.substring(3,5)
    var anoVencimento = data.substring(6)


    var vencimento = "$anoVencimento$mesVencimento$diaVencimento"
    var hoje = "$year$mes$dia"

    return vencimento.compareTo(hoje)

}

fun recebeDataRetornaMes(data: String): String {

    val mes = data.substring(3, 5)
    val ano = data.substring(6)

    return "$mes/$ano"
}

fun recebeNomeMesRetornaNomeMesAnterior(nomeMes: String): String {
    var mes = nomeMes.substring(0,2)
    var mesInt = mes.toInt()
    var mesAnteriorInt = mesInt-1
    var mesAnterior = if (mesAnteriorInt < 10) "0" + mesAnteriorInt.toString() else mesAnteriorInt.toString()

    var anoInt = nomeMes.substring(3).toInt()
    if (mesInt == 1) {
        anoInt = anoInt - 1
        mesAnterior = "12"
    }
    return mesAnterior + "/" + anoInt.toString()
}

fun recebeNomeMesRetornaNomeMesPosterior(nomeMes: String): String {
    var mes = nomeMes.substring(0,2)
    var mesInt = mes.toInt()
    var mesPosteriorInt = mesInt+1
    var mesPosterior = if (mesPosteriorInt < 10) "0" + mesPosteriorInt.toString() else mesPosteriorInt.toString()

    var anoInt = nomeMes.substring(3).toInt()
    if (mesInt == 12) {
        anoInt = anoInt + 1
        mesPosterior = "01"
    }
    return mesPosterior + "/" + anoInt.toString()
}

fun getNomeMesAtual(): String {
    val cal = Calendar.getInstance()
    val year = cal[Calendar.YEAR]
    var month = cal[Calendar.MONTH]
    month += 1

    var mes = if (month < 10) "0" + month.toString() else month


    return "${mes}/${year}"
}

fun getNomeMesPorExtensoComAno(nomeMes: String): String {

    for (mes in NomesMeses.values()){
        if (nomeMes.substring(0,2).equals(mes.numero)){
            return "${mes.name} / ${nomeMes.substring(3)}"
        }
    }
    return ""
}

fun getNomeMesPorExtenso(nomeMes: String): String {

    for (mes in NomesMeses.values()){
        if (nomeMes.substring(3,5).equals(mes.numero)){
            return "${mes.name}"
        }
    }
    return ""
}

fun getDiaHojeNoMes(): Int {
    val cal = Calendar.getInstance()
    val day = cal[Calendar.DAY_OF_MONTH]
    return day
}