package com.sleepysally.apps.mazikeen.api

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.sleepysally.apps.mazikeen.signature.model.InkPoint
import org.json.JSONArray
import org.json.JSONObject

class UserAPI {

    private val gson : Gson = Gson()
    private var queue : RequestQueue? = null
    private var context : Context

    private val BASE_URL = "https://aodaompig.xyz/wanda/api"

    companion object {
        private var singleton: UserAPI? = null;

        fun getCurrent(context: Context): UserAPI {
            if (singleton == null) {
                singleton = UserAPI(context)
            }
            return singleton!!
        }
    }

    private constructor(context: Context) {
        this.context = context
        queue = Volley.newRequestQueue(this.context)
    }

//    fun postSignature(points: ArrayList<InkPoint>) {
//        // post to the default user (John Smith)
//        postSignature("0", points)
//    }
    fun postSignature(userId: String, points: ArrayList<InkPoint>) {
        // SHOW STATUS = SUBMITTING
        val toast = Toast.makeText(this.context, "Submitting..", Toast.LENGTH_LONG);
        toast.show()

        val url = "${BASE_URL}/users/${userId}/signature"
        val signature = JSONArray(gson.toJson(points))

        val body = JSONObject()
        body.put("signature", signature)

        // Request a string response from the provided URL.
        val stringRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            Response.Listener<JSONObject> { response ->
                toast.cancel()

                Toast.makeText(context, "Signature data submitted", Toast.LENGTH_SHORT).show()
//                var builder = AlertDialog.Builder(this.context)
//                builder.setTitle("Response")
//                builder.setMessage(response.toString())
//                builder.create().show()
//                "Response is: ${response.toString()}";
            },
            Response.ErrorListener { error ->
                toast.cancel()

                var builder = AlertDialog.Builder(this.context)
                builder.setTitle("Error")
                builder.setMessage(error.message)
                builder.create().show()
            }
        )
        // Add the request to the RequestQueue.
        queue!!.add(stringRequest)
    }
}