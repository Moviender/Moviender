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

enum class Error(val code: Int) {
    CANNOT_CONNECT(1000),
    NETWORK_ERROR(1001),
    GENERAL(1002),
    USER_NOT_CREATED(1003)
}

enum class Genres(val code: Int) {
    ALL(-1),
    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751),
    FANTASY(14),
    HISTORY(36),
    HORROR(27),
    MUSIC(10402),
    MYSTERY(9648),
    ROMANCE(10749),
    SCI_FI(878),
    TV_MOVIE(10770),
    THRILLER(53),
    WAR(10752),
    WESTERN(37)
}