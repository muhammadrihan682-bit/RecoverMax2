RecoverMax - Photo/Video Recovery App
एक Android app जो deleted photos और videos को recover करता है।
🎯 Features
✅ Deep storage scanning
✅ Deleted photos/videos recovery
✅ Thumbnail preview
✅ One-click recovery
✅ Beautiful modern UI
✅ No root required
📱 Supported Formats
Images: JPG, JPEG, PNG, GIF, WEBP, BMP
Videos: MP4, MKV, AVI, MOV, FLV, WMV, 3GP, WEBM
🚀 Quick Start (GitHub Actions)
Step 1: Fork this Repository
Go to GitHub.com
Search for this repo
Click Fork button
Wait for fork to complete
Step 2: Enable GitHub Actions
Go to your forked repo
Click Actions tab
Click I understand my workflows, go ahead and enable them
Step 3: Trigger Build
Go to Code tab
Click Add file → Create new file
Name: trigger.txt
Add any content
Click Commit changes
Go to Actions tab
Wait for build to complete (3-5 minutes)
Step 4: Download APK
Click on Build APK workflow
Click on the completed build
Under Artifacts, click app-release
APK will download automatically
Step 5: Install on Phone
Transfer APK to your phone
Open file manager
Tap APK file
Click Install
Grant storage permissions
📲 How to Use
Open RecoverMax on your phone
Tap "Start Scan" button
Grant storage permissions when asked
Wait for scanning to complete
Tap any file to recover it
Check Downloads → RecoveredFiles folder
⚙️ System Requirements
Android 5.0+ (API 21+)
100MB free storage
Storage permissions enabled
🔐 Permissions Required
READ_EXTERNAL_STORAGE
WRITE_EXTERNAL_STORAGE
MANAGE_EXTERNAL_STORAGE
READ_MEDIA_IMAGES (Android 13+)
READ_MEDIA_VIDEO (Android 13+)
📊 Expected Results
Success Rate: 60-75% files
Scan Time: 1-5 minutes
Recovery Location: /Downloads/RecoveredFiles/
⚠️ Important Notes
✅ Works without root
✅ Safe and secure
✅ No data collection
⚠️ Recovery depends on storage state
⚠️ Overwritten files cannot be recovered
🛠️ For Developers
Build Locally (with Android Studio)
git clone https://github.com/muhammadrihan682-bit/RecoverMax.git
cd RecoverMax
./gradlew assembleRelease
APK location: app/build/outputs/apk/release/app-release.apk
RecoverMax/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/recovermax/app/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── .github/
│   └── workflows/
│       └── build.yml
└── settings.gradle.kts
Troubleshooting
Build fails on GitHub Actions
Check if all files are in correct directories
Ensure build.gradle.kts is properly formatted
Check GitHub Actions logs for errors
APK doesn't download
Wait 5-10 minutes for build to complete
Refresh the page
Check if build was successful
App crashes on phone
Clear app cache: Settings → Apps → RecoverMax → Storage → Clear data
Reinstall APK
Ensure Android 5.0 or higher
Files not found during scan
Make sure storage permissions are granted
Check if files exist in DCIM/Pictures folders
Try scanning again
📞 Support
For issues or questions:
Check this README
Check app permissions
Try reinstalling APK
Ensure sufficient storage space
📄 License
This project is free to use and modify.
🙏 Credits
Built with Kotlin and Android Studio.
