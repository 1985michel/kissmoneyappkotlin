package com.example.kissmoney.meta

import android.media.browse.MediaBrowser
import com.example.kissmoney.mes.CentralEstatistica
import com.example.kissmoney.mes.Estatisticas
import com.example.kissmoney.mes.Mes

object AcompanhamentoDeMeta{

    lateinit var metaJoinList: ArrayList<MetaJoin>
    lateinit var mesAtual: Mes

    init {
        metaJoinList = getMetasList()
    }

    var abastanca = 0

    fun setMyMesAtual(mes: Mes) {
        mesAtual = mes
    }

    fun setValues(callback: () -> Unit) {

        var estatisticaMesAtual = CentralEstatistica.estatisticasMensais.get(mesAtual.mesId)

        for (mj in metaJoinList) {
            if (mj.meta.nomeMeta.equals("Abastança")){
                var abastancaAtual = estatisticaMesAtual!!.abastanca
                println(">>>>>>>>>>>> abastanca atual: ${abastancaAtual}")
                abastanca = abastancaAtual
                mj.valorAtual = abastancaAtual.toDouble()
                mj.valorAtualPercentual = ((100 * abastancaAtual) / mj.meta.valor).toInt()
            }

            if (mj.meta.nomeMeta.equals("Patrimônio Líquido")) {
                var patrimonioLiquidoAtual = estatisticaMesAtual!!.totalEmCaixa
                mj.valorAtual = patrimonioLiquidoAtual
                mj.valorAtualPercentual = ((100 * patrimonioLiquidoAtual) / mj.meta.valor).toInt()
            }
        }
        println(">>>>>>>>>>>>>>>>>>>>> ESTAMOS RETORNANDO ABASTANCA ${abastanca}")
        callback()
    }





    fun getMetasList(): ArrayList<MetaJoin> {
        var metasList = ArrayList<MetaJoin>()
        var metaAbastanca = Meta(0L, "Abastança", 180.0, false, "", "")
        var metaPatrimonioLiquido = Meta(0L, "Patrimônio Líquido", 100000.0, false, "", "")
        metasList.add(MetaJoin(metaAbastanca, 0.0, 0, "dias"))
        metasList.add(MetaJoin(metaPatrimonioLiquido, 0.0, 0, "Reais"))

        return metasList
    }

}