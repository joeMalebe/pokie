# pokie
| Home screen | More details | Filter by search | empty list |
| :--- | :---: | ---: | ---: |
| <img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/f8b65cc4-245d-4513-ad04-f098cff7203d" />  | <img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/ffb6fe48-a65f-4ce1-9b2a-27929ee36e65" /> | <img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/1da9887e-1b85-420f-821d-03a3c9aed5b4" />  |<img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/83b75926-7b26-458b-ac0e-6f1c157873b4" /> |



### Basic info
JDK 17

Android Studio Otter 3 Feature Drop | 2025.2.3

Android SDK (installed via Android Studio)

An emulator or physical Android device

⚠️ Older JDK versions (8 / 11) will not work.

### Architecture considerations

- THe app is built using mvvm and clean architecture. The presentation layer depends on the domain layer which provides 
the contract via a repository for simplicity. 
- In a larger application we can hide the repository and rather expose a use Case contract between the two boundaries
to improve reusability of some functionality across different features.


### Libraries used

🧰 Tech Stack

This application is built using a modern, scalable Android stack with a strong focus on Jetpack Compose, clean architecture, and testability.

🧠 Language & Build Tools

Kotlin
The primary language used throughout the app, providing concise syntax, strong type safety, and excellent Android support.

Kotlin Symbol Processing (KSP)
Used for fast and efficient annotation processing, primarily to support dependency injection and code generation.

🧩 Dependency Injection

Hilt
Handles dependency injection across the app, simplifying object creation, scoping, and lifecycle management.

Hilt Navigation Compose
Enables seamless dependency injection into Jetpack Compose navigation destinations and ViewModels.

🎨 UI & Navigation

Jetpack Compose (via Compose BOM)
The app UI is built entirely with Jetpack Compose. The BOM ensures all Compose libraries remain version-compatible.

Navigation 3 (Nav3 Core)
Used for more flexible and state-driven navigation handling.

Lifecycle ViewModel Nav3
Integrates ViewModels with Navigation 3 while remaining lifecycle-aware.

Material Components
Implements Material Design components and theming.

AppCompat
Ensures backward compatibility and consistent behavior across Android versions.

🖼️ Image Loading

Coil (Compose)
Lightweight, Kotlin-first image loading library optimized for Jetpack Compose, with built-in caching and lifecycle awareness.

🌐 Networking & Data

Retrofit
Type-safe HTTP client used for consuming REST APIs.

OkHttp
The underlying HTTP client providing efficient networking and connection management.

OkHttp Logging Interceptor
Logs network requests and responses for debugging and development visibility.

🔄 Serialization

Kotlinx Serialization
Used for JSON parsing and serialization via Kotlin data classes, ensuring type safety and consistency.

⏳ Concurrency & Lifecycle

Kotlin Coroutines
Enables structured concurrency and asynchronous programming.

🧪 Testing

JUnit
Core framework for unit testing.

AndroidX JUnit
Android-specific testing extensions for instrumentation tests.

Mockito Kotlin
Kotlin-friendly Mockito APIs for cleaner and more readable tests.

MockWebServer
Simulates backend APIs for reliable and deterministic network testing.

Kotlinx Coroutines Test
Enables controlled testing of coroutine-based code.
