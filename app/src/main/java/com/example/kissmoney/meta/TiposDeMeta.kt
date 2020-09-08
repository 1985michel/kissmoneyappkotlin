package com.example.kissmoney.meta

enum class TiposDeMeta (val tipo: String, val descricao: String) {

    GANHO("Ganho","Estabeleça um valor mensal que você quer ganhar."),
    INVESTIMENTO("Investimento","Estabeleça um valor mensal que você quer investir."),
    GASTO("Gasto", "Estabeleça um limite mensal de gastos."),
    RESERVA("Reserva", "Estabeleça um valor que você quer ter guardado / investido até determinada data.")
}