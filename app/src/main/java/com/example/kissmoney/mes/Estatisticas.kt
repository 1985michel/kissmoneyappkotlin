package com.example.kissmoney.mes

import androidx.lifecycle.LiveData
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.meta.Meta
import com.example.kissmoney.meta.MetaViewModel
import com.example.kissmoney.util.getDiaHojeNoMes
import com.example.kissmoney.util.getNomeMesAtual
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Estatisticas {

    private lateinit var metas: List<Meta>

    private lateinit var mesAtual: Mes

    private lateinit var mesViewModel: MesViewModel
    private lateinit var metaViewModel: MetaViewModel

    var totalEmCaixa = 0.0
    var totalEmCaixaNoInicioDoMes = 0.0
    var totalEmCaixaNoInicioDosRegistros = 0.0
    var totalGanhoNoMes = 0.0
    var totalGanhoTodoHistorico = 0.0
    var totalGastoNoMes = 0.0
    var totalGastoTodoHistorico = 0.0
    var gastoDiarioNoMes = 0.0
    var gastoDiarioTodoHistorico = 0.0
    var qtdMesesComRegistros = 0
    var abastanca = 0 //qtd de dias de liberdade financeira
    var totalInvestido = 0.0


    override fun toString(): String {

        println(">>> totalEmCaixa = ${this.totalEmCaixa}")
        println(">>> totalEmCaixaNoInicioDoMes = ${this.totalEmCaixaNoInicioDoMes}")
        println(">>> totalEmCaixaNoInicioDosRegistros = $totalEmCaixaNoInicioDosRegistros")
        println(">>> totalGanhoNoMes = $totalGastoNoMes")
        println(">>> totalGanhoTodoHistorico = $totalGanhoTodoHistorico")
        println(">>> totalGastoNoMes = $totalGastoNoMes")
        println(">>> totalGastoTodoHistorico = $totalGanhoTodoHistorico")
        println(">>> gastoDiarioNoMes = $totalGastoNoMes")
        println(">>> gastoDiarioTodoHistorico = $gastoDiarioTodoHistorico")
        println(">>> qtdMesesComRegistros = $qtdMesesComRegistros")
        println(">>> abastanca = 0 $abastanca")
        println(">>> totalInvestido = $totalInvestido")

        return ""
    }

    fun processa(callback: () -> Unit) {
        mesAtual = Mes(0L, getNomeMesAtual())
        mesViewModel.getByName(mesAtual) {

            GlobalScope.launch {
                //Background processing..."
                withContext(Dispatchers.Main) {
                    setMyTotalEmCaixa() {
                        println(">>>>> setMyTotalEmCaixa retornou")
                        setMyTotalGanhoNoMes() {
                            println(">>>>> setMyTotalGanhoNoMes retornou")
                            setMyTotalGastoNoMes {
                                println(">>>>> setMyTotalGastoNoMes retornou")
                                setMyGastoDiarioNoMes {
                                    println(">>>>> setMyGastoDiarioNoMes retornou")
                                    setMyTotalGanhoTodoHistorico() {
                                        println(">>>>> setMyTotalGanhoTodoHistorico retornou")
                                        setMyTotalGastoTodoHistorico {
                                            println(">>>>> setMyTotalGastoTodoHistorico retornou")
                                            setMyGastoDiarioTodoHistorico {
                                                println(">>>>> setMyGastoDiarioTodoHistorico retornou")
                                                setMyAbastanca {
                                                    println(">>>>> setMyAbastanca retornou")
                                                    println(">>>>>>>>>>>>>>>>> EM ESTATÍSTICAS ESTAMOS SETANDO A ABASTANCA EM $abastanca")
                                                    callback()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


    }

    fun setMyMetas(callback: () -> Unit) {
        callback()
    }

    fun setViewModels(mesVM: MesViewModel, metaVM: MetaViewModel, callback: () -> Unit) {
        this.mesViewModel = mesVM
        this.metaViewModel = metaVM
        callback()
    }

    fun setMyAbastanca(callback: () -> Unit) {
        abastanca = (totalEmCaixa / gastoDiarioTodoHistorico).toInt()
        callback()
    }


    fun setMyGastoDiarioNoMes(callback: () -> Unit) {
        gastoDiarioNoMes = totalGastoNoMes / getDiaHojeNoMes()
        callback()
    }


    fun setMyGastoDiarioTodoHistorico(callback: () -> Unit) {
        var qtdDias = (qtdMesesComRegistros - 1) * 30
        gastoDiarioTodoHistorico = totalGastoTodoHistorico / (qtdDias + getDiaHojeNoMes())
        callback()
    }

    fun setMyTotalGastoNoMes(callback: () -> Unit) {
        totalGastoNoMes = 0.0
        totalGastoNoMes = totalEmCaixaNoInicioDoMes - totalEmCaixa + totalGanhoNoMes
        callback()
    }

    fun setMyTotalGanhoTodoHistorico(callback: () -> Unit) {
        totalGanhoTodoHistorico = 0.0
        GanhoJoinViewModel.setAllGanhosJoin() {
            for (gj in GanhoJoinViewModel.allGanhosJoin) {
                totalGanhoTodoHistorico += gj.valor
            }
            callback()
        }
    }


    fun setMyTotalGastoTodoHistorico(callback: () -> Unit) {

        totalGanhoTodoHistorico =
            totalEmCaixaNoInicioDosRegistros - totalEmCaixa + totalGanhoTodoHistorico
        callback()

    }


    fun setMyTotalGanhoNoMes(callback: () -> Unit) {
        totalGanhoNoMes = 0.0

        GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId) {
            for (gj in GanhoJoinViewModel.ganhosJoinDoMes) {
                if (gj.isRecebido) totalGanhoNoMes += gj.valor
            }

            callback()
        }
    }


    fun setMyTotalEmCaixa(callback: () -> Unit) {

        totalEmCaixa = 0.0
        totalEmCaixaNoInicioDoMes = 0.0


        ContaJoinViewModel.setContasJoinNoMes(mesAtual.mesId) {

            println("*************** linha 166 de estatisticas: ENCONTRAMOS AO TODO ${ContaJoinViewModel.contasJoinDoMes.size} CONTAS")

            for (cj in ContaJoinViewModel.contasJoinDoMes) {
                totalEmCaixa += cj.saldoAtualOuFinal
                totalEmCaixaNoInicioDoMes += cj.saldoInicial
            }

            setTotalEmCaixaNoInicioDosRegistros() {
                callback()
            }

            //callback()
        }

    }

    fun setTotalEmCaixaNoInicioDosRegistros(callback: () -> Unit) {

        totalEmCaixaNoInicioDosRegistros = 0.0

        ContaJoinViewModel.setAllContasJoin() {
            for (cj in ContaJoinViewModel.allContasJoin) {
                totalEmCaixaNoInicioDosRegistros += cj.saldoInicial
            }
            qtdMesesComRegistros = ContaJoinViewModel.qtdDeMesesComRegistro
            callback()
        }
    }
}