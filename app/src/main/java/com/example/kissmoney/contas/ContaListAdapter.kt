package com.example.kissmoney.contas

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kissmoney.R
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.MoneyTextWatcher
import com.example.kissmoney.util.formataParaBr
import com.example.kissmoney.util.getDataHojeString
import com.example.kissmoney.util.limpaFormatacaoDeMoeda
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
    lateinit var mesTrabalhado : Mes


    fun setViewModel(contaViewModel: ContaViewModel, mesViewModel: MesViewModel, movimentacaoMensalViewModel: MovimentacaoMensalViewModel){
        this.contaViewModel = contaViewModel
        this.mesViewModel = mesViewModel
        this.movimentacaoMensalViewModel = movimentacaoMensalViewModel

    }




    inner class ContaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nomeContaTextView: TextView = itemView.findViewById(R.id.nomeContaItemTV)
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


        holder.constraint.setOnClickListener {
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
                if (current.tipoConta == TiposDeConta.CARTEIRA.tipo){
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

                                ContaJoinViewModel.setContasJoinNoMes(mesTrabalhado.mesId){
                                    setContas(ContaJoinViewModel.contasJoinDoMes)
//                                }
//
//                                ContaJoinViewModel.setAllContasJoin() {

//                                    contas =
//                                        ContaJoinViewModel.allContasJoin // para poder rodar na tread principal
//                                    setContas(contas)

                                    val toast = Toast.makeText(
                                        holder.itemView.context as AppCompatActivity,
                                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "${nomeEditText?.text.toString()} registrado com sucesso!" + "</b></font>"),
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




    }

    internal  fun setContas(contas: List<ContaJoin>){
        this.contas = contas
        notifyDataSetChanged()
    }

    internal fun setMesTrabalhado(mes: Mes) {
        this.mesTrabalhado = mes
    }

    override fun getItemCount() = contas.size

}