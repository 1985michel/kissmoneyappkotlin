package com.example.kissmoney.mes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.meta.AcompanhamentoDeMeta
import com.example.kissmoney.meta.MetaViewModel
import com.example.kissmoney.util.formataComNCasasDecimais
import com.example.kissmoney.util.getDiaHojeNoMes

object CentralEstatistica {

    lateinit var estatisticasMensais: HashMap<Long, Estatisticas>

    lateinit var owner: LifecycleOwner

    private lateinit var mesViewModel: MesViewModel
    private lateinit var metaViewModel: MetaViewModel

    var listaDeBalancos = ArrayList<Balanco>()

    var totalGastoTodoHistorico = 0.0
    var gastoDiarioTodoHistorico = 0.0
    var totalGanhoTodoHistorico = 0.0
    var totalEmCaixaNoInicioDosRegistros = 0.0

    init {
        estatisticasMensais = HashMap<Long, Estatisticas>()
    }

    fun setMyOwner(owner: LifecycleOwner) {
        this.owner = owner
    }

    fun setMesViewMOdel(mesViewModel: MesViewModel) {
        this.mesViewModel = mesViewModel
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addAllMeses(listaMeses: ArrayList<Mes>, callback: () -> Unit) {
        //mesViewModel.allMeses.observe(owner, Observer { listaMeses ->
        listaMeses.forEach {

            //println(" >>>>>>>>>>>")

            println(" VOU ADICIONAR ESTATISTICAS DO MES ${it.nomeMes} com ID ${it.mesId}")
            addMes(it) {
                if (estatisticasMensais.size == listaMeses.size) {
                    CentralEstatistica.processa {
                        callback()
                    }
                }
            }
        }

//            callback when (estatisticasMensais.size == listaMeses.size)
//            callback()
        //})
    }

    fun addMes(mes: Mes, callback: () -> Unit) {

        println(">>>>>> ENTRANDO NO ADD MES COM O MES ${mes.nomeMes} com ID ${mes.mesId}")

        if (!estatisticasMensais.containsKey(mes.mesId)) {
            println("______________________________ ESTATISTICAS MENSAIS AINDA NÃO TEM UM MES COM o mesID ${mes.mesId}")
            var estat = Estatisticas(mes)
            estat.processa {
                estatisticasMensais.put(mes.mesId, estat)
                println("______________________________ adicionei uma estatistica com o mesID ${mes.mesId}")
                callback()
            }
        } else {
            println("______________________________JA TEM uma estatistica com o mesID ${mes.mesId}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun processa(callback: () -> Unit) {
        processaEstatisticasIndividualmente {
            processaDadosGerais {
                setDadosEstatisticasDependentesDeDadosGerais{
                    processaBalancoList{
                        callback()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun processaEstatisticasIndividualmente(callback: () -> Unit) {
        var n = 1
        estatisticasMensais.forEach { mesId, estatisticas ->
            estatisticas.processa() {
                n++
                if (n == estatisticasMensais.size) {
                    callback()
                }
            }
        }
    }

    fun processaDadosGerais(callback: () -> Unit) {

        setTotalEmCaixaNoInicioDosRegistros {
            setMyTotalGanhoTodoHistorico() {
                println(">>>>> setMyTotalGanhoTodoHistorico retornou")
                setMyGastoDiarioTodoHistorico {
                    println(">>>>> setMyTotalGastoTodoHistorico retornou")
                    setMyTotalGastoTodoHistorico { // apenas formada a informação já coletada anteriormente
                        println(">>>>> setMyGastoDiarioTodoHistorico retornou")
                        callback()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setDadosEstatisticasDependentesDeDadosGerais(callback: () -> Unit) {
        estatisticasMensais.forEach { mesId, estatisticas ->
            estatisticas.processaDadosEstatisticasDependentesDeDadosGerais() {
                callback()
            }
        }
    }


    //para alimentar os dados do mes_fragment
    @RequiresApi(Build.VERSION_CODES.N)
    fun processaBalancoList(callback: () -> Unit) {

        listaDeBalancos.clear()
        var chave = 1
        estatisticasMensais.forEach { key, estat ->
            chave ++
            if (estat.totalGastoNoMes > 0) {
                listaDeBalancos.add(
                    Balanco(
                        estat.mesAtual.nomeMes,
                        estat.balanco,
                        estat.balancoEmDias
                    )
                )
            }
            if (chave == estatisticasMensais.size) callback()
        }

    }

    fun setTotalEmCaixaNoInicioDosRegistros(callback: () -> Unit) {

        totalEmCaixaNoInicioDosRegistros = 0.0
        var contasConsideradasList = ArrayList<Long>()
        contasConsideradasList.clear()

        ContaJoinViewModel.setAllContasJoin() {
            for (cj in ContaJoinViewModel.allContasJoin) {
                if (!contasConsideradasList.contains(cj.contaId)) {
                    totalEmCaixaNoInicioDosRegistros += cj.saldoInicial
                    contasConsideradasList.add(cj.contaId)
                }
            }
            //qtdMesesComRegistros = ContaJoinViewModel.qtdDeMesesComRegistro
            callback()
        }
    }

    fun setMyGastoDiarioTodoHistorico(callback: () -> Unit) {
        //var qtdDias = (qtdMesesComRegistros - 1) * 30

        //gastoDiarioTodoHistorico = totalGastoTodoHistorico / (qtdDias + getDiaHojeNoMes())

        //vou alterar essa forma de calcular, vou me basear nos meses que têm custo mensal

        var total = 0.0
        var dias = 0

        totalGastoTodoHistorico = 0.0

        for ((key, value) in CentralEstatistica.estatisticasMensais) {
            if (value.totalGastoNoMes > 0) {
                total += value.totalGastoNoMes
                totalGastoTodoHistorico += value.totalGastoNoMes
                if (key != AcompanhamentoDeMeta.mesAtual.mesId) {
                    dias += 30
                } else {
                    dias += getDiaHojeNoMes()
                }
            }
        }

//        println("88888888888888888888888888888888888888888")
//        println("7777777777777777777777777777777777777777777")
//        println("666666666666666666666666666666666666666666")
//        println(">>>>>>>>>>>>>>>>>>>> ENCONTRAMOS ${dias} dias e o valor total de ${total}")

        gastoDiarioTodoHistorico = total / dias


//        println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
//        println(">>>> Estatísicas 126: Gasto Diario de todo o historico ($gastoDiarioTodoHistorico) = total gasto todo historico ($totalGastoTodoHistorico) / qtdDias (${qtdDias + getDiaHojeNoMes()})")
        callback()
    }

    fun setMyTotalGanhoTodoHistorico(callback: () -> Unit) {
        totalGanhoTodoHistorico = 0.0
        GanhoJoinViewModel.setAllGanhosJoin() {
            for (gj in GanhoJoinViewModel.allGanhosJoin) {
//                println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
//                println(">>>>>>>>>>> ESTATISTICAS 141:  valores ganhos: ${gj.valor}")
                totalGanhoTodoHistorico += gj.valor
            }
            totalGanhoTodoHistorico = formataComNCasasDecimais(totalGanhoTodoHistorico, 2)
            callback()
        }
    }

    fun setMyTotalGastoTodoHistorico(callback: () -> Unit) {

//        totalGastoTodoHistorico =
//            totalEmCaixaNoInicioDosRegistros - totalEmCaixa + totalGanhoTodoHistorico

//        totalGastoTodoHistorico = totalEmCaixaNoInicioDosRegistros - CentralEstatistica.estatisticasMensais.get() + totalGanhoTodoHistorico

            totalGastoTodoHistorico = formataComNCasasDecimais(totalGastoTodoHistorico, 2)
//        println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨")
//        println(">>>> Estatísicas 152: totalGastoTodoHistorico ($totalGastoTodoHistorico) = totalEmCaixaNoInicioDosRegistros ($totalEmCaixaNoInicioDosRegistros) " +
//                "- totalEmCaixa ($totalEmCaixa) + totalGanhoTodoHistorico ($totalGanhoTodoHistorico)")
        callback()

    }

}