package com.example.kissmoney.ganhos

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.contas.ContaListAdapter
import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.ganhos.mensal.GanhoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.formataParaBr
import com.example.kissmoney.util.getNomeMesPorExtenso

class GanhoListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<GanhoListAdapter.GanhoViewHolder> () {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var ganhos = emptyList<GanhoJoin>() //cached copy

    //viewmodel
    lateinit var ganhoViewModel: GanhoViewModel
    lateinit var mesViewModel: MesViewModel
    lateinit var ganhoMesViewModel: GanhoMensalViewModel

    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity"

    //setando o mes de trabalho
    lateinit var mesTrabalhado: Mes

    fun setViewModel(
        ganhoViewModel: GanhoViewModel,
        mesViewModel: MesViewModel,
        ganhoMensalViewModel: GanhoMensalViewModel
    ) {
        this.ganhoViewModel = ganhoViewModel
        this.mesViewModel = mesViewModel
        this.ganhoMesViewModel = ganhoMensalViewModel
    }

    inner class GanhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeGanhoTV: TextView = itemView.findViewById(R.id.nomeGanhoTextView)
        val valorTV: TextView = itemView.findViewById(R.id.valor_total_textView)
        val dataRecebimentoTV: TextView = itemView.findViewById(R.id.dataRecebimentoTextView)
        val tipoDeGanhoIV: ImageView = itemView.findViewById(R.id.ganhoIconImageView)

        val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GanhoListAdapter.GanhoViewHolder {

        val itemView = inflater.inflate(R.layout.item_ganhos, parent, false)
        return GanhoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GanhoViewHolder, position: Int) {
        var current = ganhos[position]

        holder.nomeGanhoTV.text = current.nomeGanho
        holder.valorTV.text = formataParaBr(current.valor.toBigDecimal())
        holder.dataRecebimentoTV.text = "${current.dataRecebimento.substring(0,2)} / ${getNomeMesPorExtenso(current.dataRecebimento)}"


        holder.tipoDeGanhoIV.setImageResource(
            if (current.isRendaPassiva) {
                R.drawable.renda_passiva_icon
            } else {
                R.drawable.trabalho_icon
            }
        )
    }

    internal fun setGanhos(ganhos: List<GanhoJoin>) {
        this.ganhos = ganhos
        notifyDataSetChanged()
    }

    internal fun setMesTrabalhado(mes: Mes) {
        this.mesTrabalhado = mes
    }

    override fun getItemCount() = ganhos.size

    fun setLarguraEAlturaInterno(dialog: Dialog, callback: () -> Unit) {
        //var height = getHeight {}
        val width = getWidth {}

        //height = (height*0.6).toInt()

        dialog.window?.setLayout(width,500)
        callback()

    }

    private fun getHeight(callback: () -> Unit): Int {
        val height = Resources.getSystem().getDisplayMetrics().heightPixels
        return height
        callback()
    }

    private fun getWidth(callback: () -> Unit): Int {
        val width: Int = Resources.getSystem().getDisplayMetrics().widthPixels
        return width
        callback()
    }

}