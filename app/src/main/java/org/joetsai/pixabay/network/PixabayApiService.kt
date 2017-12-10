package org.joetsai.pixabay.network

import org.joetsai.pixabay.SearchImgResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Joe on 2017/12/9.
 */
interface PixabayApiService {

    @GET(".")
    fun searchImages(@Query("key") key: String = "7329690-bbadad6d872ba577d5a358679",
                     @Query("q") query: String): Call<SearchImgResponse>
}