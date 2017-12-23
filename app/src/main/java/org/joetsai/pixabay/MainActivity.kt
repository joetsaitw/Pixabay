package org.joetsai.pixabay

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import org.joetsai.pixabay.Constants.GRID_SPAN_COUNT
import org.joetsai.pixabay.ImageActivity.Companion.EXTRA_IMAGE
import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.util.alert


class MainActivity : AppCompatActivity(), SearchContract.View {


    private val presenter: SearchContract.Presenter by lazy {
        SearchPresenter(this, SearchModel())
    }

    private val adapter by lazy {
        ImageAdapter(onLoadMoreListener = { presenter.onLoadNextPage() },
                onItemClickedListener = { image ->
                    //Toast.makeText(this, "pos:" + position, Toast.LENGTH_SHORT).show()

                    val showImageIntent = Intent(this, ImageActivity::class.java)
                    showImageIntent.putExtra(EXTRA_IMAGE, image)
                    startActivity(showImageIntent)
                })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //swipeRefreshLayout.setOnRefreshListener { presenter.onQueryTextSubmit() }

        recyclerView.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        recyclerView.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)

        //找到searchView
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "請輸入想要搜尋的圖片"




        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onQueryTextSubmit(query)
                //presenter.onSearchSubmit(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun addList(hits: List<Image>) {
        recyclerView.post { adapter.addImages(hits) }
    }

    override fun showList(hits: List<Image>) {
        recyclerView.post { adapter.replaceImages(hits) }
    }

    override fun showErrorView(msg: String) {
        alert {
            setTitle("發生錯誤")
            setMessage(msg)
            setPositiveButton("確定") { _, _ -> }
        }
    }

    override fun showNetWorkErrorView(msg: String) {
        alert {
            setTitle("發生錯誤")
            setMessage(msg)
            setPositiveButton("重試") { _, _ -> }
            setNegativeButton("取消", null)
        }
    }

    override fun showReloadingIndicator(active: Boolean) {
//        swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = active }
    }

    override fun enableProgressBar(isEnabled: Boolean) {
        recyclerView.post { adapter.enableProgressBar(isEnabled) }
    }

    override fun stopLoadingMore() {
        recyclerView.post { adapter.noMorePages() }
    }

    override fun showNoResultsView() {

    }
}
