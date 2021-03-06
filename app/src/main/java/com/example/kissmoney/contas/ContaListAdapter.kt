package com.example.kissmoney.contas

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.compromissos.CompromissoJoin
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ContaListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<ContaListAdapter.ContaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contas = emptyList<ContaJoin>() //cached copy


    //viewmodel
    lateinit var contaViewModel: ContaViewModel
    lateinit var mesViewModel: MesViewModel
    lateinit var movimentacaoMensalViewModel: MovimentacaoMensalViewModel

    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity"

    //setando o mes de trabalho
    lateinit var mesTrabalhado: Mes


    fun setViewModel(
        contaViewModel: ContaViewModel,
        mesViewModel: MesViewModel,
        movimentacaoMensalViewModel: MovimentacaoMensalViewModel
    ) {
        this.contaViewModel = contaViewModel
        this.mesViewModel = mesViewModel
        this.movimentacaoMensalViewModel = movimentacaoMensalViewModel

    }


    inner class ContaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeContaTextView: TextView = itemView.findViewById(R.id.nomeContaItemTV)
        val valorAtualTextView: TextView = itemView.findViewById(R.id.valor_total_textView)
        val dataAtualizacaoTextView: TextView = itemView.findViewById(R.id.dataAtualizacaoTextView)
        val contaIconImageView: ImageView = itemView.findViewById(R.id.contaIconImageView)
        val atencaoImageView: ImageView = itemView.findViewById(R.id.atencaoImageViewGanho)

        val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContaListAdapter.ContaViewHolder {

        val itemView = inflater.inflate(R.layout.item_contas, parent, false)
        return ContaViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ContaListAdapter.ContaViewHolder, position: Int) {
        var current = contas[position]

        holder.nomeContaTextView.text = current.nomeConta
        holder.valorAtualTextView.text = formataParaBr(current.saldoAtualOuFinal.toBigDecimal())
        holder.dataAtualizacaoTextView.text = current.dataAtualizacao



        holder.contaIconImageView.setImageResource(
            if (current.tipoConta.equals(TiposDeConta.CARTEIRA.tipo)) {
                R.drawable.cofre_icon_list
            } else if (current.tipoConta.equals(TiposDeConta.INVESTIMENTO.tipo)) {
                R.drawable.invest_icon_list
            } else {
                R.drawable.creditcard_icon_list
            }
        )

        if (!current.isEncerrada) {
            if (verificaSeTemMaisDe7diasDaUltimaAtualizacao(current.dataAtualizacao)) {
                holder.atencaoImageView.visibility = View.VISIBLE
            } else {
                holder.atencaoImageView.visibility = View.INVISIBLE
            }

        } else {
            holder.atencaoImageView.visibility = View.INVISIBLE
        }


        holder.constraint.setOnClickListener {
//            val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
//            //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
//            dialog.setContentView(R.layout.view_conta_botton_sheet_2)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.getWindow()?.setDimAmount(0F);
//            dialog.setCancelable(false)

            //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
            var dialog = BottomSheetDialog(holder.itemView.context, R.style.BottomSheetDialog)
            dialog.setContentView(R.layout.view_conta_botton_sheet_2)
//            dialog.setContentView(view)
            //dialog.setContentView(R.layout.crud_conta_botton_sheet)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.getWindow()?.setDimAmount(0F);
            dialog.setCancelable(false)


            var tiposSpinner = dialog.findViewById<TextView>(R.id.tipoContaSpinner2)
            var nomeEditText = dialog.findViewById<TextView>(R.id.nomeContaBottonTextView)
            var valorInicialEditText = dialog.findViewById<TextView>(R.id.saldoInicialEditText)
            var valorAtualOuFinalEditText =
                dialog.findViewById<TextView>(R.id.saldoAtualEditTextText)
            var dataAtualizacaoTextView =
                dialog.findViewById<TextView>(R.id.dataAtualizacaoTextView)
//            var isEncerrada = dialog.findViewById<SwitchCompat>(R.id.isEncerradaSwitch)
            var tipoContaSpinnerImageView: ImageView? =
                dialog.findViewById(R.id.tipoContaSpinnerImageView)

            var isEncerrada: ImageView? =
                dialog.findViewById(R.id.imageView5)

            //hora de povoar os valores

            nomeEditText?.setText(current.nomeConta)
            tiposSpinner?.text = current.tipoConta

            tipoContaSpinnerImageView?.setImageResource(
                if (current.tipoConta == TiposDeConta.CARTEIRA.tipo) {
                    R.drawable.cofre_icon_list_dark
                } else if (current.tipoConta == TiposDeConta.INVESTIMENTO.tipo) {
                    R.drawable.invest_icon_list_dark
                } else {
                    R.drawable.creditcard_icon_list_dark
                }
            )

            dataAtualizacaoTextView?.text = current.dataAtualizacao

            valorInicialEditText?.setText(formataParaBr(current.saldoInicial.toBigDecimal()))
            valorAtualOuFinalEditText?.setText(formataParaBr(current.saldoAtualOuFinal.toBigDecimal()))
            isEncerrada?.setImageResource(
                if (current.isEncerrada) {
                    R.drawable.switch_encerrada
                } else {
                    R.drawable.switch_ativa
                }
            )


            if (!current.isEncerrada) {
                if (verificaSeTemMaisDe7diasDaUltimaAtualizacao(current.dataAtualizacao)) {
                    holder.atencaoImageView.visibility = View.VISIBLE
                } else {
                    holder.atencaoImageView.visibility = View.INVISIBLE
                }

            } else {
                holder.atencaoImageView.visibility = View.INVISIBLE
            }


            val cancelBtn = dialog.findViewById<Button>(R.id.cancelButton)
            val deleteBtn = dialog.findViewById<ImageView>(R.id.deleteContaImageView)
            val updateBtn = dialog.findViewById<ImageView>(R.id.updateValorAtualContaImageView)
            val editBtn = dialog.findViewById<ImageView>(R.id.editContaImageView)

            deleteBtn?.setOnClickListener {

                val dialogIterno = Dialog(holder.itemView.context as AppCompatActivity)
                dialogIterno.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogIterno.setCancelable(false)
                dialogIterno.setContentView(R.layout.delete_dialog)
                dialogIterno.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                //val body = dialog.findViewById(R.id.body) as TextView


                var msg = dialogIterno.findViewById(R.id.msgTextView) as TextView
                msg.setText(
                    Html.fromHtml("Você confirma que deseja apagar <b>${current.nomeConta.toUpperCase()}</b> neste mês?\nAo fazê-lo a conta será encerrado no mês anterior")
                )

                var confirmaBtn = dialogIterno.findViewById(R.id.apagarButton) as Button

                confirmaBtn.setOnClickListener {
//                    contaViewModel.delete(
//                        current.getConta()
//                    )
                    movimentacaoMensalViewModel.delete(
                        current.getMovimentacao()
                    )
                    var conta = current.getConta()
                    conta.isEncerrada = true
                    contaViewModel.update(conta)


                    val toast = Toast.makeText(
                        holder.itemView.context as AppCompatActivity,
                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "Conta ${current.nomeConta} apagada com sucesso!" + "</b></font>"),
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
                setLarguraEAlturaInterno(dialogIterno) {}

                //cancelBtn?.setOnClickListener { dialog.dismiss() }

                //dialog.show()
            }


            editBtn?.setOnClickListener {

                //fecha o dialog de consulta e cria o de CRUD
                dialog.dismiss()

                val dialog = BottomSheetDialog(holder.itemView.context as AppCompatActivity)
                //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
                dialog.setContentView(R.layout.crud_conta_botton_sheet)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.getWindow()?.setDimAmount(0F);
                dialog.setCancelable(false)


                var tiposSpinner = dialog.findViewById<Spinner>(R.id.tipoContaSpinner2)
                var nomeEditText = dialog.findViewById<EditText>(R.id.nomeContaBottonTextView)
                var valorInicialEditText = dialog.findViewById<EditText>(R.id.saldoInicialEditText)
                var valorAtualOuFinalEditText =
                    dialog.findViewById<EditText>(R.id.saldoAtualEditTextText)
                var dataAtualizacaoTextView =
                    dialog.findViewById<TextView>(R.id.dataAtualizacaoTextView)
                var isEncerrada = dialog.findViewById<SwitchCompat>(R.id.isEncerradaSwitch)
                var tipoContaSpinnerImageView: ImageView? =
                    dialog.findViewById(R.id.tipoContaSpinnerImageView)

                var adapterSpinner = ArrayAdapter(
                    holder.itemView.context as AppCompatActivity,
                    R.layout.spinner_item_white,
                    TiposDeConta.values()
                )
                adapterSpinner.setDropDownViewResource(R.layout.spinner_item_white)
                tiposSpinner?.adapter = adapterSpinner

                tiposSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        tipoContaSpinnerImageView?.setImageResource(
                            if (tiposSpinner?.selectedItem.toString() == TiposDeConta.CARTEIRA.tipo) {
                                R.drawable.cofre_icon_list_dark
                            } else if (tiposSpinner?.selectedItem.toString() == TiposDeConta.INVESTIMENTO.tipo) {
                                R.drawable.invest_icon_list_dark
                            } else {
                                R.drawable.creditcard_icon_list_dark
                            }
                        )
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        tipoContaSpinnerImageView?.setImageResource(R.color.darkblue_background)
                    }
                }


                //hora de povoar os valores

                nomeEditText?.setText(current.nomeConta)
                tiposSpinner?.setSelection(
                    if (current.tipoConta == TiposDeConta.CARTEIRA.tipo) {
                        0
                    } else if (current.tipoConta == TiposDeConta.DIVIDAS.tipo) {
                        1
                    } else {
                        2
                    }
                )

                dataAtualizacaoTextView?.text = current.dataAtualizacao

                valorInicialEditText?.setText(formataParaBr(current.saldoInicial.toBigDecimal()))
                valorAtualOuFinalEditText?.setText(formataParaBr(current.saldoAtualOuFinal.toBigDecimal()))
                isEncerrada?.isChecked = current.isEncerrada


                valorInicialEditText?.addTextChangedListener(
                    MoneyTextWatcher(
                        valorInicialEditText,
                        Locale("pt", "BR")
                    )
                )

                valorAtualOuFinalEditText?.addTextChangedListener(
                    MoneyTextWatcher(
                        valorAtualOuFinalEditText,
                        Locale("pt", "BR")
                    )
                )

                val saveBtn = dialog.findViewById<Button>(R.id.salvarButton)
                val cancelBtn = dialog.findViewById<Button>(R.id.cancelButton)


                //apresentando o datapicker calendar
                dataAtualizacaoTextView?.setOnClickListener {
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

                        dataAtualizacaoTextView?.text = date
                    }


                saveBtn?.setOnClickListener {

                    if (nomeEditText?.text.isNullOrEmpty()) {
                        val toast = Toast.makeText(
                            holder.itemView.context as AppCompatActivity,
                            Html.fromHtml("<font color='#e3f2fd' ><b>" + "Obrigatório informar um nome!" + "</b></font>"),
                            Toast.LENGTH_LONG
                        )
                        //colocando o toast vermelho
                        toast.view?.setBackgroundColor(Color.parseColor("#FF1A1A"))

                        toast.show()
                    } else {

                        var nome = nomeEditText?.text.toString()
                        var tipo = tiposSpinner?.selectedItem.toString()
                        var valorInicial =
                            limpaFormatacaoDeMoeda(valorInicialEditText?.text.toString()).trim()
                                .toDouble()
                        var valorAtual =
                            limpaFormatacaoDeMoeda(valorAtualOuFinalEditText?.text.toString()).trim()
                                .toDouble()
                        var isEncerrada = isEncerrada?.isChecked
                        var dataAtualizacao = dataAtualizacaoTextView?.text.toString()

                        var conta = Conta(current.contaId, nome, tipo, isEncerrada!!)
                        var movimentacaoMensal = MovimentacaoMensal(
                            current.movimentacaoId, current.mesId, current.contaId,
                            valorInicial, valorAtual, dataAtualizacao
                        )

                        ContaManager.updateContaComMovimentacao(
                            conta,
                            movimentacaoMensal,
                            contaViewModel,
                            mesViewModel,
                            movimentacaoMensalViewModel
                        ) {


                            GlobalScope.launch {
                                //Background processing..."
                                withContext(Dispatchers.Main) {
                                    //"Update UI here!")

                                    ContaJoinViewModel.setContasJoinNoMes(mesTrabalhado.mesId) {
                                        setContas(ContaJoinViewModel.contasJoinDoMes)
//                                }
//
//                                ContaJoinViewModel.setAllContasJoin() {

//                                    contas =
//                                        ContaJoinViewModel.allContasJoin // para poder rodar na tread principal
//                                    setContas(contas)

                                        val toast = Toast.makeText(
                                            holder.itemView.context as AppCompatActivity,
                                            Html.fromHtml("<font color='#e3f2fd' ><b>" + "${nomeEditText?.text.toString()} atualizado com sucesso!" + "</b></font>"),
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

            updateBtn?.setOnClickListener {

                val dialogIterno = Dialog(holder.itemView.context as AppCompatActivity)
                dialogIterno.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogIterno.setCancelable(false)
                dialogIterno.setContentView(R.layout.updateconta)
                dialogIterno.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                var valorET =
                    dialogIterno.findViewById(R.id.updateContaSaldoAtualOuFInalEditText) as EditText
                var nomeConta = dialogIterno.findViewById(R.id.nomeContaTextView) as TextView
                nomeConta.text = current.nomeConta

                valorET.addTextChangedListener(
                    MoneyTextWatcher(
                        valorET,
                        Locale("pt", "BR")
                    )
                )

                valorET?.setText("R$ 0,00")


                var dataSaldoTextView =
                    dialogIterno.findViewById(R.id.dataSaldoTextView) as TextView

                dataSaldoTextView?.text = getDataHojeString()


                //apresentando o datapicker calendar
                dataSaldoTextView?.setOnClickListener {
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

                        dataSaldoTextView?.text = date
                    }

                var confirmaBtn = dialogIterno.findViewById(R.id.salvaUpdateBtn) as Button

                confirmaBtn.setOnClickListener {

                    current.saldoAtualOuFinal =
                        limpaFormatacaoDeMoeda(valorET?.text.toString()).trim().toDouble()
                    current.dataAtualizacao = dataSaldoTextView?.text.toString()

                    movimentacaoMensalViewModel.update(
                        current.getMovimentacao()
                    )

                    val toast = Toast.makeText(
                        holder.itemView.context as AppCompatActivity,
                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "Conta ${current.nomeConta} atualizada com sucesso!" + "</b></font>"),
                        Toast.LENGTH_LONG
                    )

                    //colocando o toast verde
                    toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))

                    toast.show()
                    dialogIterno.dismiss()
                    dialog.dismiss()


                }

                var cancelarBtn = dialogIterno.findViewById(R.id.cancelButton2) as Button
                cancelarBtn.setOnClickListener { dialogIterno.dismiss() }

                dialogIterno.show()
                setLarguraEAlturaInterno(dialogIterno) {}
            }


            cancelBtn?.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }


    }

    internal fun setContas(contas: List<ContaJoin>) {
        this.contas = contas.sortedWith(compareBy<ContaJoin>{it.tipoConta})
        notifyDataSetChanged()
    }

    internal fun setMesTrabalhado(mes: Mes) {
        this.mesTrabalhado = mes
    }

    override fun getItemCount() = contas.size

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