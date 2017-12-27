package org.joetsai.pixabay.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import org.joetsai.pixabay.R
import org.joetsai.pixabay.common.Constants.GRID_SPAN_COUNT
import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.image.ImageActivity
import org.joetsai.pixabay.image.ImageActivity.Companion.ARG_IMAGE_LIST
import org.joetsai.pixabay.image.ImageActivity.Companion.ARG_PAGE
import org.joetsai.pixabay.main.adapter.ImageAdapter
import org.joetsai.pixabay.util.alert
import org.joetsai.pixabay.util.gone
import org.joetsai.pixabay.util.visible
import java.io.Serializable


class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter: MainContract.Presenter by lazy {
        MainPresenter(this, MainModel())
    }

    private val adapter by lazy {
        ImageAdapter(onLoadMoreListener = { presenter.onLoadNextPage() },
                onImageClickedListener = { position, imageList ->
                    val showImageIntent = Intent(this, ImageActivity::class.java)
                    showImageIntent.putExtra(ARG_PAGE, position)
                    showImageIntent.putExtra(ARG_IMAGE_LIST, imageList as Serializable)
                    startActivity(showImageIntent)
                })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Init RecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        recyclerView.adapter = adapter


        // Init SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onQueryTextSubmit(query)
                searchView.clearFocus() // Close the keyboard
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun addList(hits: List<Image>) {
        recyclerView.post { adapter.addImages(hits) }
    }

    override fun showList(hits: List<Image>) {
        recyclerView.post { adapter.replaceImages(hits) }
    }

    override fun clearList() {
        recyclerView.visible()
        errorView.gone()
        adapter.clearImages()
    }

    override fun showErrorView(msg: String) {
        alert {
            setTitle("發生錯誤")
            setMessage(msg)
            setPositiveButton("確定") { _, _ -> }
        }
    }

    override fun showNetWorkErrorView(isLoadMore: Boolean, msg: String) {
        alert {
            setTitle("發生錯誤")
            setMessage(msg)
            setPositiveButton("重試") { _, _ ->
                if (isLoadMore) {
                    presenter.onLoadNextPage()
                } else {
                    presenter.onQueryTextSubmit(searchView.query.toString())
                }
            }
            setNegativeButton("取消", null)
        }
    }


    override fun enableProgressBar(isEnabled: Boolean) {
        recyclerView.post { adapter.enableProgressBar(isEnabled) }
    }

    override fun stopLoadingMore() {
        recyclerView.post { adapter.noMorePages() }
    }

    override fun showNoResultsFoundView() {
        errorView.visible()
        recyclerView.gone()
    }
}
