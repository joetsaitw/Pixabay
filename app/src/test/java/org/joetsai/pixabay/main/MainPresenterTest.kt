package org.joetsai.pixabay.main

import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import org.joetsai.pixabay.data.SearchImgResponse
import org.joetsai.pixabay.main.MainPresenter.Companion.INIT_PAGE
import org.joetsai.pixabay.network.ApiCallback
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class MainPresenterTest {

    @Mock
    private lateinit var model: MainContract.Model

    @Mock
    private lateinit var view: MainContract.View


    private lateinit var presenter: MainPresenter

    @Captor
    private lateinit var apiCallbackCaptor: ArgumentCaptor<ApiCallback<SearchImgResponse>>


    private lateinit var response: SearchImgResponse
    private lateinit var emptyResponse: SearchImgResponse


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Init presenter
        presenter = MainPresenter(view, model)

        // Fake response
        response = Gson().fromJson(fakeResponseString, SearchImgResponse::class.java)
        emptyResponse = Gson().fromJson(emptyResponseString, SearchImgResponse::class.java)

    }

    @Test
    fun queryIsEmptyWontSendSearchApi() {
        // Arrange
        val query = ""

        // Act
        presenter.onQueryTextSubmit(query)

        // Assert
        verify(model, never()).searchApi(eq(query), eq(INIT_PAGE), any())
    }

    @Test
    fun onQueryTextSubmit_onSuccessFoundResults() {
        // Arrange
        val query = "123"


        // Act
        presenter.onQueryTextSubmit(query)

        // Assert
        verify(model).searchApi(eq(query), eq(INIT_PAGE), capture(apiCallbackCaptor))
        apiCallbackCaptor.value.onSuccess(response)


        // View's action must in order
        val inOrder = inOrder(view)
        // Clear the list first
        inOrder.verify(view).clearList()
        // Then show progressbar
        inOrder.verify(view).enableProgressBar(true)
        // Hide the progressbar and show images
        inOrder.verify(view).enableProgressBar(false)
        inOrder.verify(view).showList(response.hits)


        assertEquals(2, presenter.page)
    }

    @Test
    fun onQueryTextSubmit_onSuccessFoundNoResults() {
        // Arrange
        val query = "123"

        // Act
        presenter.onQueryTextSubmit(query)

        // Assert
        verify(model).searchApi(eq(query), eq(INIT_PAGE), capture(apiCallbackCaptor))

        apiCallbackCaptor.value.onSuccess(emptyResponse)


        // View's action must in order
        val inOrder = inOrder(view)
        // Clear the list first
        inOrder.verify(view).clearList()
        // Then show progressbar
        inOrder.verify(view).enableProgressBar(true)
        // Hide the progressbar and show no result view
        inOrder.verify(view).enableProgressBar(false)
        inOrder.verify(view).showNoResultsFoundView()
        inOrder.verify(view).stopLoadingMore()

        assertEquals(1, presenter.page)
    }

    @Test
    fun onQueryTextSubmit_onError() {
        // Arrange
        val query = "123"

        // Act
        presenter.onQueryTextSubmit(query)

        // Assert
        verify(model).searchApi(eq(query), eq(INIT_PAGE), capture(apiCallbackCaptor))


        val statusCode = 100
        val errorMsg = "error"

        apiCallbackCaptor.value.onError(statusCode, errorMsg)


        // View's action must in order
        val inOrder = inOrder(view)
        // Clear the list first
        inOrder.verify(view).clearList()
        // Then show progressbar
        inOrder.verify(view).enableProgressBar(true)
        // Hide the progressbar and show error view
        inOrder.verify(view).showErrorView(errorMsg)
        inOrder.verify(view).enableProgressBar(false)

        assertEquals(1, presenter.page)
    }

    @Test
    fun onQueryTextSubmit_onNetWorkerror() {
        // Arrange
        val query = "123"

        // Act
        presenter.onQueryTextSubmit(query)

        // Assert
        verify(model).searchApi(eq(query), eq(INIT_PAGE), capture(apiCallbackCaptor))


        val statusCode = 100
        val errorMsg = "error"

        apiCallbackCaptor.value.onNetworkError(errorMsg)


        // View's action must in order
        val inOrder = inOrder(view)
        // Clear the list first
        inOrder.verify(view).clearList()
        // Then show progressbar
        inOrder.verify(view).enableProgressBar(true)
        // Hide the progressbar and show error view
        inOrder.verify(view).showNetWorkErrorView(false, errorMsg)
        inOrder.verify(view).enableProgressBar(false)

        assertEquals(1, presenter.page)
    }


    @Test
    fun onQueryTextSubmit_onUnexpectedError() {
        // Arrange
        val query = "123"

        // Act
        presenter.onQueryTextSubmit(query)

        // Assert
        verify(model).searchApi(eq(query), eq(INIT_PAGE), capture(apiCallbackCaptor))


        val statusCode = 100
        val errorMsg = "error"

        apiCallbackCaptor.value.onUnexpectedError(errorMsg)


        // View's action must in order
        val inOrder = inOrder(view)
        // Clear the list first
        inOrder.verify(view).clearList()
        // Then show progressbar
        inOrder.verify(view).enableProgressBar(true)
        // Hide the progressbar and show error view
        inOrder.verify(view).showErrorView(errorMsg)
        inOrder.verify(view).enableProgressBar(false)

        assertEquals(1, presenter.page)
    }

    @Test
    fun onLoadNextPage_onSuccess() {
        // Arrange
        onQueryTextSubmit_onSuccessFoundResults()
        val query = "123"
        val page = presenter.page


        // Act
        presenter.onLoadNextPage()



        // Assert
        verify(model).searchApi(eq(query), eq(page), capture(apiCallbackCaptor))
        apiCallbackCaptor.value.onSuccess(response)


        // View's action must in order
        val inOrder = inOrder(view)
        // Then show progressbar
        inOrder.verify(view).enableProgressBar(true)
        // Hide the progressbar and show images
        inOrder.verify(view).enableProgressBar(false)
        inOrder.verify(view).addList(response.hits)


        assertEquals(page + 1, presenter.page)
    }

}