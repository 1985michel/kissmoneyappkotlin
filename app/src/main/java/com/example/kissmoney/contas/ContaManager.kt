package com.example.kissmoney.contas

import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.recebeDataRetornaMes


object ContaManager {

    fun criaContaComMovimentacao(conta: Conta, movimentacaoMensal: MovimentacaoMensal,
    contaViewModel: ContaViewModel, mesViewModel: MesViewModel, movimentacaoMensalViewModel: MovimentacaoMensalViewModel, callback: () -> Unit) {

        setaMes(movimentacaoMensal, mesViewModel) {
            criaConta(movimentacaoMensal, contaViewModel, conta) {
                movimentacaoMensalViewModel.insertMonitorado(movimentacaoMensal){
                    //ContaJoinViewModel.setAllContasJoin(){
                        callback()
                    //}
                }
            }
        }
    }

    private fun setaMes(movimentacaoMensal: MovimentacaoMensal, mesViewModel: MesViewModel, callback: () -> Unit) {
        var mes = Mes(0L, recebeDataRetornaMes(movimentacaoMensal.dataAtualizacao))
        mesViewModel.getByName(mes){
            movimentacaoMensal.mesId = mes.mesId
            callback()
        }
    }

    private fun criaConta(
        movimentacaoMensal: MovimentacaoMensal,
        contaViewModel: ContaViewModel,
        conta: Conta,
        callback: () -> Unit
    ) {
            contaViewModel.insertMonitorado(conta){
                movimentacaoMensal.contaId = conta.contaId
                callback()
            }

    }
}