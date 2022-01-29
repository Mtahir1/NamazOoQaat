Android mobile application providing the prayer timings based on the location of the user.
  
 ### Installation
 <hr /> 
  Clone this repository and import into Android Studio

  > git clone git@github.com:wolox/<reponame>.git
  
 ### Configuration
 <hr> 
  Create app/keystore.gradle with the following info:

  > ext.key_alias='...' <br />
  > ext.key_password='...' <br />
  > ext.store_password='...'
  
 ### Generating Signed Apk
 <hr>
  From Android Studio:

  1. Build menu
  2. Generate Signed APK...
  3. Fill in the keystore information (you only need to do this once manually and then let Android Studio remember it)
