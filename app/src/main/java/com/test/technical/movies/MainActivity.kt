package com.test.technical.movies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.someText
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor {
      val original = it.request()
      val originalHttpUrl = original.url()

      val url = originalHttpUrl.newBuilder()
          .addQueryParameter("api_key", getString(R.string.themoviedb_apiKey))
          .build()

      val requestBuilder = original.newBuilder()
          .url(url)

      val request = requestBuilder.build()
      it.proceed(request)
    }

    val baseEndpoint = "https://api.themoviedb.org/3/"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseEndpoint)
        .client(clientBuilder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val theMovieDBApi = retrofit.create(TheMovieDBApi::class.java)
    theMovieDBApi.details(557)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          someText.text = it.toString()
        }
  }
}
