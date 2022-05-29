package com.uniwa.moviender.data

enum class FriendState(val code: Int) {
    PENDING(1),
    REQUEST(2),
    FRIEND(3),
    SESSION(4)
}


enum class FriendRequestStatus(val code: Int) {
    SUCCESSFUL_FRIEND_REQUEST(11),
    USERNAME_NOT_FOUND(-10),
    ALREADY_EXISTS(-11),
    SAME_UID(-12),
    ACCEPT_REQUEST(12),
    DECLINE_REQUEST(10)
}


enum class SessionStatus(val code: Int) {
    WAITING_FOR_VOTES(20),
    SUCCESSFUL_FINISH(21),
    FAILED_FINISH(-20)
}


enum class SessionUserStatus(val code: Int) {
    VOTING(30),
    WAITING(31),
    VOTING_AGAIN(32)
}

enum class RecommendationType(val code: Int) {
    KNN(40),
    SVD(41)
}
