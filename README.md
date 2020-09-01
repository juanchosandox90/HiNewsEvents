


# HiNews&Events

![ic_launcher](https://user-images.githubusercontent.com/69962068/91865062-a57f4200-ec79-11ea-8cfb-3b32393b83ae.png)

## :notebook_with_decorative_cover: Introduction 
HiNews&Event; It is created with HMS kits for phones running with the Android-based HMS service an application where you can follow news-content and events related to Huawei in different categories and easily access innovations and developments in the sector and within Huawei 

With the HiNews & Events application;
You can access news and content specific to many categories such as technology, economy, business, enterteintment, latest headlines etc.
You can access internal and global Huawei events-trainings informations, video recordings of tech talks, webinars and meetups.

## Aapp View
<center>
![app_01 09 20 8mb](https://user-images.githubusercontent.com/69962068/91869348-71f2e680-ec7e-11ea-813a-275ec627f4c5.gif)
</center>

## Screenshots
![ss1](https://user-images.githubusercontent.com/69962068/91864791-5507e480-ec79-11ea-910c-2601a8dc36c8.png)


 ## :question: Before You Start 
 **You need to agconnect-services.json for run this project correctly.**

- If you don't have a Huawei Developer account check this document for create; https://developer.huawei.com/consumer/en/doc/start/10104
- Open your Android project and find Debug FingerPrint (SHA256) with follow this steps; View -> Tool Windows -> Gradle -> Tasks -> Android -> signingReport
- Login to Huawei Developer Console (https://developer.huawei.com/consumer/en/console)
- If you don't have any app check this document for create; https://developer.huawei.com/consumer/en/doc/distribution/app/agc-create_app
- Add SHA256 FingerPrint into your app with follow this steps on Huawei Console; My Apps -> Select App -> Project Settings
- Make enable necessary SDKs with follow this steps; My Apps -> Select App -> Project Settings -> Manage APIs
- For this project you have to set enable Map Kit, Site Kit, Auth Service, ML Kit
- Than go again Project Settings page and click "agconnect-services.json" button for download json file.
- Move to json file in base "app" folder that under your android project. (https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/69407812#h1-1577692046342)
- Go to app level gradle file and change application id of your android project. It must be same with app id on AppGallery console you defined.

### :information_source: Things to Know
- Since the application is written entirely in HMS, you must have HMS Core installed on your device.
- For Android devices without HMS Core, you can download the latest version from this link; https://tik.to/9l6


## :rocket: Features 
* Sign In with Huawei Id
* View categorized News lists
* Paging with News list 
* View News detail
* Edit font size
* View News all content via source link
* View Ads on news detail page
* Share news link and summary content
* Notification on/off options

## :milky_way: Future Features 
* Push frequences edit and organize options with below 
  - Only important news - Daily news - Dont miss anything
* News download and add to bookmarks options
* Show downloads and bookmarks
* Notification edit options : notification sound - do not disturb options
* Dark mode option
* Language change option with EN-TR options
* Theme change option
* Enable-Disable Display images only on wifi option
* Enable-Disable Display videos only on wifi option
* Cashe clear option
* Feedback leave option
* Invite friends option
* Privacy Statement
* View terms of service
* View third party licences
* View about us
* Rate us option
* 
* Cloud DB Integration
* Video Stream Structure



## :heavy_check_mark: Kits Used 
* [Auth Service](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-auth-service-introduction)
* [Account Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050048870)
* [Push Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/service-introduction-0000001050040060)
* [Ads Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/publisher-service-introduction-0000001050064960)




## :wrench:  Future Kits

* [Analytics Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050745149)
* [Crash Service](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-crash-introduction)
* [Location Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050706106)
* [Site Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-sdk-introduction-0000001050158571)
* [IAP Kit](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/iap-service-introduction-v4)
* [Scan Kit](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/service-introduction-0000001050041994-V5)
* [Open Testing ](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-betatest-introduction)
* [DRM Kit ](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/introduction-0000001050041933-V5)
* [Cloud DB ](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/clouddb-quick_start_overview)


## :star2: Infrastructure, Technologies, Libraries Used 
* [Kotlin ](https://kotlinlang.org/)
* [Android Jetpack ](https://developer.android.com/jetpack)
* [Navigation Component ](https://developer.android.com/guide/navigation/navigation-getting-started)
* [ViewPager2 ](https://developer.android.com/jetpack/androidx/releases/viewpager2)
* [Retrofit2 ](https://square.github.io/retrofit/)
* [Paging ](https://developer.android.com/topic/libraries/architecture/paging)
* [Glide ](https://bumptech.github.io/glide/doc/getting-started.html)
* [Lifecycle ](https://developer.android.com/jetpack/androidx/releases/lifecycle)
* [LiveData ](https://developer.android.com/reference/androidx/lifecycle/LiveData)
* [ViewModel ](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Room ](https://developer.android.com/topic/libraries/architecture/room)


## :link: Useful Links 
* [Huawei Developers Medium Page EN](https://medium.com/huawei-developers)
* [Huawei Developers Medium Page TR](https://medium.com/huawei-developers-tr) 
* [Huawei Developers Forum](https://forums.developer.huawei.com/forumPortal/en/home)
