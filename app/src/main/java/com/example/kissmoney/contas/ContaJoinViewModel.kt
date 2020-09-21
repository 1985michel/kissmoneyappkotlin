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

    private lateinit var allContas: LiveData<List<Conta>>
    private lateinit var allMovimentacoes: LiveData<List<MovimentacaoMensal>>
    private lateinit var allMeses: LiveData<List<Mes>>

    var allContasJoin = ArrayList<ContaJoin>()

    var contasJoinDoMes = ArrayList<ContaJoin>()

    lateinit var owner : LifecycleOwner

    var qtdDeMesesComRegistro = 0


    fun setApplicationObject(contaViewModel: ContaViewModel, mesViewModel: MesViewModel, movimentacaoMensalViewModel: MovimentacaoMensalViewModel, callback: () -> Unit) {

        allContas = contaViewModel.allContas
        allMovimentacoes = movimentacaoMensalViewModel.allMovimentacoes
        allMeses = mesViewModel.allMeses
        callback()
    }

    fun setMyOwner(owner: LifecycleOwner, callback: () -> Unit){
        this.owner = owner
        callback()
    }

    fun setAllContasJoin(callback: () -> Unit){

        var moviList = ArrayList<MovimentacaoMensal>()
        var contaList = ArrayList<Conta>()
        var mesList = ArrayList<Mes>()


        var mesesComRegistros = ArrayList<Long>()

        getData(moviList, mesList, contaList){

            allContasJoin.clear()

            var mesW = Mes(0L, "")
            var contaW = Conta(0L, "", "", false)

            for (movi in moviList){
                for (mes in mesList) {
                    if(mes.mesId == movi.mesId) {
                        mesW = mes
                        mesesComRegistros.add(mes.mesId)
                    }
                }
                for (conta in contaList){
                    if (conta.contaId == movi.contaId) contaW = conta
                }
                allContasJoin.add(ContaJoin(contaW,movi,mesW))
            }

            var mesesFiltrado = ArrayList<Long>()
            for (id in mesesComRegistros) {
                if (!mesesFiltrado.contains(id)) mesesFiltrado.add(id)
            }

            qtdDeMesesComRegistro = mesesFiltrado.size

            callback()
        }
    }

    fun setContasJoinNoMes(mesId: Long, callback: () -> Unit){

        var moviList = ArrayList<MovimentacaoMensal>()
        var contaList = ArrayList<Conta>()
        var mesList = ArrayList<Mes>()

        getData(moviList, mesList, contaList){

            //allContasJoin.clear()
            contasJoinDoMes.clear()


            var mesW = Mes(0L, "")
            var contaW = Conta(0L, "", "", false)

            for (movi in moviList){
                for (mes in mesList) {
                    if(mes.mesId == movi.mesId) mesW = mes
                }
                for (conta in contaList){
                    if (conta.contaId == movi.contaId) contaW = conta
                }
                if (movi.mesId == mesId) contasJoinDoMes.add(ContaJoin(contaW,movi,mesW))
            }

            callback()
        }
    }

    private fun getData(
        moviList: ArrayList<MovimentacaoMensal>,
        mesList: ArrayList<Mes>,
        contaList: ArrayList<Conta>,
        callback: () -> Unit
    ) {
        moviList.clear()
        mesList.clear()
        contaList.clear()
        getMovimentacoes(moviList) {
            getMeses(mesList) {
                getContas(contaList) {
                    callback()
                }
            }
        }
    }

    private fun getContas(contaList: ArrayList<Conta>, callback: () -> Unit) {
        allContas.observe(owner, Observer {contas ->
            contaList.clear()
            contaList.addAll(contas)
            callback()
        })
    }

    private fun getMeses(mesList: ArrayList<Mes>, callback: () -> Unit) {
        allMeses.observe(owner, Observer {
            mesList.clear()
            mesList.addAll(it)
            callback()
        })
    }

    private fun getMovimentacoes(moviList: ArrayList<MovimentacaoMensal>, callback: () -> Unit) {
        moviList.clear()
        allMovimentacoes.observe(owner, Observer {
            moviList.clear()
            moviList.addAll(it)
            callback()
        })
    }
}