package com.sleepysally.apps.mazikeen

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sleepysally.apps.mazikeen.api.UserController
import com.sleepysally.apps.mazikeen.signature.InkView
import com.sleepysally.apps.mazikeen.signature.model.InkPoint
import kotlinx.android.synthetic.main.fragment_sign_board.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * A placeholder fragment containing a simple view.
 */
class SignBoardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GET DISPLAY METRICS
        var dm = DisplayMetrics()
        this.activity?.windowManager?.defaultDisplay?.getMetrics(dm)

        // INIT INK VIEW
        inkView.init(dm)
        inkView.onSigningFinished = { inkPoints: ArrayList<InkPoint> ->

//            // manually generates JSON
//            val msgBuilder = StringBuilder()
//            msgBuilder.append("[")
//            msgBuilder.append("\n")
//            for (point in inkPoints) {
//                msgBuilder.append(point.toString())
//            }
//            msgBuilder.append("]")
//
//            val signature = msgBuilder.toString()
//            Log.d("SIGNATURE PATH", signature)
//
//
//            // show alert dialog
//            var builder = AlertDialog.Builder(this.context)
//            builder.setTitle("Your Signature Data")
//            builder.setMessage(signature)
//
//            var dialog = builder.create()
//            dialog.show()

            UserController.getCurrent(this.context!!).postSignature("-M3Zgg5kQsCE4OozFU_m", inkPoints)

            // should clear the board?
            true
        }

        fab.setOnClickListener {
            inkView.clear()
        }
    }
}
