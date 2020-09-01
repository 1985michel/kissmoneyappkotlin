package com.example.kissmoney.logo

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kissmoney.MainActivity
import com.example.kissmoney.R


class logoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                //set the new Content of your activity
                (activity as MainActivity).setContentView(R.layout.fragment_boasvindas)
            }
        }.start()

        return inflater.inflate(R.layout.fragment_logo, container, false)
    }
}