package com.eone.core.utils

import com.eone.core.domain.model.Content

interface Callback {
    fun onItemClicked (content: Content)
}