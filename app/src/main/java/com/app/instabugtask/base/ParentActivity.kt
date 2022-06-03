package com.app.instabugtask.base
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.app.database.DatabaseHelperSingleton
import com.app.instabugtask.R

abstract class ParentActivity: AppCompatActivity() {
    protected abstract val layoutResource: Int
    protected abstract val toolbar: Toolbar?
    protected abstract fun initializeComponents()

    private val mDatabase by lazy { DatabaseHelperSingleton.getInstance(this) }

    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFactory = ViewModelFactory(this, mDatabase, null)
        super.onCreate(savedInstanceState)

        setContentView(layoutResource)

        if (toolbar != null) configureToolbar()
        initializeComponents()
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }
}