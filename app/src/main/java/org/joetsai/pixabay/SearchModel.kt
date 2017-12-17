package org.joetsai.pixabay

import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.PixabayApiService
import org.joetsai.pixabay.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SearchModel : SearchContract.Model {

    override fun searchApi(callback: SearchContract.Model.Test, onSuccess: (List<Image>) -> Unit, onError: () -> Unit) {
        val apiService = ServiceGenerator.createService(PixabayApiService::class.java)
        val call = apiService.searchImages(query = "yellow")

        call.enqueue(object : Callback<SearchImgResponse> {
            override fun onResponse(call: Call<SearchImgResponse>?, response: Response<SearchImgResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it.hits) }
                } else {
                    onError()
                }
            }

            override fun onFailure(call: Call<SearchImgResponse>?, t: Throwable?) {
                // 網路錯誤
                onError()
            }
        })

    }

}