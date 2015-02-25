#從Eclipse ADT轉移到Android Studio
CodeDate的Android Studio系列專欄，在連載的中途發表Android 5 Lollipop，還有全新的開發工具Android Studio。因為版本與開發工具的變化非常大，因此決定把系列專欄昇級為Android 5與Android Studio，專欄與範例程式專案在2015/03/02同步更新。

因為之前的範例程式專案採用Eclipse ADT，如果你已經依照之前的專欄撰寫應用程式，可以考慮將它轉移到Android Studio，再繼續依照後續的專欄學習全新的Android Studio。

依照下列的步驟執行轉移的工作：

1. 依照<http://www.codedata.com.tw/mobile/android-tutorial-the-1st-class-2-android-sdk/>的說明，安裝好Android Studio開發環境。
2. 啟動Android Studio，選擇「Import project(Eclipse ADT, Gradle, etc.)」：

    ![](https://github.com/macdidi5/AndroidTutorial/blob/master/images/migrate/AndroidTutorial5_migrate_01.png)
 
3. 選擇使用Eclipse ADT開發的Android應用程式專案：

    ![](https://github.com/macdidi5/AndroidTutorial/blob/master/images/migrate/AndroidTutorial5_migrate_02.png)

4. 選擇轉移後的專案儲存位置，選擇「Next」:

    ![](https://github.com/macdidi5/AndroidTutorial/blob/master/images/migrate/AndroidTutorial5_migrate_03.png)

5. 選擇「Finish」:

    ![](https://github.com/macdidi5/AndroidTutorial/blob/master/images/migrate/AndroidTutorial5_migrate_04.png)

6. 等候Android Studio完成轉換工作並開啟專案後，你會發現專案有一些錯誤。開啟「MyAndroidTutorial -> app -> build.gradle」，參考下面的內容，修改並儲存這個檔案（注意applicationId的設定，必須參考你實際的專案修正）：

        apply plugin: 'com.android.application'

        android {
            compileSdkVersion 21
            buildToolsVersion "21.1.2"

            defaultConfig {
                applicationId "net.macdidi.myandroidtutorial"
                minSdkVersion 15
                targetSdkVersion 21
                versionCode 1
                versionName "1.0"
            }
            buildTypes {
                release {
                    minifyEnabled false
                    proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
                }
            }
        }

        dependencies {
            compile fileTree(dir: 'libs', include: ['*.jar'])
            compile 'com.android.support:appcompat-v7:21.0.3'
        }

7. 開啟「MyAndroidTutorial -> app -> src -> main -> AndroidManifest.xml」，參考下面的內容，修改並儲存這個檔案（移除SDK與版本設定）：

        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            package="net.macdidi.myandroidtutorial">
        
            <!-- 省略 -->
        
            <application
                android:allowBackup="true"
                android:icon="@drawable/ic_launcher"
                android:label="@string/app_name"
                android:theme="@style/AppTheme" >
                
                <!-- 省略 --> 
                
            </application>
        
        </manifest>

8. 選擇「Try Again」：

    ![](https://github.com/macdidi5/AndroidTutorial/blob/master/images/migrate/AndroidTutorial5_migrate_05.png)

9. 選擇「Yes」讓Android Studio關閉與重新啟動專案：

    ![](https://github.com/macdidi5/AndroidTutorial/blob/master/images/migrate/AndroidTutorial5_migrate_06.png)

10. 接下來就可以依照專欄的內容，使用Android Studio開發工具繼續學習。
