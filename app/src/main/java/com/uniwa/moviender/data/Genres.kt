package com.uniwa.moviender.data

import com.uniwa.moviender.R

val genres: HashMap<Int, Int> = hashMapOf(
    Genres.PERSONALIZED_RECOMMENDATIONS.code to R.string.genre_recommendations,
    Genres.ACTION.code to R.string.genre_action,
    Genres.ADVENTURE.code to R.string.genre_adventure,
    Genres.ANIMATION.code to R.string.genre_animations,
    Genres.COMEDY.code to R.string.genre_comedy,
    Genres.CRIME.code to R.string.genre_crime,
    Genres.DOCUMENTARY.code to R.string.genre_documentary,
    Genres.DRAMA.code to R.string.genre_drama,
    Genres.FAMILY.code to R.string.genre_family,
    Genres.FANTASY.code to R.string.genre_fantasy,
    Genres.HISTORY.code to R.string.genre_history,
    Genres.HORROR.code to R.string.genre_horror,
    Genres.MUSIC.code to R.string.genre_music,
    Genres.MYSTERY.code to R.string.genre_mystery,
    Genres.ROMANCE.code to R.string.genre_romance,
    Genres.SCI_FI.code to R.string.genre_science_fiction,
    Genres.TV_MOVIE.code to R.string.genre_tv_movie,
    Genres.THRILLER.code to R.string.genre_thriller,
    Genres.WAR.code to R.string.genre_war,
    Genres.WESTERN.code to R.string.genre_western
)

val nameToId: HashMap<Int, Int> = hashMapOf(
    R.string.genre_recommendations to Genres.PERSONALIZED_RECOMMENDATIONS.code,
    R.string.genre_action to Genres.ACTION.code,
    R.string.genre_adventure to Genres.ADVENTURE.code,
    R.string.genre_animations to Genres.ANIMATION.code,
    R.string.genre_comedy to Genres.COMEDY.code,
    R.string.genre_crime to Genres.COMEDY.code,
    R.string.genre_documentary to Genres.DOCUMENTARY.code,
    R.string.genre_drama to Genres.DRAMA.code,
    R.string.genre_family to Genres.FAMILY.code,
    R.string.genre_fantasy to Genres.FANTASY.code,
    R.string.genre_history to Genres.HISTORY.code,
    R.string.genre_horror to Genres.HORROR.code,
    R.string.genre_music to Genres.MUSIC.code,
    R.string.genre_mystery to Genres.MYSTERY.code,
    R.string.genre_romance to Genres.ROMANCE.code,
    R.string.genre_science_fiction to Genres.SCI_FI.code,
    R.string.genre_tv_movie to Genres.TV_MOVIE.code,
    R.string.genre_thriller to Genres.THRILLER.code,
    R.string.genre_war to Genres.WAR.code,
    R.string.genre_western to Genres.WESTERN.code,
)