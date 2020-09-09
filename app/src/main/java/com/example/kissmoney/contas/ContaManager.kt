package com.example.kissmoney.contas

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.recebeDataRetornaMes


object ContaManager {

    fun criaContaComMovimentacao(conta: Conta, movimentacaoMensal: MovimentacaoMensal,
    contaViewModel: ContaViewModel, mesViewModel: MesViewModel, movimentacaoMensalViewModel: MovimentacaoMensalViewModel) {

        println("$$$$$$$$$$$$$$$$$")
        println("$$$$$$$$$$$$$$$$$")
        println("$$$$$$$$$$$$$$$$$ em CONTA MANAGER")

        setaMes(movimentacaoMensal, mesViewModel) {
            println("$$$$$$$$$$$$$$$$$ SETAMOS O MES ${movimentacaoMensal.mesId}")
            criaConta(movimentacaoMensal, contaViewModel, conta) {
                println("$$$$$$$$$$$$$$$$$ SETAMOS A CONTA ${movimentacaoMensal.contaId}")
                movimentacaoMensalViewModel.insert(movimentacaoMensal)
            }
        }


    }

    private fun setaMes(movimentacaoMensal: MovimentacaoMensal, mesViewModel: MesViewModel, callback: () -> Unit) {
        var mes = Mes(0L, recebeDataRetornaMes(movimentacaoMensal.dataAtualizacao))
        mesViewModel.getByName(mes){
            movimentacaoMensal.mesId = mes.mesId
            callback()
        }
        //mesViewModel.insert(mes)
        //movimentacaoMensal.mesId = mes.mesId
    }

    private fun criaConta(
        movimentacaoMensal: MovimentacaoMensal,
        contaViewModel: ContaViewModel,
        conta: Conta,
        callback: () -> Unit
    ) {
            contaViewModel.insertMonitorado(conta){
                println(">>>>> LINHA 46 em criar conta no conta manager ID: ${conta.contaId}")
                movimentacaoMensal.contaId = conta.contaId
                println(">>>>> em criar conta no conta manager ID: ${movimentacaoMensal.contaId}")
                callback()
            }

    }
}