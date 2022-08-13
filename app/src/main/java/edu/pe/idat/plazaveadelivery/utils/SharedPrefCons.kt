package edu.pe.idat.plazaveadelivery.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SharedPrefCons(activity:Activity) {
    private var prefs: SharedPreferences? = null

    init{
        prefs = activity.getSharedPreferences("edu.pe.idat.plazaveadelivery", Context.MODE_PRIVATE)
    }

    fun remove(key: String){
        prefs?.edit()?.remove(key)?.apply()
    }

    fun setSomeBooleanValue(nombre: String, valor: Boolean) {
        val edit = prefs?.edit()
        edit?.putBoolean(nombre, valor)
        edit?.apply()
    }

    fun getSomeBooleanValue(nombre: String) : Boolean {
        return prefs?.getBoolean(nombre,false) ?: false
    }
}