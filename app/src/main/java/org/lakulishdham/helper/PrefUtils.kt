package com.adcreators.youtique.helper

import android.content.Context
import com.google.gson.Gson
import org.lakulishdham.model.UserData

object PrefUtils {

    //dummy for now
    var AUTH_KEY = "AUTH_KEY"
    var USER_DATA = "USER_DATA"
    var IS_LOGIN = "IS_LOGIN"

    fun clearAll(context: Context) {
        Prefs.with(context).removeAll()
    }

    fun setAuthKey(context: Context, authKey: String) {
        Prefs.with(context).save(AUTH_KEY, authKey)
    }

    fun getAuthKey(context: Context): String {
        return Prefs.with(context).getString(AUTH_KEY, "")!!
    }

    fun setUserData(context: Context,data: UserData) {
        val s = Gson().toJson(data)
        Prefs.with(context).save(USER_DATA,s)
    }

    fun getUserData(context: Context) : UserData {
        val data : UserData = Gson().fromJson(Prefs.with(context).getString(USER_DATA,""),UserData::class.java)
        return data
    }

    fun setUserLogin(context: Context,isLogin : Boolean) {
        Prefs.with(context).save(IS_LOGIN,isLogin)
    }

    fun isUserLogin(context: Context) : Boolean {
        return Prefs.with(context).getBoolean(IS_LOGIN,false)
    }

}