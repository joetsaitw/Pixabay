package org.joetsai.pixabay

import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.ApiCallback


interface SearchContract {

    interface View {
        fun showList(hits: List<Image>)
        fun addList(hits: List<Image>)
        fun showErrorView(msg: String)
        fun showNetWorkErrorView(msg: String)

        fun showNoResultsView()

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

        interface Test {
            fun ok()
            fun no()
        }
    }

}