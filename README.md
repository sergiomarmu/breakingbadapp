# :rocket:Breaking Bad APP:rocket:

This is an example to do an app with good practices and architecture, if you disagre with something, please let me know.

### APP
Let's go to talk about the app. This app is based on a one of the best TV series, Breaking Bad.
I have used a [The Breaking Bad API](https://breakingbadapi.com/), that's is a amazing FREE API.

Display a list of all Breakind Bad characters, a detail view of each character and the possibility to add a character as favourite.
See SOLUTION.md in the root project for more info.


### TECHNOLOGIES
* Architecture - MVVM with [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* Android Jetpack - [Jetpack](https://developer.android.com/jetpack)
* Programming language - [Kotlin](https://kotlinlang.org/)
* Coroutines/Flows - [Coroutines/Flow](https://kotlinlang.org/docs/reference/coroutines-overview.html)
* Dependency injection - [Hilt(Dagger)](https://dagger.dev/hilt/)
* Images loading - [Glide](https://github.com/bumptech/glide)
* HTTP client - [Retrofit](https://square.github.io/retrofit/)
* Material Components - [Material](https://material.io/)

### TECHNOLOGIES USED FOR TESTING
* jUnit - [jUnit](https://junit.org/junit5/)
* MockWebServer - [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
* Mockito - [Mockito](https://site.mockito.org/)
* KotlinCorutinesTest - [KotlinCorutinesTest](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)




### Architecture
I have used an architecture based on differents modules [link](https://developer.android.com/jetpack/guide) (See SOLUTION.MD for more):

* App
* Core
* Domain
* Data

### Enjoy :smile: