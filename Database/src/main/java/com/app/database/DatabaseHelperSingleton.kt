package com.app.database
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log

@SuppressLint("StaticFieldLeak")
class DatabaseHelperSingleton(context: Context) {
    private var mContext: Context = context

    companion object {
        const val WORD_TITLE = "all_words"
        const val TAG = "DatabaseError"

        private var instance: DatabaseHelperSingleton? = null
        fun getInstance(context: Context): DatabaseHelperSingleton {
            synchronized(DatabaseHelperSingleton::class.java) {
                if (instance == null) {
                    instance = DatabaseHelperSingleton(context)
                }
            }
            return instance!!
        }
    }

    fun close() {
        db.close()
    }

    private val db by lazy { WordsDbHelper(mContext) }

    fun insertWordsToDatabase(data: String) {
        Log.e(TAG, "insertWordsToDatabase: >>> ${db.hashCode()}")
        val values = ContentValues().apply {
            put(BaseColumns._ID, 1)
            put(WordsDbHelper.WordsEntry.COLUMN_NAME_TITLE, WORD_TITLE)
            put(WordsDbHelper.WordsEntry.COLUMN_NAME_DATA, data)
        }

        db.writableDatabase?.insertWithOnConflict(
            WordsDbHelper.WordsEntry.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun gettingWordFromDatabase(): String {
        return try {
            val db = db.readableDatabase

            val projection = arrayOf(
                BaseColumns._ID,
                WordsDbHelper.WordsEntry.COLUMN_NAME_TITLE,
                WordsDbHelper.WordsEntry.COLUMN_NAME_DATA
            )

            val selection = "${WordsDbHelper.WordsEntry.COLUMN_NAME_TITLE} = ?"
            val selectionArgs = arrayOf(WORD_TITLE)

            val cursor = db.query(
                WordsDbHelper.WordsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            cursor.close()
            cursor.use {
                if (!cursor.moveToFirst()) {
                    throw Exception("could not be found")
                }

                val wordColumn = cursor.getColumnIndex(WordsDbHelper.WordsEntry.COLUMN_NAME_DATA)
                cursor.getString(wordColumn)
            }
        } catch (t: Throwable) {
            Log.e(TAG, "gettingWordFromDatabase: >>> ${t.message}")
            return ""
        }
    }

}