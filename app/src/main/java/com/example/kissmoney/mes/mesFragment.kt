package com.example.kissmoney.mes

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.kissmoney.R
import com.example.kissmoney.databinding.FragmentMesBinding
import kotlinx.android.synthetic.main.item_compromisso.*


class mesFragment : Fragment() {

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



        val binding = DataBindingUtil.inflate<FragmentMesBinding>(
            inflater,
            R.layout.fragment_mes,
            container,
            false
        )



        binding.progressBarDiasDeLiberdade.setOnClickListener {
            binding.textoCentralTextView.text = "125 Dias de Liberdade.\n Meta: 180 dias"
        }

        binding.progressBarGastoNoMes.setOnClickListener {
            binding.textoCentralTextView.text = "Já foram gastos 76% do que foi ganho no mês."
        }

        binding.progressBarPoupadoNoMes.setOnClickListener {
            binding.textoCentralTextView.text = "Você investiu 10% da sua renda. Parabéns!"
        }

        binding.buttonGreen.setOnClickListener {
            binding.textoCentralTextView.text = "125 Dias de Liberdade.\n Meta: 180 dias"
            binding.progressBarDiasDeLiberdade.alpha = 1F
            binding.progressBarGastoNoMes.alpha = 0.1F
            binding.progressBarPoupadoNoMes.alpha = 0.1F
        }

        binding.buttonRed.setOnClickListener {
            binding.textoCentralTextView.text = "Já foram gastos 76% do que foi ganho no mês."

            binding.progressBarDiasDeLiberdade.alpha = 0.1F
            binding.progressBarGastoNoMes.alpha = 0.1F
            binding.progressBarPoupadoNoMes.alpha = 1F
        }

        binding.buttonGrey.setOnClickListener {
            binding.textoCentralTextView.text = "Você investiu 10% da sua renda. Parabéns!"

            binding.progressBarDiasDeLiberdade.alpha = 0.1F
            binding.progressBarGastoNoMes.alpha = 1F
            binding.progressBarPoupadoNoMes.alpha = 0.1F

        }

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

        (activity as AppCompatActivity).supportActionBar?.title ="Kiss"
        (activity as AppCompatActivity).supportActionBar?.subtitle = Html.fromHtml("<font color='#808080'><small>Mantenha simples. Mantenha o controle.</small> </font>")



        return binding.root

        //return inflater.inflate(R.layout.fragment_mes, container, false)


    }








}