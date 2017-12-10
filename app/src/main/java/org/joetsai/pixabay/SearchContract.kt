package org.joetsai.pixabay

/**
 * Created by Joe on 2017/12/9.
 */
interface SearchContract {

    interface View {

    }

    interface Presenter {
        fun onSearchViewClicked()
    }

    interface Model {
        fun searchApi()
    }

}