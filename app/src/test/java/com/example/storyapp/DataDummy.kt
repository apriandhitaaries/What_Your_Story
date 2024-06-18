package com.example.storyapp

import com.example.storyapp.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items = mutableListOf<ListStoryItem>()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photo-url $i",
                "createdAt $i",
                "name $i",
                "description $i",
                lat = i.toDouble(),
                id = "id $i",
                lon = i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}