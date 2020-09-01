package com.example.kissmoney.interacao

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kissmoney.R
import com.example.kissmoney.databinding.FragmentChatBinding


class chatFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_boasvindas, container, false)

        val binding = DataBindingUtil.inflate<FragmentChatBinding>(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )

        val args = chatFragmentArgs.fromBundle(requireArguments())
        var etapa = args.msgs

        if (etapa.equals("boas vindas")) {
            var msgs = getBoasVindasStrings()
            chatManager(msgs,0)

        }






        return binding.root
    }

    fun chatManager(msgs: HashMap<Int, String>, posicao: Int){
        var posi = posicao
        msgs.get(posicao)?.let {
            chating(it){
                if (posi<msgs.size){
                    posi++
                    chatManager(msgs,posi)
                }
            }
        }
    }

    private fun chating(texto : String, callback: () -> Unit) {

        val dialog = Dialog(activity as AppCompatActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.base_de_comunicacao)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var msgEditText = dialog.findViewById(R.id.msg1textView) as TextView

        val mp = playSound()
        typingAnimation(msgEditText, texto, 1, mp)


        var nextBtn = dialog.findViewById(R.id.nextButton) as ImageView
        var backBtn = dialog.findViewById(R.id.backButton) as ImageView
        var closeBtn = dialog.findViewById(R.id.closeButton) as ImageView

        nextBtn.setOnClickListener {
            dialog.dismiss()
            callback()

        }
        backBtn.setOnClickListener { callback() }
        closeBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
        setLarguraEAltura(dialog) {}
    }


    private fun typingAnimation(view: TextView, text: String, length: Int, mp: MediaPlayer) {

        var delay = 100L
        if (Character.isWhitespace(text.elementAt(length - 1))) {
            delay = 200L
        }
        view.text = text.substring(0, length)

        when (length) {
            text.length -> {
                mp.stop()
                return
            }
            else -> Handler().postDelayed({
                typingAnimation(view, text, length + 1,mp)
            }, delay)
        }


    }

    fun playSound(): MediaPlayer {
        Log.v(TAG, "Initializing sounds...")


        var resId = getResources().getIdentifier(R.raw.keyboard3.toString(),
            "raw", activity?.packageName)

        val mp: MediaPlayer = MediaPlayer()
        val mediaPlayer = MediaPlayer.create(activity, resId)
        mediaPlayer.isLooping =  true
        mediaPlayer.start()

        return mediaPlayer

    }

    fun getBoasVindasStrings() : HashMap<Int,String> {
        var map = HashMap<Int, String>()

        map.put(0,getString(R.string.bv1))
        map.put(1,getString(R.string.bv2))
        map.put(2,getString(R.string.bv3))
        map.put(3,getString(R.string.bv4))
        map.put(4,getString(R.string.bv5))
        map.put(5,getString(R.string.bv6))
        map.put(6,getString(R.string.bv7))
        map.put(7,getString(R.string.bv8))
        map.put(8,getString(R.string.bv9))
        map.put(9,getString(R.string.bv10))
        map.put(10,getString(R.string.bv11))
        map.put(11,getString(R.string.bv12))
        map.put(12,getString(R.string.bv13))
        map.put(13,getString(R.string.bv14))
        map.put(14,getString(R.string.bv15))
        map.put(15,getString(R.string.bv16))

        return map
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

}