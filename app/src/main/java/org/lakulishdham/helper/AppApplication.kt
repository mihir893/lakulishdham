package org.lakulishdham.helper

import android.app.Application
import android.os.Handler
import androidx.lifecycle.*
import com.adcreators.youtique.helper.PrefUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.lakulishdham.BuildConfig
import org.lakulishdham.api.Apis
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class AppApplication : Application(){
    companion object {
        var gson: Gson? = null
        var retrofit: Retrofit? = null
        var newRetrofit: Retrofit? = null
    }

    override fun onCreate() {
        super.onCreate()
        initGson()
        initRetrofit()
        initNewRetrofit();
        AppLogger.e("APPLICATION==>ON_CREATE")
    }

    private fun initRetrofit() {
        val cacheSize = 20 * 1024 * 1024 // 20 MB
        val cache = Cache(cacheDir, cacheSize.toLong())

        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }

        val okHttpClient = OkHttpClient.Builder()
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(null)
            .addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                if (Functions.isInternetConnected(applicationContext)) {
                    val maxAge = 60 * 5 // read from cache for 2 minute
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()

                } else {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(HeaderInterceptor())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(Apis.getBaseURL())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson!!))
            .build()


    }

    private fun initNewRetrofit() {
        val cacheSize = 20 * 1024 * 1024 // 20 MB
        val cache = Cache(cacheDir, cacheSize.toLong())

        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }

        val okHttpClient = OkHttpClient.Builder()
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(null)
            .addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                if (Functions.isInternetConnected(applicationContext)) {
                    val maxAge = 60 * 5 // read from cache for 2 minute
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()

                } else {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        newRetrofit = Retrofit.Builder()
            .baseUrl(Apis.NEW_DATA_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson!!))
            .build()


    }


    private inner class HeaderInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            var authKey: String
            authKey = PrefUtils.getAuthKey(applicationContext)

            if (authKey.isNotEmpty()) {
                authKey = "Bearer $authKey"
            }

            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", authKey)
                .build()
            return chain.proceed(newRequest)
        }
    }


    private fun initGson() {
        gson = GsonBuilder()
            .setLenient()
            .create()
    }

}