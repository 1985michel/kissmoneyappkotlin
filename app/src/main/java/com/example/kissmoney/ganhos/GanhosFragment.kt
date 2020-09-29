package com.example.kissmoney.ganhos

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
import com.example.kissmoney.contas.ContasFragmentArgs
import com.example.kissmoney.databinding.FragmentGanhosBinding
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


class GanhosFragment : Fragment() {

    //do pickdate
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private val TAG = "MainActivity"

    //viewmodels
    lateinit var ganhoViewModel: GanhoViewModel
    lateinit var mesViewModel: MesViewModel
    lateinit var ganhoMensalViewModel: GanhoMensalViewModel

    //adapter do recyclerview
    lateinit var adapter: GanhoListAdapter

    //variáveis base para exibição dos dados
    lateinit var mesAtual: Mes
    lateinit var mesAnterior: Mes
    lateinit var mesPosterior: Mes


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ganhoViewModel = ViewModelProvider(this).get(GanhoViewModel::class.java)
        mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)
        ganhoMensalViewModel =
            ViewModelProvider(this).get(GanhoMensalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentGanhosBinding>(
            inflater, R.layout.fragment_ganhos, container, false
        )

        //setando adapter e variáveis no recyclerview
        val recyclerView = binding.recyclerViewGanho
        adapter = GanhoListAdapter(activity as Activity)
        adapter.setViewModel(ganhoViewModel, mesViewModel, ganhoMensalViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity as AppCompatActivity)

        //linha divisória
        //recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        var itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                this.requireContext(),
                R.drawable.divider
            )!!)
        recyclerView.addItemDecoration(itemDecoration)

        // início navegação entre meses
        val args = ContasFragmentArgs.fromBundle(requireArguments())
        var idMes = args.idMes

        mesAtual = Mes(idMes, getNomeMesAtual())

        //se tiver recebido um id, vou ao banco buscar o nome do mes
        if (idMes != 0L) {

            mesViewModel.getById(mesAtual) {
                GlobalScope.launch {
                    //Background processing..."
                    withContext(Dispatchers.Main) {
                        GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId) {
                            setaGanhosNoAdapter()
                            setaValoresNaView(binding)
                        }
                        setMesAnteriorEMesPosterior(binding)
                    }
                }
            }
        } else { //se não tiver recebido um id, vou ao banco buscar por nome

            mesViewModel.getByName(mesAtual) {
                GlobalScope.launch {
                    //Background processing..."
                    withContext(Dispatchers.Main) {
                        GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId) {
                            setaGanhosNoAdapter()
                            setaValoresNaView(binding)
                        }
                        setMesAnteriorEMesPosterior(binding)
                    }
                }
            }
        }
        //fim da navegação entre meses

        binding.fab.setOnClickListener {

//            val dialog = BottomSheetDialog(activity as AppCompatActivity)
//            dialog.setContentView(R.layout.crud_ganho_button_sheet)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            //dialog.getWindow()?.setDimAmount(0F);
//            dialog.setCancelable(false)

            val view = layoutInflater.inflate(R.layout.crud_ganho_button_sheet, null)
            var dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
            dialog.setContentView(view)
            //dialog.setContentView(R.layout.crud_conta_botton_sheet)
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
            tipoContaIV?.setImageResource(R.drawable.trabalho_icon_3)

            dataRecebimentoTV?.text = getDataHojeString()

            valorET?.addTextChangedListener(
                MoneyTextWatcher(
                    valorET,
                    Locale("pt", "BR")
                )
            )

            valorET?.setText("R$ 0,00")

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
                    activity as AppCompatActivity,
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
                    var isRendaPassiva = isRendaPassivaSwitch?.isChecked
                    var isGanhoRegular = isGanhoRegularSwitch?.isChecked
                    var isRecebido = isRecebidoSwitch?.isChecked
                    var dataRecebimento = dataRecebimentoTV?.text.toString()
                    var observacoes = observacaoET?.text.toString()

                    var ganho = Ganho(0L, nome, isRendaPassiva!!, isGanhoRegular!! )
                    var ganhoMensal = GanhoMensal(0L, 0L, ganho.ganhoId, valor, dataRecebimento, isRecebido!!, observacoes)

                    GanhoManager.criaGanhoComGanhoMensal(ganho, ganhoMensal,ganhoViewModel, ganhoMensalViewModel, mesViewModel) {
                        GlobalScope.launch {
                            //Background processing..."
                            withContext(Dispatchers.Main) {
                                //"Update UI here!")
                                GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId){
                                    setaGanhosNoAdapter()
                                }
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


        binding.importarRecorrenteImagView2.setOnClickListener {

            importarGanhosDoMesAnterior {
                setaGanhosNoAdapter()

                val toast = Toast.makeText(
                    activity as AppCompatActivity,
                    Html.fromHtml("<font color='#e3f2fd' ><b>" + "Ganhos recorrentes importados!" + "</b></font>"),
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

    private fun setaValoresNaView(binding: FragmentGanhosBinding) {

        var total = 0.0
        var totalPendente = 0.0

        for (gj in GanhoJoinViewModel.ganhosJoinDoMes) {
            total += gj.valor
            if (!gj.isRecebido) totalPendente += gj.valor
        }

        binding.nomeDoMestextView2.text = getNomeMesPorExtensoComAno(mesAtual.nomeMes)
        binding.valorTotalTv.text = formataParaBr(total.toBigDecimal())
        binding.textView39.text = formataParaBr(totalPendente.toBigDecimal())

    }

    private fun setaGanhosNoAdapter() {
        GlobalScope.launch {
            //Background processing..."
            withContext(Dispatchers.Main) {
                //"Update UI here!")

                adapter.setMesTrabalhado(mesAtual)
                adapter.setGanhos(GanhoJoinViewModel.ganhosJoinDoMes)

            }
        }
    }

    private fun setMesAnteriorEMesPosterior(binding: FragmentGanhosBinding) {
        mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
        mesPosterior = Mes(0L, recebeNomeMesRetornaNomeMesPosterior(mesAtual.nomeMes))
        mesViewModel.getByName(mesAnterior) {}
        mesViewModel.getByName(mesPosterior) {}

        GlobalScope.launch {
            //Background processing..."
            withContext(Dispatchers.Main) {
                //"Update UI here!")
                // para poder rodar na tread principal
                binding.mesAnteriorImageViewCaixa2.setOnClickListener {
                    Navigation.findNavController(requireView())
                        .navigate(GanhosFragmentDirections.actionGanhosFragmentSelf(mesAnterior.mesId))
                }
                binding.mesPosteriorImageViewCaixa2.setOnClickListener {
                    Navigation.findNavController(requireView())
                        .navigate(GanhosFragmentDirections.actionGanhosFragmentSelf(mesPosterior.mesId))
                }
            }
        }
    }

    private fun importarGanhosDoMesAnterior(callback: () -> Unit) {

        var ganhosList = ArrayList<Ganho>()
        var ganhoMensalListMesAnterior = ArrayList<GanhoMensal>()
        var ganhoMensalListMesAtual = ArrayList<GanhoMensal>()

        var isExisteNoMesAtual = false

        getGanhosMensaisMes(mesAtual, ganhoMensalListMesAtual) {
            getGanhosMensaisMes(mesAnterior, ganhoMensalListMesAnterior) {
                getGanhosMesAnterior(ganhoMensalListMesAnterior, ganhosList) {

                    ganhosList.forEach { ganho ->

                        if (ganho.isRecorrente) {
                            ganhoMensalListMesAnterior.forEach { gm ->
                                if (gm.ganhoId == ganho.ganhoId) {
                                    ganhoMensalListMesAtual.forEach { gmAtual ->
                                        if (gmAtual.ganhoId == gm.ganhoId) isExisteNoMesAtual = true
                                    }

                                    if (!isExisteNoMesAtual) {
                                        clonaGanhoMensalEInsereNoMesPosterior(gm)
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

    private fun getGanhosMensaisMes(
        mes: Mes,
        ganhosMensaisList : ArrayList<GanhoMensal>,
        callback: () -> Unit
    ) {

        ganhosMensaisList.clear()

        ganhoMensalViewModel.allGanhosMensais.observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            gms ->
            gms.forEach { gm ->
                if (gm.mesId == mes.mesId)  ganhosMensaisList.add(gm)
            }
            callback()
        })
    }

    private fun getGanhosMesAnterior(
        ganhosMensaisList : ArrayList<GanhoMensal>,
        ganhosList : ArrayList<Ganho>,
        callback: () -> Unit
    ) {

        ganhosList.clear()

        ganhoViewModel.allGanhos.observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
                ganhos ->
            ganhos.forEach { g ->
                ganhosMensaisList.forEach { gms ->
                    if (gms.ganhoId == g.ganhoId ) ganhosList.add(g)
                }
            }
            callback()
        })
    }

    private fun clonaGanhoMensalEInsereNoMesPosterior(gm: GanhoMensal) {
        var clone = gm.copy()
        clone.dataRecebimento = avancaUmMesNaData(gm.dataRecebimento)
        clone.mesId = mesAtual.mesId
        clone.isRecebido = false
        clone.ganhoMensalId = 0L
        ganhoMensalViewModel.insert(clone)
    }
}