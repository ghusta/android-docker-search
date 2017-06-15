# android-docker-search
:whale: Docker Search on Android

# Docker

Uses Registry Hub REST API v1.  
In particular :
- Search : [GET /v1/search](https://docs.docker.com/v1.6/reference/api/registry_api/#search)
- List repository tags : [GET /v1/repositories/(namespace)/(repository)/tags](https://docs.docker.com/v1.6/reference/api/registry_api/#list-repository-tags)

See [reference documentation](https://docs.docker.com/v1.6/reference/api/registry_api/).

# REST Client

Uses [Retrofit 2](https://square.github.io/retrofit/) and [Jackson 2](https://github.com/FasterXML/jackson)

# Build the app

With Gradle ([doc](https://developer.android.com/studio/build/building-cmdline.html#DebugMode)) :

    gradlew assembleDebug

Or (needs signing the APK - [Sign your app](https://developer.android.com/studio/publish/app-signing.html))

    gradlew assembleRelease

# Check the code quality with Lint

With Gradle ([doc](https://developer.android.com/studio/write/lint.html#lint-task)) :

    gradlew lint

Then open the report generated at `./app/build/reports/lint-results.html`.

More on Android Lint :

- https://developer.android.com/studio/write/lint.html
- http://tools.android.com/tips/lint

# Install the app

Download and install the APK [on my website](http://g.husta.free.fr/android/#docker-search) or on [GitHub](https://github.com/ghusta/android-docker-search/releases).
