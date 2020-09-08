package com.example.kissmoney.contas

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kissmoney.R
import com.example.kissmoney.databinding.FragmentContasBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentContasBinding>(
            inflater, R.layout.fragment_contas, container, false
        )

        val tiposDeConta = TiposDeConta.values()

        binding.fab.setOnClickListener {

            val dialog = BottomSheetDialog(activity as AppCompatActivity)
            val view = layoutInflater.inflate(R.layout.crud_conta_botton_sheet, null)
            dialog.setContentView(view)

            var tiposSpinner = dialog.findViewById<Spinner>(R.id.tipoContaSpinner2)

            var adapter = ArrayAdapter(activity as AppCompatActivity, R.layout.spinner_item, TiposDeConta.values() )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tiposSpinner?.adapter = adapter


            dialog.show()
        }

        (activity as AppCompatActivity).supportActionBar?.title ="Kiss"





//        return inflater.inflate(R.layout.fragment_contas, container, false)

        return binding.root
    }


}