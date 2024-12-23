<p align="center">
  <img src="app\src\main\res\mipmap-xxxhdpi\ic_launcher_foreground.webp" alt="Chiba Player Logo" width="200"/>
</p>

# Chiba Player

## Overview

Chiba Player is a modern Android-based media player designed to enhance the experience of managing and playing multimediaplaylists. With its versatile functionality and user-friendly interface, Chiba Player caters to users looking for flexibility in loading playlists from various sources, including default URLs, custom URLs, and local files.

## Features

1.  **Playlist Management**

    *   **Default Playlist:** Load a predefined playlist for instantplayback.
    *   **Custom URL:** Enter a URL to fetch and play personalized playlists.
    *   **Local Files:** Select and load playlists directly from the device’s storage.
2.  **User-Friendly Interface**

    *   **Dynamic Navigation:** Focus-based navigation for easy control on TVs and large screens.
    *   **Progress Indicators:** Real-time feedback during playlist loading.
    *   **Error Handling:** Informative messages for invalid files or URLs.
3.  **Multi-Screen Compatibility**

    *   Supports both portrait and landscape orientations.
        *Optimized for smartphones, tablets, and large-screen devices (e.g., Android TV).
4.  **Responsive Design**

    *   Adapts layouts dynamically based on screen size.
    *   Provides landscape-specific layouts for larger screens or TVs.
      
## App Screenshots

<p align="right">
  <img src="app\src\main\res\mipmap-xxxhdpi\Screenshot\hello.png" alt="Screenshot of menu" width="300"/>
</p>
<p align="center">
  <img src="app\src\main\res\mipmap-xxxhdpi\Screenshot\menu.png" alt="Screenshot of TypePlayerActivity" width="300"/>
</p>
<p align="center">
  <em>Screenshot of TypePlayerActivity, where users can select the type of playlist.</em>
</p>

<p align="center">
  <img src="app\src\main\res\mipmap-xxxhdpi\Screenshot\category.png" alt="Screenshot of CategoryActivity" width="300"/>
</p>
<p align="center">
  <em>Screenshot of CategoryActivity, showing the list of categories.</em>
</p>
<p align="center">
  <img src="app\src\main\res\mipmap-xxxhdpi\Screenshot\player.png" alt="Screenshot of player" width="300"/>
</p>

## Technical Details

### Playlist Format

Chiba Player supports playlists in the **M3U** format. This is a common format for media playlists, allowing you to list URLs of media streams.

**Example M3U Playlist:**
#EXTM3U #EXTINF:-1,
Channel 1 http://example.com/stream1 #EXTINF:-1,
Channel 2 http://example.com/stream2

### Data Model

*   **Channel:** Represents a single media stream (e.g., a TV channel, a radio station). Each channel has a name and a URL pointing to the stream.
*   **Category:** A group of related channels (e.g., "News," "Sports," "Music"). A channel can belong to one or more categories.

### Default Playlist

Chiba Player includes a default playlist containing a selection of public domain media streams. This playlist is loaded fromthe following URL: `https://example.com/default_playlist.m3u` (replace with the actual URL).

### Error Handling

Chiba Player provides informative error messages for the following situations:

*   **Invalid URL:** If the user enters a URL that is not properly formatted, an error dialog will be displayed.
*   **File Not Found:** If the user selects a local file that does not exist, an error message will be shown.
*   **Network Error:** If there is a problem connecting to a URL, a network error message will be displayed.
*   **Unsupported playlist format:** If the file is not in the M3U format, an error will be displayed.

### Singleton Management

The `ManagePlaylist` class is implemented as a singleton to ensure that there is only one instance of the playlist data throughout the application. This allows different parts of the app to access and modify the same playlist data consistently.

### Language and Frameworks

*   **Programming Language:** Kotlin
*   **Core Libraries:**
    *   Android Jetpack components
    *   Glide for image loading
    *   RecyclerView for list display

### Architecture

