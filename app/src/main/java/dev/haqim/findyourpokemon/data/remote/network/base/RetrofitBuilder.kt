package dev.haqim.findyourpokemon.data.remote.network.base

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import dev.haqim.findyourpokemon.BuildConfig

class RetrofitBuilder private constructor() {
    private var httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    private val baseUrl = "https://pokeapi.co/api/v2/"

    companion object{
        @Volatile private var instance: RetrofitBuilder? = null

        fun builder(): RetrofitBuilder {
            return instance ?: synchronized(this) {
                instance ?: RetrofitBuilder()
            }
        }
    }

    fun enableChucker(context: Context): RetrofitBuilder {
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
        }
        return this
    }
    
    fun addInterceptorLogger(): RetrofitBuilder {
        val logger = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logger.level = HttpLoggingInterceptor.Level.BODY
        }
        else {
            logger.level = HttpLoggingInterceptor.Level.BASIC
        }
        httpClientBuilder.addInterceptor(logger)
        
        return this
    }
    
    fun addInterceptorHeaderToken(token: String): RetrofitBuilder {
        httpClientBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token").build()
            chain.proceed(request)
        }

        return this
    }

    fun setConnectTimeOut(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS): RetrofitBuilder{
        httpClientBuilder.connectTimeout(timeout, unit)
        return this
    }

    fun setReadTimeOut(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS): RetrofitBuilder{
        httpClientBuilder.readTimeout(timeout, unit)
        return this
    }

    fun build(): Retrofit {
        val client = httpClientBuilder.build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

}
