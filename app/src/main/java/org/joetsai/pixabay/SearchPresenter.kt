package org.joetsai.pixabay


class SearchPresenter(private val view: SearchContract.View, private val model: SearchContract.Model) : SearchContract.Presenter, SearchContract.Model.Test {


    override fun ok() {
        // view.showList()
    }

    override fun no() {
        view.showErrorView()
    }

    override fun onSearchSend(query: String) {

    }

    override fun onLoadNextPage() {
        model.searchApi(this,
                onSuccess = { list -> view.showList(list) },
                onError = { view.showErrorView() })
    }
}
