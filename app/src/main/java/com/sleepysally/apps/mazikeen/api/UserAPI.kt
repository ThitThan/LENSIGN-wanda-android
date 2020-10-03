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
}