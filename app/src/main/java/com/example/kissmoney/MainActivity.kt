package com.example.kissmoney

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.kissmoney.contas.ContaJoinViewModel
import com.example.kissmoney.contas.ContaViewModel
import com.example.kissmoney.contas.mensal.MovimentacaoMensalViewModel
import com.example.kissmoney.database.KissmoneyDatabase
import com.example.kissmoney.databinding.ActivityMainBinding
import com.example.kissmoney.mes.MesViewModel
import kotlinx.coroutines.CoroutineScope


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)

        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        var cjvm = ContaJoinViewModel

        var contaViewModel = ViewModelProvider(this).get(ContaViewModel::class.java)
        var mesViewModel = ViewModelProvider(this).get(MesViewModel::class.java)
        var movimentacaoMensalViewModel= ViewModelProvider(this).get(MovimentacaoMensalViewModel::class.java)

        cjvm.setMyOwner(this){
            cjvm.setApplicationObject(contaViewModel, mesViewModel, movimentacaoMensalViewModel){
                cjvm.setAllContasJoin(){}
            }
        }

        //botão de navegação superior
        val navController = this.findNavController(R.id.myNavHostFragment)

        //abaixo sem o menu lateral
        NavigationUI.setupActionBarWithNavController(this, navController)

        //colocando o Logo no actionbar
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setLogo(R.drawable.kiss_logo_2);
        getSupportActionBar()?.setDisplayUseLogoEnabled(true);



    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)

        //sem o menu lateral
        return navController.navigateUp()

        //com o menu lateral
        //return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    //ocultando o teclado ao clicar fora
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        //O método abaixo oculta o teclado sempre que clicamos fora do editText
        //tem que colocar essa linha no layout (root) da view(xml):
        //android:focusableInTouchMode="true"

        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}