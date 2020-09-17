package com.example.kissmoney.ganhos

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.ganhos.mensal.GanhoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel

object GanhoJoinViewModel {

    private lateinit var allGanhos: LiveData<List<Ganho>>
    private lateinit var allGanhosMensais: LiveData<List<GanhoMensal>>
    private lateinit var allMeses: LiveData<List<Mes>>

    var allGanhosJoin = ArrayList<GanhoJoin>()

    var ganhosJoinDoMes = ArrayList<GanhoJoin>()

    lateinit var owner : LifecycleOwner

    fun setApplicationObject(ganhoViewModel: GanhoViewModel, ganhoMensalViewModel: GanhoMensalViewModel, mesViewModel: MesViewModel, callback: () -> Unit) {
        allGanhos = ganhoViewModel.allGanhos
        allGanhosMensais = ganhoMensalViewModel.allGanhosMensais
        allMeses = mesViewModel.allMeses
        callback()
    }

    fun setMyOwner(owner: LifecycleOwner, callback: () -> Unit){
        this.owner = owner
        callback()
    }

    fun setGanhosJoinNoMes(mesId: Long, callback: () -> Unit) {

        var ganhoMensalList = ArrayList<GanhoMensal>()
        var ganhoList = ArrayList<Ganho>()
        var mesList = ArrayList<Mes>()

        getData(ganhoList, mesList, ganhoMensalList) {

            ganhosJoinDoMes.clear()

            var mesW = Mes(0L, "")
            var ganhoW = Ganho(0L, "", false, false)

            for (ganhoMensal in ganhoMensalList) {
                for (mes in mesList) {
                    if (mes.mesId == ganhoMensal.mesId) mesW = mes
                }
                for (ganho in ganhoList) {
                    if (ganho.ganhoId == ganhoMensal.ganhoId) ganhoW = ganho
                }
                if (ganhoMensal.mesId == mesId) ganhosJoinDoMes.add(GanhoJoin(ganhoW, ganhoMensal, mesW))
            }
        }
    }

    private fun getData(
        ganhoList: ArrayList<Ganho>,
        mesList: ArrayList<Mes>,
        ganhoMensalList: ArrayList<GanhoMensal>,
        callback: () -> Unit
    ){
        getGanhosMensais(ganhoMensalList){
            getMeses(mesList){
                getGanhos(ganhoList){
                    callback()
                }
            }
        }
    }


    private fun getGanhos(ganhoList: ArrayList<Ganho>, callback: () -> Unit) {
        allGanhos.observe(owner, Observer {ganhos ->
            ganhoList.clear()
            ganhoList.addAll(ganhos)
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

    private fun getGanhosMensais(ganhoMensalList: ArrayList<GanhoMensal>, callback: () -> Unit){
        allGanhosMensais.observe(owner, Observer {
            ganhoMensalList.clear()
            ganhoMensalList.addAll(it)
            callback()
        })
    }

}