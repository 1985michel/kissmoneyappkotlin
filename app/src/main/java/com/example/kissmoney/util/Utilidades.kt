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

    if (valor.isInfinite()) return 0.0
    //return valor.toBigDecimal().setScale(casas, RoundingMode.DOWN).toDouble()

    var resultado = valor.toBigDecimal().setScale(casas, RoundingMode.DOWN).toDouble()

    //o resultaod deve ser sempre positivo. O tratamento de sinal é feito na view que vai determinar o feedback
    if (resultado < 0.0) resultado = resultado *-1

    return resultado
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
