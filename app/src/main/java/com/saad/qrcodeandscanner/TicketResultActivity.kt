package com.saad.qrcodeandscanner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import org.json.JSONObject

class TicketResultActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private var txtName: TextView? = null
    private var txtDuration: TextView? = null
    private var txtDirector: TextView? = null
    private var txtGenre: TextView? = null
    private var txtRating: TextView? = null
    private var txtPrice: TextView? = null
    private var txtError: TextView? = null
    private var imgPoster: ImageView? = null
    private var btnBuy: Button? = null
    private var progressBar: ProgressBar? = null
    private var ticketView: TicketView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_result)

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        txtName = findViewById(R.id.name)
        txtDirector = findViewById(R.id.director)
        txtDuration = findViewById(R.id.duration)
        txtPrice = findViewById(R.id.price)
        txtRating = findViewById(R.id.rating)
        imgPoster = findViewById(R.id.poster)
        txtGenre = findViewById(R.id.genre)
        btnBuy = findViewById(R.id.btn_buy)
        imgPoster = findViewById(R.id.poster)
        txtError = findViewById(R.id.txt_error)
        ticketView = findViewById(R.id.layout_ticket)
        progressBar = findViewById(R.id.progressBar)

        val barcode = intent.getStringExtra("code")

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(applicationContext, "Barcode is empty!", Toast.LENGTH_LONG).show()
            finish()
        }

        // search the barcode
        callMovieApi(barcode)
    }

    /**
     * Searches the barcode by making http call
     * Request was made using Volley network library but the library is
     * not suggested in production, consider using Retrofit
     */
    /*private fun searchBarcode(barcode: String) {
        // making volley's json request
        val jsonObjReq = JsonObjectRequest(Request.Method.GET,
                URL + barcode, null,
                object : Response.Listener<JSONObject>() {

                    fun onResponse(response: JSONObject) {
                        Log.e(TAG, "Ticket response: $response")

                        // check for success status
                        if (!response.has("error")) {
                            // received movie response
                            renderMovie(response)
                        } else {
                            // no movie found
                            showNoTicket()
                        }
                    }
                }, object : Response.ErrorListener() {

            fun onErrorResponse(error: VolleyError) {
                Log.e(TAG, "Error: " + error.getMessage())
                showNoTicket()
            }
        })

        MyApplication.getInstance().addToRequestQueue(jsonObjReq)
    }*/

    private fun callMovieApi(barcode: String) {
        if (checkNetworkConnectivity(this@TicketResultActivity)) {
            showDialog(this@TicketResultActivity)
            disposable = ApiInterface.feedMecreate().movieAPi(barcode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.isSuccessful) {
                            dismissDialog()
                            renderMovie(result.body())
                            // no movie found


                        } else {
                            dismissDialog()
                            showNoTicket()
                            toast("something went wrong.")
                        }
                    }, { error ->
                        dismissDialog()
                        showNoTicket()
                        toast("something went wrong.")

                    }

                    )
        } else {
            toast("please check internet connection")
        }
    }

    private fun showNoTicket() {
        txtError!!.visibility = View.VISIBLE
        ticketView!!.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }

    /**
     * Rendering movie details on the ticket
     */
    private fun renderMovie(movie: MovieModel?) {
        try {

            // converting json to movie object
//            val movie = Gson().fromJson(response.toString(), Movie::class.java)

            if (movie != null) {
                txtName!!.text = movie!!.name
                txtDirector!!.text = movie.director
                txtDuration!!.text = movie.duration
                txtGenre!!.text = movie.genre
                txtRating!!.text = "" + movie.rating
                txtPrice!!.text = movie.price
                Glide.with(this).load(movie.poster).into(imgPoster!!)

                /*if (movie.isReleased) {
                    btnBuy!!.text = getString(R.string.btn_buy_now)
                    btnBuy!!.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                } else {
                    btnBuy!!.text = getString(R.string.btn_coming_soon)
                    btnBuy!!.setTextColor(ContextCompat.getColor(this, R.color.btn_disabled))
                }*/
                ticketView!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.GONE
            } else {
                // movie not found
                showNoTicket()
            }
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON Exception: " + e.message)
            showNoTicket()
            Toast.makeText(applicationContext, "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // exception
            showNoTicket()
            Toast.makeText(applicationContext, "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class Movie {
        var name: String? = null
            internal set
        var director: String? = null
            internal set
        var poster: String? = null
            internal set
        var duration: String? = null
            internal set
        var genre: String? = null
            internal set
        var price: String? = null
            internal set
        var rating: Float = 0.toFloat()
            internal set

        @SerializedName("released")
        var isReleased: Boolean = false
            internal set
    }

    companion object {
        private val TAG = TicketResultActivity::class.java.simpleName

        // url to search barcode
        private val URL = "https://api.androidhive.info/barcodes/search.php?code="
    }
}
