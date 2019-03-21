package com.saad.qrcodeandscanner

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by fahad.waqar on 2/8/2018.
 */
interface ApiInterface {


    @POST("barcodes/search.php")
    fun movieAPi(@Query("code")code: String): Observable<Response<MovieModel>>


    companion object {
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://testing-a13ac.firebaseio.com/")
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

        fun feedMecreate(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.androidhive.info/")
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    }
}