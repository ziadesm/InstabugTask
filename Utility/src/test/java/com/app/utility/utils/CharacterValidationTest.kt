package com.app.utility.utils
import com.app.utility.CharacterValidation
import com.app.utility.model.CharacterModel
import com.app.utility.model.SortWord
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CharacterValidationTest{

    @Test
    fun `expect 6 items without space`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE   \n  78  Bbbf Bbbf HTML html Html html")

        assertThat(result).hasSize(6)
    }

    @Test
    fun `expect no duplication in list`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE    +00 78 00 Bbbf Bbbf HTML html Html html")

        assertThat(result).containsNoDuplicates()
    }

    @Test
    fun `expect 7 items with special characters`() {
        val result = CharacterValidation.getCharacterValidationList("   DRRE    +00 78 00 Bbbf Bbbf HTML html Html html")

        assertThat(result).hasSize(7)
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