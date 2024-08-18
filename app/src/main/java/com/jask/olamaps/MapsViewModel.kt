package com.jask.olamaps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jask.olamaps.model.geocoding.GeoCoding
import com.jask.olamaps.network.AccessRetrofitClient
import com.jask.olamaps.network.RetrofitClient
import com.jask.olamaps.repository.RepositoryImpl
import com.jask.olamaps.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel : ViewModel() {

    private val repository = RepositoryImpl(
        accessApiService = AccessRetrofitClient.accessApiService,
        apiService = RetrofitClient.apiService
    )

    private var isApiCalled = false

    var accessToken = ""
    fun getAccessToken(
        clientId: String,
        clientSecret: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (isApiCalled) return

        isApiCalled = true

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAccessToken(clientId, clientSecret)

            if (result is ApiState.Success) {
                Log.d("ola", "Access Token: ${result.data.accessToken}")
                accessToken = result.data.accessToken

                withContext(Dispatchers.Main) {
                    onSuccess()
                }

            } else if (result is ApiState.Error) {
                Log.d("ola", "ERROR: ${result.errorMsg}")

                withContext(Dispatchers.Main) {
                    onFailure(result.errorMsg)
                }
            }
        }
    }

    fun getAddress(latLng: String, onSuccess: (GeoCoding) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getAddress(latLng)

            if (result is ApiState.Success) {
                withContext(Dispatchers.Main) {
                    onSuccess(result.data)
                }

            } else if (result is ApiState.Error) {
                Log.d("TAG", "ERROR: ${result.errorMsg}")
            }
        }
    }

}