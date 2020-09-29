package com.example.kissmoney.compromissos

import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.compromissos.mensal.CompromissoMensalViewModel
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.recebeDataRetornaMes

object CompromissoManager {

    fun criaCompromissoComCompromissoMensal(
        compromisso: Compromisso, compromissoMensal: CompromissoMensal,
        mesViewModel: MesViewModel, compromissoViewModel: CompromissoViewModel,
        compromissoMensalViewModel: CompromissoMensalViewModel, callback: () -> Unit
    ) {
        setaMes(compromissoMensal, mesViewModel) {
            criaCompromisso(compromissoMensal, compromisso, compromissoViewModel) {
                compromissoMensalViewModel.insertMonitorado(compromissoMensal) {
                    callback()
                }
            }
        }
    }

    fun criaCompromissoMensal(
        compromissoMensal: CompromissoMensal,
        mesViewModel: MesViewModel,
        compromissoMensalViewModel: CompromissoMensalViewModel, callback: () -> Unit
    ) {
        setaMes(compromissoMensal, mesViewModel) {
                compromissoMensalViewModel.insertMonitorado(compromissoMensal) {
                    callback()
                }
        }
    }

    fun updateCompromissoComMovimentacao(compromisso: Compromisso, compromissoMensal: CompromissoMensal,
                                         mesViewModel: MesViewModel, compromissoViewModel: CompromissoViewModel,
                                         compromissoMensalViewModel: CompromissoMensalViewModel, callback: () -> Unit) {
        setaMes(compromissoMensal, mesViewModel) {
            compromissoViewModel.update(compromisso)
            compromissoMensalViewModel.update(compromissoMensal)
            callback()
        }
    }

    private fun criaCompromisso(
        compromissoMensal: CompromissoMensal,
        compromisso: Compromisso,
        compromissoViewModel: CompromissoViewModel,
        callback: () -> Unit
    ) {
        compromissoViewModel.insertMonitorado(compromisso) {
            compromissoMensal.compromissoId = compromisso.compromissoId
            callback()
        }

    }

    private fun setaMes(
        compromissoMensal: CompromissoMensal,
        mesViewModel: MesViewModel,
        callback: () -> Unit
    ) {
        var mes = Mes(0L, recebeDataRetornaMes(compromissoMensal.dataVencimento))
        mesViewModel.getByName(mes) {
            compromissoMensal.mesId = mes.mesId
            callback()
        }
    }


}