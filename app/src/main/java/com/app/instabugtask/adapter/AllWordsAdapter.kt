package com.app.instabugtask.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.instabugtask.R
import com.app.utility.diffutils.DiffUtilCallBack
import com.app.utility.model.CharacterModel
import com.app.utility.diffutils.ItemsDiffCallback

class AllWordsAdapter(
    private val mContext: Context,
): RecyclerView.Adapter<AllWordsAdapter.SellViewHolder>() {
    private val callback by lazy { DiffUtilCallBack(this) }

    private val mNewList by lazy { arrayListOf<CharacterModel>() }
    private val mOldList by lazy { arrayListOf<CharacterModel>() }

    fun submitList(list: List<CharacterModel>) {
        mNewList.clear()
        mNewList.addAll(list)
        updateListItems(list)
    }

    fun clearList() {
        val count = this.itemCount
        notifyItemRangeRemoved(0, count)
        mNewList.clear()
        mOldList.clear()
    }

    private fun updateListItems(list: List<CharacterModel>) {
        val diffCallback = ItemsDiffCallback(mOldList, mNewList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mOldList.clear()
        mOldList.addAll(list)
        diffResult.dispatchUpdatesTo(callback)
    }

    inner class SellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mWordName: AppCompatTextView
        private val mWordCount: AppCompatTextView

        init {
            mWordName = itemView.findViewById(R.id.wordName)
            mWordCount = itemView.findViewById(R.id.wordCount)
        }
        fun bindWord(word: CharacterModel) {
            mWordName.text = word.character_name
            mWordCount.text = "${word.character_count}"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        return SellViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item_all_words_count, parent, false))
    }
    override fun getItemCount(): Int { return mNewList.size }

    override fun onBindViewHolder(
        holder: SellViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        val word = mNewList[position]
        // set item data
        holder.bindWord(word)
    }
    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {}
}