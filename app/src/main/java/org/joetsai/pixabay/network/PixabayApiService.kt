package org.joetsai.pixabay.network

import org.joetsai.pixabay.data.SearchImgResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface PixabayApiService {

    // "." 代表端點網址
    @GET(".")
    fun searchImages(@Query("key") key: String = "7329690-bbadad6d872ba577d5a358679",
                     @Query("per_page") perPage: Int = 24, // 3的倍數即可
                     @Query("q") query: String,
                     @Query("page") page: Int): Call<SearchImgResponse>
}