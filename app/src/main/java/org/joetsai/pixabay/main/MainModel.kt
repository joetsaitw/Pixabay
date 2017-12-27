package org.joetsai.pixabay.main

import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.ApiCallback
import org.joetsai.pixabay.network.PixabayApiService
import org.joetsai.pixabay.network.ServiceGenerator


class MainModel : MainContract.Model {


    override fun searchApi(query: String, page: Int, callback: ApiCallback<SearchImgResponse>) {
        val apiService = ServiceGenerator.createService(PixabayApiService::class.java)

        val call = apiService.searchImages(query = query, page = page)
        call.enqueue(callback)

    }

}