package com.example.reoil.data

interface UserProfile {
    val username: String?
    val phone: String?
    val address: String?
    val imageUrl: String?
}

data class DefaultUserProfile(
    override val username: String? = null,
    override val phone: String? = null,
    override val address: String? = null,
    override val imageUrl: String? = null
) : UserProfile


