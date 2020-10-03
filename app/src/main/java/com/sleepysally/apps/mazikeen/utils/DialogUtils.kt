package com.sleepysally.apps.mazikeen.utils

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import java.io.UnsupportedEncodingException


object DialogUtils {
    fun showVolleyError(context: Context, error: VolleyError) {
        //get status code here
        val statusCode = error.networkResponse.statusCode.toString()
        var errorMessage = "..."
        //get response body and parse with appropriate encoding
        if (error.networkResponse.data != null) {
            try {
                errorMessage = String(error.networkResponse.data, Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }

        AlertDialog.Builder(context).apply {
            setTitle("Error $statusCode")
            setMessage(errorMessage)
            setNeutralButton("OK") { dialog: DialogInterface, i: Int ->
                dialog.dismiss()
            }
            setPositiveButton("COPY") { dialog: DialogInterface, i: Int ->
                dialog.dismiss()

                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("errorMsg", errorMessage)
                clipboard.setPrimaryClip(clip)
            }
        }.create().show()
    }

    fun createWithRecyclerView(context: Activity, title: String, adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>): AlertDialog.Builder {
        var mRecyclerView = RecyclerView(context)
        // you can use LayoutInflater.from(context).inflate(...) if you have xml layout
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.setAdapter(adapter)

        return AlertDialog.Builder(context).apply {
            setTitle(title)
            setView(mRecyclerView)
        }
    }
}