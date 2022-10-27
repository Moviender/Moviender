# Moviender
A group recommendation system

### [Screenshots](https://imgur.com/a/k5NtIkB)
## Features
### Sessions
You can engage group recommendations with your friends through what we call sessions. A session is started by one member of the group, which choose the type of recommendations and then includes the voting stage and the results stage.  
The are **two types of recommendations** available. One which is genre based and one that recommends movies that are similar to another.

*Only one session can be active at a time with each one of your friends*
### Cold start
In the process of registering an account you would be ask to vote some hand-picked selected movies that we thought that best represents each genre and also to pick the genres you like. This will help the algorithm to provide you with better recommendations
### Voting
You can vote up to 20 movies picked by the recommendation system. The movies that voted as “liked” by both participants will be shown as the results of the voting and thus are the movies that both would want to watch.
### Movies hub
Movies hub provides you the ability the explore the Moviender’s movies catalog, see information about a movie and vote it. The votes will help the recommendations algorithms to provide you better results.
You can explore the movies either by searching for a specific title or by filtering the whole catalog by the movies genres you want. Each genre will be represented by a number of movies, ranked based on this genre.
#### Personal recommendations
Alongside with the movies genres you can also have the option to let the system provides you with personalized recommendations powered by a WSM algorithm.
### Friends
In order to be able to use the app’s main function, group recommendations, you must make friends and start voting sessions with them!
##### Notifications
You will receive notifications for a friend request

## Requirements
### API
To be able to use the app the you must also host the [API](https://github.com/Moviender/Moviender-API).
### Firebase
In order for the app to work you should create a Firebase project and activate the following sign-in methods:
- Email/Password
- Google

You must also provide a google-services.json file to the **app** directory. You can download the file from Firebase projects settings.