package com.example.mob3000.ui.viewModel

import com.example.mob3000.data.model.TestResultat

interface Big5UiState {
    data class Success(val data: String) : Big5UiState
    object Error : Big5UiState
    object Loading : Big5UiState

}