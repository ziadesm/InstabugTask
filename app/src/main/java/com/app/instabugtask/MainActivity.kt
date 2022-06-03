package com.app.instabugtask
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.instabugtask.adapter.AllWordsAdapter
import com.app.instabugtask.base.ParentActivity
import com.app.instabugtask.state_holder.MainStateIntention
import com.app.instabugtask.state_holder.StateHolderView
import com.app.network_helper.NetworkResponse
import com.app.utility.KeyboardUtils

class MainActivity : ParentActivity(), SearchView.OnQueryTextListener,
    MenuItem.OnMenuItemClickListener {
    override val layoutResource: Int
        get() = R.layout.activity_main
    override val toolbar: Toolbar?
        get() = null

    private val mStateHolder: StateHolderView by lazy {
        ViewModelProvider(this, viewModelFactory)[StateHolderView::class.java]
    }
    private val mAdapter by lazy { AllWordsAdapter(this) }

    private lateinit var mRecycler: RecyclerView
    private lateinit var mProgress: ProgressBar
    private var searchView: SearchView? = null

    override fun initializeComponents() {
        mRecycler = findViewById(R.id.allWords)
        mProgress = findViewById(R.id.progress)

        setupRecyclerView()

        observeAllErrors()
        observeAllWords()

        mStateHolder.setStateHolder(MainStateIntention.CallNetworkWithoutUrl)
    }

    private fun setupRecyclerView() {
        if (::mRecycler.isInitialized) {
            mRecycler.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    private fun observeAllErrors() {
        mStateHolder.errors.observe(this) {
            when(it) {
                is NetworkResponse.Loading -> showAndHideProgress(true)
                is NetworkResponse.ApiError -> {
                    showAndHideProgress(false)
                    Toast.makeText(this, "${it.body.message} Code: ${it.code}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
    private fun observeAllWords() {
        mStateHolder.words.observe(this) {
            showAndHideProgress(false)
            if (!it.isNullOrEmpty()) mAdapter.submitList(it)
            else {
                mAdapter.clearList()
                Toast.makeText(this, "List is empty :D", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun showAndHideProgress(b: Boolean) {
        if (::mProgress.isInitialized)
            mProgress.visibility = VISIBLE.takeIf { b } ?: GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.toolbar_icon_action, menu)

        val sort = menu?.findItem(R.id.sort)
        sort?.setOnMenuItemClickListener(this)

        val search = menu?.findItem(R.id.search)
        searchView = search?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        KeyboardUtils.hideKeyboard(this.currentFocus, this)
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        mStateHolder.setStateHolder(MainStateIntention.SearchFromWordsList(newText))
        return true
    }
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.sort -> {
                mStateHolder.setStateHolder(MainStateIntention.SortAscendingDescending)
            }
        }
        return true
    }
}