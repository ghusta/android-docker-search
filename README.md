# android-docker-search
:whale: Docker Search on Android

[![Build Status](https://img.shields.io/travis/ghusta/android-docker-search/master?logo=travis)](https://travis-ci.org/ghusta/android-docker-search) [![GitHub release](https://img.shields.io/github/v/release/ghusta/android-docker-search?sort=semver&logo=GitHub)](https://github.com/ghusta/android-docker-search/releases)

## Overview

This Android application enables you do perform a **Docker Search** on your device, like if you did it on a computer in a terminal, with docker-engine installed.

The results should be the same than `docker search` with command line interface (see [reference doc](https://docs.docker.com/engine/reference/commandline/search/)) :

    docker search [OPTIONS] TERM

### Features

- Search images on [Docker Hub](https://hub.docker.com/explore/)
- Display all tags for an image, including the date and size
- Display the web page for an image, in a separate [Chrome Custom Tab](https://developer.chrome.com/multidevice/android/customtabs)
- Share the URL of the image
- Minimum supported version : [Android 5.0 (Lollipop)](https://developer.android.com/about/versions/android-5.0.html)

## Screenshots

![screenshot 1](/media/android-app-screenshot_1.png)

## Install the app

Download and install the APK on [GitHub](https://github.com/ghusta/android-docker-search/releases).

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
