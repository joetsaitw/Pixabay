package org.joetsai.pixabay.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * 統一的 Response/Error Handling
 * Created by Joe on 2017/12/21.
 */
interface ApiCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response != null) {
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) onSuccess(body) else onUnexpectedError()
            } else {
                onError(response.code(), response.errorBody()?.string() ?: "無法預期的錯誤，請稍後再嘗試。")
            }
        } else {
            onUnexpectedError()
        }
    }


    override fun onFailure(call: Call<T>?, t: Throwable?) {
        if (t is IOException) {
            onNetworkError("連線逾時，請檢查網路連線。")
        } else {
            onUnexpectedError(t?.message ?: "無法預期的錯誤，請稍後再嘗試。")
        }

    }


    fun onSuccess(response: T)
    fun onError(statusCode: Int, errorMsg: String)
    fun onNetworkError(errorMsg: String)
    fun onUnexpectedError(errorMsg: String = "無法預期的錯誤，請稍後再嘗試。")
}