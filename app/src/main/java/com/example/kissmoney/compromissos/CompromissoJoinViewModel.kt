package com.example.kissmoney.compromissos

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.compromissos.mensal.CompromissoMensalViewModel
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.avancaUmMesNaData

object CompromissoJoinViewModel {

    private lateinit var allCompromissos: LiveData<List<Compromisso>>
    private lateinit var allCompromissosMensais: LiveData<List<CompromissoMensal>>
    private lateinit var allMeses: LiveData<List<Mes>>

    private lateinit var compromissoMensalViewModel: CompromissoMensalViewModel
    private lateinit var mesViewModel: MesViewModel

    var compromissosJoinDoMes = ArrayList<CompromissoJoin>()

    lateinit var owner: LifecycleOwner

    fun setApplicationObject(
        compromissoViewModel: CompromissoViewModel,
        compromissoMensalViewModel: CompromissoMensalViewModel,
        mesViewModel: MesViewModel,
        callback: () -> Unit
    ) {

        this.compromissoMensalViewModel = compromissoMensalViewModel
        this.mesViewModel = mesViewModel

        allCompromissos = compromissoViewModel.allCompromissos
        allCompromissosMensais = compromissoMensalViewModel.allCompromissosMensais
        allMeses = mesViewModel.allMeses
        callback()
    }

    fun setMyOwner(owner: LifecycleOwner, callback: () -> Unit) {
        this.owner = owner
        callback()
    }

    fun setCompromissosJoinNoMes(mesId: Long, callback: () -> Unit) {

        var compromissoList = ArrayList<Compromisso>()
        var compromissoMensalList = ArrayList<CompromissoMensal>()
        var mesList = ArrayList<Mes>()

        getData(compromissoList, compromissoMensalList, mesList) {
            compromissosJoinDoMes.clear()

            var mesW = Mes(0L, "")
            var compromissoW = Compromisso(0L, "")

            for (cm in compromissoMensalList) {
                for (mes in mesList) {
                    if (mes.mesId == cm.mesId) mesW = mes
                }
                for (compromisso in compromissoList) {
                    if (compromisso.compromissoId == cm.compromissoId) compromissoW = compromisso
                }
                if (cm.mesId == mesId) compromissosJoinDoMes.add(
                    CompromissoJoin(
                        compromissoW,
                        cm,
                        mesW
                    )
                )
            }
            callback()
        }
    }

    private fun getData(
        compromissoList: ArrayList<Compromisso>,
        compromissoMensalList: ArrayList<CompromissoMensal>,
        mesList: ArrayList<Mes>,
        callback: () -> Unit
    ) {
        compromissoList.clear()
        compromissoMensalList.clear()
        mesList.clear()
        getCompromissosMensais(compromissoMensalList) {
            getMeses(mesList) {
                getCompromissos(compromissoList) {
                    callback()
                }
            }
        }
    }

    private fun getCompromissos(compromissoList: ArrayList<Compromisso>, callback: () -> Unit) {
        allCompromissos.observe(owner, Observer {
            compromissoList.clear()
            compromissoList.addAll(it)
            callback()
        })
    }

    private fun getMeses(mesList: ArrayList<Mes>, callback: () -> Unit) {
        allMeses.observe(ContaJoinViewModel.owner, Observer {
            mesList.clear()
            mesList.addAll(it)
            callback()
        })
    }

    private fun getCompromissosMensais(
        compromissoMensalList: ArrayList<CompromissoMensal>,
        callback: () -> Unit
    ) {
        allCompromissosMensais.observe(owner, Observer {
            compromissoMensalList.clear()
            compromissoMensalList.addAll(it)
            callback()
        })
    }


}