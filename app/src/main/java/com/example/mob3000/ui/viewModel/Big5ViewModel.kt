package com.example.mob3000.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob3000.data.model.TestResultat
import com.example.mob3000.data.repository.Big5Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class Big5ViewModel: ViewModel() {
    val repository = Big5Repository()

    fun fetchData(testId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getResults(testId)
                Log.d("Big5ViewModel", "Response: ${response.body().toString()}")
            } catch (e: Exception) {
                e.stackTrace
                Log.e("Big5ViewModel", "Error: ${e.message}", e)
            }
        }
    }
}

/*

class Big5ViewModel(
    private val repository: Big5Repository
) : ViewModel() {
    private val _data = MutableStateFlow<List<TestResultat>>(emptyList())
    val data = _data.asStateFlow()

    private val _showErrorToast = Channel<Boolean> ()
    val showErrorToastFlow = _showErrorToast.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getResults.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _data.value = result.data ?: emptyList()
                    }

                    is Resource.Error -> {
                        _showErrorToast.send(true)
                    }
                }
            }
        }
    }

    fun fetchData(testId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getResults(testId)
                _data.value = result
            } catch (e: Exception) {
                _data.value = "Error: ${e.message}"
            }
        }
    }
}

 */