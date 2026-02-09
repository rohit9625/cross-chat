# Cross Chat
_Communication Barrier? Not Anymore!_

With **Cross Chat**, you can communicate with people across languages, countries, and locales. For eg:
- You are chatting with a person from _Spain_
- They send messages in _Spanish_
- You get the original message in real-time
- And the translated text according to your preferred language (eg. English)

>_Note: The translations may take 2-10 seconds depending on message length_

Check out the [demo video](https://www.loom.com/share/6a465e20843848ab9c01389a1011807b) here to see _Cross Chat_ in action.

Its primary focus is on:
- Breaking language barriers in real-time text conversations
- Server-driven translations with client-side flexibility
- Clean separation between messaging and translation concerns

This project was built as part of a Hackathon hosted by **[lingo.dev](https://lingo.dev/)** from _31/01/2026 to 8/02/2026_.

You can download the first pre-release version of the _Cross Chat_ Android app here: [v1.0.0-alpha.1](https://github.com/rohit9625/cross-chat/releases/tag/v1.0.0-alpha.1)

>Note: The first alpha release contains some bugs, as building a production-ready application within a week wasn’t feasible for me. However these issues will be addressed in upcoming releases 🙂

---
**Cross Chat** demonstrates how multilingual chat can be implemented in a scalable, production-friendly way using modern Android and backend technologies.
## Core Features
- **Real-time Messaging** – Instant delivery using Socket.IO
- **Auto Translation** – Messages translated based on the user’s preferred language
- **Modern UI** – Jetpack Compose with Material 3
- **Offline First** – Local caching with Room Database
- **Secure Authentication** – JWT-based authentication

---

## 🧰 Tech Stack

### Android App (Frontend)
- **UI**: Jetpack Compose, Material 3 UI
- **App Navigation**: Navigation 3
- **DI(Dependency Injection)**: Koin
- **Networking**: Ktor Client, Socket.IO Java-based SDK for _Web Sockets_
- **Local Storage**: Room Database and Jetpack DataStore

### Backend
Please refer to the backend repository here: https://github.com/rohit9625/cross-chat-backend

---

## ️️⚙️ Setup & Installation for Running Locally

### Prerequisites
- Android Studio (latest version preferred)
- JDK 17 or higher
- Android SDK 34+ (Target SDK 36)

### Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/androhit/cross-chat.git
   cd cross-chat
   ```

2. **Backend Configuration**:
   The app is pre-configured to connect to a hosted backend. If you wish to use a local backend, update the `BASE_URL` in `app/build.gradle.kts`:
   ```kotlin
   buildTypes {
       debug {
           buildConfigField("String", "BASE_URL", "\"http://YOUR_LOCAL_IP:PORT\"")
       }
   }
   ```
    _Note: Please check out the [cross-chat-backend](https://github.com/rohit9625/cross-chat-backend) for running backend server locally._

3. **Build the project**:
   - Open the project in Android Studio.
   - Sync the project with Gradle files.
   - Build the project using `Build > Make Project`.

4. **Run the app**:
   - Select an emulator or a physical device.
   - Click the **Run** button.

## 🏗 Project Architecture
The project is structured into several modules/directories based on features:
- `auth`: Authentication code lives here
- `chat`: Chats and messages related code
- `profile`: Managing user preferences/settings

Each of the above modules is further divided into:
- `data`: Implementation of repositories, local (Room), and remote (Ktor/Socket.io) data sources.
- `domain`: Core business logic, repository interfaces, and models.
- `ui`: Jetpack Compose screens, ViewModels, and UI state management.

Apart from that, we have some root-level modules that share the logic across different feature modules
- `navigation`: Application flow control using Navigation 3.
- `di`: Dependency injection setup using Koin
- `designsystem`: Reusable UI components and theme definitions.

_Note: The above architecture pattern follows the official Android guidelines and best practices_

## 🤝 Contributing
Contributions are always welcome. But, please contact me before forking or creating a PR :)

Developed with pure dedication by [Rohit Verma](https://github.com/rohit9625)

[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/rohit0111/)
