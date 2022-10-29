# <div align="center">Moviender</div>
<div align="center"><img src="https://i.imgur.com/En1tCdx.png" height="480"></div>
<div align="center"><p>The front-end of our group reccommendation system</p></div>

<a name="readme-top"></a>
## Table of contents
- [Introduction](#introduction)
- [Features](#features)
    - [Sessions](#sessions)
    - [Voting](#voting)
    - [Accounts](#accounts)
    - [Movies hub](#movies-hub)
    - [Friends](#friends)
    - [Notifications](#notifications)
- [Requirements](#requirements)
- [Built with](#built-with)
- [Credits](#credits)

## Introduction
Have you ever wanted to see a movie with a friend but know you will spend more time looking for one instead of watching it? Moviender will come in handy! Get recommendations that **both** you and your friend would like to watch and spend your free time actually watching a movie! And all these happens **asynchronously**, so no need of waiting to come together to start searching.


## Features
### Sessions
It all starts with what we call a “session”. Essentially, a session is a four-part process which include the following stages:
1.	Choosing the type of the session
2.	Session specific actions
3.	Voting
4.	See results

<sup>Only one session can be active at a time with each of your friends</sup>
<sup>The type of the session is determined by the one who starts it</sup>

#### Session types
#### Genre-based
In this type of session you will choose for what genres you would like to receive recommendations for. Then the recommender will try to produce results that fall in those genres and also will be interest for both of you.

#### Similar movie
Instead of the more generic genre-based type, you have also the option to receive recommendations that are similar to a movie you specified.

### Voting
In the third stage of the session, you will need to vote to up to 20 movies that the recommender picked. We provide an intuitive Tinder-like UI that displays the poster of a movie and tapping on it will expand it revealing some basic info for this movie, such as:
-	The genres
-	Overview
-	Average rating (from TMDB)

Then you can choose to like or dislike the movie by taping on the corresponding button or by **swiping** left or right accordingly.

### Accounts
In order to use the app you will need to register an account. By registering an account we also try to solve the so-called “Cold start problem” where re recommender knows nothing of you. So, you will be prompted to rate some hand-picked movies that we thought that best represent each genre and that, you will choose in which genres you have interest in.

### Movies hub
Movies hub provides you the ability the explore the Moviender’s movies catalog, see information about a movie and rate it. The rates will help the recommendations algorithms to provide you better results.
You can explore the movies catalog either by searching for a specific title or by filtering the whole catalog by the movies genres you want. Each genre will be represented by a number of movies, ranked based on this genre.

#### Personal recommendations
Alongside with the movies genres you can also have the option to let the system provides you with personalized recommendations powered by a WSM algorithm.

### Friends
In order to be able to use the app’s main function, group recommendations, you must first make some friends and start sessions with them!

<sup>To make a friend request use the other person's username</sup>

### Notifications
Receive notifications for friend requests

<br>
<div align="center"><img src="https://i.imgur.com/obIV8F4.png" height="580"></img></div>
<p align="right"><sup>(<a href="#readme-top">back to top</a>)</sup></p>

## Requirements

### API
To be able to use the app the you must also host the RESTful [API](https://github.com/Moviender/Moviender-API).

### Firebase
In order for the app to work you should create a Firebase project and activate the following sign-in methods:
- Email/Password
- Google

You must also provide a google-services.json file to the **app** directory. You can download the file from Firebase projects settings.

<p align="right"><sup>(<a href="#readme-top">back to top</a>)</sup></p>

## Built with
Here is a brief summary of what technologies have been used for the app:
-	[Retrofit](https://github.com/square/retrofit) and [Moshi](https://github.com/square/moshi) to communicate with the RESTful API
-	[Room](https://developer.android.com/training/data-storage/room) for persist storage
-	[Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for movies paging and use of [RemoteMediator](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db) for local caching
-	[Firebase](https://github.com/firebase/FirebaseUI-Android) for authentication
-	[Coroutines](https://developer.android.com/kotlin/coroutines)
-	[FCM](https://firebase.google.com/docs/cloud-messaging) for push notifications
-	[Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)
-	[Coil](https://github.com/coil-kt/coil) to load profile images

## Credits
This software uses the following open source packages:
- [Retrofit](https://github.com/square/retrofit)
- [Moshi](https://github.com/square/moshi)
- [Coil](https://github.com/coil-kt/coil)
- [FirebaseUI for Android](https://github.com/firebase/FirebaseUI-Android)
- [Material rating bar](https://github.com/zhanghai/MaterialRatingBar)
- [OkHttp](https://github.com/square/okhttp)
- [CardStackView](https://github.com/yuyakaido/CardStackView)

<p align="right"><sup>(<a href="#readme-top">back to top</a>)</sup></p>