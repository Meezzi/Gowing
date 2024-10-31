package com.meezzi.localtalk.data

data class CategorySection(
    val id: String = "",
    val name: String = ""
)

enum class Categories(val displayName: String) {
    FREE_BOARD("자유게시판"),
    SECRET_BOARD("비밀게시판"),
    MARKET_BOARD("장터게시판"),
    JOB_BOARD("채용게시판"),
    PROMOTION_BOARD("홍보게시판"),
    FOOD_BOARD("맛집게시판"),
    ADVICE_BOARD("고민상담"),
    ALBA_BOARD("알바경험담"),
    GATHERING_BOARD("소모임"),
    TEENAGE_BOARD("10대 모여라"),
    TWENTIES_BOARD("20대 모여라"),
    THIRTIES_BOARD("30대 모여라"),
    FORTIES_BOARD("40대 모여라"),
    FIFTIES_BOARD("50대 모여라"),
    SIXTIES_BOARD("60대 모여라")
}

data class Post(
    val city: String = "",
    var category: CategorySection = CategorySection(),
    var authorId: String? = "",
    val authorName: String? = "",
    @field:JvmField
    val isAnonymous: Boolean = false,
    val date: String = "",
    val time: String = "",
    val postId: String = "",
    var title: String = "",
    var content: String = "",
    var postImageUrl: List<String> = emptyList(),
    val likes: Int = 0,
    val comments: List<Comment> = emptyList()
)

data class Comment(
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val commentId: String = "",
    val date: String = "",
    val time: String = "",
    val content: String = "",
    val likes: Int = 0,
)