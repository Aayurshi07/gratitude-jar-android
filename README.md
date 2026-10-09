# Gratitude Jar

An Android app where you write down something you're grateful for and it goes into a jar as a coloured marble. When you want a lift, "Pull a memory" brings one of the old notes back.

I built it in Kotlin with Jetpack Compose as a small project to get comfortable with Android Studio and Compose layouts.

![Gratitude Jar running on the emulator](screenshots/screenshot1.png)

## How it works

Type a note and tap "Drop a marble". It's added to the list with a coloured dot, and the count at the top goes up. "Pull a memory" picks one of your saved notes at random and shows it in a card at the top.

## Running it

Open the project in Android Studio, let Gradle sync, then run it on an emulator or a connected phone. I tested it on the Medium Phone emulator (API 37).