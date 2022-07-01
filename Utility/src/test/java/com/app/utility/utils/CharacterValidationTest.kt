package com.app.utility.utils
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.utility.CharacterValidation
import com.app.utility.model.CharacterModel
import com.app.utility.model.SortWord
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterValidationTest{

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun test_weekend_function() {
        val result = CharacterValidation.getWeekDays("saturday", context)
        assertThat(result).isEqualTo("Saturday")
    }

    @Test
    fun test_weekend_sunday_function() {
        val result = CharacterValidation.getWeekDays("sunday", context)
        assertThat(result).isEqualTo("Sunday")
    }

    @Test
    fun test_weekend_monday_function() {
        val result = CharacterValidation.getWeekDays("monday", context)
        assertThat(result).isEqualTo("Monday")
    }

    @Test
    fun `expect 6 items without space`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE   \n  78  Bbbf Bbbf HTML html Html html")

        assertThat(result).hasSize(5)
    }

    @Test
    fun `expect no duplication in list`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE    +00 78 00 Bbbf Bbbf HTML html Html html")

        assertThat(result).containsNoDuplicates()
    }

    @Test
    fun `expect 7 items with special characters`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE    +00 78 00 Bbbf Bbbf HTML html Html html")

        assertThat(result).hasSize(5)
    }

    @Test
    fun `find HTML WORD to make sure`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE  +  +00 78 00 Bbbf Bbbf HTML html Html html")

        assertThat(result).contains(CharacterModel("html", 2))
    }

    @Test
    fun `check null string value`() {
        val result = CharacterValidation.getCharacterValidationList(null)

        assertThat(result).hasSize(1)
    }

    @Test
    fun `sort list in ASC order should begin with Bbbf`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE  +   Bbbf Bbbf HTML html Html html")
        val sorted = CharacterValidation.sortingCharactersBy(SortWord.ASC, result)
        assertThat(sorted?.first()).isEqualTo(CharacterModel("Bbbf", 2))
    }

    @Test
    fun `sort list in DESC order should end with html`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE  +   Bbbf Bbbf HTML html Html html")
        val sorted = CharacterValidation.sortingCharactersBy(SortWord.DESC, result)
        assertThat(sorted?.first()).isEqualTo(CharacterModel("html", 2))
    }
}