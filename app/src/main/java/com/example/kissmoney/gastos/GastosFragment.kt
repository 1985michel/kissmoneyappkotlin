package com.example.kissmoney.gastos

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.kissmoney.MainActivity
import com.example.kissmoney.R
import com.example.kissmoney.contas.ContasFragmentArgs
import com.example.kissmoney.contas.ContasFragmentDirections
import com.example.kissmoney.databinding.FragmentGanhosBinding
import com.example.kissmoney.databinding.FragmentGastosBinding
import com.example.kissmoney.ganhos.GanhoJoinViewModel
import com.example.kissmoney.ganhos.GanhosFragmentDirections
import com.example.kissmoney.mes.CentralEstatistica
import com.example.kissmoney.mes.Estatisticas
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesViewModel
import com.example.kissmoney.util.*
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = ""
//        return inflater.inflate(R.layout.fragment_gastos, container, false)

        val binding = DataBindingUtil.inflate<FragmentGastosBinding>(
            inflater, R.layout.fragment_gastos, container, false
        )


        // início navegação entre meses
        val args = ContasFragmentArgs.fromBundle(requireArguments())
        var idMes = args.idMes


        mesAtual = Mes(idMes, getNomeMesAtual())
        mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))

        if (idMes != 0L) {
            mesViewModel.getById(mesAtual) {
                mesViewModel.getByName(mesAnterior) {
                    mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
                    mesViewModel.getByName(mesAnterior) {
                        //setDadosNaView(binding)
                        setMesAnteriorEMesPosterior(binding) {
                           // CentralEstatistica.processa {
                                setDadosNaView(binding)
                           // }
                        }
                    }
                }
            }
        } else {
            mesViewModel.getByName(mesAtual) {
                mesViewModel.getByName(mesAnterior) {
                    mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
                    mesViewModel.getByName(mesAnterior) {
                        setDadosNaView(binding)
                        setMesAnteriorEMesPosterior(binding){

                        }
                    }
                }
            }
        }


        binding.reprocessarIV.setOnClickListener {

            val toast = Toast.makeText(

                activity as AppCompatActivity,
                Html.fromHtml("<font color='#e3f2fd' ><b>" + "Dados recalculados!" + "</b></font>"),
                Toast.LENGTH_LONG
            )

            //colocando o toast verde
            toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))

            toast.show()


            CentralEstatistica.processa {

                setDadosNaView(binding)
//                GlobalScope.launch {
//                    //Background processing..."
//                    withContext(Dispatchers.Main) {
//                        //"Update UI here!")
//                        // para poder rodar na tread principal
//                        val toast = Toast.makeText(
//
//                            activity as AppCompatActivity,
//                            Html.fromHtml("<font color='#e3f2fd' ><b>" + "Dados recalculados!" + "</b></font>"),
//                            Toast.LENGTH_LONG
//                        )
//
//                        //colocando o toast verde
//                        toast.view?.setBackgroundColor(Color.parseColor("#32AB44"))
//
//                        toast.show()
//                    }
//                }


            }
        }


        return binding.root


    }

    private fun setDadosNaView(binding: FragmentGastosBinding) {


        GlobalScope.launch {
            //Background processing..."
            withContext(Dispatchers.Main) {
                //"Update UI here!")


                var estatisticaMesAtual = CentralEstatistica.estatisticasMensais.get(mesAtual.mesId)
                var estatisticaMesAnterior =
                    CentralEstatistica.estatisticasMensais.get(mesAnterior.mesId)

                println(">>>>>>>>>>>>>>>>>>>>>> Estatística obtida")
                if (estatisticaMesAtual != null) {

                    binding.nomeMesTextView.text = getNomeMesPorExtensoComAno(mesAtual.nomeMes)
                    binding.valorGastoTotalTextView.text =
                        formataParaBr(estatisticaMesAtual?.totalGastoNoMes!!.toBigDecimal())
                    binding.valorCompromissosPendentesTextView.text =
                        formataParaBr(estatisticaMesAtual?.totalCompromissosPendentesDoMes.toBigDecimal())
                    binding.valorCompromissosTextView2.text =
                        formataParaBr(estatisticaMesAtual?.totalCompromissosDoMes.toBigDecimal())

                    var previsaoCustoMensal =
                        estatisticaMesAtual?.totalCompromissosPendentesDoMes + estatisticaMesAtual.totalGastoNoMes
                    binding.valorPrevisaoMensalTextView.text =
                        formataParaBr(previsaoCustoMensal.toBigDecimal())

                    binding.custoDeVidaDiariotextView.text =
                        formataParaBr(estatisticaMesAtual?.gastoDiarioNoMes.toBigDecimal())

//                    binding.custoDiarioTodoHistoricoTV.text = formataParaBr(estatisticaMesAtual?.gastoDiarioTodoHistorico.toBigDecimal())
                    binding.custoDiarioTodoHistoricoTV.text = formataParaBr(CentralEstatistica.gastoDiarioTodoHistorico.toBigDecimal())

                    if (estatisticaMesAtual?.totalGastoNoMes!! + estatisticaMesAtual?.totalCompromissosPendentesDoMes!! < estatisticaMesAtual?.totalCompromissosDoMes ||
                        estatisticaMesAtual?.totalGastoNoMes < 0) {
                        binding.imageView8.visibility = View.VISIBLE
                        binding.textView40.visibility = View.VISIBLE
                    } else {
                        binding.imageView8.visibility = View.GONE
                        binding.textView40.visibility = View.GONE
                    }

                    if (estatisticaMesAnterior != null) {

                        if (estatisticaMesAtual?.totalGastoNoMes!! > 0) {

//                            var percentualVariacaoGasto =
//                                (estatisticaMesAnterior?.totalGastoNoMes!! * 100) / estatisticaMesAtual?.totalGastoNoMes!!

                            var percentualVariacaoGasto = 100 - (
                                ( estatisticaMesAtual?.totalGastoNoMes!! * 100)/estatisticaMesAnterior?.totalGastoNoMes!!)


//                            var percentualVariacaoGastoString =
//                                if (percentualVariacaoGasto > 0) " + "
//                                else if (percentualVariacaoGasto < 0) " - "
//                                else ""
//                            percentualVariacaoGastoString =
//                                "$percentualVariacaoGastoString ${percentualVariacaoGasto.toString()}"

                            var variacaoGastoPercentual = ""
                            var sinal = ""
                            if (estatisticaMesAnterior?.totalGastoNoMes!! > estatisticaMesAtual?.totalGastoNoMes!!) {
                                sinal = "-"
                                binding.textViewFeedBackGastos.text = "Parabéns! Você está reduzindo seus gastos!"
                            } else if (estatisticaMesAnterior?.totalGastoNoMes!! < estatisticaMesAtual?.totalGastoNoMes!!) {
                                sinal = "+"
                                binding.textViewFeedBackGastos.text = "Cuidado! Você está aumentando seus gastos!"
                            } else {
                                sinal = ""
                                binding.textViewFeedBackGastos.text = "Seus gastos estão iguais ao mês anterior. Vamos economizar?"
                            }

                            variacaoGastoPercentual =
                                "$sinal ${formataComNCasasDecimais( percentualVariacaoGasto, 1)} %"



                            binding.comparacaoMesAnteriorGastoTotalTextView.text =
                                variacaoGastoPercentual.replace('.',',')


                        } else {
                            binding.textViewFeedBackGastos.text = ""
                            binding.comparacaoMesAnteriorGastoTotalTextView.text = "0.0%"
                        }


                        if (previsaoCustoMensal > 0) {

                            var previsaoCustoMensalMesAnterior =
                                estatisticaMesAnterior?.totalCompromissosPendentesDoMes + estatisticaMesAnterior.totalGastoNoMes


//                            var percentualVariacaoPrevisaoCustoMensal =
//                                previsaoCustoMensalMesAnterior * 100 / previsaoCustoMensal

                            var percentualVariacaoPrevisaoCustoMensal = 100 - (previsaoCustoMensal * 100 / previsaoCustoMensalMesAnterior)

                            //operações de divisão com números positivos sempre serão positivos
//                            var percentualVariacaoPrevisaoCustoString =
//                                if (percentualVariacaoPrevisaoCustoMensal > 0) " + "
//                                else if (percentualVariacaoPrevisaoCustoMensal < 0) " - "
//                                else ""

                            var variacaoGastoPercentual = ""
                            var sinal = ""
                            if (previsaoCustoMensalMesAnterior > previsaoCustoMensal) {
                                sinal = "-"
                                binding.textViewFeedBackGastosPrevisao.text = "Parabéns! Você está no caminho de reduzir seus gastos!"
                            } else if (previsaoCustoMensalMesAnterior < previsaoCustoMensal) {
                                sinal = "+"
                                binding.textViewFeedBackGastosPrevisao.text = "Cuidado! Você está aumentando seus gastos!"
                            } else {
                                sinal = ""
                                binding.textViewFeedBackGastosPrevisao.text = "Seus gastos seguem o mesmo padrão do mês anterior."
                            }




                            variacaoGastoPercentual = " $sinal ${formataComNCasasDecimais(percentualVariacaoPrevisaoCustoMensal,1)} %"
//                            percentualVariacaoPrevisaoCustoString =
//                                "$percentualVariacaoPrevisaoCustoString ${percentualVariacaoPrevisaoCustoMensal.toString()}"
                            binding.comparacaoAoMesAnteriorPrevisaoTextView.text =
                                variacaoGastoPercentual.replace('.',',')

                        } else {
                            binding.comparacaoAoMesAnteriorPrevisaoTextView.text = "0.0 %"
                            binding.textViewFeedBackGastosPrevisao.text = ""
                        }


                    }
                } else {
                    CentralEstatistica.addMes(mesAtual){setDadosNaView(binding)}
                }

//                binding.nomeMesTextView.text = getNomeMesPorExtensoComAno(mesAtual.nomeMes)
//                binding.valorGastoTotalTextView.text =
//                    formataParaBr(estatisticaMesAtual?.totalGastoNoMes!!.toBigDecimal())
//                binding.valorCompromissosPendentesTextView.text =
//                    formataParaBr(estatisticaMesAtual?.totalCompromissosPendentesDoMes.toBigDecimal())
//                binding.valorCompromissosTextView2.text =
//                    formataParaBr(estatisticaMesAtual?.totalCompromissosDoMes.toBigDecimal())
//                binding.valorPrevisaoMensalTextView.text =
//                    formataParaBr((estatisticaMesAtual?.totalCompromissosPendentesDoMes + estatisticaMesAtual.totalGastoNoMes).toBigDecimal())
//                binding.custoDeVidaDiariotextView.text =
//                    formataParaBr(estatisticaMesAtual?.gastoDiarioNoMes.toBigDecimal())
//
//                var percentualVariacaoGasto = (estatisticaMesAnterior?.totalGastoNoMes!! * 100 ) / estatisticaMesAtual?.totalGastoNoMes!!
//                binding.comparacaoMesAnteriorGastoTotalTextView.text = percentualVariacaoGasto.toString()


            }
        }

    }

    private fun setMesAnteriorEMesPosterior(binding: FragmentGastosBinding, callback: () -> Unit) {
        //mesAnterior = Mes(0L, recebeNomeMesRetornaNomeMesAnterior(mesAtual.nomeMes))
        mesPosterior = Mes(0L, recebeNomeMesRetornaNomeMesPosterior(mesAtual.nomeMes))
        //mesViewModel.getByName(mesAnterior) {}
        mesViewModel.getByName(mesPosterior) {

            println(">>>>>>>>>>>>>>>> MES POSTERIOR ${mesPosterior.nomeMes}   ID: ${mesPosterior.mesId}")

            GlobalScope.launch {
                //Background processing..."
                withContext(Dispatchers.Main) {
                    //"Update UI here!")
                    // para poder rodar na tread principal
                    binding.mesAnteriorImageViewGastos.setOnClickListener {
                        Navigation.findNavController(requireView())
                            .navigate(GastosFragmentDirections.actionGastosFragmentSelf(mesAnterior.mesId))
                    }
                    binding.mesPosteriorImageViewGastos.setOnClickListener {
                        Navigation.findNavController(requireView())
                            .navigate(GastosFragmentDirections.actionGastosFragmentSelf(mesPosterior.mesId))
                    }
                    callback()
                }
            }

        }


    }


}