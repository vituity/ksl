# KSL - Kotlin Structured Logging

## What the heck is this?

KSL is a really thin library to add some extension methods to the SLF4J Logger class
to make structured logging easier in Kotlin applications. While the SLF4J MDC class already
provides basic support for structured logging it's quite messy and verbose to use.

## So what does it do?

Well, like we said, it makes structured logging cleaner. Here's how you would add some
structured metadata to a log message with SLF4J and also use the same values in the log
message itself.

```kotlin
class AuthenticationService {
  fun login(username: String, password: String) {
      MDC.put("username", username)
      logger.info("Logging in {}", username)
      MDC.clear()
  }

  companion object {
    val LOGGER = LoggerFactory.getLogger(AuthenticationService::class.java)
  }
}
```

Not terrible, but it gets annoying if you have a lot of variables you want to put into
your log message AND the metadata. Here's how you would do this with KSL

```kotlin
class AuthenticationService {
  fun login(username: String, password: String) {
      logger.info_("Logging in {user}", object { val user = username })
  }
  
  companion object {
    val LOGGER = LoggerFactory.getLogger(AuthenticationService::class.java)
  }
}
```

If you want to include a value in the metadata but not the message itself, simply pass the
value in without using it in the message. In this example isAuthorized will be stashed in the
MDC data but never shows in the message.

```kotlin
class AuthenticationService {
  fun login(username: String, password: String) {
      val isAuthorized = true;
      logger.info_("Logging in {user}", object { val user = username; val isAuthorized = isAuthorized })
  }
  
  companion object {
    val LOGGER = LoggerFactory.getLogger(AuthenticationService::class.java)
  }
}
```

## Doesn't performance suck

We do have to reflect over properties in the anonymous object you pass in which does cost
cycles, but we do check to ensure the specific log level you are using is enabled before doing
anything. On the Lenovo laptop with an Intel Core i7-4770HQ this was developed on 100,000
iterations of the internal format functions finish in around 400ms with a small number of values,
that's 4.5 microseconds to reflect properties and build the string for each call.

TL;DR; No, it doesn't.

## Okay, so how do I use this

Well, it's not on Maven Central yet and I don't have time to write documentation more than this
basic FAQ right now - so figure it out for now.