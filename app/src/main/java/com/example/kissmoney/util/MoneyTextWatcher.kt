package com.example.kissmoney.util


import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*


class MoneyTextWatcher : TextWatcher {



    private val editTextWeakReference: WeakReference<EditText?>?
    private val locale: Locale = Locale("pt", "BR")
    //private final Locale locale;

    constructor(editText: EditText?, locale: Locale?) {
        editTextWeakReference = WeakReference<EditText?>(editText)
        //this.locale = if (locale != null) locale else Locale.getDefault()
    }

    constructor(editText: EditText?) {
        editTextWeakReference = WeakReference<EditText?>(editText)
        //locale = Locale.getDefault()
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun afterTextChanged(editable: Editable?) {
        val editText: EditText = editTextWeakReference?.get() ?: return
        editText.removeTextChangedListener(this)

        var isNegative = false
        var editableString = editable.toString()
        if (editable != null) {
            if (editableString.contains('-')) {
                isNegative = true
                if (editable != null) {
                    editableString = editableString.replace("-","")
                }
            }
        }

        val parsed: BigDecimal? = parseToBigDecimal(editableString, locale)
        //val parsed: BigDecimal? = parseToBigDecimal(editable.toString(), locale)
        var formatted: String = NumberFormat.getCurrencyInstance(locale).format(parsed)

        if (isNegative && !(formatted.equals("R\$ 0,00") || formatted.equals("-R\$ 0,00"))) formatted = "-${formatted}"
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    private fun parseToBigDecimal(value: String?, locale: Locale?): BigDecimal? {
        val replaceable = java.lang.String.format(
            "[%s,.\\s]",
            NumberFormat.getCurrencyInstance(locale).currency.symbol
        )
        val cleanString = value!!.replace(replaceable.toRegex(), "")
        return BigDecimal(cleanString).setScale(
            2, BigDecimal.ROUND_FLOOR
        ).divide(
            BigDecimal(100), BigDecimal.ROUND_FLOOR
        )
    }
}

//como invocar
//binding.editTextValorCaixa.addTextChangedListener(MoneyTextWatcher(binding.editTextValorCaixa, Locale("pt", "BR")))