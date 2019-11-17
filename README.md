# ParallelogramHorizontalLoader
<img src="screens/sample.gif"/>
A parallelogram horizontal loader indicator library for Android.

## Getting Started

### Setting up the dependency
Add the library to your module ```build.gradle```
    
```
implementation 'com.sanjay.parallelogramhorizontalloader:parallelogram-horizontal-loader:1.0.0'
```

## Demo App
To run the demo project, clone the repository and run it via Android Studio.

## Usage
### Adding directly in layout.xml
```
<com.sanjay.parallelogramhorizontalloader.ParallelogramHorizontalLoader
        android:id="@+id/horizontalLoader"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:pg_animate_fill_color="@color/red"
        app:pg_count="6"
        app:pg_normal_fill_color="@color/white"
        app:pg_selection_interval_duration="300"
        app:pg_size="20dp" />
```
## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
