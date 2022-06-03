package com.app.database
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.regex.Matcher

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var db: DatabaseHelperSingleton
    private lateinit var context: Context

    @get:Rule
    var instantTask = InstantTaskExecutorRule()

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = DatabaseHelperSingleton(context)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserList() =
        kotlin.run {
            db.insertWordsToDatabase("user")
            val inside = db.gettingWordFromDatabase()
            assertThat(inside, equalTo("user"))
        }

    @Test
    @Throws(Exception::class)
    fun readInList() {
        val insideDatabase = db.gettingWordFromDatabase()
//        assertThat(insideDatabase).isEqualTo("user")
    }
}