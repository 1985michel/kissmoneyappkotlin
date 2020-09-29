package com.example.kissmoney.mes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kissmoney.R
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.databinding.FragmentNovomesBinding
import com.example.kissmoney.meta.AcompanhamentoDeMeta
import com.example.kissmoney.meta.Meta
import com.example.kissmoney.util.formataParaBr
import com.example.kissmoney.util.getNomeMesAtual
import com.example.kissmoney.util.getNomeMesPorExtensoComAno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class mesFragment : Fragment() {

    var metas = ArrayList<Meta>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


//        val dialog = Dialog(activity as AppCompatActivity)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.fragment_logo)
//        //val body = dialog.findViewById(R.id.body) as TextView
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//        dialog.show()
//
//        dialog.window?.setLayout(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels)
//        object : CountDownTimer(4000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {}
//            override fun onFinish() {
//                dialog.dismiss()
//            }
//        }.start()


//        if (isAbertura) {
//            isAbertura = false
//            Navigation.findNavController(requireView()).navigate(R.id.action_mesFragment_to_logoFragment)
//        }


        val binding = DataBindingUtil.inflate<FragmentNovomesBinding>(
            inflater,
            R.layout.fragment_novomes,
//            R.layout.fragment_mes,
            container,
            false
        )

        val recyclerView = binding.recyclerviewmes
        var adapter = MesListAdapter(requireActivity())
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val mLayoutManager = LinearLayoutManager(requireActivity())
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()

        binding.nomeMesTextView.text = getNomeMesPorExtensoComAno(AcompanhamentoDeMeta.mesAtual.nomeMes)
        binding.valorBalancoTextView.text = formataParaBr(
            CentralEstatistica.estatisticasMensais.get(AcompanhamentoDeMeta.mesAtual.mesId)!!.balanco.toBigDecimal()
        )
        binding.variacaoDiasMesTextView.text =
            CentralEstatistica.estatisticasMensais.get(AcompanhamentoDeMeta.mesAtual.mesId)!!.balancoEmDias.toString()

        binding.imageView.setImageResource(
            if (binding.variacaoDiasMesTextView.text.toString().toInt() > 0) {
                R.drawable.greenrow4
            } else if (binding.variacaoDiasMesTextView.text.toString().toInt() < 0) {
                R.drawable.redrow4
            } else {
                R.drawable.round_colorless
            }
        )

        AcompanhamentoDeMeta.setValues {
            adapter.setDados(AcompanhamentoDeMeta.metaJoinList)
        }





//        var mesAtual = Mes(0L, getNomeMesAtual())
//
//        var mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)






//        binding.progressBarDiasDeLiberdade.setOnClickListener {
//            binding.textoCentralTextView.text = "125 Dias de Liberdade.\n Meta: 180 dias"
//        }
//
//        binding.progressBarGastoNoMes.setOnClickListener {
//            binding.textoCentralTextView.text = "Já foram gastos 76% do que foi ganho no mês."
//        }
//
//        binding.progressBarPoupadoNoMes.setOnClickListener {
//            binding.textoCentralTextView.text = "Você investiu 10% da sua renda. Parabéns!"
//        }
//
//        binding.buttonGreen.setOnClickListener {
//            binding.textoCentralTextView.text = "125 Dias de Liberdade.\n Meta: 180 dias"
//            binding.progressBarDiasDeLiberdade.alpha = 1F
//            binding.progressBarGastoNoMes.alpha = 0.1F
//            binding.progressBarPoupadoNoMes.alpha = 0.1F
//        }
//
//        binding.buttonRed.setOnClickListener {
//            binding.textoCentralTextView.text = "Já foram gastos 76% do que foi ganho no mês."
//
//            binding.progressBarDiasDeLiberdade.alpha = 0.1F
//            binding.progressBarGastoNoMes.alpha = 0.1F
//            binding.progressBarPoupadoNoMes.alpha = 1F
//        }
//
//        binding.buttonGrey.setOnClickListener {
//            binding.textoCentralTextView.text = "Você investiu 10% da sua renda. Parabéns!"
//
//            binding.progressBarDiasDeLiberdade.alpha = 0.1F
//            binding.progressBarGastoNoMes.alpha = 1F
//            binding.progressBarPoupadoNoMes.alpha = 0.1F
//
//        }

        binding.walletImageView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mesFragment_to_contasFragment)
        }



        binding.ganhosImageView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mesFragment_to_gatnhosFragment)
        }

        binding.cardImageView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mesFragment_to_compromissosFragment)
        }

        binding.gastosImageView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mesFragment_to_gastosFragment)
        }







        (activity as AppCompatActivity).supportActionBar?.title =""
//        (activity as AppCompatActivity).supportActionBar?.subtitle = Html.fromHtml("<font color='#808080'><small>Mantenha simples. Mantenha o controle.</small> </font>")



        return binding.root

        //return inflater.inflate(R.layout.fragment_mes, container, false)


    }

    fun setaMetas(callback: () -> Unit) {

        metas.clear()
        var metaReservaDeEmergencia = Meta(0L, "Reserva de Emergência", 180.0, false,"", "")
        metas.add(metaReservaDeEmergencia)
        var metaInvestimento = Meta(0L, "Investimentos", 1000.00, false,"", "")
        metas.add(metaInvestimento)
        var metaGastos = Meta(0L, "Gasto", 1500.0, false,"", "")
        metas.add(metaGastos)
    }








}