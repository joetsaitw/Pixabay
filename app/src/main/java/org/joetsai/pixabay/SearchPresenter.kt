package org.joetsai.pixabay

import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.ApiCallback


class SearchPresenter(private val view: SearchContract.View, private val model: SearchContract.Model) : SearchContract.Presenter {


    companion object {
        val INIT_PAGE = 1
    }

    // 目前搜尋頁數，初始值 = 1
    private var page: Int = INIT_PAGE

    // 要查詢的圖片
    private lateinit var query: String

    /**
     * 非空字元才Call Search Api
     * 並初始化 page = 1
     * @param query 要搜尋的圖片名稱
     */
    override fun onQueryTextSubmit(query: String?) {

        if (query != null && query.isNotBlank()) {

            println("query:" + query)

            this.page = INIT_PAGE
            this.query = query

//            view.showReloadingIndicator(true)
            view.enableProgressBar(true)

            model.searchApi(query = query, page = INIT_PAGE, callback = object : ApiCallback<SearchImgResponse> {
                override fun onSuccess(response: SearchImgResponse) {
                    if (response.hits.isNotEmpty()) {
                        view.showList(response.hits)
                    } else {
                        view.showErrorView("找不到查詢項目")
                        view.stopLoadingMore()
                    }

//                    view.showReloadingIndicator(false)

                    view.enableProgressBar(false)
                }

                override fun onError(statusCode: Int, errorMsg: String) {
                    if (statusCode == 400 && errorMsg.contains("\"page\" is out of valid range.")) {
                        view.showErrorView(errorMsg)
                    }
                    view.showReloadingIndicator(false)
                }

                override fun onNetworkError(errorMsg: String) {
                    view.showNetWorkErrorView(errorMsg)
                    view.showReloadingIndicator(false)
                }

                override fun onUnexpectedError(errorMsg: String) {
                    view.showErrorView(errorMsg)
                    view.showReloadingIndicator(false)
                }
            })
        }
    }

    override fun onLoadNextPage() {
        page++

        view.enableProgressBar(true)

        model.searchApi(query = query, page = page, callback = object : ApiCallback<SearchImgResponse> {
            override fun onSuccess(response: SearchImgResponse) {
                view.addList(response.hits)
                view.enableProgressBar(false)
            }

            override fun onError(statusCode: Int, errorMsg: String) {
                if (statusCode == 400 && errorMsg.contains("\"page\" is out of valid range.")) {
                    view.stopLoadingMore()
                }
                view.enableProgressBar(false)
            }

            override fun onNetworkError(errorMsg: String) {
                view.showNetWorkErrorView(errorMsg)
                view.enableProgressBar(false)
            }

            override fun onUnexpectedError(errorMsg: String) {
                view.showErrorView(errorMsg)
                view.enableProgressBar(false)
            }
        })
    }


    /**
     * 搜尋字串改變時，
     */
    override fun onQueryTextChange() {
        page = INIT_PAGE
    }
}
