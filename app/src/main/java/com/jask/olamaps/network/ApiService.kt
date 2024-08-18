package com.jask.olamaps.network

import com.jask.olamaps.R
import com.jask.olamaps.model.AccessTokenDto
import com.jask.olamaps.model.geocoding.GeoCoding
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

object AccessRetrofitClient {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://account.olamaps.io/realms/olamaps/protocol/openid-connect/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val accessApiService: AccessApiService = retrofitBuilder.create(AccessApiService::class.java)
}

object RetrofitClient {
    private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val originalUrl = originalRequest.url

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("api_key", "pQDOpFEWeMjSk9SR7m4nlMIbVvwmLZxZnfVLGP7p")
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://api.olamaps.io/")
        .client(okhttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofitBuilder.create(ApiService::class.java)
}

interface AccessApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "openid",
    ): Response<AccessTokenDto>
}

interface ApiService {

    @GET("places/v1/reverse-geocode")
    suspend fun getAddress(
        @Query("latlng") latlng: String
    ): Response<GeoCoding>
}