# MentorMe App with RESTful APIs

This repository contains the implementation for the "MentorMe" mobile application with web services for the backend.

## Features Implemented

1. **Splash Screen**
    - Automatically navigates to the next screen after 5 seconds.

2. **User Authentication**
    - Signup, login, and logout functionalities.
    - Navigation based on the user's authentication status.

3. **Mentor Management**
    - Display all mentors on the homepage.
    - Add mentors to the mentor's list.
    - Mark mentors as favorites for quick access.
    - Review mentors based on user experience.

4. **Push Notifications**
    - Notifications for new messages, mentor updates, and other important events.
    - Notifications are also displayed on the notifications screen.

5. **Search and Filter**
    - Implement search functionality with filters to facilitate finding mentors.

6. **Booking System**
    - Book sessions with mentors.

7. **Profile Management**
    - Change profile DP and cover photo.
    - Edit profile information.
    - Update favorites and user reviews.

8. **Chat System**
    - Text messages, voice notes, image, video, and file uploads.
    - Camera feature for taking and uploading photos within chats.
    - Video/audio calls using Agora API.
    - Delete or edit messages within 5 minutes.
    - Notifications for chat screenshots.

9. **Offline Functionality (to be implemented)**
    - Display old messages and relevant content while offline.
    - Queue and send messages once the internet connection is restored.
    - Use SQLite for offline data storage.
    - Cache images using Glide library.

## Technologies Used

- **Backend:** Web Services (PHP)
- **Database:** MySQL for storing messages, calls, and images; SQLite for offline access.
- **Push Notifications:** Firebase Cloud Messaging (FCM)
- **APIs:** Agora API for video/audio calls
- **Libraries:** Glide for image caching

## Setup Instructions

1. **Clone the repository:**
    ```sh
       git clone https://github.com/Vaneeza-7/Mentor-me-android-app-with-RESTful-APIs.git
     ```

2. **Install dependencies:**
    - Ensure you have XAMPP server with MySql.
    - Set up the database and configure the connection settings in the backend code.
    - You can see complete steps [here](https://github.com/Vaneeza-7/Server-Side-Code-for-mentorme-android-app-with-restful-api)

3. **Run the application:**
    - Start the server to handle web service requests.
    - Deploy the mobile application on an Android device/emulator.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
