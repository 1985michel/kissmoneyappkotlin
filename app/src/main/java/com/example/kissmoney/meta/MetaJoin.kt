package com.example.kissmoney.meta

class MetaJoin (meta: Meta, valotAtual: Double, valorAtualPercentual: Int, variavel: String) {

    var meta: Meta
    var valorAtual = 0.0
    var valorAtualPercentual = 0
    var variavel = ""

    init {
        this.meta = meta
        this.valorAtualPercentual = valorAtualPercentual
        this.valorAtual = valorAtual
        this.variavel = variavel
    }
}