package com.test.technical.movies

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY_PARAM_NAME = "api_key"
private const val CONNECT_TIMEOUT = 60L
private const val READ_TIMEOUT = 300L
private const val MAX_CACHE_SIZE = 1024L * 1024L * 16L
private const val CACHE_DIR_NAME = "themoviedb"

@Module
class AppModule(private val context: Context) {
  @Singleton
  @Provides
  fun provideTheMovieDBApi(retrofit: Retrofit): TheMovieDBApi = retrofit.create(TheMovieDBApi::class.java)

  @Singleton
  @Provides
  fun provideRetrofit(
      callAdapterFactory: CallAdapter.Factory,
      converterFactory: Converter.Factory,
      client: OkHttpClient
  ): Retrofit =
      Retrofit.Builder()
          .addCallAdapterFactory(callAdapterFactory)
          .addConverterFactory(converterFactory)
          .client(client)
          .baseUrl(BASE_URL)
          .build()

  @Singleton
  @Provides
  fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

  @Singleton
  @Provides
  fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

  @Singleton
  @Provides
  fun provideClient(cache: Cache, interceptors: List<@JvmSuppressWildcards Interceptor>): OkHttpClient =
      OkHttpClient.Builder()
          .cache(cache)
          .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
          .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
          .apply { interceptors.forEach { addInterceptor(it) }}
          .build()

  @Singleton
  @Provides
  fun provideInterceptors(): List<Interceptor> = listOf(
      Interceptor {
        val original = it.request()

        val url = original.url().newBuilder()
            .addQueryParameter(API_KEY_PARAM_NAME, context.getString(R.string.themoviedb_apiKey))
            .build()

        val requestBuilder = original.newBuilder().url(url)

        it.proceed(requestBuilder.build())
      }
  )

  @Singleton
  @Provides
  fun provideCache(): Cache = Cache(File(context.cacheDir, CACHE_DIR_NAME), MAX_CACHE_SIZE)
}
