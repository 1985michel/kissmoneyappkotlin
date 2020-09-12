package com.example.kissmoney.contas

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.toColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kissmoney.R
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.databinding.FragmentContasBinding
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.MoneyTextWatcher
import com.example.kissmoney.util.getDataHojeString
import com.example.kissmoney.util.limpaFormatacaoDeMoeda
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*
import java.util.*


class ContasFragment : Fragment() {

    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity" //do pickdate

    //viewmodel
    lateinit var contaViewModel: ContaViewModel
    lateinit var mesViewModel: MesViewModel
    lateinit var movimentacaoMensalViewModel: MovimentacaoMensalViewModel

    lateinit var adapter: ContaListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contaViewModel = ViewModelProvider(this).get(ContaViewModel::class.java)
        mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)
        movimentacaoMensalViewModel =
            ViewModelProvider(this).get(MovimentacaoMensalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentContasBinding>(
            inflater, R.layout.fragment_contas, container, false
        )

        val recyclerView = binding.recyclerViewCaixa
        adapter = ContaListAdapter(activity as Activity)

        adapter.setViewModel(contaViewModel, mesViewModel, movimentacaoMensalViewModel)

        setaContasNoAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity as AppCompatActivity)



        binding.fab.setOnClickListener {

            val dialog = BottomSheetDialog(activity as AppCompatActivity)
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
            var tipoContaSpinnerImageView : ImageView? = dialog.findViewById(R.id.tipoContaSpinnerImageView)

            var adapterSpinner = ArrayAdapter(
                activity as AppCompatActivity,
                R.layout.spinner_item_white,
                TiposDeConta.values()
            )
            adapterSpinner.setDropDownViewResource(R.layout.spinner_item_white)
            tiposSpinner?.adapter = adapterSpinner

            tiposSpinner?.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    tipoContaSpinnerImageView?.setImageResource(
                        if (tiposSpinner?.selectedItem.toString() == TiposDeConta.CARTEIRA.tipo){
                            R.drawable.cofre_icon_list_dark
                        } else if (tiposSpinner?.selectedItem.toString() == TiposDeConta.INVESTIMENTO.tipo){
                            R.drawable.invest_icon_list_dark
                        } else{
                            R.drawable.creditcard_icon_list_dark
                        })
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    tipoContaSpinnerImageView?.setImageResource(R.color.darkblue_background)
                }
            }


//            tiposSpinner?.onItemSelectedListener { view, b ->
//                tipoContaSpinnerImageView?.setImageResource(
//                if (tiposSpinner?.selectedItem.toString() == TiposDeConta.CARTEIRA.tipo){
//                    R.drawable.cofre_icon_list
//                } else if (tiposSpinner?.selectedItem.toString() == TiposDeConta.INVESTIMENTO.tipo){
//                    R.drawable.invest_icon_list
//                } else if (tiposSpinner?.selectedItem.toString() == TiposDeConta.DIVIDAS.tipo){
//                    R.drawable.creditcard_icon_list
//                }else {
//                    R.color.darkblue_background
//                }
//            )
//            }

            dataAtualizacaoTextView?.text = getDataHojeString()

            valorInicialEditText?.setText("R$ 0,00")
            valorAtualOuFinalEditText?.setText("R$ 0,00")

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
                    activity as AppCompatActivity,
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
                        activity as AppCompatActivity,
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

                    var conta = Conta(0L, nome, tipo, isEncerrada!!)
                    var movimentacaoMensal = MovimentacaoMensal(
                        0L, 0L, conta.contaId,
                        valorInicial, valorAtual, dataAtualizacao
                    )

                    ContaManager.criaContaComMovimentacao(
                        conta,
                        movimentacaoMensal,
                        contaViewModel,
                        mesViewModel,
                        movimentacaoMensalViewModel
                    ) {
                        println(">>>>>>>>>>>>>>>>>>>>>>>> a conta foi criada <<<<<<<<<<<<<< ")
                        //ContaJoinViewModel.setAllContasJoin(){
                        println(">>>>>>>>>>>>>>>>>>>>>>>> setando o novo adapter <<<<<<<<<<<<<< ")

//                        MainScope().launch {
//                            withContext(Dispatchers.Default) {
//                                //"Background processing...")
//                            }
//                            //"Update UI here!")
//                        }

                        GlobalScope.launch {
                            //Background processing..."
                            withContext(Dispatchers.Main) {
                                //"Update UI here!")
                                ContaJoinViewModel.setAllContasJoin() {
                                    setaContasNoAdapter() // para poder rodar na tread principal
                                }
                            }
                        }
                    }

                    val toast = Toast.makeText(
                        activity as AppCompatActivity,
                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "${nomeEditText?.text.toString()} registrado com sucesso!" + "</b></font>"),
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

        (activity as AppCompatActivity).supportActionBar?.title = "Kiss"

        return binding.root
    }

    private fun setaContasNoAdapter() {

//        GlobalScope.launch {
//            //TODO("Background processing...")
//            withContext(Dispatchers.Main) {
//                // TODO("Update UI here!")
//            }
//            TODO("Continue background processing...")
//        }
        adapter.setContas(ContaJoinViewModel.allContasJoin)
    }
}