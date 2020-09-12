package com.example.kissmoney.contas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLink.Builder.fromAction
import androidx.navigation.NavDeepLinkRequest.Builder.fromAction
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ContaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContaRepository

    val allContas: LiveData<List<Conta>>

    init {
        val contaDao = KissmoneyDatabase.getDatabase(application, viewModelScope).contaDao()
        repository = ContaRepository(contaDao)
        allContas = repository.allContas
    }

    fun insert(conta: Conta) = viewModelScope.launch(Dispatchers.IO) {
        var id = repository.insert(conta)
        println(">>>>>>>> EM CONTA view model ID GERADO: $id")
        conta.contaId = id
    }

    fun insertMonitorado(conta: Conta, callback: () -> Unit) {

        var id = repository.insertMonitorado(conta)
        println(">>>>>>>> EM CONTA view model ID GERADO: $id")
        conta.contaId = id
        callback()
    }


    fun update(conta: Conta) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(conta)
    }

    fun updateMonitorado(conta: Conta, callback: () -> Unit) {
        repository.updateMonitorado(conta)
        callback()
    }

    fun delete(conta: Conta) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(conta)
    }

    fun getById(conta: Conta) = viewModelScope.launch(Dispatchers.IO) {
        var contaEncontrada = repository.getById(conta.contaId)
        if (contaEncontrada is Conta) {
            conta.nomeConta = contaEncontrada.nomeConta
            conta.tipoConta = contaEncontrada.tipoConta
            conta.isEncerrada = contaEncontrada.isEncerrada
        }
    }
}