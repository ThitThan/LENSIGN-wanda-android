package com.sleepysally.apps.mazikeen.api

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.sleepysally.apps.mazikeen.signature.model.DatasetModel
import com.sleepysally.apps.mazikeen.signature.model.InkPoint
import com.sleepysally.apps.mazikeen.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class DatasetsAPI {
    companion object {
        const val BASE_URL = "https://lensign-signature-models.herokuapp.com"

        private var singleton: DatasetsAPI? = null
        fun getCurrent(context: Context): DatasetsAPI {
            if (singleton == null) {
                singleton = DatasetsAPI(context)
            }
            return singleton!!
        }
    }

    val gson = Gson()
    val queue : RequestQueue by lazy {
        Volley.newRequestQueue(this.context)
    }

    private var context: Context
    private constructor(context: Context) {
        this.context = context
    }

    fun getDatasetsList(onFinish: (List<DatasetModel>) -> Unit, onError: ((VolleyError) -> Unit)?) {
        // Request a string response from the provided URL.
        val req = JsonObjectRequest(
            Request.Method.GET,
            "$BASE_URL/training/datasets",
            JSONObject(),
            Response.Listener<JSONObject> { response ->
//                AlertDialog.Builder(context).apply {
//                    setTitle("Response")
//                    setMessage(response.toString(4))
//                }.create().show()
                val result = arrayListOf<DatasetModel>()
                val jsonArr = response.getJSONArray("data")
                for (i in 0 until jsonArr.length()) {
                    val jsonObj = jsonArr[i].toString()
                    result.add(gson.fromJson(jsonObj, DatasetModel::class.java))
                }

                onFinish.invoke(result)
            },
            Response.ErrorListener { error ->
                onError?.invoke(error)
            }
        )
        queue.add(req)
    }

    fun postSignature(datasetId: String, points: ArrayList<InkPoint>, onFinish: (JSONObject) -> Unit, onError: ((VolleyError) -> Unit)?) {
        // SHOW STATUS = SUBMITTING
        val toast = Toast.makeText(this.context, "Submitting..", Toast.LENGTH_LONG);
        toast.show()

        val url = "$BASE_URL/training/datasets/$datasetId/signatures"
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

                onFinish.invoke(response)
            },
            Response.ErrorListener { error ->
                toast.cancel()
                onError?.invoke(error)
            }
        )
        // Add the request to the RequestQueue.
        queue!!.add(stringRequest)
    }
}