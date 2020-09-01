package com.example.kissmoney.interacao

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kissmoney.R
import com.example.kissmoney.databinding.FragmentBoasvindasBinding


class BoasvindasFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_boasvindas, container, false)

        val binding = DataBindingUtil.inflate<FragmentBoasvindasBinding>(inflater,R.layout.fragment_boasvindas,container,false)

        //apresentando o script inicial


        //binding.button.setOnClickListener {

            println(">>>>>>>>>>> entrando")
            val dialog = Dialog(activity as AppCompatActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.base_de_comunicacao)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            var msgEditText = dialog.findViewById(R.id.msg1textView) as TextView

            var texto = "Este é um grande texto que estou utilizando para verificar como fica.\nAté que fica bom"
            typingAnimation(msgEditText,texto,4 )


            var nextBtn = dialog.findViewById(R.id.nextButton) as ImageView
            var backBtn = dialog.findViewById(R.id.backButton) as ImageView
            var closeBtn = dialog.findViewById(R.id.closeButton) as ImageView

            nextBtn.setOnClickListener {  }
            backBtn.setOnClickListener {  }
            closeBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
            setLarguraEAltura(dialog) {}
        //}









        return binding.root
    }

    fun setLarguraEAltura(dialog: Dialog, callback: () -> Unit) {
        val height = getHeight {}
        val width = getWidth {}
        dialog.window?.setLayout(width, height / 2)
        callback()

    }

    private fun getHeight(callback: () -> Unit): Int {
        val height = Resources.getSystem().getDisplayMetrics().heightPixels
        return height
        callback()
    }

    private fun getWidth(callback: () -> Unit): Int {
        val width: Int = Resources.getSystem().getDisplayMetrics().widthPixels
        return width
        callback()
    }

    private fun typingAnimation(view: TextView, text: String, length: Int) {
        var delay = 100L
        if(Character.isWhitespace(text.elementAt(length-1))){
            delay = 300L
        }
        view.text = text.substring(0,length)
        when (length) {
            text.length -> return
            else -> Handler().postDelayed({
                typingAnimation(view, text, length+1 )
            }, delay)
        }
    }

}