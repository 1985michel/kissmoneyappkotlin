package com.example.kissmoney.logo

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.kissmoney.MainActivity
import com.example.kissmoney.R
import com.example.kissmoney.databinding.FragmentBoasvindasBinding
import com.example.kissmoney.databinding.FragmentLogoBinding


class LogoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentLogoBinding>(inflater,R.layout.fragment_logo,container,false)


        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                //set the new Content of your activity
                //(activity as MainActivity).setContentView(R.layout.fragment_boasvindas)
                Navigation.findNavController(requireView()).navigate(R.id.action_logoFragment_to_boasvindasFragment)

            }
        }.start()

        return binding.root
        //return inflater.inflate(R.layout.fragment_logo, container, false)
    }
}