package org.lakulishdham.api

import com.google.gson.JsonObject
import org.lakulishdham.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST(Apis.LOGIN)
    fun doLogin(@Body loginRequest: LoginRequest): Call<VerifyResponse>

    @POST(Apis.SEND_OTP)
    fun sendOTP(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST(Apis.VERIFY_OTP)
    fun verifyOTP(@Body loginRequest: LoginRequest): Call<VerifyResponse>

    @POST(Apis.RESET_PASSWORD)
    fun resetPassword(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET(Apis.GALLERY_LIST)
    fun galleryList(): Call<GalleryListResponse>

    @GET(Apis.SUBSCRIPTION_LIST)
    fun getSubscriptions(): Call<SubscriptionListResponse>

    @POST(Apis.LOGOUT)
    fun logout(): Call<LoginResponse>

    @POST(Apis.GENERATE_ORDER)
    fun generateOrder(@Body request: GenerateOrderRequest): Call<GenerateOrderResponse>

    @POST(Apis.GENERATE_SUBSCRIPTION)
    fun generateSubscription(@Body request: GenerateOrderRequest): Call<GenerateSubscriptionResponse>

    @GET(Apis.COUNTRY_LIST)
    fun countryList(): Call<CountryListResponse>

    @POST(Apis.SIGN_UP)
    fun signUp(@Body request: SignupRequest): Call<VerifyResponse>

    @POST(Apis.UPDATE_PROFILE)
    fun updateProfile(@Body request: SignupRequest): Call<LoginResponse>

    @POST(Apis.UPDATE_PASSWORD)
    fun updatePassword(@Body request: UpdatePasswordRequest): Call<BaseResponse>

    @GET(Apis.MEMBER_LIST)
    fun getMembers(): Call<MemberListResponse>

    @POST(Apis.ADD_MEMBER)
    fun addMembers(@Body jsonObject: JsonObject): Call<AddMemberResponse>

    @POST(Apis.UPDATE_MEMBER)
    fun updateMembers(@Body jsonObject: JsonObject): Call<AddMemberResponse>

    @POST(Apis.DELETE_MEMBER)
    fun DeleteMembers(@Body jsonObject: JsonObject): Call<BaseResponse>

    @POST(Apis.ADD_DONATION)
    fun AddDonations(@Body request: AddDonationRequest): Call<AddDonationsResponse>

    @GET(Apis.GET_DONATIONS)
    fun GetDonations(): Call<DonationListResponse>

    @POST(Apis.CANCEL_SUBSCRIPTION)
    fun CancelSubscription(): Call<LoginResponse>
}