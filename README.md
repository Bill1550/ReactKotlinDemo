# React Kotlin Demo
A simple project to explore running a React Native (RN) app within a larger Android app
built in with Kotlin.

The basic Android app architecture is a single Activity / multiple Fragment design. 
The React Native app runs in two or more fragments, each fragment displaying a 
different aspect of the same app.
There are additional fragments with native Android content.

The example implements messaging in both directions between the Android and RN code.
When the RN app is launched in a fragment a message is sent in to tell the RN app which screen 
to display.
The RN app can send messages to the Android code requesting navigation to other screens.

 