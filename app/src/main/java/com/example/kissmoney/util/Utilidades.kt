package com.example.kissmoney.util


import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


fun formataParaBr(valor: BigDecimal): String {
    //val moeda = DecimalFormat.getCurrencyInstance(Locale("pt", "BR"))
    //return moeda.format(valor).replace(moeda.currency.currencyCode, moeda.currency.currencyCode + " ")
    val brazilianFormat = DecimalFormat
        .getCurrencyInstance(Locale("pt", "br"))
    if (valor == BigDecimal.valueOf(0.0)) return "R$ 0,00"
    return brazilianFormat.format(valor)
}

fun formataComNCasasDecimais(valor: Double, casas: Int): Double {
    return valor.toBigDecimal().setScale(casas, RoundingMode.DOWN).toDouble()
}

fun limpaFormatacaoDeMoeda(valor: String): String {

    var isNegative = false

    val valorSemPontos = valor.replace(".", "")
    val valorSemLetras = valorSemPontos.replace("R", "")
    val valorSemCifrao = valorSemLetras.replace("$","")
    var valorSemEspacos = valorSemCifrao.replace(" ", "")
    var valorEmDecimal = valorSemEspacos.replace(",", ".")
    if (valorEmDecimal.contains('-')) {
        valorEmDecimal = valorEmDecimal.substring(2)
        isNegative = true
    } else {
        valorEmDecimal = valorEmDecimal.trim()
    }

    println("NA FORMATACAO >>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<< [${valorEmDecimal}]")

    if (isNegative) return "-$valorEmDecimal"
    else return "$valorEmDecimal"
}

//fun limpaFormatacaoDeMoedaRetornaDouble(valor: String): Double {
//    val valorSemPontos = valor.replace(".", "")
//    val valorSemLetras = valorSemPontos.replace("R", "")
//    val valorSemEspacos = valorSemLetras.replace(" ", "")
//    val valorEmDecimal = valorSemEspacos.replace(",", ".")
//    return valorEmDecimal.toDouble()
//}
