package com.example.kissmoney.gastos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kissmoney.R
import com.example.kissmoney.contas.ContasFragmentArgs
import com.example.kissmoney.databinding.FragmentGanhosBinding
import com.example.kissmoney.databinding.FragmentGastosBinding
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.mes.Estatisticas
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.formataParaBr
import com.example.kissmoney.util.getNomeMesAtual
import com.example.kissmoney.util.getNomeMesPorExtensoComAno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GastosFragment : Fragment() {

    //variáveis base para exibição dos dados
    lateinit var mesAtual: Mes
    lateinit var mesAnterior: Mes
    lateinit var mesPosterior: Mes

    lateinit var mesViewModel: MesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title =""
//        return inflater.inflate(R.layout.fragment_gastos, container, false)

        val binding = DataBindingUtil.inflate<FragmentGastosBinding>(
            inflater, R.layout.fragment_gastos, container, false
        )


        // início navegação entre meses
        val args = ContasFragmentArgs.fromBundle(requireArguments())
        var idMes = args.idMes


        mesAtual = Mes(idMes, getNomeMesAtual())

        if (idMes != 0L) {

            mesViewModel.getById(mesAtual) {

                binding.nomeMesTextView.text = getNomeMesPorExtensoComAno(mesAtual.nomeMes)

                binding.valorGastoTotalTextView.text = formataParaBr(Estatisticas.totalGastoNoMes.toBigDecimal())
//                binding.valorCompromissosPendentesTextView.text = formataParaBr(Estatisticas.)

            }
        } else {


            mesViewModel.getByName(mesAtual) {

            }

        }


        return binding.root



    }


}