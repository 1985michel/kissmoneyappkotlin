package com.example.kissmoney.mes

import androidx.lifecycle.LiveData
import com.example.kissmoney.compromissos.CompromissoJoinViewModel
import com.example.kissmoney.compromissos.CompromissoViewModel
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.mes.CentralEstatistica.estatisticasMensais
import com.example.kissmoney.meta.AcompanhamentoDeMeta
import com.example.kissmoney.meta.Meta
import com.example.kissmoney.meta.MetaViewModel
import com.example.kissmoney.util.formataComNCasasDecimais
import com.example.kissmoney.util.getDiaHojeNoMes
import com.example.kissmoney.util.getNomeMesAtual
import com.example.kissmoney.util.recebeNomeMesRetornaNomeMesAnterior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode

//class Estatisticas (mes: Mes, mesVM: MesViewModel, metaVM: MetaViewModel) {

class Estatisticas (mes: Mes) {

    private lateinit var metas: List<Meta>

    var mesAtual: Mes
    //private lateinit var mesAnterior: Mes

//    private  var mesViewModel: MesViewModel
//    private  var metaViewModel: MetaViewModel


    init {
        this.mesAtual = mes
//        this.metaViewModel = metaVM
//        this.mesViewModel = mesVM

    }

    var totalEmCaixa = 0.0
    var totalEmCaixaNoInicioDoMes = 0.0
//    var totalEmCaixaNoInicioDosRegistros = 0.0

    var totalGanhoNoMes = 0.0
//    var totalGanhoTodoHistorico = 0.0

    var totalGastoNoMes = 0.0
    var gastoDiarioNoMes = 0.0
//    var totalGastoTodoHistorico = 0.0
//    var gastoDiarioTodoHistorico = 0.0

//    var totalGastoNoMesAnterior = 0.0

    var totalCompromissosDoMes = 0.0
    var totalCompromissosPendentesDoMes = 0.0

//    var totalCompromissosDoMesAnterior = 0.0
//    var totalCompromissosPendentesDoMesAnterior = 0.0


//    var qtdMesesComRegistros = 0
    var abastanca = 0 //qtd de dias de liberdade financeira
    var totalInvestido = 0.0


    var balanco = 0.0
    var balancoEmDias = 0


    override fun toString(): String {

        println(">>> totalEmCaixa = ${this.totalEmCaixa}")
        println(">>> totalEmCaixaNoInicioDoMes = ${this.totalEmCaixaNoInicioDoMes}")
//        println(">>> totalEmCaixaNoInicioDosRegistros = $totalEmCaixaNoInicioDosRegistros")
        println(">>> totalGanhoNoMes = $totalGanhoNoMes")
//        println(">>> totalGanhoTodoHistorico = $totalGanhoTodoHistorico")
//        println(">>> totalGastoNoMes = $totalGastoNoMes")
//        println(">>> totalGastoTodoHistorico = $totalGanhoTodoHistorico")
//        println(">>> gastoDiarioNoMes = $totalGastoNoMes")
//        println(">>> gastoDiarioTodoHistorico = $gastoDiarioTodoHistorico")
//        println(">>> qtdMesesComRegistros = $qtdMesesComRegistros")
        println(">>> abastanca = 0 $abastanca")
        println(">>> totalInvestido = $totalInvestido")

        return ""
    }

     fun imprime() {

        println(">>> totalEmCaixa = ${this.totalEmCaixa}")
        println(">>> totalEmCaixaNoInicioDoMes = ${this.totalEmCaixaNoInicioDoMes}")
//        println(">>> totalEmCaixaNoInicioDosRegistros = $totalEmCaixaNoInicioDosRegistros")
        println(">>> totalGanhoNoMes = $totalGanhoNoMes")
//        println(">>> totalGanhoTodoHistorico = $totalGanhoTodoHistorico")
//        println(">>> totalGastoNoMes = $totalGastoNoMes")
//        println(">>> totalGastoTodoHistorico = $totalGanhoTodoHistorico")
//        println(">>> gastoDiarioNoMes = $totalGastoNoMes")
//        println(">>> gastoDiarioTodoHistorico = $gastoDiarioTodoHistorico")
//        println(">>> qtdMesesComRegistros = $qtdMesesComRegistros")
        println(">>> abastanca = 0 $abastanca")
        println(">>> totalInvestido = $totalInvestido")

    }

