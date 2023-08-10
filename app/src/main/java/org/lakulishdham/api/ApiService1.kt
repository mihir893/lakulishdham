package org.lakulishdham.api

import com.google.gson.JsonObject
import org.json.JSONObject
import org.lakulishdham.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService1 {

    @POST(Apis.GET_STATES)
    fun fetchStates(@Body jsonObject: JsonObject): Call<StateListResponse>

    @POST(Apis.GET_CITIES)
    fun fetchCities(@Body jsonObject: JsonObject): Call<DistrictListResponse>


}