package edu.pe.idat.plazaveadelivery.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import edu.pe.idat.plazaveadelivery.R

class SharedPrefCons(activity:Activity) {
    private var prefs: SharedPreferences? = null

    init{
        prefs = activity.getSharedPreferences(R.string.shared_pref_key.toString(), Context.MODE_PRIVATE)
    }
    fun save( key: String, objeto: Any){
        try {
            val json = Gson().toJson(objeto)
            with(prefs?.edit()){
                this?.putString(key, json)
                this?.commit()
            }
        }catch (e: Exception){
            Log.d("Error", "Err: ${e.message}")
        }
    }
    fun getData(key: String): String?{
        val data = prefs?.getString(key, "")
        return data
    }

    fun remove(key: String){
        prefs?.edit()?.remove(key)?.apply()
    }
}