    fun processaDadosEstatisticasDependentesDeDadosGerais(callback: () -> Unit) {
        setMyAbastanca {
            setMyBalancoEmDias {
                callback()
            }
        }
    }

    fun processa(callback: () -> Unit) {
//        mesAtual = Mes(0L, getNomeMesAtual())
//        mesViewModel.getByName(mesAtual) {


        println(" >>>>>>>>> ENTRANDO EM processa da classe ESTATÍSTICAS")

//            GlobalScope.launch {
//                //Background processing..."
//                withContext(Dispatchers.Main) {
//                    setMyTotalEmCaixa() {
//                        println(">>>>> setMyTotalEmCaixa retornou")
//                        setMyTotalGanhoNoMes() {
//                            println(">>>>> setMyTotalGanhoNoMes retornou")
//                            setMyTotalGastoNoMes {
//                                println(">>>>> setMyTotalGastoNoMes retornou")
//                                setMyGastoDiarioNoMes {
//                                    println(">>>>> setMyGastoDiarioNoMes retornou")
//                                    setMyTotalGanhoTodoHistorico() {
//                                        println(">>>>> setMyTotalGanhoTodoHistorico retornou")
//                                        setMyTotalGastoTodoHistorico {
//                                            println(">>>>> setMyTotalGastoTodoHistorico retornou")
//                                            setMyGastoDiarioTodoHistorico {
//                                                println(">>>>> setMyGastoDiarioTodoHistorico retornou")
//                                                setMyAbastanca {
//                                                    println(">>>>> setMyAbastanca retornou")
////                                                    println(">>>>>>>>>>>>>>>>> EM ESTATÍSTICAS ESTAMOS SETANDO A ABASTANCA EM $abastanca")
//                                                    setMyCompromissosDoMes {
//                                                        println(">>>>> setMyCompromissosDoMes retornou")
//                                                        setMyBalanco {
//                                                            println(">>>>> setMyBalanco retornou")
//                                                            setMyBalancoEmDias{
//                                                                println(">>>>> setMyBalancoEmDias retornou")
//                                                                println(" >>>>>>>>> callback de processa da classe ESTATÍSTICAS")
//                                                                callback()
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }



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
//                                setMyTotalGanhoTodoHistorico() {
//                                    println(">>>>> setMyTotalGanhoTodoHistorico retornou")
//                                    setMyTotalGastoTodoHistorico {
//                                        println(">>>>> setMyTotalGastoTodoHistorico retornou")
//                                        setMyGastoDiarioTodoHistorico {
//                                            println(">>>>> setMyGastoDiarioTodoHistorico retornou")
//                                            setMyAbastanca {
//                                                println(">>>>> setMyAbastanca retornou")
//                                                    println(">>>>>>>>>>>>>>>>> EM ESTATÍSTICAS ESTAMOS SETANDO A ABASTANCA EM $abastanca")
                                                setMyCompromissosDoMes {
                                                    println(">>>>> setMyCompromissosDoMes retornou")
                                                    setMyBalanco {
                                                        println(">>>>> setMyBalanco retornou")
//                                                        setMyBalancoEmDias{
//                                                            println(">>>>> setMyBalancoEmDias retornou")
                                                            println(" >>>>>>>>> callback de processa da classe ESTATÍSTICAS")
                                                            callback()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
//                        }
//                   }
//                }
//            }
//        }











//            mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
//            mesViewModel.getByName(mesAnterior) {
//
//                GlobalScope.launch {
//                    //Background processing..."
//                    withContext(Dispatchers.Main) {
//                        setMyCompromissosDoMesAnterior {}
//                    }
//                }
//            }
        //}
    }

    fun setMyMetas(callback: () -> Unit) {
        callback()
    }

//    fun setViewModels(mesVM: MesViewModel, metaVM: MetaViewModel, callback: () -> Unit) {
//        this.mesViewModel = mesVM
//        this.metaViewModel = metaVM
//        callback()
//    }

    fun setMyAbastanca(callback: () -> Unit) {
        //abastanca = (totalEmCaixa / gastoDiarioTodoHistorico).toInt()
        abastanca = (totalEmCaixa / CentralEstatistica.gastoDiarioTodoHistorico).toInt()
//        println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
//        println(">>>> Estatísicas 109: Abastança ($abastanca) = total em Caixa ($totalEmCaixa) / gasto diario todo historico ($gastoDiarioTodoHistorico)")
        callback()
    }

    fun setMyBalanco(callback: () -> Unit) {
        //essa forma de calculo está errada. O balanço é mensal, deve ser só dentro do mês
        //balanco = totalEmCaixa - totalEmCaixaNoInicioDosRegistros
        balanco = totalEmCaixa - totalEmCaixaNoInicioDoMes
        callback()
    }

    fun setMyBalancoEmDias(callback: () -> Unit) {
        balancoEmDias = (balanco / CentralEstatistica.gastoDiarioTodoHistorico).toInt()
        callback()
    }


    fun setMyGastoDiarioNoMes(callback: () -> Unit) {

        //se estivermos trabalhando com o mês corrente, calcule com base no dia de hoje
        if (mesAtual.nomeMes ==  AcompanhamentoDeMeta.mesAtual.nomeMes) {
            gastoDiarioNoMes = totalGastoNoMes / getDiaHojeNoMes()
            callback()
        } else {
            //se for um mês já concluído, calcule com base em 30 dias
            gastoDiarioNoMes = totalGastoNoMes / 30.0
            callback()
        }

    }


//    fun setMyGastoDiarioTodoHistorico(callback: () -> Unit) {
//        var qtdDias = (qtdMesesComRegistros - 1) * 30
//
//        //gastoDiarioTodoHistorico = totalGastoTodoHistorico / (qtdDias + getDiaHojeNoMes())
//
//        //vou alterar essa forma de calcular, vou me basear nos meses que têm custo mensal
//
//        var total = 0.0
//        var dias = 1
//
//        for ( (key, value) in CentralEstatistica.estatisticasMensais) {
//            if( value.totalGastoNoMes > 0) {
//                total += value.totalGastoNoMes
//                if (key != AcompanhamentoDeMeta.mesAtual.mesId) {
//                    dias += 30
//                } else {
//                    dias += getDiaHojeNoMes()
//                }
//            }
//        }
//
//        println ("88888888888888888888888888888888888888888")
//        println("7777777777777777777777777777777777777777777")
//        println("666666666666666666666666666666666666666666")
//        println(">>>>>>>>>>>>>>>>>>>> ENCONTRAMOS ${dias} dias e o valor total de ${total}")
//
//        gastoDiarioTodoHistorico = total / dias
//
//
//
////        println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
////        println(">>>> Estatísicas 126: Gasto Diario de todo o historico ($gastoDiarioTodoHistorico) = total gasto todo historico ($totalGastoTodoHistorico) / qtdDias (${qtdDias + getDiaHojeNoMes()})")
//        callback()
//    }


    // Atenção! A fatura do cartão (tratada como uma conta) por ser negativa, já é descontada do caixa do mês anterior
    // por isso não entra no gasto do mês atual
    fun setMyTotalGastoNoMes(callback: () -> Unit) {
        totalGastoNoMes = 0.0
        totalGastoNoMes = totalEmCaixaNoInicioDoMes - totalEmCaixa + totalGanhoNoMes

        totalGastoNoMes = formataComNCasasDecimais(totalGastoNoMes,2)
        callback()
    }


//    fun setMyTotalGanhoTodoHistorico(callback: () -> Unit) {
//        totalGanhoTodoHistorico = 0.0
//        GanhoJoinViewModel.setAllGanhosJoin() {
//            for (gj in GanhoJoinViewModel.allGanhosJoin) {
////                println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
////                println(">>>>>>>>>>> ESTATISTICAS 141:  valores ganhos: ${gj.valor}")
//                totalGanhoTodoHistorico += gj.valor
//            }
//            totalGanhoTodoHistorico = formataComNCasasDecimais(totalGanhoTodoHistorico,2)
//            callback()
//        }
//    }

    fun setMyCompromissosDoMes(callback: () -> Unit) {
        totalCompromissosDoMes = 0.0
        totalCompromissosPendentesDoMes = 0.0

        CompromissoJoinViewModel.setCompromissosJoinNoMes(mesAtual.mesId) {
            for (comp in CompromissoJoinViewModel.compromissosJoinDoMes) {
                totalCompromissosDoMes += comp.valor
                if (!comp.isPago) totalCompromissosPendentesDoMes += comp.valor
            }
            callback()
        }

    }

//    fun setMyCompromissosDoMesAnterior(callback: () -> Unit) {
//        totalCompromissosDoMesAnterior = 0.0
//        totalCompromissosPendentesDoMesAnterior
//        CompromissoJoinViewModel.setCompromissosJoinNoMes(mesAtual.mesId) {
//            for (comp in CompromissoJoinViewModel.compromissosJoinDoMes) {
//                totalCompromissosDoMes += comp.valor
//                if (!comp.isPago) totalCompromissosPendentesDoMes += comp.valor
//            }
//            callback()
//        }
//    }


//    fun setMyTotalGastoTodoHistorico(callback: () -> Unit) {
//
//        totalGastoTodoHistorico =
//            totalEmCaixaNoInicioDosRegistros - totalEmCaixa + totalGanhoTodoHistorico
//
//        totalGastoTodoHistorico = formataComNCasasDecimais(totalGastoTodoHistorico,2)
////        println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
////        println(">>>> Estatísicas 152: totalGastoTodoHistorico ($totalGastoTodoHistorico) = totalEmCaixaNoInicioDosRegistros ($totalEmCaixaNoInicioDosRegistros) " +
////                "- totalEmCaixa ($totalEmCaixa) + totalGanhoTodoHistorico ($totalGanhoTodoHistorico)")
//        callback()
//
//    }


    fun setMyTotalGanhoNoMes(callback: () -> Unit) {

        totalGanhoNoMes = 0.0

        GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId) {
            for (gj in GanhoJoinViewModel.ganhosJoinDoMes) {
                if (gj.isRecebido) {
                    totalGanhoNoMes += gj.valor
                    println(" >>>>> ------------ foi ganho em ${mesAtual.nomeMes} >>> ${gj.nomeGanho} >>> ${gj.valor}")
                }
            }

            //arredondando com duas casas decimais
            totalGanhoNoMes = formataComNCasasDecimais(totalGanhoNoMes, 2)
            println(" >>>>> ------------ TOTAL ganho em ${mesAtual.nomeMes} >>> ${totalGanhoNoMes}")
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

            callback()

//            setTotalEmCaixaNoInicioDosRegistros() {
//                callback()
//            }

            //callback()
        }

    }

//    fun setTotalEmCaixaNoInicioDosRegistros(callback: () -> Unit) {
//
//        totalEmCaixaNoInicioDosRegistros = 0.0
//        var contasConsideradasList = ArrayList<Long>()
//        contasConsideradasList.clear()
//
//        ContaJoinViewModel.setAllContasJoin() {
//            for (cj in ContaJoinViewModel.allContasJoin) {
//                if (!contasConsideradasList.contains(cj.contaId)) {
//                    totalEmCaixaNoInicioDosRegistros += cj.saldoInicial
//                    contasConsideradasList.add(cj.contaId)
//                }
//            }
//            qtdMesesComRegistros = ContaJoinViewModel.qtdDeMesesComRegistro
//            callback()
//        }
//    }
}