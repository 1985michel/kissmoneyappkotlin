package com.example.kissmoney.mes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.example.kissmoney.R
import com.example.kissmoney.databinding.FragmentChatBinding
import com.example.kissmoney.databinding.FragmentMesBinding
import com.example.kissmoney.interacao.chatFragment
import kotlinx.android.synthetic.main.fragment_mes.*


class mesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


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
        }

        binding.buttonRed.setOnClickListener {
            binding.textoCentralTextView.text = "Já foram gastos 76% do que foi ganho no mês."
        }

        binding.buttonGrey.setOnClickListener {
            binding.textoCentralTextView.text = "Você investiu 10% da sua renda. Parabéns!"
        }


        return binding.root

        //return inflater.inflate(R.layout.fragment_mes, container, false)


    }

}