package com.example.kissmoney.mes

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kissmoney.meta.MetaViewModel

object CentralEstatistica {

    lateinit var estatisticasMensais: HashMap<Long, Estatisticas>

    private lateinit var mesViewModel: MesViewModel
    private lateinit var metaViewModel: MetaViewModel

    init {
        estatisticasMensais = HashMap<Long, Estatisticas>()
    }

    

    fun addMes(mes: Mes, callback: () -> Unit) {

        if (!estatisticasMensais.containsKey(mes.mesId)) {
            var estat = Estatisticas(mes)
            estat.processa(){
                estatisticasMensais.put(mes.mesId,estat)
                println("______________________________ adicionei uma estatistica com o mesID ${mes.mesId}")
                callback()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun processa(callback: () -> Unit) {
        estatisticasMensais.forEach { mesId, estatisticas ->
            estatisticas.processa(){
                callback()
            }
        }
    }


}