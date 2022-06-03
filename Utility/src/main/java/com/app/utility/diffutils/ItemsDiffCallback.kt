package com.app.utility.diffutils
import androidx.recyclerview.widget.DiffUtil

class ItemsDiffCallback<T>(
    private val mOld: List<T>,
    private val mNew: List<T>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int { return mOld.size }

    override fun getNewListSize(): Int {
        return mNew.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOld[oldItemPosition].toString() == mNew[newItemPosition].toString()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = mOld[oldItemPosition]
        val new = mNew[newItemPosition]
        return old.toString().equals(new.toString())
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}