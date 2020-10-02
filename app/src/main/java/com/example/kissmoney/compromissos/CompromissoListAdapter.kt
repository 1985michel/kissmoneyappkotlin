package com.example.kissmoney.compromissos

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
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
import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.compromissos.mensal.CompromissoMensalViewModel
import com.example.kissmoney.ganhos.Ganho
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.ganhos.GanhoManager
import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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
        val atencaoImageView: ImageView = itemView.findViewById(R.id.atencaoImageViewGanho)
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

        var corFontPago = Color.LTGRAY
        if (current.isPago) {
//            holder.nomeCompromisso.setTextColor(corFontPago)
//            holder.valor.setTextColor(corFontPago)
//            holder.isRecorrenteImageView.alpha = 0.4F
//            holder.dataVencimento.alpha = 0.4F
            holder.constraint.alpha = 0.3F
        } else {
            holder.constraint.alpha = 1F
        }



        var statusVencimento = verificaStatusVencimento(current.dataVencimento)
        if (!current.isPago) {
            if (statusVencimento <= 0) {
                holder.atencaoImageView.visibility = View.VISIBLE
            } else if (statusVencimento > 0) {
                holder.atencaoImageView.visibility = View.INVISIBLE
            }
        } else {

        }




        holder.constraint.setOnClickListener {

//            val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
//            dialog.setContentView(R.layout.view_compromisso_botton_sheet)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            //dialog.getWindow()?.setDimAmount(0F);
//            dialog.setCancelable(false)

            var dialog = BottomSheetDialog(holder.itemView.context, R.style.BottomSheetDialog)
            dialog.setContentView(R.layout.view_compromisso_botton_sheet)
//            dialog.setContentView(view)
            //dialog.setContentView(R.layout.crud_conta_botton_sheet)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.getWindow()?.setDimAmount(0F);
            dialog.setCancelable(false)

            var nomeET = dialog.findViewById<TextView>(R.id.editTextTextPersonName2)
            var valorET = dialog.findViewById<TextView>(R.id.editTextTextPersonName3)
            var isPago = dialog.findViewById<SwitchCompat>(R.id.switch1)
            var isRecorrente = dialog.findViewById<SwitchCompat>(R.id.switch2)
            var dataVencimentoTV = dialog.findViewById<TextView>(R.id.textView18)
            var imgRecorrente = dialog.findViewById<ImageView>(R.id.imageView7)

            if (current.isRecorrente) {
                imgRecorrente?.setImageResource(R.drawable.update_dark)
            } else {
                imgRecorrente?.setImageResource(R.color.darkblue_background)
            }


            dataVencimentoTV?.text = current.dataVencimento
            nomeET?.text = current.nomeCompromisso
            valorET?.text = formataParaBr(current.valor.toBigDecimal())
            isPago?.isChecked = current.isPago
            isRecorrente?.isChecked = current.isRecorrente

            var cancelBtn = dialog.findViewById<Button>(R.id.cancelarBtn2)
            val deleteBtn = dialog.findViewById<ImageView>(R.id.deleteContaImageView3)
            val editBtn = dialog.findViewById<ImageView>(R.id.editContaImageView3)

            deleteBtn?.setOnClickListener {

                val dialogIterno = Dialog(holder.itemView.context as AppCompatActivity)
                dialogIterno.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogIterno.setCancelable(false)
                dialogIterno.setContentView(R.layout.delete_dialog)
                dialogIterno.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                var msg = dialogIterno.findViewById(R.id.msgTextView) as TextView
                msg.setText(
                    Html.fromHtml("Você confirma que deseja apagar <b>${current.nomeCompromisso.toUpperCase()}</b> neste mês?")
                )

                var confirmaBtn = dialogIterno.findViewById(R.id.apagarButton) as Button

                confirmaBtn.setOnClickListener {
//                    compromissoViewModel.delete(
//                        current.getCompromisso()
//                    )
                    compromissoMensalViewModel.delete(
                        current.getCompromissoMensal()
                    )

                    val toast = Toast.makeText(
                        holder.itemView.context as AppCompatActivity,
                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "Conta ${current.nomeCompromisso} apagada com sucesso!" + "</b></font>"),
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
//                dialog.setContentView(R.layout.crud_compromisso_botton_sheet)
//                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                //dialog.getWindow()?.setDimAmount(0F);
//                dialog.setCancelable(false)

                val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
                //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
                dialog.setContentView(R.layout.crud_compromisso_botton_sheet)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.getWindow()?.setDimAmount(0F);
                dialog.setCancelable(false)

                var nomeET = dialog.findViewById<EditText>(R.id.editTextTextPersonName2)
                var valorET = dialog.findViewById<EditText>(R.id.editTextTextPersonName3)
                var dataVencimentoTV = dialog.findViewById<TextView>(R.id.textView18)
                var isRecorrenteSwitch = dialog.findViewById<SwitchCompat>(R.id.switch2)
                var isPagoSwitch = dialog.findViewById<SwitchCompat>(R.id.switch1)
                var imgRecorrente = dialog.findViewById<ImageView>(R.id.imageView6)

                dataVencimentoTV?.text = getDataHojeString()

                valorET?.setText("R$ 0,00")
                valorET?.addTextChangedListener(
                    MoneyTextWatcher(
                        valorET,
                        Locale("pt", "BR")
                    )
                )



                nomeET?.setText(current.nomeCompromisso)
                valorET?.setText(formataParaBr(current.valor.toBigDecimal()))
                dataVencimentoTV?.setText(current.dataVencimento)
                isRecorrenteSwitch?.isChecked = current.isRecorrente
                isPagoSwitch?.isChecked = current.isPago

                if (current.isRecorrente) {
                    imgRecorrente?.setImageResource(R.drawable.update_dark)
                }

                isRecorrenteSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        imgRecorrente?.setImageResource(R.drawable.update_dark)
                    } else {
                        imgRecorrente?.setImageResource(R.color.darkblue_background)
                    }
                }



                val saveBtn = dialog.findViewById<Button>(R.id.salver_btn2)
                val cancelBtn = dialog.findViewById<Button>(R.id.cancelarBtn2)

                //apresentando o datapicker calendar
                dataVencimentoTV?.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val year = cal[Calendar.YEAR]
                    val month = cal[Calendar.MONTH]
                    val day = cal[Calendar.DAY_OF_MONTH]
                    val dialog = DatePickerDialog(
                        holder.itemView.context as AppCompatActivity,
                        mDateSetListener,
                        year, month, day
                    )
                    //dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

                        dataVencimentoTV?.text = date
                    }

                saveBtn?.setOnClickListener {

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
                        var isRecorrente = isRecorrenteSwitch?.isChecked
                        var isPago = isPagoSwitch?.isChecked
                        var dataVencimento = dataVencimentoTV?.text.toString()

                        var compromisso = Compromisso(current.compromissoId, nome, isRecorrente!!)
                        var compromissoMensal =
                            CompromissoMensal(current.compromissoMensalId, current.mesId, current.compromissoId, valor, dataVencimento, isPago!!)

                        CompromissoManager.updateCompromissoComMovimentacao(compromisso, compromissoMensal, mesViewModel, compromissoViewModel, compromissoMensalViewModel){
                            GlobalScope.launch {
                                //Background processing..."
                                withContext(Dispatchers.Main) {
                                    //"Update UI here!")
                                    CompromissoJoinViewModel.setCompromissosJoinNoMes(mesTrabalhado.mesId){
                                        setCompromissos(CompromissoJoinViewModel.compromissosJoinDoMes)
                                    }//
                                }
                            }
                        }

                        val toast = Toast.makeText(
                            holder.itemView.context as AppCompatActivity,
                            Html.fromHtml("<font color='#e3f2fd' ><b>" + "${nomeET?.text.toString()} registrado com sucesso!" + "</b></font>"),
                            Toast.LENGTH_LONG
                        )

                        //colocando o toast verde
                        toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))

                        toast.show()

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

    internal fun setCompromissos(compromissos: List<CompromissoJoin>) {
        this.compromissos = compromissos.sortedWith(compareBy<CompromissoJoin>{it.isPago}.thenBy { it.dataVencimento }.thenByDescending { it.valor })
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