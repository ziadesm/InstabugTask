package com.app.utility
import android.util.Log
import androidx.core.text.isDigitsOnly
import com.app.utility.model.CharacterModel
import com.app.utility.model.SortWord

object CharacterValidation {
    fun getCharacterValidationList(str: String?): List<CharacterModel> {
        val list: MutableList<CharacterModel> = mutableListOf()

        val regex = Regex("[^A-Za-z0-9 ]")
        val str1 = regex.replace(str.toString(), " ")
        val words = str1.split(" ")

        val wordMap: MutableMap<String, Int?> = words.groupingBy { it }.eachCount().toSortedMap()

        val wordSet: Set<String> = wordMap.keys
        wordSet.map { word ->
            if (word != "" && wordMap[word]!! >= 1 && word.length > 1 && !word.isDigitsOnly())
                list.add(CharacterModel(word, wordMap[word]!!))
        }

        return list
    }

    fun sortingCharactersBy(
        sortType: SortWord,
        value: List<CharacterModel>?
    ): List<CharacterModel>? {
        Log.e("SortType", "sortingCharactersBy: >>> $sortType")
        return value?.sortedBy { it.character_name }.takeIf { sortType == SortWord.ASC }
            ?: value?.sortedByDescending { it.character_name }
    }

    fun searchingForCharacter(
        sort: SortWord,
        search: String,
        value: List<CharacterModel>?
    ): List<CharacterModel>? {
        return value?.filter { it.character_name.contains(search, true) }?.sortedBy { it.character_name }.takeIf { sort == SortWord.ASC }
            ?: value?.filter { it.character_name.contains(search, true) }?.sortedByDescending { it.character_name }
    }
}