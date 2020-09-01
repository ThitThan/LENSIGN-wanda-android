package com.sleepysally.apps.mazikeen.api

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
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
import java.util.*
import kotlin.collections.ArrayList

class SignatureAPI {
    companion object {
        const val BASE_URL = "https://lensign-signature-models.herokuapp.com"

        private var singleton: SignatureAPI? = null
        fun getCurrent(context: Context): SignatureAPI {
            if (singleton == null) {
                singleton = SignatureAPI(context)
            }
            return singleton!!
        }
    }

    private val gson : Gson = Gson()
    private var queue : RequestQueue
    private var context: Context
    private constructor(context: Context) {
        this.context = context
        queue = Volley.newRequestQueue(this.context)
    }

    fun classifySignature(userId: String, rawPoints: ArrayList<InkPoint>) {
        val url = "${BASE_URL}/model/classify"
//        val url = "${BASE_URL}/test-params"

        val START_TIME = rawPoints[0].timeMs
        val points = rawPoints.map { pt ->
            arrayOf(
                pt.x.toFloat(),
                pt.y.toFloat(),
                pt.pressure.toFloat(),
                (pt.timeMs - START_TIME).toFloat()
            )
        }
        val body = JSONObject().apply {
            put("x", JSONArray(gson.toJson(points)))
        }

        val toast = Toast.makeText(this.context, "Submitting..", Toast.LENGTH_LONG)

        // Request a string response from the provided URL.
        val stringRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            Response.Listener<JSONObject> { response ->
                var builder = AlertDialog.Builder(this.context)
                builder.setTitle("Response")
                builder.setMessage(response.toString(4))
                builder.create().show()
            },
            Response.ErrorListener { error ->
                var builder = AlertDialog.Builder(this.context)
                builder.setTitle("Error")
                builder.setMessage(error.localizedMessage)
                builder.create().show()
            }
        )
        AlertDialog.Builder(this.context).apply {
            setTitle("JSON")
            setMessage(Arrays.toString(stringRequest.body))
            setPositiveButton("SUBMIT") { _: DialogInterface, _: Int ->
                // SHOW STATUS = SUBMITTING
                toast.show()

                // Add the request to the RequestQueue.
                queue.add(stringRequest)
            }
        }.create().show()
    }
}