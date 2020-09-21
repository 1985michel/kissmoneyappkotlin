package com.example.kissmoney.mes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.meta.MetaJoin
import com.example.kissmoney.util.formataParaBr

class MesListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<MesListAdapter.MesViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dados = emptyList<MetaJoin>() //trocar depois

    inner class MesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTV: TextView = itemView.findViewById(R.id.textView23)
        val resultadoEmDiasTV: TextView = itemView.findViewById(R.id.qtd_dias_tv)
        val metaTV: TextView = itemView.findViewById(R.id.textView19)
        val resultadoPercentual: TextView = itemView.findViewById(R.id.textView21)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MesListAdapter.MesViewHolder {
        val itemView = inflater.inflate(R.layout.itemmes, parent, false)
        return MesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MesViewHolder, position: Int) {
        var current = dados[position]

        holder.nomeTV.text = current.meta.nomeMeta
        if (current.variavel.equals("dias")) {
            holder.metaTV.text = "Meta: ${current.meta.valor.toInt()} Dias"
            holder.resultadoEmDiasTV.text = "${current.valorAtual.toInt()} Dias"
        } else {
            holder.metaTV.text = "Meta: ${formataParaBr(current.meta.valor.toBigDecimal())}"
            holder.resultadoEmDiasTV.text = "${formataParaBr(current.valorAtual.toBigDecimal())}"
        }

        holder.resultadoPercentual.text = "${current.valorAtualPercentual} %"
    }

    internal fun setDados(dados: List<MetaJoin>) {
        this.dados = dados
    }

    override fun getItemCount() = dados.size
}