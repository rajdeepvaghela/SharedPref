# SharePref

This is a simple template of how you can use enums to store data in `SharedPreference` and `DataStorePreference` in Android. 

## Approach

The `Pref` and `Store` enum class provides a set of predefined keys and default values that can be used to store and retrieve values in shared preferences. You can set and get values of different data types, including String, Boolean, Float, Long and Integer. 

### For `SharedPreference`

To set a value in shared preferences, call the `set()` method on a `Pref` enum instance and pass the value you want to set. For example, to set the user name, you can call:

```kotlin
Pref.UserName.set("John Doe")
```

To get a value from shared preferences, call the `get()` method on a `Pref` enum instance. For example, to get the user name, you can call:

```kotlin
val userName = Pref.UserName.get(default = "Unknown")
```

---
You can choose to store your shared preferences in a secure way by passing true to the `isSecure` parameter of the `get()` and `set()` methods. This will encode the key using Base64 encoding to make it more difficult to read by others.

```kotlin
// Store the user's email in a secure way
Pref.UserEmail.set("user@example.com", isSecure = true)

// Get the user's email in a secure way
val userEmail = Pref.UserEmail.get<String>(isSecure = true) // here `isSecure` is optional 
```
### For `DataStorePreference`

Setting value here remains same as that for Shared Preference approach, just this needs to be called from a coroutine scope.

```kotlin
scope.launch {
    Pref.UserName.set("John Doe")
}
```

To get a value here, you need to collect the flow which you get from `getFlow()` method on a `Store` enum instance.

```kotlin
val userNameFlow = Pref.UserName.getFlow<String>()
scope.launch {
    userNameFlow.collect { userName ->
        // logic to handle userName
    }
}
```
Here too you can secure data using `isSecure` parameter in the `getFlow()` and `set()` methods.
