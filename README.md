# Food and Fun!
Android application displaying various hand-picked attractions and restaurants in the local Chicago area, and separate application which sends broadcasts to launch on default the chosen category in the main application. Created using the Android flavor of Java. CS 478 Project 3, UIC Spring 2022.

## Table of contents
* [General Info](#general-info)
* [Getting Started](#getting-started)
* [Project Requirements](#project-requirements)
* [Technologies](#technologies)
* [Credits](#credits)
* [Visual Demonstration](#visual-demonstration)

## General info
This project is the third project for CS 478 (Software Development for Mobile Platforms) at the University of Illinois at Chicago, Spring 2022. Our task was to design and code two new Android apps meant to work together on an Android device running version Android 11 (API 30). The first app helps visitors in Chicago decide on points of interest in the city. The second app has specific information about the points of interest. 

## Getting Started
If you would like to download the app from the Google Play Store, click **here**. If you would like to browse through the source code in the environment it was created in, make sure you have **Android Studio 2021.1.1** installed.
* Windows: [download here](https://redirector.gvt1.com/edgedl/android/studio/install/2021.1.1.20/android-studio-2021.1.1.20-windows.exe)
* Mac (Intel): [download here](https://redirector.gvt1.com/edgedl/android/studio/install/2021.1.1.20/android-studio-2021.1.1.20-mac.dmg)
* Mac (ARM): [download here](https://redirector.gvt1.com/edgedl/android/studio/install/2021.1.1.20/android-studio-2021.1.1.20-mac_arm.dmg)
* Linux: [download here](https://redirector.gvt1.com/edgedl/android/studio/ide-zips/2021.1.1.20/android-studio-2021.1.1.20-linux.tar.gz)
* Chrome OS: [download here](https://redirector.gvt1.com/edgedl/android/studio/install/2021.1.1.20/android-studio-2021.1.1.20-cros.deb)

Once you are ready, open the project(s) in Android Studio. If you want to simply explore Food and Fun!, open Project3App2. If you would like to view the broadcasting capabilities of the companion application, open both Project3App1 and Project3App2 (each in its own window). Ensure that the Run/Debug Configurations are set and set the device to a Pixel 3a XL using API 30. The broadcasting application should load up and look something like this:

![App 1 Start](images/app1start.png)

and Food and Fun! should look something like this:

![App 2 Start](images/app2start.png)


To test the broadcasting capabilities, you will ***first need to launch Project3App2*** to ensure that it is running in the background while the broadcast is sent. Run the project, then head on over to Project3App1 and run that project. Chose whichever category you'd like (Food and Fun! automatically defaults on attractions), and Food and Fun! should receive the broadcasted intent and load with your chosen category. 

You can then begin testing the Food and Fun!'s features, which include dynamically adding a fragment to showcase the selected list item's main website (on first click). If a menu item is already selected and you chose another, the website will change accordingly. Going back from this state will revert the application back to its original state (the single ListViewFragment showing different recommendations). You can also change the category on your own by pressing the options menu in the overflow area of the menu bar, which will then launch the activity corresponding to the chosen category. The app handles orientation changes flawlessly through configurations in the manifest file. 

## Project Requirements
**Application 1** was required to define an activity containing two read-only text views and two buttons. The buttons, when selected, will first show a short toast message, then broadcast two different implicity intents (e.g., attractions and restaurants) depending on the button pressed. The text views describe the meaning of the buttons to the device user. Application 2 was required to receive the intents. Depending on the kind of intent that was received, 

**Application 2** (Food and Fun!) was required to launch one of two activities: the first activity (attractions) displays information about 5 points of interest in the city of Chicago, Illinois; the second activity shows at least 5 restaurants located within Chicago’s city limits. Application 2 also maintains an options menu and an action bar. The action bar shows the name of the application and the overflow area. The options menu allows a device user to switch between the two categories. Each of the two activities in Application 2 contains two fragments. The first fragment displays a list (either the attractions or the restaurants, depending on the activity). The device user may select any item from either list; the currently selected item will stay highlighted until another item is selected. The second fragment shows the official web site of the highlighted item using a Webview.

### Other Requirements ###
* Both applications were required display optimally in landscape mode. 
* The activities in Application 2 initially show only the first fragment across the entire width of the screen. 
    * As soon as a user selects an item, the first fragment is “shrunk” to about 1/3 of the screen’s width. This fragment will appear in the left-hand side of the screen, with the second fragment taking up the remaining 2/3 of the display on the right. 
    * Pressing the “back” button will return the activity to its initial configuration. 
* The action bar in Application 2 should be displayed at all times regardless of whether the device is in portrait or landscape mode.
* The state of Application 2 should be retained across device reconfigurations, e.g., when the device is switched from landscape to portrait mode and vice versa. 
    * This means that the selected list item (in the first fragment) and the page displayed in the second fragment will be kept during configuration changes.
* Using a ViewModel with LiveData for communication between fragments was required.
    
## Technologies
Project is created with:
* Android Studio Bumblebee 2021.1.1
* Java SE Development Kit (JDK) 11.0.11

## Credits
All credits for the project idea go to Professor Ugo Buy. Credits to some of the [AttractionsActivity.java](Project3App2/app/src/main/java/com/example/project3app2/AttractionsActivity.java),  [RestaurantsActivity.java](Project3App2/app/src/main/java/com/example/project3app2/RestaurantsActivity.java), [TitlesFragment.java](Project3App2/app/src/main/java/com/example/project3app2/TitlesFragment.java), and  [ListViewModel.java](Project3App2/app/src/main/java/com/example/project3app2/ListViewModel.java) codebase also go to Professor Ugo Buy as the beginning template for our project. 

All credits to technologies used are given to their owners and all items specified in their respective licenses are adhered to throughout this project.  

## Visual Demonstration
The following link leads to a visual demonstration of the project.
* https://youtu.be/CzaECngvmzw
