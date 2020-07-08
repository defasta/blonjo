package com.apdef.mentari.views

import java.text.NumberFormat
import java.util.*

class Utils {
    companion object{
        fun rupiah(number: Double): String{
            val LocaleID = Locale("in","ID")
            val numberFormat= NumberFormat.getCurrencyInstance(LocaleID)
            return numberFormat.format(number).toString()
        }
    }
}