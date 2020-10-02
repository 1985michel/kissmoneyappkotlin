package com.example.kissmoney.ganhos

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.compromissos.CompromissoJoin
import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.ganhos.mensal.GanhoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GanhoListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<GanhoListAdapter.GanhoViewHolder>() {

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
        val isRecorrenteIV: ImageView = itemView.findViewById(R.id.imageView9)
        val atencaoImageView: ImageView = itemView.findViewById(R.id.atencaoImageViewGanho)

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
                R.drawable.renda_passiva_icon_2
            } else {
                R.drawable.trabalho_icon
            }
        )

        holder.isRecorrenteIV.visibility = if (current.isGanhoRegular) View.VISIBLE else View.INVISIBLE

        var statusVencimento = verificaStatusVencimento(current.dataRecebimento)
        if (!current.isRecebido) {
            println("${current.nomeGanho} recebido: ${current.isRecebido} <<<<<<<<<<<<<<")
            if (statusVencimento <= 0) {
                holder.atencaoImageView.visibility = View.VISIBLE
            } else if (statusVencimento > 0) {
                holder.atencaoImageView.visibility = View.INVISIBLE
            }
        } else {
            holder.atencaoImageView.visibility = View.INVISIBLE
        }


        holder.constraint.setOnClickListener {

//            val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
//            dialog.setContentView(R.layout.view_ganho_button_sheet)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            //dialog.getWindow()?.setDimAmount(0F);
//            dialog.setCancelable(false)

            var dialog = BottomSheetDialog(holder.itemView.context, R.style.BottomSheetDialog)
            dialog.setContentView(R.layout.view_ganho_button_sheet)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.getWindow()?.setDimAmount(0F);
            dialog.setCancelable(false)

            var nomeET = dialog.findViewById<TextView>(R.id.nomeGanhoBottonEditText)
            var valorET = dialog.findViewById<TextView>(R.id.editTextTextPersonName)
            var isRendaPassivaSwitch = dialog.findViewById<SwitchCompat>(R.id.is_renda_passiva_switch)
            var isGanhoRegularSwitch = dialog.findViewById<SwitchCompat>(R.id.is_ganho_regular_switch)
            var isRecebidoSwitch = dialog.findViewById<SwitchCompat>(R.id.is_recebido_switch)
            var dataRecebimentoTV = dialog.findViewById<TextView>(R.id.data_recebimento_tv)
            var observacaoET = dialog.findViewById<TextView>(R.id.observacoesEditText)

            var tipoContaIV = dialog.findViewById<ImageView>(R.id.imageView4)

            dataRecebimentoTV?.text = current.dataRecebimento
            nomeET?.text = current.nomeGanho
            valorET?.text = formataParaBr(current.valor.toBigDecimal())
            isRendaPassivaSwitch?.isChecked = current.isRendaPassiva
            isGanhoRegularSwitch?.isChecked = current.isGanhoRegular
            isRecebidoSwitch?.isChecked = current.isRecebido
            observacaoET?.text = current.observacao

            if (current.isRendaPassiva) {
                tipoContaIV?.setImageResource(R.drawable.renda_passiva_icon_2)
            } else {
                tipoContaIV?.setImageResource(R.drawable.trabalho_icon_3)
            }

            var cancelBtn = dialog.findViewById<Button>(R.id.cancelarBtn)
            val deleteBtn = dialog.findViewById<ImageView>(R.id.deleteContaImageView2)
            val editBtn = dialog.findViewById<ImageView>(R.id.editContaImageView2)

            deleteBtn?.setOnClickListener {

                val dialogIterno = Dialog(holder.itemView.context as AppCompatActivity)
                dialogIterno.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogIterno.setCancelable(false)
                dialogIterno.setContentView(R.layout.delete_dialog)
                dialogIterno.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                var msg = dialogIterno.findViewById(R.id.msgTextView) as TextView
                msg.setText(
                    Html.fromHtml("Você confirma que deseja apagar <b>${current.nomeGanho.toUpperCase()}</b> neste mês?")
                )

                var confirmaBtn = dialogIterno.findViewById(R.id.apagarButton) as Button

                confirmaBtn.setOnClickListener {
//                    ganhoViewModel.delete(
//                        current.getGanho()
//                    )

                    ganhoMesViewModel.delete(
                        current.getGanhoMensal()
                    )

                    val toast = Toast.makeText(
                        holder.itemView.context as AppCompatActivity,
                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "Conta ${current.nomeGanho} apagada com sucesso!" + "</b></font>"),
                        Toast.LENGTH_LONG
                    )

                    //colocando o toast verde
                    toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))

                    toast.show()
                    dialogIterno.dismiss()
                    dialog.dismiss()
                }

                var cancelarBtn = dialogIterno.findViewById(R.id.cancelarButton) as Button

                cancelarBtn.setOnClickListener { dialogIterno.dismiss() }

                dialogIterno.show()
                setLarguraEAlturaInterno(dialogIterno){}
            }

            editBtn?.setOnClickListener {

                //fecho o dialog de exibição
                dialog.dismiss()

//                val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
//                //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
//                dialog.setContentView(R.layout.crud_ganho_button_sheet)
//                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                //dialog.getWindow()?.setDimAmount(0F);
//                dialog.setCancelable(false)

                val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
                //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
                dialog.setContentView(R.layout.crud_ganho_button_sheet)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.getWindow()?.setDimAmount(0F);
                dialog.setCancelable(false)

                var nomeET = dialog.findViewById<EditText>(R.id.nomeGanhoBottonEditText)
                var valorET = dialog.findViewById<EditText>(R.id.editTextTextPersonName)
                var isRendaPassivaSwitch = dialog.findViewById<SwitchCompat>(R.id.is_renda_passiva_switch)
                var isGanhoRegularSwitch = dialog.findViewById<SwitchCompat>(R.id.is_ganho_regular_switch)
                var isRecebidoSwitch = dialog.findViewById<SwitchCompat>(R.id.is_recebido_switch)
                var dataRecebimentoTV = dialog.findViewById<TextView>(R.id.data_recebimento_tv)
                var observacaoET = dialog.findViewById<EditText>(R.id.observacoesEditText)

                var tipoContaIV = dialog.findViewById<ImageView>(R.id.imageView4)

                dataRecebimentoTV?.text = current.dataRecebimento
                nomeET?.setText(current.nomeGanho)
                valorET?.setText(formataParaBr(current.valor.toBigDecimal()))
                isRendaPassivaSwitch?.isChecked = current.isRendaPassiva
                isGanhoRegularSwitch?.isChecked = current.isGanhoRegular
                isRecebidoSwitch?.isChecked = current.isRecebido
                observacaoET?.setText(current.observacao)

                if (current.isRendaPassiva) {
                    tipoContaIV?.setImageResource(R.drawable.renda_passiva_icon_2)
                } else {
                    tipoContaIV?.setImageResource(R.drawable.trabalho_icon_3)
                }

                valorET?.addTextChangedListener(
                    MoneyTextWatcher(
                        valorET,
                        Locale("pt", "BR")
                    )
                )

                var salvarBtn = dialog.findViewById<Button>(R.id.salver_btn)
                var cancelBtn = dialog.findViewById<Button>(R.id.cancelarBtn)

                isRendaPassivaSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        tipoContaIV?.setImageResource(R.drawable.renda_passiva_icon_2)
                    } else {
                        tipoContaIV?.setImageResource(R.drawable.trabalho_icon_3)
                    }
                }

                //apresentando o datapicker calendar
                dataRecebimentoTV?.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val year = cal[Calendar.YEAR]
                    val month = cal[Calendar.MONTH]
                    val day = cal[Calendar.DAY_OF_MONTH]
                    val dialog = DatePickerDialog(
                        holder.itemView.context as AppCompatActivity,
                        mDateSetListener,
                        year, month, day
                    )
                    dialog.show()
                }
                mDateSetListener =
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                        var month = month
                        month = month + 1
                        Log.d(
                            TAG,
                            "onDateSet: mm/dd/yyy: $month/$day/$year"
                        )

                        var mes = if (month < 10) "0" + month.toString() else month
                        var dia = if (day < 10) "0" + day.toString() else day

                        //val date = "$month/$day/$year"
                        val date = "$dia/$mes/$year"

                        dataRecebimentoTV?.text = date
                    }


                salvarBtn?.setOnClickListener {

                    if (nomeET?.text.isNullOrEmpty()) {
                        val toast = Toast.makeText(
                            holder.itemView.context as AppCompatActivity,
                            Html.fromHtml("<font color='#e3f2fd' ><b>" + "Obrigatório informar um nome!" + "</b></font>"),
                            Toast.LENGTH_LONG
                        )
                        //colocando o toast vermelho
                        toast.view?.setBackgroundColor(Color.parseColor("#FF1A1A"))

                        toast.show()
                    } else {

                        var nome = nomeET?.text.toString()
                        var valor = limpaFormatacaoDeMoeda(valorET?.text.toString()).trim()
                            .toDouble()
                        var isRendaPassiva = isRendaPassivaSwitch?.isChecked
                        var isGanhoRegular = isGanhoRegularSwitch?.isChecked
                        var isRecebido = isRecebidoSwitch?.isChecked
                        var dataRecebimento = dataRecebimentoTV?.text.toString()
                        var observacoes = observacaoET?.text.toString()

                        var ganho = Ganho(current.ganhoId, nome, isRendaPassiva!!, isGanhoRegular!! )
                        var ganhoMensal = GanhoMensal(current.ganhoMensalId, current.mesId, ganho.ganhoId, valor, dataRecebimento, isRecebido!!, observacoes)

                        GanhoManager.updateGanhoComGanhoMensal(ganho, ganhoMensal,ganhoViewModel, ganhoMesViewModel, mesViewModel) {
                            GlobalScope.launch {
                                //Background processing..."
                                withContext(Dispatchers.Main) {
                                    //"Update UI here!")
                                    GanhoJoinViewModel.setGanhosJoinNoMes(mesTrabalhado.mesId){
                                        setGanhos(GanhoJoinViewModel.ganhosJoinDoMes)

                                        val toast = Toast.makeText(
                                            holder.itemView.context as AppCompatActivity,
                                            Html.fromHtml("<font color='#e3f2fd' ><b>" + "${nomeET?.text.toString()} atualizado com sucesso!" + "</b></font>"),
                                            Toast.LENGTH_LONG
                                        )

                                        //colocando o toast verde
                                        toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))
                                        toast.show()
                                    }
                                }
                            }
                        }
                        dialog.dismiss()
                    }
                }
                cancelBtn?.setOnClickListener { dialog.dismiss() }
                dialog.show()
            }
            cancelBtn?.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    internal fun setGanhos(ganhos: List<GanhoJoin>) {
        this.ganhos = ganhos.sortedWith(compareBy<GanhoJoin>{it.isRecebido}.thenBy { it.dataRecebimento }.thenByDescending { it.valor })
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

        dialog.window?.setLayout(width, 500)
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