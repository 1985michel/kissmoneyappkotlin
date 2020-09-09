package com.example.kissmoney.contas

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.kissmoney.MainActivity
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalDao
import com.example.kissmoney.contas.mensal.MovimentacaoMensalRepository
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.database.KissmoneyDatabase
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesDao
import com.example.kissmoney.mes.MesRepository
import com.example.kissmoney.mes.MesViewModel

object ContaJoinViewModel {

//    private lateinit var repositoryConta: ContaRepository
//    private lateinit var repositoryMovimentacaoMensal: MovimentacaoMensalRepository
//    private lateinit var repositoryMes: MesRepository

    private lateinit var allContas: LiveData<List<Conta>>
    private lateinit var allMovimentacoes: LiveData<List<MovimentacaoMensal>>
    private lateinit var allMeses: LiveData<List<Mes>>

    var allContasJoin = ArrayList<ContaJoin>()

    lateinit var owner : LifecycleOwner


    fun setApplicationObject(contaViewModel: ContaViewModel, mesViewModel: MesViewModel, movimentacaoMensalViewModel: MovimentacaoMensalViewModel) {
//        val contaDao = KissmoneyDatabase.getDatabase(application, viewModelScope).contaDao()
//        val movimentacaoDao = KissmoneyDatabase.getDatabase(application, viewModelScope).movimentacaoMensalDao()
//        val mesDao = KissmoneyDatabase.getDatabase(application, viewModelScope).mesDao()

//
//
//        repositoryConta = ContaRepository(contaDao)
//        repositoryMovimentacaoMensal = MovimentacaoMensalRepository(movimentacaoDao)
//        repositoryMes = MesRepository(mesDao)

        allContas = contaViewModel.allContas
        allMovimentacoes = movimentacaoMensalViewModel.allMovimentacoes
        allMeses = mesViewModel.allMeses
    }

    fun setMyOwner(owner: LifecycleOwner){
        this.owner = owner
    }

    fun setAllContasJoin(){

        allContasJoin = ArrayList<ContaJoin>()

        var moviList = ArrayList<MovimentacaoMensal>()

        var contaList = ArrayList<Conta>()

        var mesList = ArrayList<Mes>()

        getData(moviList, mesList, contaList){
            var mesW = Mes(0L, "")
            var contaW = Conta(0L, "", "", false)

            for (movi in moviList){
                for (mes in mesList) {
                    if(mes.mesId == movi.mesId) mesW = mes
                }
                for (conta in contaList){
                    if (conta.contaId == movi.contaId) contaW = conta
                }
                allContasJoin.add(ContaJoin(contaW,movi,mesW))
            }

            println("$$$$$$$$$$$$$$$$$")
            println("$$$$$$$$$$$$$$$$$")
            println("$$$$$$$$$$$$$$$$$ no CJVM encontramos ${allContasJoin.size} contas")
        }





    }

    private fun getData(
        moviList: ArrayList<MovimentacaoMensal>,
        mesList: ArrayList<Mes>,
        contaList: ArrayList<Conta>,
        callback: () -> Unit
    ) {
        getMovimentacoes(moviList) {
            getMeses(mesList) {
                getContas(contaList) {}
            }
            callback()
        }
    }

    private fun getContas(contaList: ArrayList<Conta>, callback: () -> Unit) {
        allContas.observe(owner, Observer {contas ->

            contaList.addAll(contas)
            println("$$$$$$$$$$$$$$$$$ no CJVM encontramos ${contaList.size} contas")
            callback()
        })


    }

    private fun getMeses(mesList: ArrayList<Mes>, callback: () -> Unit) {
        allMeses.observe(owner, Observer {
            mesList.addAll(it)
            println("$$$$$$$$$$$$$$$$$ no CJVM encontramos ${mesList.size} meses")
            callback()
        })

    }

    private fun getMovimentacoes(moviList: ArrayList<MovimentacaoMensal>, callback: () -> Unit) {
        allMovimentacoes.observe(owner, Observer {
            moviList.addAll(it)
            println("$$$$$$$$$$$$$$$$$ no CJVM encontramos ${moviList.size} movimentacoes")
            callback()
        })

    }
}