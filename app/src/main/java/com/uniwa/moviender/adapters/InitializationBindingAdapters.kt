package com.uniwa.moviender.adapters

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import coil.load
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.uniwa.moviender.R
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.data.placeholdersIndexes
import com.uniwa.moviender.data.placeholdersMap
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.hub.friends.ProfileAdapter
import com.uniwa.moviender.ui.initialization.MoviesRowAdapter
import com.uniwa.moviender.ui.initialization.genres.GenresPreferencesAdapter
import com.uniwa.moviender.ui.session.movies.genreBased.SessionGenresAdapter
import com.uniwa.moviender.ui.session.movies.similar.SimilarMoviesAdapter
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MoviesRowAdapter
    adapter.submitList(data)
}

@BindingAdapter("friendList")
fun bindFriendList(recyclerView: RecyclerView, friendList: List<Friend>?) {
    val adapter = recyclerView.adapter as ProfileAdapter
    adapter.submitList(friendList)
}

@BindingAdapter("genresList")
fun bindGenreList(recyclerView: RecyclerView, genres: List<Int>?) {
    val adapter = recyclerView.adapter as SessionGenresAdapter
    val list = genres?.sortedBy { genreId ->
        recyclerView.resources.getString(genreId)
    }
    adapter.submitList(list)
}

@BindingAdapter("genres")
fun bindGenres(recyclerView: RecyclerView, genres: List<Int>?) {
    val adapter = recyclerView.adapter as GenresPreferencesAdapter
    val list = genres?.sortedBy { genreId ->
        recyclerView.resources.getString(genreId)
    }
    adapter.submitList(list)
}

@BindingAdapter("idToString")
fun bindResourceId(materialCheckBox: MaterialCheckBox, resourceId: Int) {
    materialCheckBox.text = materialCheckBox.resources.getString(resourceId)
}

@BindingAdapter("genres")
fun bindGenres(textView: TextView, genresIds: List<Int>?) {
    textView.text = genresIds?.map { genreId ->
        textView.resources.getString(genres[genreId]!!)
    }
        ?.joinToString(separator = " | ")
}

@BindingAdapter("imageUrl")
fun bindPosterImage(imageView: ImageView, filePath: String?) {
    val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500/"
    filePath?.let {
        val posterUri = POSTER_BASE_URL.plus(filePath).toUri().buildUpon().scheme("https").build()
        imageView.load(posterUri) {
            // TODO Images for error and loading
            placeholder(R.drawable.movies_poster_placeholder)
        }
    }
}

@BindingAdapter("btnVisibility")
fun bindBtnVisibility(
    extendedFloatingActionButton: ExtendedFloatingActionButton,
    visibility: Int?
) {
    val animation =
        AnimationUtils.loadAnimation(extendedFloatingActionButton.context, R.anim.btn_scale)
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            extendedFloatingActionButton.isClickable = false
        }

        override fun onAnimationEnd(p0: Animation?) {
            extendedFloatingActionButton.isClickable = true
        }

        override fun onAnimationRepeat(p0: Animation?) {
            TODO("Not yet implemented")
        }
    })
    if (visibility == View.VISIBLE) {
        extendedFloatingActionButton.show()
        extendedFloatingActionButton.startAnimation(animation)
        TransitionManager.beginDelayedTransition(
            extendedFloatingActionButton.parent as ViewGroup,
            AutoTransition()
        )
    }
}

@BindingAdapter("searchedResults")
fun bindSearchedResults(recyclerView: RecyclerView, searchedResults: List<Movie>?) {
    val adapter = recyclerView.adapter as SimilarMoviesAdapter

    adapter.submitList(searchedResults)
}

@BindingAdapter("profilePic")
fun bindProfilePic(circleImageView: CircleImageView, profilePicUrl: String?) {
    val img =
        if (placeholdersIndexes.contains(profilePicUrl)) placeholdersMap[profilePicUrl] else profilePicUrl
    circleImageView.load(img) {

    }
}