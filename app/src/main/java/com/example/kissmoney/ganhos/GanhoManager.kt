package com.example.kissmoney.ganhos


import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.ganhos.mensal.GanhoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.recebeDataRetornaMes

object GanhoManager {

    fun criaGanhoComGanhoMensal(ganho: Ganho, ganhoMensal: GanhoMensal,
                                ganhoViewModel: GanhoViewModel, ganhoMensalViewModel: GanhoMensalViewModel, mesViewModel: MesViewModel,
                                callback: () -> Unit) {
        setaMes(ganhoMensal, mesViewModel) {
            criaGanho(ganho, ganhoMensal, ganhoViewModel) {
                ganhoMensalViewModel.insertMonitorado(ganhoMensal) {
                    callback()
                }
            }
        }
    }

    fun updateGanhoComGanhoMensal(ganho: Ganho, ganhoMensal: GanhoMensal,
                                ganhoViewModel: GanhoViewModel, ganhoMensalViewModel: GanhoMensalViewModel, mesViewModel: MesViewModel,
                                callback: () -> Unit) {
        setaMes(ganhoMensal, mesViewModel) {
            println(">>>>>>>>>>>>>>> Vamos atualizar o ganho: ${ganho.nomeGanho}")
            ganhoViewModel.update(ganho)
            ganhoMensalViewModel.update(ganhoMensal)
            callback()
        }
    }

    private fun setaMes( ganhoMensal: GanhoMensal, mesViewModel: MesViewModel, callback: () -> Unit) {
        var mes = Mes(0L, recebeDataRetornaMes(ganhoMensal.dataRecebimento))
        mesViewModel.getByName(mes){
            ganhoMensal.mesId = mes.mesId
            callback()
        }
    }

    private fun criaGanho(
        ganho: Ganho,
        ganhoMensal: GanhoMensal,
        ganhoViewModel: GanhoViewModel,
        callback: () -> Unit
    ){
        ganhoViewModel.insertMonitorado(ganho) {
            ganhoMensal.ganhoId = ganho.ganhoId
            callback()
        }
    }
}