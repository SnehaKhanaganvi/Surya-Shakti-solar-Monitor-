# ☀️ SuryaShakti — Solar Energy Monitor

A smart Android application for monitoring solar energy generation, consumption, and savings in real time.

---

## 📱 About the App

**SuryaShakti** (meaning *Solar Power* in Hindi) is an Android app that helps solar panel owners track their daily energy generation, consumption, battery levels, and financial savings. It simulates weather conditions to estimate solar output and sends smart notifications during peak sun hours.

---

## ✨ Features

- 📊 **Dashboard** — Real-time overview of solar generation, consumption, net energy, and green score
- ⚡ **Generation Log** — Track daily kWh generated with weather conditions
- 🔋 **Consumption Tracker** — Monitor energy usage and grid dependency
- 💰 **Savings Calculator** — View financial savings and grid export earnings (₹)
- 🌤️ **Weather Simulator** — Simulates weather to estimate daily solar output
- 🔔 **Peak Sun Alerts** — Push notifications during optimal solar generation periods
- 🟢 **Green Score** — A score reflecting your daily solar self-sufficiency

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| **Kotlin** | Primary language |
| **Android Navigation Component** | Fragment navigation with bottom nav |
| **ViewModel + LiveData** | MVVM architecture |
| **Room Database** | Local data persistence |
| **MPAndroidChart** | Energy charts and graphs |
| **Kotlin Coroutines** | Async operations |
| **ViewBinding** | View access |
| **Notification API** | Peak sun alerts |

---

## 📋 Requirements

- **Android Studio** Hedgehog or later
- **Min SDK:** Android 7.0 (API 24)
- **Target SDK:** Android 14 (API 34)
- **Java Version:** 17
- **Kotlin:** 1.9+

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/SuryaShakti.git
cd SuryaShakti
```

### 2. Open in Android Studio

- Open **Android Studio**
- Click **File → Open**
- Select the `SuryaShakti` folder

### 3. Sync Gradle

Android Studio will automatically sync. If not, click:
**File → Sync Project with Gradle Files**

### 4. Run the App

- Connect an Android device or start an emulator
- Click the ▶️ **Run** button

---

## 📁 Project Structure

```
SuryaShakti/
├── app/
│   └── src/main/java/com/suryashakti/solarmonitor/
│       ├── MainActivity.kt               # Entry point
│       ├── data/
│       │   ├── AppDatabase.kt            # Room database
│       │   ├── EnergyDao.kt              # Data access object
│       │   ├── EnergyLog.kt              # Energy data model
│       │   └── EnergyRepository.kt       # Data repository
│       ├── ui/
│       │   ├── dashboard/                # Dashboard screen
│       │   ├── generation/               # Generation log screen
│       │   ├── consumption/              # Consumption screen
│       │   └── savings/                  # Savings screen
│       ├── viewmodel/
│       │   └── EnergyViewModel.kt        # Shared ViewModel
│       ├── utils/
│       │   ├── WeatherSimulator.kt       # Weather simulation logic
│       │   └── NotificationHelper.kt     # Push notifications
│       └── custom/
│           └── CircularProgressView.kt   # Custom progress UI
```

---

## 📊 App Screens

| Screen | Description |
|---|---|
| **Dashboard** | Solar % circle, green score, net energy, weather |
| **Generation Log** | Daily generation history with charts |
| **Consumption** | Energy consumption tracking |
| **Savings** | Financial savings breakdown in ₹ |

---

## 🔔 Notifications

The app sends push notifications when:
- ☀️ Peak sun hours are detected
- 🚀 Solar generation exceeds consumption (exporting to grid)

---

## 🤝 Contributing

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**SuryaShakti Team**
- GitHub: [@yourusername](https://github.com/yourusername)

---

> ⚡ *Harness the power of the sun with SuryaShakti!*
