package dev.haqim.findyourpokemon.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.haqim.findyourpokemon.BuildConfig
import dev.haqim.findyourpokemon.data.local.room.PokemonDatabase
import dev.haqim.findyourpokemon.data.remote.RemoteDataSource
import dev.haqim.findyourpokemon.data.remote.network.callback.PokemonAPI
import dev.haqim.findyourpokemon.data.repository.PokemonRepository
import dev.haqim.findyourpokemon.domain.repository.IPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    
    companion object{
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context) =
            Room
                .databaseBuilder(context, PokemonDatabase::class.java, "find_your_pokemon.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule{

    companion object{
        @Provides
        @Singleton
        fun provideOkHttp() =
            OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

        @Provides
        @Singleton
        fun providerRetrofit(
            okHttpClient: OkHttpClient.Builder,
            @ApplicationContext context: Context
        ): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            else {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = okHttpClient
                .addInterceptor(loggingInterceptor)
                .addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .collector(ChuckerCollector(context))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
                .build()

            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        @Provides
        @Singleton
        fun providePokemonAPI(
            retrofit: Retrofit
        ): PokemonAPI {
            return retrofit.create(PokemonAPI::class.java)
        }
    }
    
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    
    @Binds
    @Singleton
    abstract fun providePokemonRepository(
        repository: PokemonRepository
    ): IPokemonRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIO

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @DispatcherIO
    @Provides
    fun provideCoroutineDispatcherIO() = Dispatchers.IO
}


