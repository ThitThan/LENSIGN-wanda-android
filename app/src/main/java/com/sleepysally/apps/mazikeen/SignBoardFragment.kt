package com.sleepysally.apps.mazikeen

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.sleepysally.apps.mazikeen.api.DatasetsAPI
import com.sleepysally.apps.mazikeen.api.SignatureAPI
import com.sleepysally.apps.mazikeen.databinding.FragmentSignBoardBinding
import com.sleepysally.apps.mazikeen.signature.adapter.DatasetAdapter
import com.sleepysally.apps.mazikeen.signature.model.DatasetModel
import com.sleepysally.apps.mazikeen.signature.model.InkPoint
import com.sleepysally.apps.mazikeen.utils.DialogUtils
import com.sleepysally.apps.mazikeen.viewmodels.DatasetsViewModel
import kotlinx.android.synthetic.main.fragment_sign_board.*

/**
 * A placeholder fragment containing a simple view.
 */
class SignBoardFragment : Fragment() {

    companion object {
        const val MODE_COLLECT_DATA = 1
        const val MODE_CLASSIFY = 2
    }

    lateinit var binding: FragmentSignBoardBinding
    var selectedMode = MODE_COLLECT_DATA
    var allDatasets = arrayListOf<DatasetModel>()
    var selectedDataset: DatasetModel? = null

    val datasetsViewModel: DatasetsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentSignBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupModeSelector()
        setupDatasetSelector()
        setupViewModels()

        // INIT INK VIEW
        inkView.onSigningFinished = { inkPoints: ArrayList<InkPoint> ->
            if (selectedDataset != null) {
                when (selectedMode) {
                    MODE_COLLECT_DATA -> {
                        DatasetsAPI.getCurrent(requireActivity()).postSignature(
    //                        "-M3Zgg5kQsCE4OozFU_m",
                            selectedDataset!!.id,
                            inkPoints,
                            { res ->
                                AlertDialog.Builder(this.context).apply {
                                    setTitle("Response")
                                    setMessage(res.toString(4))
                                }.create().show()
                            },
                            { error ->
                                DialogUtils.showVolleyError(requireActivity(), error)
                            }
                        )
                    }
                    MODE_CLASSIFY -> {
                        SignatureAPI.getCurrent(requireActivity()).classify(selectedDataset!!.id, inkPoints)
                    }
                }
            }
            else {
                AlertDialog.Builder(context).apply {
                    setTitle("Please select the dataset first")
                    setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                        // ...
                    }
                }.create().show()
            }
            // should clear the board?
            true
        }

        fab.setOnClickListener {
//            inkView.clear()
            AlertDialog.Builder(context).apply {
                setTitle("Response")
                setMessage("${allDatasets.size} items")
            }.create().show()
        }

    }

    fun setupModeSelector() {
        modeSelector.addTab(
            modeSelector.newTab().setText("Collect Data").setTag(MODE_COLLECT_DATA)
        )
        modeSelector.addTab(
            modeSelector.newTab().setText("Classify").setTag(MODE_CLASSIFY)
        )

        modeSelector.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0?.tag != null) {
                    selectedMode = p0?.tag as Int
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) { }
            override fun onTabReselected(p0: TabLayout.Tab?) { }
        })
    }

    fun setupDatasetSelector() {
        binding.fabDataset.setOnClickListener {
            val adapter = DatasetAdapter(allDatasets)
            val dialog = DialogUtils.createWithRecyclerView(
                requireActivity(),
                "Select the Dataset",
                adapter
            ).create()

            adapter.onItemClick = { selected: DatasetModel, i: Int ->
                datasetsViewModel.selectedDataset.value = selected
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.fabFetchDataset.setOnClickListener {
            datasetsViewModel.fetch()
        }
    }

    fun setupViewModels() {
//        AlertDialog.Builder(context).apply {
//            setTitle("Response")
//            setMessage(response.toString(4))
//        }.create().show()
        datasetsViewModel.datasets.observe(viewLifecycleOwner, Observer {
            allDatasets.clear()
            allDatasets.addAll(it)

            Toast.makeText(requireActivity(), "${it.size} datasets loaded", Toast.LENGTH_SHORT).show()
        })
        datasetsViewModel.selectedDataset.observe(viewLifecycleOwner, Observer {
            selectedDataset = it
            binding.tvDatasetName.text = it?.name ?: "-"
        })
        datasetsViewModel.fetch()
    }
}
