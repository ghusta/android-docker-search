# android-docker-search
:whale: Docker Search on Android

## Overview

This Android application enables you do perform a **Docker Search** on your device, like if you did it on a computer in a terminal, with docker-engine installed.

### Features

- Search images on Docker Hub
- Display all tags for an image, including the date and size
- Display the web page for an image, in a separate Chrome Tab
- Share the URL of the image

## Screenshots

![screenshot 1](/media/android-app-screenshot_1.png)

## Docker API

Uses Registry Hub REST API v1 or v2.  
In particular :
- Search : [GET /v1/search](https://docs.docker.com/v1.6/reference/api/registry_api/#search)
- List repository tags : [GET /v2/repositories/(namespace)/(repository)/tags](https://docs.docker.com/v1.6/reference/api/registry_api/#list-repository-tags)

See [reference documentation](https://docs.docker.com/v1.6/reference/api/registry_api/).

## REST Client

Uses [Retrofit 2](https://square.github.io/retrofit/) and [Jackson 2](https://github.com/FasterXML/jackson)

## Build the app

With Gradle ([doc](https://developer.android.com/studio/build/building-cmdline.html#DebugMode)) :

    gradlew assembleDebug

Or (needs signing the APK - [Sign your app](https://developer.android.com/studio/publish/app-signing.html))

    gradlew assembleRelease

## Check the code quality with Lint

With Gradle ([doc](https://developer.android.com/studio/write/lint.html#lint-task)) :

    gradlew lint

Then open the report generated at `./app/build/reports/lint-results.html`.

More on Android Lint :

- https://developer.android.com/studio/write/lint.html
- http://tools.android.com/tips/lint

## Install the app

Download and install the APK [on my website](http://g.husta.free.fr/android/#docker-search) or on [GitHub](https://github.com/ghusta/android-docker-search/releases).
