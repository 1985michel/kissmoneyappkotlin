package com.example.kissmoney.mes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.util.formataParaBr
import com.example.kissmoney.util.getNomeMesPorExtensoComAno

class BalancoListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<BalancoListAdapter.BalancoViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var dados = emptyList<Balanco>()

    inner class BalancoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeMesTV : TextView = itemView.findViewById(R.id.nomeMesItemTextView)
        val valorBalanco : TextView = itemView.findViewById(R.id.valorBanlancoTV)
        val variacaoDias : TextView = itemView.findViewById(R.id.variacaoDiasMesTV)
        val imgVariacaoDias : ImageView = itemView.findViewById(R.id.imageView10)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BalancoListAdapter.BalancoViewHolder {
        val itemView = inflater.inflate(R.layout.item_balanco, parent, false)
        return BalancoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BalancoViewHolder, position: Int) {
        var current = dados[position]

        holder.nomeMesTV.text = getNomeMesPorExtensoComAno(current.nomeMes)
        holder.valorBalanco.text = formataParaBr(current.valor.toBigDecimal())
        holder.variacaoDias.text = current.variacaoDias.toString()
        holder.imgVariacaoDias.setImageResource(
            if (current.variacaoDias > 0) {
                R.drawable.greenrow4
            } else if (current.variacaoDias < 0) {
                R.drawable.redrow4
            } else {
                R.drawable.round_colorless
            }
        )
    }

    internal fun setDados (dados: List<Balanco>) {
        this.dados = dados
    }

    override fun getItemCount() = dados.size

}