package org.joetsai.pixabay.data

import org.joetsai.pixabay.common.Constants.ADAPTER_IMAGE
import org.joetsai.pixabay.main.adapter.ViewType
import java.io.Serializable


data class SearchImgResponse(
        val totalHits: Int, //500
        val hits: List<Image>,
        val total: Int //39850
)

data class Image(
        val previewHeight: Int, //99
        val likes: Int, //98
        val favorites: Int, //80
        val tags: String, //paint, makeup, cracky
        val webformatHeight: Int, //426
        val views: Int, //12668
        val webformatWidth: Int, //640
        val previewWidth: Int, //150
        val comments: Int, //12
        val downloads: Int, //7668
        val pageURL: String, //https://pixabay.com/en/paint-makeup-cracky-girl-cosmetics-2985569/
        val previewURL: String, //https://cdn.pixabay.com/photo/2017/11/29/09/15/paint-2985569_150.jpg
        val webformatURL: String, //https://pixabay.com/get/eb3cb90a2df2083ed95c4518b74b4594ea71ebd504b0144097f8c17bafe8bc_640.jpg
        val imageWidth: Int, //4896
        val user_id: Int, //1982503
        val user: String, //lightstargod
        val type: String, //photo
        val id: Int, //2985569
        val userImageURL: String, //https://cdn.pixabay.com/user/2017/11/29/19-47-26-843_250x250.jpg
        val imageHeight: Int //3264
) : Serializable, ViewType {
    override fun getViewType(): Int {
        return ADAPTER_IMAGE
    }
}