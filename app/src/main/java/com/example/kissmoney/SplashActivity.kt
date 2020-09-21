package com.example.kissmoney

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import com.example.kissmoney.mes.Estatisticas
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.meta.MetaViewModel


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)


        var mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)

        var contaViewModel = ViewModelProvider(this).get(ContaViewModel::class.java)
        var movimentacaoMensalViewModel= ViewModelProvider(this).get(MovimentacaoMensalViewModel::class.java)

        var ganhoViewModel = ViewModelProvider(this).get(GanhoViewModel::class.java)
        var ganhoMesViewModel = ViewModelProvider(this).get(GanhoMensalViewModel::class.java)

        var compromissoViewModel = ViewModelProvider(this).get(CompromissoViewModel::class.java)
        var compromissoMensalViewModel = ViewModelProvider(this).get(CompromissoMensalViewModel::class.java)

        var metaViewModel = ViewModelProvider(this).get(MetaViewModel::class.java)

        var cjvm = ContaJoinViewModel

        cjvm.setMyOwner(this){
            cjvm.setApplicationObject(contaViewModel, mesViewModel, movimentacaoMensalViewModel){
                //cjvm.setAllContasJoin(){}
            }
        }

        var gjvm = GanhoJoinViewModel

        gjvm.setMyOwner(this){
            gjvm.setApplicationObject(ganhoViewModel, ganhoMesViewModel, mesViewModel){}
        }

        var compjvm = CompromissoJoinViewModel

        compjvm.setMyOwner(this) {
            compjvm.setApplicationObject(compromissoViewModel, compromissoMensalViewModel, mesViewModel){}
        }



        Estatisticas.setViewModels(mesViewModel, metaViewModel) {
            Estatisticas.processa(){
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                println(">>>>>>>>>>> ESTATISTICA STRING >>>>>>>>>>>>>>")
                Estatisticas.toString()
                startActivity(intent)
            }
        }






        //startActivity(intent)
        //finish()
    }
}