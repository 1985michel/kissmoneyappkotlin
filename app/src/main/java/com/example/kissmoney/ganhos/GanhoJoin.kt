package com.example.kissmoney.ganhos


import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.mes.Mes


class GanhoJoin (ganho: Ganho, ganhoMensal: GanhoMensal, mes: Mes) {

    var ganhoId = 0L
    var nomeGanho = ""
    var isRendaPassiva = false
    var isGanhoRegular = false
    var ganhoMensalId = 0L
    var mesId: Long = 0L
    var mesNome = ""
    var valor: Double = 0.00
    var dataRecebimento: String = ""
    var isRecebido: Boolean = false
    var observacao: String = ""

    init {
        ganhoId = ganho.ganhoId
        nomeGanho = ganho.nomeGanho
        isRendaPassiva = ganho.isRendaPassiva
        isGanhoRegular = ganho.isRecorrente
        ganhoMensalId = ganhoMensal.ganhoMensalId
        mesId = ganhoMensal.mesId
        mesNome = mes.nomeMes
        valor = ganhoMensal.valor
        dataRecebimento = ganhoMensal.dataRecebimento
        isRecebido = ganhoMensal.isRecebido
        observacao = ganhoMensal.observacao
    }

    override fun toString(): String {
        println(">>>>>>>>>>>>>>>>>>>> Ganho Join : <<<<<<<<<<<<<<<<<")
        println("GANHO ID: $ganhoId")
        println("Nome Ganho: $nomeGanho")
        println("Is Renda Passiva: $isRendaPassiva")
        println("Is Encerrada: $isGanhoRegular")
        println("Ganho Mensal ID: $ganhoMensalId")
        println("MES ID $mesId")
        println("MES NOME $mesNome")
        println("VALOR $valor")
        println("DATA RECEBIMENTO $dataRecebimento")
        println("IS RECEBIDO $isRecebido")
        println("OBSERVAÇÂO: $observacao")
        return ""
    }

    fun getGanho(): Ganho {
        return Ganho(this.ganhoId, this.nomeGanho, this.isRendaPassiva, this.isGanhoRegular)
    }

    fun getGanhoMensal(): GanhoMensal {
        return GanhoMensal(this.ganhoMensalId, this.mesId, this.ganhoId, this.valor, this.dataRecebimento, this.isRecebido, this.observacao)
    }
}