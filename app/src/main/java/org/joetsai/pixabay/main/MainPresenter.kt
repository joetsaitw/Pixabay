package org.joetsai.pixabay.main

import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.network.ApiCallback


class MainPresenter(private val view: MainContract.View, private val model: MainContract.Model) : MainContract.Presenter {


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

            this.page = INIT_PAGE
            this.query = query

            view.clearList()
            view.enableProgressBar(true)

            model.searchApi(query = query, page = INIT_PAGE, callback = object : ApiCallback<SearchImgResponse> {
                override fun onSuccess(response: SearchImgResponse) {
                    view.enableProgressBar(false)
                    if (response.hits.isNotEmpty()) {
                        page++
                        view.showList(response.hits)
                    } else {
                        view.showNoResultsFoundView()
                        view.stopLoadingMore()
                    }
                }

                override fun onError(statusCode: Int, errorMsg: String) {
                    view.showErrorView(errorMsg)
                    view.enableProgressBar(false)
                }

                override fun onNetworkError(errorMsg: String) {
                    view.showNetWorkErrorView(false, errorMsg)
                    view.stopLoadingMore()
                    view.enableProgressBar(false)
                }

                override fun onUnexpectedError(errorMsg: String) {
                    view.showErrorView(errorMsg)
                    view.enableProgressBar(false)
                }
            })
        }
    }

    /**
     * 使用[onQueryTextSubmit] 所搜尋的字串搜尋下一頁
     */
    override fun onLoadNextPage() {

        view.enableProgressBar(true)

        model.searchApi(query = query, page = page, callback = object : ApiCallback<SearchImgResponse> {
            override fun onSuccess(response: SearchImgResponse) {
                page++
                view.enableProgressBar(false)
                view.addList(response.hits)
            }

            override fun onError(statusCode: Int, errorMsg: String) {
                // 此次搜尋結果已到底了
                if (statusCode == 400 && errorMsg.contains("\"page\" is out of valid range.")) {
                    view.stopLoadingMore()
                } else {
                    view.showErrorView(errorMsg)
                }
                view.enableProgressBar(false)
            }

            override fun onNetworkError(errorMsg: String) {
                view.showNetWorkErrorView(true, errorMsg)
                view.enableProgressBar(false)
            }

            override fun onUnexpectedError(errorMsg: String) {
                view.showErrorView(errorMsg)
                view.enableProgressBar(false)
            }
        })
    }
}
