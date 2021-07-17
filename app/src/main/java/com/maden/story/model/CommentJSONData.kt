package com.maden.story.model

data class CommentJSONData(
    val comment: String,
    val date: String,
    val email: String,
    val like: List<String>,
    val username: String,
    val uuid: String
)
