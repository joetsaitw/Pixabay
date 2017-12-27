package org.joetsai.pixabay.main

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.joetsai.pixabay.main.MainPresenter.Companion.INIT_PAGE
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class MainPresenterTest {

    @Mock
    private lateinit var model: MainContract.Model

    @Mock
    private lateinit var view: MainContract.View


    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = MainPresenter(view, model)

//        Mockito.`when`()
    }

    @Test
    fun queryIsEmptyWontSendSearchApi() {
        presenter.onQueryTextSubmit("")

        verify(model, never()).searchApi(anyString(), INIT_PAGE, any())
    }

    @Test
    fun onQueryTextSubmit() {

    }

    @Test
    fun onLoadNextPage() {

    }

}