package com.example.kissmoney.contas

enum class TiposDeConta (val tipo: String){

    //https://medium.com/@thasaquino/kotlin-enum-e-when-5af250f8538e

    CARTEIRA("Conta Corrente / Carteira"),
    DIVIDAS("Cartão de Crédito / Divida"),
    INVESTIMENTO ("Investimento / Poupança")

}