# APISDKSample
This is a sample app in which MUVI API SDK is implemented.
You can find the procedure how to impliment the MUVI API SDK & will get responses from MUVI. To use API SDK you need to initialize the SDK By calling following method in you launcer activity.

        if (NetworkStatus.getInstance().isConnected(this))
          {
             SDKInitializer.getInstance().init(Context,  SDKInitializer.SDKInitializerListner, authTokenStr);
           }
        
        
Screenshots
-----------
[![mutt dark](https://github.com/Muvidevelopers/APISDKSample/blob/master/screenshot1.jpg)](https://github.com/Muvidevelopers/APISDKSample/blob/master/screenshot1.jpg)
[![mutt dark](https://github.com/Muvidevelopers/APISDKSample/blob/master/screenshot2.jpg)](https://github.com/Muvidevelopers/APISDKSample/blob/master/screenshot2.jpg)