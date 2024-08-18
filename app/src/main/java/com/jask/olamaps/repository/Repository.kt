package com.jask.olamaps.repository

import com.jask.olamaps.model.AccessTokenDto
import com.jask.olamaps.model.geocoding.GeoCoding
import com.jask.olamaps.utils.ApiState

interface Repository {
    suspend fun getAccessToken(clientId: String, clientSecret: String): ApiState<AccessTokenDto>
    suspend fun getAddress(latLong: String): ApiState<GeoCoding>
}