package com.jask.olamaps.repository

import android.util.Log
import com.jask.olamaps.model.AccessTokenDto
import com.jask.olamaps.model.geocoding.GeoCoding
import com.jask.olamaps.network.AccessApiService
import com.jask.olamaps.network.ApiService
import com.jask.olamaps.utils.ApiState
import org.json.JSONObject

class RepositoryImpl(
    private val accessApiService: AccessApiService,
    private val apiService: ApiService
) : Repository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String
    ): ApiState<AccessTokenDto> = try {
        val response = accessApiService.getAccessToken(clientId, clientSecret)

        if (response.isSuccessful) {
            Log.d("TAG", "getAccessToken: Success")
            ApiState.Success(response.body()!!)
        } else {
            response.errorBody()!!.charStream().use { reader ->
                val jsonObj = JSONObject(reader.readText())
                throw Exception(jsonObj.toString())
            }

        }
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }

    override suspend fun getAddress(
        latLong: String
    ): ApiState<GeoCoding> = try {
        val response = apiService.getAddress(latLong)

        if (response.isSuccessful) {
            ApiState.Success(response.body()!!)
        } else {
            response.errorBody()!!.charStream().use { reader ->
                val jsonObj = JSONObject(reader.readText())
                throw Exception(jsonObj.toString())
            }

        }
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }
}