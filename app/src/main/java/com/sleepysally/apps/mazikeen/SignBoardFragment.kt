package com.sleepysally.apps.mazikeen

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sleepysally.apps.mazikeen.signature.InkView
import kotlinx.android.synthetic.main.fragment_sign_board.*

/**
 * A placeholder fragment containing a simple view.
 */
class SignBoardFragment : Fragment() {
    var mInkView : InkView? = null

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

        fab.setOnClickListener {
            inkView.clear()
        }
    }
}
