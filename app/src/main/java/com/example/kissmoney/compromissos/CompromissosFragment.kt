package com.example.kissmoney.compromissos

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kissmoney.R
import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.compromissos.mensal.CompromissoMensalViewModel
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.contas.ContaViewModel
import com.example.kissmoney.contas.ContasFragmentDirections
import com.example.kissmoney.databinding.FragmentCompromissosBinding
import com.example.kissmoney.databinding.FragmentContasBinding
import com.example.kissmoney.ganhos.GanhosFragmentArgs
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class CompromissosFragment : Fragment() {

    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity"

    //viewmodels
    lateinit var compromissoViewModel: CompromissoViewModel
    lateinit var compromissoMensalViewModel: CompromissoMensalViewModel
    lateinit var mesViewModel: MesViewModel

    //adapter do recyclerview
    lateinit var adapter: CompromissoListAdapter

    //variáveis para exibição de dados
    lateinit var mesAtual: Mes
    lateinit var mesAnterior: Mes
    lateinit var mesPosterior: Mes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compromissoViewModel = ViewModelProvider(this).get(CompromissoViewModel::class.java)
        compromissoMensalViewModel =
            ViewModelProvider(this).get(CompromissoMensalViewModel::class.java)
        mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentCompromissosBinding>(
            inflater, R.layout.fragment_compromissos, container, false
        )

        //setando adapter e variáveis no recyclerview
        var recyclerView = binding.recyclerViewCompromissos
        adapter = CompromissoListAdapter(activity as Activity)
        adapter.setViewModel(compromissoViewModel, compromissoMensalViewModel, mesViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity as AppCompatActivity)

        //linha divisória
        //recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        var itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                this.requireContext(),
                R.drawable.divider
            )!!
        )
        recyclerView.addItemDecoration(itemDecoration)

        // início navegação entre meses
        val args = GanhosFragmentArgs.fromBundle(requireArguments())
        var idMes = args.idMes

        mesAtual = Mes(idMes, getNomeMesAtual())

        // se tiver recebido um id, vou ao banco buscar o nome do mês
        if (idMes != 0L) {
            mesViewModel.getById(mesAtual) {

                GlobalScope.launch {
                    //Background processing..."
                    withContext(Dispatchers.Main) {

                        CompromissoJoinViewModel.setCompromissosJoinNoMes(mesAtual.mesId) {
                            setaCopromissosNoAdapter()
                            setaValoresNaView(binding)
                        }
                        setMesAnteriorEMesPosterior(binding)
                    }
                }
            }
        } else { // se não tiver recebido um id, vou ao banco buscar por nome, se não existir registro, o viewmodel cria
            mesViewModel.getByName(mesAtual) {

                GlobalScope.launch {
                    //Background processing..."
                    withContext(Dispatchers.Main) {

                        CompromissoJoinViewModel.setCompromissosJoinNoMes(mesAtual.mesId) {
                            setaCopromissosNoAdapter()
                            setaValoresNaView(binding)
                        }
                        setMesAnteriorEMesPosterior(binding)
                    }
                }
            }
        }

        binding.fab.setOnClickListener {

//            val dialog = BottomSheetDialog(activity as AppCompatActivity)
//            dialog.setContentView(R.layout.crud_compromisso_botton_sheet)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            //dialog.getWindow()?.setDimAmount(0F);
//            dialog.setCancelable(false)

            val view = layoutInflater.inflate(R.layout.crud_compromisso_botton_sheet, null)
            var dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
            dialog.setContentView(view)
            //dialog.setContentView(R.layout.crud_conta_botton_sheet)
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

                    dataVencimentoTV?.text = date
                }

            saveBtn?.setOnClickListener {

                if (nomeET?.text.isNullOrEmpty()) {
                    val toast = Toast.makeText(
                        activity as AppCompatActivity,
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

                    var compromisso = Compromisso(0L, nome, isRecorrente!!)
                    var compromissoMensal =
                        CompromissoMensal(0L, 0L, 0L, valor, dataVencimento, isPago!!)

                    CompromissoManager.criaCompromissoComCompromissoMensal(
                        compromisso,
                        compromissoMensal,
                        mesViewModel,
                        compromissoViewModel,
                        compromissoMensalViewModel
                    ) {
                        GlobalScope.launch {
                            //Background processing..."
                            withContext(Dispatchers.Main) {
                                //"Update UI here!")
                                CompromissoJoinViewModel.setCompromissosJoinNoMes(mesAtual.mesId) {
                                    setaCopromissosNoAdapter()
                                }//
                            }
                        }
                    }

                    val toast = Toast.makeText(
                        activity as AppCompatActivity,
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

        binding.importarRecorrenteImagView.setOnClickListener {


            importaCompromissosDoMesAnterior() {
                setaCopromissosNoAdapter()

                val toast = Toast.makeText(
                    activity as AppCompatActivity,
                    Html.fromHtml("<font color='#e3f2fd' ><b>" + "Compromissos recorrentes importados!" + "</b></font>"),
                    Toast.LENGTH_LONG
                )

                //colocando o toast verde
                toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))

                toast.show()
            }


        }

        (activity as AppCompatActivity).supportActionBar?.title = ""
        return binding.root
    }

    private fun setaValoresNaView(binding: FragmentCompromissosBinding) {
        var total = 0.0
        var totalPendente = 0.0

        for (compJ in CompromissoJoinViewModel.compromissosJoinDoMes) {
            total += compJ.valor
            if (!compJ.isPago) totalPendente += compJ.valor
        }

        binding.nomeDoMestextView4.text = getNomeMesPorExtensoComAno(mesAtual.nomeMes)
        binding.totalTextView.text = formataParaBr(total.toBigDecimal())
        binding.textView25.text = formataParaBr(totalPendente.toBigDecimal())
    }

    private fun setMesAnteriorEMesPosterior(binding: FragmentCompromissosBinding) {
        mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
        mesPosterior = Mes(0L, recebeNomeMesRetornaNomeMesPosterior(mesAtual.nomeMes))
        mesViewModel.getByName(mesAnterior) {}
        mesViewModel.getByName(mesPosterior) {}

        GlobalScope.launch {
            //Background processing..."
            withContext(Dispatchers.Main) {
                //"Update UI here!")
                // para poder rodar na tread principal
                binding.mesAnteriorImageViewCaixa4.setOnClickListener {
                    Navigation.findNavController(requireView())
                        .navigate(
                            CompromissosFragmentDirections.actionCompromissosFragmentSelf(
                                mesAnterior.mesId
                            )
                        )
                }
                binding.mesPosteriorImageViewCaixa4.setOnClickListener {
                    Navigation.findNavController(requireView())
                        .navigate(
                            CompromissosFragmentDirections.actionCompromissosFragmentSelf(
                                mesPosterior.mesId
                            )
                        )
                }
            }
        }
    }

    private fun setaCopromissosNoAdapter() {
        GlobalScope.launch {
            //Background processing..."
            withContext(Dispatchers.Main) {
                //"Update UI here!")

                adapter.setMesTrabalhado(mesAtual)
                adapter.setCompromissos(CompromissoJoinViewModel.compromissosJoinDoMes)

            }
        }
    }

    private fun importaCompromissosDoMesAnterior(callback: () -> Unit) {


        var compromissoList = ArrayList<Compromisso>()
        var compromissoMensalListMesAnterior = ArrayList<CompromissoMensal>()

        var compromissoMensalListMesAtual = ArrayList<CompromissoMensal>()

        //variavel que armazena se o compromisso já existe no mes atual
        var isExisteNoMesAtual = false


        getCompromissosMensaisMes(mesAtual, compromissoMensalListMesAtual) {
            //pego os compromissos mensais do mes anterior
            getCompromissosMensaisMes(mesAnterior, compromissoMensalListMesAnterior) {

                //pego os compromissos que são do mes anterior
                getCompromissosMesAnterior(compromissoMensalListMesAnterior, compromissoList) {

                    //percorro os compromissos do mes anterior e verifico se sao recorrentes
                    compromissoList.forEach { comp ->

                        if (comp.isRecorrente) {

                            //se forem recorrentes, verifico se ja existem no mes atual
                            compromissoMensalListMesAnterior.forEach { cm ->
                                if (cm.compromissoId == comp.compromissoId) {

//                                    CompromissoJoinViewModel.compromissosJoinDoMes.forEach { cj ->
//                                        if (cj.compromissoId == cm.compromissoId) isExisteNoMesAtual =
//                                            true
//                                    }
                                    compromissoMensalListMesAtual.forEach { compAtual ->
                                        if (compAtual.compromissoId == cm.compromissoId) isExisteNoMesAtual = true
                                    }

                                    if (!isExisteNoMesAtual) {
                                        clonaCompromissoMensalEInsereNoMesPosterior(cm)
                                    }
                                }

                                isExisteNoMesAtual = false
                            }
                        }
                    }
                    callback()
                }
            }
        }
    }

    private fun getCompromissosMesAnterior(
        compromissoMensalListMesAnterior: ArrayList<CompromissoMensal>,
        compromissoList: ArrayList<Compromisso>,
        callback: () -> Unit
    ) {

        compromissoList.clear()

        compromissoViewModel.allCompromissos.observe(
            this.viewLifecycleOwner,
            androidx.lifecycle.Observer { comps ->
                comps.forEach { cp ->
                    compromissoMensalListMesAnterior.forEach { cms ->
                        if (cms.compromissoId == cp.compromissoId) compromissoList.add(cp)
                    }
                }
                callback()
            })
    }

    private fun getCompromissosMensaisMes(
        mes: Mes,
        compromissoMensalListMesAnterior: ArrayList<CompromissoMensal>,
        callback: () -> Unit
    ) {

        compromissoMensalListMesAnterior.clear()

        compromissoMensalViewModel.allCompromissosMensais.observe(
            this.viewLifecycleOwner,
            androidx.lifecycle.Observer { cms ->
                cms.forEach { cm ->
                    if (cm.mesId == mes.mesId) compromissoMensalListMesAnterior.add(cm)
                }
                callback()
            })
    }


    private fun clonaCompromissoMensalEInsereNoMesPosterior(cm: CompromissoMensal) {
        var clone = cm.copy()
        clone.dataVencimento = avancaUmMesNaData(cm.dataVencimento)
        clone.mesId = mesAtual.mesId
        clone.compromissoMensalId = 0L
        compromissoMensalViewModel.insert(clone)
    }


}


//CompromissoJoinViewModel.setCompromissosJoinNoMes(mesAtual.mesId){
//    setaCopromissosNoAdapter()
//
//    val toast = Toast.makeText(
//        activity as AppCompatActivity,
//        Html.fromHtml("<font color='#e3f2fd' ><b>" + "Compromissos recorrentes do mês anterior importados com sucesso!" + "</b></font>"),
//        Toast.LENGTH_LONG
//    )
//
//    //colocando o toast verde
//    toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))
//
//    toast.show()
//}