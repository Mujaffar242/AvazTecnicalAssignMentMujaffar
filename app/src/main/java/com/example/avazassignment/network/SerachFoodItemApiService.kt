package com.example.avazassignment.network

import com.example.avazassignment.model.SearchResultResponseModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

import android.text.TextUtils
import com.example.avazassignment.BuildConfig
import okhttp3.Credentials
import okhttp3.logging.HttpLoggingInterceptor
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.concurrent.TimeUnit


/*
* use retrofit library  for networking request
* */
private const val BASE_URL = "https://api.thenounproject.com/collection/"




interface GetFoodItemsApiService {
    @GET("{iconName}/icons?limit=10")
    fun serachFoodItem(@Path("iconName") iconName: String)
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            : Deferred<SearchResultResponseModel>
}


/*
* moshi library for remove callback methods of retrofit
* */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()








private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(RetrofitClient.getClient(BASE_URL).build())
    .baseUrl(BASE_URL)
    .build()



object GetFoodItemApi {
    val retrofitService: GetFoodItemsApiService by lazy { retrofit.create(GetFoodItemsApiService::class.java) }
}


/*
* create http client for add oauth1 signature with apis call
* */
class RetrofitClient {
    companion object {
        private var retrofit: Retrofit? = null
        fun getClient(baseUrl: String): OkHttpClient.Builder {
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.NONE
            }

            val consumer = OkHttpOAuthConsumer("2951ddc48cd74e73a118fc59b6ffa32e", "4b1e28cb926447ae9fed6d67e7611d42")

            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(60000, TimeUnit.SECONDS)
            httpClient.writeTimeout(120000, TimeUnit.SECONDS)
            httpClient.readTimeout(120000, TimeUnit.SECONDS)
            httpClient.retryOnConnectionFailure(true)
            httpClient.addInterceptor(SigningInterceptor(consumer))
            httpClient.addInterceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()


                val modifiedRequest = requestBuilder.build()
                chain.proceed(modifiedRequest)
            }

            httpClient.addNetworkInterceptor(logging)

            return httpClient
        }

    }
}