*   **Activity-Based:** Utilizes modern ComponentActivity for streamlined lifecycle management.
*   **Singleton Management:** `ManagePlaylist` singleton for centralized playlist handling.
*   **Custom Adapters:** Flexible RecyclerView adapters for categories and playlists.

### Compatibility

*   **Minimum SDK:** Android 5.0 (API Level 21)
*   **Target SDK:** Android 13 (API Level 33)

### File Structure

*   **Core Packages**
    *   `ch.app.chibaplayer`: Core package containing the entry point (`MainActivity`).
    *   `ch.app.chibaplayer.activity`: Activities handling various user interactions (e.g., `UrlActivity`, `TypePlayerActivity`, `CategoryActivity`).
    *   `ch.app.chibaplayer.adapter`: Adapters for RecyclerView (e.g., `CategoryAdapter`, `PlaylistAdapter`).
    *   `ch.app.chibaplayer.domain`: Data models (`Channel`, `Category`).
    *   `ch.app.chibaplayer.metier`: Business logic (e.g., playlist fetching and management).
*   **Key Files**
    *   `MainActivity`: Launch screen showing aloading GIF before transitioning to `TypePlayerActivity`.
    *   `TypePlayerActivity`: Allows users to choose between Default, URL, or Local playlists. Handles playlist loading and navigation.
    *   `CategoryActivity`: Displays categorized channels for selection.
    *   `PlaylistAdapter` & `CategoryAdapter`: RecyclerView adapters for displaying playlists and categories dynamically.

## Setup Instructions

### Prerequisites

*   Android Studio (latest version recommended)
*   Android device or emulator running API Level 21+
*   If using a physical device, ensure that **Developer Mode** is enabled.

### Steps to Run

1.  **Clone the repository:**
    bash git clone https://github.com/chiheb122/chiba-player.git
2.  **Open the project in Android Studio.**
3.  **Sync Gradle files and resolve dependencies:** Android Studio will prompt you to do this after opening the project.
4.  **Set up an Android emulator (if needed):**
    *   In Android Studio, go to "Tools" -> "Device Manager."
    *   Click "Create Device" and follow the instructions to create a new emulator.
5.  **Build and run the app:**
    *   Click the green "Run" button in Android Studio.
    *   Select your emulator or physical device from the list.

## Usage Instructions

1.  **Launch the App:**
    *   The app opens with a splash screen featuring a loading animation.
    *   Transitions to the type selection screen (`TypePlayerActivity`).
2.  **Select Playlist Type:**
    *   **Default:** Loads a predefined URL.
    *   **URL:** Enter a custom playlist URL in the input field.
    *   **Local File:** Open Android’s file picker to select a playlist file.
3.  **Explore Categories:**
    *   Categories are displayed in `CategoryActivity`, grouped for easy navigation.
4.  **Play Media:**
    *   Select a channel from the playlist to start streaming.

## Future Enhancements

*   **Advanced Playback Controls:**
    *   Fast forward and rewind.
    *   Volume control and mute.
    *   Full-screen mode.
    *   Playback speed adjustment.
*   **Integration with External APIs:**
    *   Support for streaming from platforms like YouTube or Twitch.
    *   Integration with online radio services.
*   **Cloud-Based Playlist Synchronization:**
    *   Allow users to create accounts and sync their playlists across multiple devices.
    *   Backup and restore playlists.
*   **Playlist Editing and Saving:**
    *   Add, remove, and reorder channels in a playlist.
    *   Save custom playlists to local storage.
    *   Rename playlists.

## Contributing

We welcome contributions from the community! To contribute:

1.  Fork the repository.
2.  Create a feature branch:
bash git checkout -b feature-name
3. Commit your changes and push:
bash git push origin feature-name
4.  Submit a pull request.

## License

This project is licensed under the MIT License.

## Acknowledgments

*   Glide for efficient image loading.
*   Android Jetpack for modern app development components.

Feel free to report bugs, suggest features, or submit pull requests to make Chiba Player even better!
