package org.joetsai.pixabay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import org.joetsai.pixabay.Constants.Companion.GRID_SPAN_COUNT
import org.joetsai.pixabay.data.Image


class MainActivity : AppCompatActivity(), SearchContract.View {

    private val adapter by lazy {
        ImageAdapter(onLoadMoreListener = {
            println("Load More")
        })
    }

    val presenter: SearchContract.Presenter by lazy {
        SearchPresenter(this, SearchModel())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)


        recyclerView.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        recyclerView.adapter = adapter

        presenter.onLoadNextPage()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)

        //找到searchView
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "請輸入想要搜尋的圖片"
        return super.onCreateOptionsMenu(menu)
    }

    override fun showList(hits: List<Image>) {
        adapter.addImages(hits)
    }

    override fun showErrorView() {

    }
}
