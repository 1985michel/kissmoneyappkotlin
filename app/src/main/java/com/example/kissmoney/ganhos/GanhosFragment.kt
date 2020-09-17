package com.example.kissmoney.ganhos

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kissmoney.R
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.contas.ContaViewModel
import com.example.kissmoney.contas.ContasFragmentArgs
import com.example.kissmoney.contas.ContasFragmentDirections
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.databinding.FragmentContasBinding
import com.example.kissmoney.databinding.FragmentGanhosBinding
import com.example.kissmoney.ganhos.mensal.GanhoMensalViewModel
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

                        GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId){
                            setaGanhosNoAdapter()
                            setaValoresNaView(binding)
                        }
                        setMesAnteriorEMesPosterior(binding)
                    }
                }
            }
        } else { //se não tiver recebido um id, vou ao banco buscar por nome
            mesViewModel.getByName(mesAtual){
                GlobalScope.launch {
                    //Background processing..."
                    withContext(Dispatchers.Main) {

                        GanhoJoinViewModel.setGanhosJoinNoMes(mesAtual.mesId){
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

            val dialog = BottomSheetDialog(activity as AppCompatActivity)
            //val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
            dialog.setContentView(R.layout.crud_ganho_button_sheet)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //dialog.getWindow()?.setDimAmount(0F);
            dialog.setCancelable(false)

            //cancelBtn?.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }

        (activity as AppCompatActivity).supportActionBar?.title = ""

        return binding.root
    }

    private fun setaValoresNaView(binding: FragmentGanhosBinding) {

        var total = 0.0

        for(gj in GanhoJoinViewModel.ganhosJoinDoMes) {
            total += gj.valor
        }

        binding.nomeDoMestextView2.text = getNomeMesPorExtensoComAno(mesAtual.nomeMes)
        binding.valorTotalTv.text = formataParaBr(total.toBigDecimal())

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


}