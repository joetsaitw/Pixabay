package org.joetsai.pixabay.main

import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.ApiCallback


interface MainContract {

    interface View {
        fun showList(hits: List<Image>)
        fun addList(hits: List<Image>)

        fun clearList()

        fun showErrorView(msg: String)
        fun showNetWorkErrorView(isLoadMore: Boolean, msg: String)

        fun showNoResultsFoundView()

        // Row ProgressBar
        fun enableProgressBar(isEnabled: Boolean)


        fun stopLoadingMore()
    }

    interface Presenter {
        fun onQueryTextSubmit(query: String?)
        fun onQueryTextChange()
        fun onLoadNextPage()
    }

    interface Model {

        fun searchApi(query: String, page: Int, callback: ApiCallback<SearchImgResponse>)
    }

}