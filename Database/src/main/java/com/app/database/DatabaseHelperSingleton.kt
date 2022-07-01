package com.app.database
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.app.database.WordsDbHelper.WordsEntry.TABLE_NAME

class DatabaseHelperSingleton constructor(
    private val context: Context
) {
    private var mContext: Context = context

    companion object : SingletonHolder<DatabaseHelperSingleton, Context>(::DatabaseHelperSingleton)
    private val WORD_TITLE = "all_words"
    private val TAG = "DatabaseError"

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

            val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null);

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