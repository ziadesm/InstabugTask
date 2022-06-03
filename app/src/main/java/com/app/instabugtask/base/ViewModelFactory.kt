package com.app.instabugtask.base
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.app.database.DatabaseHelperSingleton
import com.app.instabugtask.state_holder.StateHolderView

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val database: DatabaseHelperSingleton,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = StateHolderView(database, handle) as T
}