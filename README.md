
## Project Overview

This is an **Android Event Management Application** built with Java and Kotlin. It allows users to manage seminars, events, and bookings for a college seminar hall. The app features a calendar interface, task management, PDF report generation, and user authentication.

## Project Structure

```
Event - Copy/
├── app/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/arun/event/
│   │   │   │   ├── ui/              # Activities (MainActivity, AddNewTaskActivity, etc.)
│   │   │   │   ├── adapter/         # RecyclerView & ListView adapters
│   │   │   │   ├── db/              # Database (Room, DAO, entities)
│   │   │   │   ├── repository/      # TaskRepository for data management
│   │   │   │   ├── utils/           # Utility classes
│   │   │   │   └── listeners/       # Click listeners
│   │   │   ├── res/                 # Resources (layouts, drawables, values)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                    # Unit tests
│   │   └── androidTest/             # Instrumented tests
│   ├── build.gradle                 # App-level dependencies
│   └── proguard-rules.pro
├── build.gradle                  # Project-level build configuration
├── settings.gradle
├── gradle.properties
└── gradle/                       # Gradle wrapper

```

## Key Features

### 1. **User Authentication**
- Login system with username/password validation
- UserLoginActivity

### 2. **Calendar View**
- Interactive calendar grid with date selection
- CalendarView
- CalendarAdapter

### 3. **Task Management**
- Create, read, update, delete (CRUD) events
- MainActivity
- AddNewTaskActivity
- Task Entity

### 4. **Database**
- Room Database for persistent storage
- TaskDatabase
- Date type conversion support
- TaskDao
- TaskRepository

### 5. **PDF Report Generation**
- Generate and export event reports as PDF
- Uses iTextPDF library
- MainActivity - createPdf()

### 6. **Permissions Management**
- Read/Write external storage permissions
- Uses Dexter library for runtime permissions

## Tech Stack

### Dependencies
```gradle
- Android AppCompat 1.6.1
- AndroidX Material 1.11.0
- Room Database 2.6.1
- Lifecycle (ViewModel, LiveData) 2.6.2
- RecyclerView
- CardView
- Dexter 6.2.2 (Permissions)
- iTextPDF 5.5.10 (PDF generation)
- Kotlin Stdlib 1.9.22
```

### Minimum Requirements
- **Min SDK:** 21
- **Target SDK:** 34
- **Compile SDK:** 34
- **Java Version:** 17

## Database Schema

### Task Entity
```java
@Entity(tableName = "task")
public class Task {
    int id (Primary Key)
    String title
    int dept
    int session
    String staffName
    String mobileNumber
    String notes
    Date date
}
```

## Main Activities

| Activity | Purpose |
|----------|---------|
| UserLoginActivity | Initial login screen |
| LoginActivity | Dean authentication |
| MainActivity | Main calendar & event display |
| AddNewTaskActivity | Create/edit events |
| ReportActivity | Generate PDF reports |

## Building & Running

```bash
# Build the project
./gradlew build

# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Run tests
./gradlew test
./gradlew connectedAndroidTest
```

## Departments & Sessions

**Departments:** Utils.getDept()
- I/II/III BSC (CS, CT, IT)
- I/II BCA
- I/II MSC (CS, CT, IT)
- I/II MCA
- M.Phil
- Others

**Sessions:** Utils.getSession()
- Fore Noon
- After Noon
- Full Day

## File Storage

Generated reports are saved to:
```
/sdcard/Download/kasc/semi-report.pdf
```

## Notes

- The app uses `AsyncTask` for database operations
- PDF reports include table with Date, Title, Staff Name, Department, and Session
- Swipe gestures enabled for task deletion
- Live data observers for real-time UI updates

User: made  me this as pdf

GitHub Copilot: I'll help you convert this README to a PDF. Here are the steps to create a PDF from the README:

## Option 1: Using VS Code Extensions (Easiest)

1. Install the **"Markdown PDF"** extension in VS Code
2. Create a file named `README.md` in your project folder
3. Copy the README content into it
4. Right-click on `README.md` → Select **"Markdown PDF: Export (pdf)"**
5. The PDF will be generated in the same folder

## Option 2: Manual Export

1. Copy the README content
2. Paste it into a tool like:
   - [Markdown to PDF Online](https://markdowntopdf.com/)
   - [Pandoc](https://pandoc.org/) (command-line tool)
   - Microsoft Word → Save as PDF

## Option 3: Using Pandoc (Windows Command)

If you have Pandoc installed, run:

```bash
pandoc README.md -o README.pdf
```

## Option 4: Create a Python Script

````python
from markdown2pdf.markdown2pdf import convert

convert('README.md', 'README.pdf')
````

Then in terminal:
```bash
python convert_readme.py
```

**I recommend Option 1** (Markdown PDF extension) - it's the quickest and requires no additional setup. Would you like me to help with any of these approaches?
