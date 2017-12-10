package org.joetsai.pixabay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import org.joetsai.pixabay.network.PixabayApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ImageAdapter()


        val apiService = ServiceGenerator.createService(PixabayApiService::class.java)

        val call = apiService.searchImages(query = "yellow")

        call.enqueue(object : Callback<SearchImgResponse>{
            override fun onResponse(call: Call<SearchImgResponse>?, response: Response<SearchImgResponse>) {
                if (response.isSuccessful) {
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<SearchImgResponse>?, t: Throwable?) {

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)

        //找到searchView
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        return super.onCreateOptionsMenu(menu)
    }
}
