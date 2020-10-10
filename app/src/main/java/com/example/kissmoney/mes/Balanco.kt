package com.example.kissmoney.mes

class Balanco (nomeMes : String, valor: Double, variacaoDias: Int) {

    var nomeMes = ""
    var valor = 0.0
    var variacaoDias = 0

    init {
        this.nomeMes = nomeMes
        this.valor = valor
        this.variacaoDias = variacaoDias
    }
}