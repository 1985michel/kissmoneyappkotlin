package com.example.kissmoney

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kissmoney.compromissos.CompromissoJoinViewModel
import com.example.kissmoney.compromissos.CompromissoViewModel
import com.example.kissmoney.compromissos.mensal.CompromissoMensalViewModel
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.contas.ContaViewModel
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.ganhos.GanhoViewModel
import com.example.kissmoney.ganhos.mensal.GanhoMensalViewModel
import com.example.kissmoney.mes.CentralEstatistica
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.meta.AcompanhamentoDeMeta
import com.example.kissmoney.meta.MetaViewModel
import com.example.kissmoney.util.getNomeMesAtual


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)


        var mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)

        var contaViewModel = ViewModelProvider(this).get(ContaViewModel::class.java)
        var movimentacaoMensalViewModel =
            ViewModelProvider(this).get(MovimentacaoMensalViewModel::class.java)

        var ganhoViewModel = ViewModelProvider(this).get(GanhoViewModel::class.java)
        var ganhoMesViewModel = ViewModelProvider(this).get(GanhoMensalViewModel::class.java)

        var compromissoViewModel = ViewModelProvider(this).get(CompromissoViewModel::class.java)
        var compromissoMensalViewModel =
            ViewModelProvider(this).get(CompromissoMensalViewModel::class.java)

        var metaViewModel = ViewModelProvider(this).get(MetaViewModel::class.java)

        var cjvm = ContaJoinViewModel

        cjvm.setMyOwner(this) {
            cjvm.setApplicationObject(contaViewModel, mesViewModel, movimentacaoMensalViewModel) {
                //cjvm.setAllContasJoin(){}
            }
        }

        var gjvm = GanhoJoinViewModel

        gjvm.setMyOwner(this) {
            gjvm.setApplicationObject(ganhoViewModel, ganhoMesViewModel, mesViewModel) {}
        }

        var compjvm = CompromissoJoinViewModel

        compjvm.setMyOwner(this) {
            compjvm.setApplicationObject(
                compromissoViewModel,
                compromissoMensalViewModel,
                mesViewModel
            ) {}
        }

        CentralEstatistica.setMyOwner(this)


        // vou rodar estatísticas do mes atual e do mes anterior

//        var mesAtual = Mes(0L, getNomeMesAtual())
//        mesViewModel.getByName(mesAtual) {
//            CentralEstatistica.addMes(mesAtual) {
//
//                //já facilitando para a view de Mes (tela inicial)
//                AcompanhamentoDeMeta.setMyMesAtual(mesAtual)
//
//                var mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
//                mesViewModel.getByName(mesAnterior){
//                    CentralEstatistica.addMes(mesAnterior){
//                        startActivity(intent)
//                        finish() //encerra a splash
//                    }
//                }
//            }
//        }

        // vou rodar estatísticas de todos os meses


        var mesAtual = Mes(0L, getNomeMesAtual())

        var listaDeMeses = ArrayList<Mes>()

        getListaDeMeses(mesViewModel, listaDeMeses) {
            println(">>>>>>>> AGORA VOU PEGAR O MES ATUAL")
            mesViewModel.getByName(mesAtual) {

                //já facilitando para a view de Mes (tela inicial)
                AcompanhamentoDeMeta.setMyMesAtual(mesAtual)
                CentralEstatistica.setMesViewMOdel(mesViewModel)


                println(">>>>>>>>>>>>>>>>>Vou chamar o addallmeses com uma lista que contém ${listaDeMeses.size} elementos")
                CentralEstatistica.addAllMeses(listaDeMeses) {
                    startActivity(intent)
                    finish() //encerra a splash
                }
            }
        }


//        mesViewModel.getByName(mesAtual) {
//
//            //já facilitando para a view de Mes (tela inicial)
//            AcompanhamentoDeMeta.setMyMesAtual(mesAtual)
//            CentralEstatistica.setMesViewMOdel(mesViewModel)
//
////            GlobalScope.launch {
////                //Background processing..."
////                withContext(Dispatchers.Main) {
//            //"Update UI here!")
//
//            println("Vou chamar o addallmeses com uma lista que contém ${listaDeMeses.size} elementos")
//            CentralEstatistica.addAllMeses(listaDeMeses) {
//                startActivity(intent)
//                finish() //encerra a splash
//            }
////                }
////            }
//        }

//        CentralEstatistica.setMesViewMOdel(mesViewModel)
//        CentralEstatistica.addAllMeses {
//            startActivity(intent)
//            finish() //encerra a splash
//        }


//        Estatisticas.setViewModels(mesViewModel, metaViewModel) {
//            Estatisticas.processa(){
////                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
////                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
////                println(">>>>>>>>>>> ESTATISTICA STRING >>>>>>>>>>>>>>")
////                Estatisticas.toString()
//                startActivity(intent)
//                finish() //encerra a splash
//            }
//        }


        //startActivity(intent)
        //finish()
    }

    private fun getListaDeMeses(
        mesViewModel: MesViewModel,
        listaDeMeses: ArrayList<Mes>,
        callback: () -> Unit
    ) {
        //var listaDeMeses = ArrayList<Mes>()
        mesViewModel.allMeses.observe(CentralEstatistica.owner, Observer { listaMeses ->
            listaMeses.forEach {
                listaDeMeses.add(it)
            }
            println("<<<<<<<<<<<<<<<<<<<<< TERMINAMOS O PROCESSAMENTO DA LISTA DE MESES. ELA TEM ${listaDeMeses.size} elementos")
            callback()
        })

    }
}