package stoneapp.secminhr.cavern.cavernObject

data class Account(
    val username: String,
    val nickname: String,
    val postCount: Int,
    val email: String?,
    val avatarLink: String,
    val role: Role,
)