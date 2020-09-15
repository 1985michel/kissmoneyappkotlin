package com.example.kissmoney.contas

import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.mes.Mes

class ContaJoin(conta: Conta, movimentacaoMensal: MovimentacaoMensal, mes: Mes){


    var mesId = 0L
    var mesNome = ""
    var contaId = 0L
    var nomeConta = ""
    var tipoConta = ""
    var movimentacaoId = 0L
    var saldoInicial = 0.0
    var saldoAtualOuFinal = 0.0
    var dataAtualizacao = ""
    var isEncerrada = false

    init {
        mesId = movimentacaoMensal.mesId
        mesNome = mes.nomeMes
        contaId = conta.contaId
        nomeConta = conta.nomeConta
        tipoConta = conta.tipoConta
        isEncerrada = conta.isEncerrada
        movimentacaoId = movimentacaoMensal.movimentacaoMensalId
        saldoInicial = movimentacaoMensal.saldoInicial
        saldoAtualOuFinal = movimentacaoMensal.saldoAtualOuFinal
        dataAtualizacao = movimentacaoMensal.dataAtualizacao
    }

    override fun toString(): String {
        println(">>>>>>>>>>>>>>>>>>>> Conta Join : <<<<<<<<<<<<<<<<<")
        println("ID MES ${mesId}")
        println("NOME MES ${mesNome}")
        println("CONTA ID ${contaId}")
        println("NOME CONTA ${nomeConta}")
        println("TIPO CONTA ${tipoConta}")
        println("IS ENCERRADA ${isEncerrada}")
        println("MOVIMENTACAO ID ${movimentacaoId}")
        println("SALDO INICIAL ${saldoInicial}")
        println("SALDO ATUAL OU FINAL ${saldoAtualOuFinal}")
        println("DATA ATUALIZACAO ${dataAtualizacao}")
        return ""
    }

    fun getConta(): Conta {
        return Conta(this.contaId, this.nomeConta, this.tipoConta, this.isEncerrada)
    }
}