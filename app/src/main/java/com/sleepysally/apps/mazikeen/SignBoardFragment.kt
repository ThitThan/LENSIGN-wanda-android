package com.sleepysally.apps.mazikeen

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.sleepysally.apps.mazikeen.api.SignatureAPI
import com.sleepysally.apps.mazikeen.api.UserAPI
import com.sleepysally.apps.mazikeen.signature.model.InkPoint
import kotlinx.android.synthetic.main.fragment_sign_board.*

/**
 * A placeholder fragment containing a simple view.
 */
class SignBoardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding =  FragmentSignBoardBinding.inflate(inflater, container, false)
//        return binding.root
        return inflater.inflate(R.layout.fragment_sign_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modeSelector.addTab(
            modeSelector.newTab().apply {
                text = "Collect Data"
            }
        )
        modeSelector.addTab(
            modeSelector.newTab().apply {
                text = "Classify"
            }
        )

        // INIT INK VIEW
        inkView.onSigningFinished = { inkPoints: ArrayList<InkPoint> ->
//            UserAPI.getCurrent(this.context!!).postSignature("-M3Zgg5kQsCE4OozFU_m", inkPoints)
            SignatureAPI.getCurrent(requireActivity()).classifySignature("-M3Zgg5kQsCE4OozFU_m", inkPoints)

            // should clear the board?
            true
        }

        fab.setOnClickListener {
            inkView.clear()
        }
    }
}
