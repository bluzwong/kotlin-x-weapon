package com.github.bluzwong.example.recyclerview

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import com.github.bluzwong.example.R
import com.github.bluzwong.kotlin_x_weapon.*
import java.util.ArrayList
/**
 * Created by wangzhijie@wind-mobi.com on 2015/9/18.
 */
public class XRecyclerViewActivity : AppCompatActivity() {
    val list:MutableList<MainListItem> = ArrayList<MainListItem>()
    var adapter: MainAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_xrecyclerview)

        for (item in 0..20) {
            list add MainListItem(R.layout.listitem_main_activity, "test ${item}" + if (item % 3 == 0) "\n" else "")
        }
        adapter = MainAdapter(getLayoutInflater())
        adapter!!.items = list
        val main_recycler_view = findViewById(R.id.main_recycler_view) as XRecyclerView
        val endlessAdapter = EndlessAdapterWrapper(main_recycler_view, adapter!!, layoutInflater)
        endlessAdapter.setOnLoadListener {
            runDelayed(1000) {
                list add MainListItem(R.layout.listitem_main_activity, "test ${255}")
                runOnUiThread {
                    endlessAdapter.loadFinish()
                }
            }
        }
        //main_recycler_view setLayoutManager StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        main_recycler_view.layoutManager = GridLayoutManager(this, 2)
        runDelayed(200) {
            //main_recycler_view.setAdapter(endlessAdapter)
            main_recycler_view.scheduleLayoutAnimation()
        }

        val main_swipe_refresh = findViewById(R.id.main_swipe_refresh) as SwipeRefreshLayout
        main_swipe_refresh setOnRefreshListener {
            runDelayed(1000) {
                //main_recycler_view.setAdapter(endlessAdapter)
                main_recycler_view.startScheduleLayoutAnimation()
                main_swipe_refresh setRefreshing false
            }
        }
    }

}