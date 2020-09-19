package com.example.kissmoney.compromissos

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
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.compromissos.mensal.CompromissoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.formataParaBr
import com.example.kissmoney.util.getNomeMesPorExtenso

class CompromissoListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<CompromissoListAdapter.CompromissoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var compromissos = emptyList<CompromissoJoin>() //cached copy

    //viewModels
    lateinit var compromissoViewModel: CompromissoViewModel
    lateinit var compromissoMensalViewModel: CompromissoMensalViewModel
    lateinit var mesViewModel: MesViewModel

    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity"

    //setando o mes de trabalho
    lateinit var mesTrabalhado: Mes

    fun setViewModel(
        compromissoViewModel: CompromissoViewModel,
        compromissoMensalViewModel: CompromissoMensalViewModel,
        mesViewModel: MesViewModel
    ) {
        this.compromissoMensalViewModel = compromissoMensalViewModel
        this.compromissoViewModel = compromissoViewModel
        this.mesViewModel = mesViewModel
    }

    inner class CompromissoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeCompromisso: TextView = itemView.findViewById(R.id.nomeCompromissoTextView)
        val dataVencimento: TextView = itemView.findViewById(R.id.dataVencimentoTextView)
        val valor: TextView = itemView.findViewById(R.id.valorCompromisso)
        val atencaoImageView: ImageView = itemView.findViewById(R.id.atencaoImageView)
        val isRecorrenteImageView: ImageView = itemView.findViewById(R.id.isRecorrenteImageView)

        val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompromissoListAdapter.CompromissoViewHolder {

        val itemView = inflater.inflate(R.layout.item_compromisso, parent, false)
        return CompromissoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompromissoViewHolder, position: Int) {

        var current = compromissos[position]

        holder.nomeCompromisso.text = current.nomeCompromisso
        holder.valor.text = formataParaBr(current.valor.toBigDecimal())
        holder.dataVencimento.text = "${current.dataVencimento.substring(
            0,
            2
        )} / ${getNomeMesPorExtenso(current.dataVencimento)}"

        holder.isRecorrenteImageView.setImageResource(
            if (current.isRecorrente) R.drawable.despesa_recorrente_2
            else
                R.color.lightgreybackground
        )
    }

    internal fun setCompromissos(compromissos: List<CompromissoJoin>) {
        this.compromissos = compromissos
        notifyDataSetChanged()
    }

    internal fun setMesTrabalhado(mes: Mes) {
        this.mesTrabalhado = mes
    }

    override fun getItemCount() = compromissos.size

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