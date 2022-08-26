package com.eone.core.utils

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.eone.core.domain.model.Content


class ContentDiffCallback(oldContentList: List<Content>, newContentList: List<Content>) :
    DiffUtil.Callback() {
    private val mOldContentList: List<Content>
    private val mNewContentList: List<Content>
    override fun getOldListSize(): Int {
        return mOldContentList.size
    }

    override fun getNewListSize(): Int {
        return mNewContentList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldContentList[oldItemPosition].id  == mNewContentList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContent: Content = mOldContentList[oldItemPosition]
        val newContent: Content = mNewContentList[newItemPosition]
        return oldContent.title == newContent.title
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

    init {
        mOldContentList = oldContentList
        mNewContentList = newContentList
    }
}