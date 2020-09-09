package com.example.kissmoney.contas

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kissmoney.R
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.databinding.FragmentContasBinding
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.MoneyTextWatcher
import com.example.kissmoney.util.getDataHojeString
import com.example.kissmoney.util.limpaFormatacaoDeMoeda
import com.example.kissmoney.util.recebeDataRetornaMes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity" //do pickdate

    //viewmodel
    lateinit var contaViewModel: ContaViewModel
    lateinit var mesViewModel: MesViewModel
    lateinit var movimentacaoMensalViewModel: MovimentacaoMensalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        contaViewModel = ViewModelProvider(this).get(ContaViewModel::class.java)
        mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)
        movimentacaoMensalViewModel = ViewModelProvider(this).get(MovimentacaoMensalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentContasBinding>(
            inflater, R.layout.fragment_contas, container, false
        )

        val recyclerView = binding.recyclerViewCaixa
        val adapter = ContaListAdapter(activity as Activity)

        //adapter.setViewModel...

        adapter.setContas(ContaJoinViewModel.allContasJoin)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity as AppCompatActivity)

        binding.fab.setOnClickListener {

            val dialog = BottomSheetDialog(activity as AppCompatActivity)
            //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
            dialog.setContentView(R.layout.crud_conta_botton_sheet)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.getWindow()?.setDimAmount(0F);

            var tiposSpinner = dialog.findViewById<Spinner>(R.id.tipoContaSpinner2)
            var nomeEditText = dialog.findViewById<EditText>(R.id.nomeContaTextView)
            var valorInicialEditText = dialog.findViewById<EditText>(R.id.saldoInicialEditText)
            var valorAtualOuFinalEditText = dialog.findViewById<EditText>(R.id.saldoAtualEditTextText)
            var dataAtualizacaoTextView = dialog.findViewById<TextView>(R.id.dataAtualizacaoTextView)
            var isEncerrada = dialog.findViewById<SwitchCompat>(R.id.isEncerradaSwitch)

            var adapter = ArrayAdapter(activity as AppCompatActivity, R.layout.spinner_item_white, TiposDeConta.values() )
            adapter.setDropDownViewResource(R.layout.spinner_item_white)
            tiposSpinner?.adapter = adapter

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
                    var valorInicial = limpaFormatacaoDeMoeda(valorInicialEditText?.text.toString()).trim().toDouble()
                    var valorAtual = limpaFormatacaoDeMoeda(valorAtualOuFinalEditText?.text.toString()).trim().toDouble()
                    var isEncerrada = isEncerrada?.isChecked
                    var dataAtualizacao = dataAtualizacaoTextView?.text.toString()

                    var conta = Conta(0L, nome, tipo, isEncerrada!!)
                    var movimentacaoMensal = MovimentacaoMensal(0L,0L,conta.contaId,
                    valorInicial,valorAtual,dataAtualizacao)

                    ContaManager.criaContaComMovimentacao(conta, movimentacaoMensal, contaViewModel, mesViewModel, movimentacaoMensalViewModel)

                    val toast = Toast.makeText(
                        activity as AppCompatActivity,
                        Html.fromHtml("<font color='#e3f2fd' ><b>" + "${nomeEditText?.text.toString()} registrado com sucesso!" + "</b></font>"),
                        Toast.LENGTH_LONG
                    )

                    //colocando o toast verde
                    toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))

                    toast.show()

                    ContaJoinViewModel.setAllContasJoin()

                    dialog.dismiss()
                }

            }

            cancelBtn?.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }

        (activity as AppCompatActivity).supportActionBar?.title ="Kiss"





//        return inflater.inflate(R.layout.fragment_contas, container, false)

        return binding.root
    }




}