package org.joetsai.pixabay

import org.joetsai.pixabay.data.Image

/**
 * Created by Joe on 2017/12/9.
 */
interface SearchContract {

    interface View {
        fun showList(hits: List<Image>)
        fun showErrorView()
    }

    interface Presenter {
        fun onSearchSend(query: String)
        fun onLoadNextPage()
    }

    interface Model {

        fun searchApi(callback: Test,
                      onSuccess: (List<Image>) -> Unit,
                      onError: () -> Unit)

        interface Test {
            fun ok()
            fun no()
        }
    }

}