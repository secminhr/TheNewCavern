package stoneapp.secminhr.cavern.api


val mapping = mapOf(
    "login" to "button.login.button",
    "markdown_key" to "markdown",
    "id_key" to "id",
    "names_key" to "names",
    "comments_key" to "comments",
    "likes_key" to "likes",
    "status_key" to "status",
    "username_key" to "username",
    "author_username_key" to "author",
    "posts_count_key" to "posts_count",
    "role_key" to "level",
    "login_key" to "login",
    "hash_key" to "hash",
    "email_key" to "email",
    "message_key" to "message",
    "content_key" to "content",
    "post_key" to "post",
    "posts_key" to "posts",
    "is_liked" to "islike",
    "total_posts_num" to "all_posts_count",
    "page_limit" to "page_limit",
    "comments_number_key" to "comments_count",
    "pid_key" to "pid",
    "upvote_key" to "likes_count",
    "date_key" to "time",
    "author_key" to "name",
    "title_key" to "title",
    "comment_id_key" to "comment_id"
)
fun getStringFromMap(key: String): String {
    return mapping[key] ?: ""
}