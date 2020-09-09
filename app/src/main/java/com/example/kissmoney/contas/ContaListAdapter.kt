package com.example.kissmoney.contas

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.util.formataParaBr

class ContaListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<ContaListAdapter.ContaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contas = emptyList<ContaJoin>() //cached copy


    inner class ContaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeContaTextView: TextView = itemView.findViewById(R.id.nomeContaTextView)
        val valorAtualTextView: TextView = itemView.findViewById(R.id.valor_total_textView)
        val dataAtualizacaoTextView: TextView = itemView.findViewById(R.id.dataAtualizacaoTextView)
        val contaIconImageView: ImageView = itemView.findViewById(R.id.contaIconImageView)
        val atencaoImageView: ImageView = itemView.findViewById(R.id.atencaoImageView)

        val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContaListAdapter.ContaViewHolder {

        val itemView = inflater.inflate(R.layout.item_contas, parent, false)

        return ContaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContaListAdapter.ContaViewHolder, position: Int) {
        var current = contas[position]
        holder.nomeContaTextView.text = current.nomeConta
        holder.valorAtualTextView.text = formataParaBr(current.saldoAtualOuFinal.toBigDecimal())
        holder.dataAtualizacaoTextView.text = current.dataAtualizacao


        holder.contaIconImageView.setImageResource(
            if (current.tipoConta.equals(TiposDeConta.CARTEIRA.tipo)){
                R.drawable.cofre_icon_list
            } else if (current.tipoConta.equals(TiposDeConta.INVESTIMENTO.tipo)){
                R.drawable.invest_icon_list
            } else {
                R.drawable.creditcard_icon_list
            }
        )

    }

    internal  fun setContas(contas: List<ContaJoin>){
        this.contas = contas
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> foram recebidas ${contas.size} CONTAS" )
        notifyDataSetChanged()
    }

    override fun getItemCount() = contas.size

}