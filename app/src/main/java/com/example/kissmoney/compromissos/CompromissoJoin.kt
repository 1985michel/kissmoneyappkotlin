package com.example.kissmoney.compromissos

import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.mes.Mes

class CompromissoJoin (compromisso: Compromisso, compromissoMensal: CompromissoMensal, mes: Mes){

    var mesId = 0L
    var mesNome = ""
    var compromissoId = 0L
    var nomeCompromisso = ""
    var compromissoMensalId = 0L
    var valor = 0.00
    var dataVencimento = ""
    var isPago = false
    var isRecorrente = false

    init {
        mesId = mes.mesId
        mesNome = mes.nomeMes
        compromissoId = compromisso.compromissoId
        nomeCompromisso = compromisso.nomeCompromisso
        compromissoMensalId = compromissoMensal.compromissoMensalId
        valor = compromissoMensal.valor
        dataVencimento = compromissoMensal.dataVencimento
        isPago = compromissoMensal.isPago
        isRecorrente = compromisso.isRecorrente
    }

    fun getCompromisso(): Compromisso {
        return Compromisso(this.compromissoId, this.nomeCompromisso, this.isRecorrente)
    }

    fun getCompromissoMensal(): CompromissoMensal {
        return CompromissoMensal(this.compromissoMensalId, this.mesId, this.compromissoId, this.valor, this.dataVencimento, this.isPago)
    }

}