package com.sleepysally.apps.mazikeen.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sleepysally.apps.mazikeen.api.DatasetsAPI
import com.sleepysally.apps.mazikeen.signature.model.DatasetModel

class DatasetsViewModel(app: Application): AndroidViewModel(app) {
    val datasets: MutableLiveData<List<DatasetModel>> by lazy {
        MutableLiveData<List<DatasetModel>>().also {
            fetch()
        }
    }
    val selectedDataset = MutableLiveData<DatasetModel?>().apply {
        value = null
    }

    fun fetch() {
        DatasetsAPI.getCurrent(getApplication<Application>().applicationContext).getDatasetsList (
            {
                datasets.value = it
            },
            { error ->
                error.printStackTrace()
            }
        )
    }
}