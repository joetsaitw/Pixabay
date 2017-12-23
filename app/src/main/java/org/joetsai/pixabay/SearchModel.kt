package org.joetsai.pixabay

import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.PixabayApiService
import org.joetsai.pixabay.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SearchModel : SearchContract.Model {


    override fun searchApi(query: String, page: Int, callback: ApiCallback<SearchImgResponse>) {
        val apiService = ServiceGenerator.createService(PixabayApiService::class.java)

        val call = apiService.searchImages(query = query, page = page)
        call.enqueue(callback)

    }

}