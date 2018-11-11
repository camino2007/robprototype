package com.rxdroid.data.search

data class UserDto(var login: String,
                   var name: String?,
                   var githubUserId: Int,
                   var publicRepoCount: Int,
                   var publicGistCount: Int)