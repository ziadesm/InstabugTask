package com.app.instabugtask.state_holder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.database.DatabaseHelperSingleton
import com.app.network.RequestHandler
import com.app.instabugtask.repo.CallingInstaBugFree
import com.app.network_helper.NetworkResponse
import com.app.utility.CharacterValidation
import com.app.utility.model.CharacterModel
import com.app.utility.model.SortWord
import java.util.concurrent.*

class StateHolderView constructor(
    private val mDatabase: DatabaseHelperSingleton,
    saved: SavedStateHandle? = null
): ViewModel() {
    private val repo by lazy { CallingInstaBugFree(RequestHandler.getInstance()) }

    private val mExecutor by lazy { Executors.newCachedThreadPool() }

    private val _errors: MutableLiveData<NetworkResponse<Any, Throwable>> = MutableLiveData()
    val errors: LiveData<NetworkResponse<Any, Throwable>>
        get() = _errors

    private val _words = saved?.getLiveData<List<CharacterModel>>("all_words") ?: MutableLiveData(emptyList())
    val words: LiveData<List<CharacterModel>>
        get() = _words

    private val _backUp = saved?.getLiveData<List<CharacterModel>>("sort_search_words") ?: MutableLiveData()

    private var _sort = SortWord.ASC

    init {
        setStateHolder(MainStateIntention.CallNetworkWithoutUrl)

        val words = mDatabase.gettingWordFromDatabase()
        _words.postValue(convertStringWordList(words))
        _backUp.postValue(convertStringWordList(words))
    }

    private fun callNetwork() {
        _errors.value = NetworkResponse.Loading
        mExecutor.submit {
            when(val response = repo.callInstaBugWebsite("https://instabug.com")) {
                is NetworkResponse.Success -> {
                    _words.postValue(convertStringWordList(response.body))
                    _backUp.postValue(convertStringWordList(response.body))
                    mDatabase.insertWordsToDatabase(response.body)
                }
                is NetworkResponse.ApiError -> _errors.postValue(response)
                else -> {}
            }
        }
    }

    fun setStateHolder(state: MainStateIntention) {
        when (state) {
            is MainStateIntention.CallNetworkWithoutUrl -> callNetwork()
            is MainStateIntention.SearchFromWordsList -> searchFromWords(state.search)
            is MainStateIntention.SortAscendingDescending -> sortingAscendingDescending(_sort)
        }
    }
    private fun sortingAscendingDescending(sortType: SortWord) {
        _sort = SortWord.ASC.takeIf { sortType == SortWord.DESC } ?: SortWord.DESC
        mExecutor.execute {
            _words.postValue(CharacterValidation.sortingCharactersBy(_sort, _words.value))
        }
    }
    private fun searchFromWords(search: String?) {
        if (!search.isNullOrEmpty()) {
            mExecutor.execute {
                _words.postValue(CharacterValidation.searchingForCharacter(_sort, search, _words.value))
            }
        } else _words.value = _backUp.value
    }

    private fun convertStringWordList(word: String): List<CharacterModel> {
        return CharacterValidation.getCharacterValidationList(word)
    }
}

sealed class MainStateIntention {
    object CallNetworkWithoutUrl: MainStateIntention()
    data class SearchFromWordsList(val search: String?): MainStateIntention()
    object SortAscendingDescending: MainStateIntention()
}