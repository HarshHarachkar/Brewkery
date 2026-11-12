<div align="center">

<img src="docs/images/brewkery-logo.png" width="15%" alt="Brewkery Banner"/>

# Brewkery

### Your Daily Dose of Coffee, Crafted Digitally.

A modern Android coffee and bakery application built with **Kotlin and XML**, offering product discovery, drink customization, cart management, and order tracking.

<br/>

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![UI](https://img.shields.io/badge/UI-XML-orange?style=for-the-badge)
![API](https://img.shields.io/badge/API-REST-blue?style=for-the-badge)

<br/>

**[Explore Repository](https://github.com/HarshHarachkar/Brewkery)**

</div>

---

## 📖 About Brewkery

Brewkery is a modern coffee and bakery Android application designed to provide a smooth and convenient ordering experience.

Users can explore a variety of coffee and bakery products, view detailed product information, customize their drinks according to their preferences, manage their shopping cart, and track their orders.

The application focuses on a premium user interface, dynamic pricing, REST API integration, and a consistent experience across light and dark themes.

---

## ✨ Key Features

| Feature | Description |
|---|---|
| 🏠 Home | Explore featured products, categories and promotions. |
| ☕ Product Menu | Browse coffee and bakery products. |
| 📋 Product Details | View product descriptions, ratings and pricing. |
| 🎨 Customization | Select drink size, milk options and sugar levels. |
| 🛒 Shopping Cart | Add, remove and manage customized products. |
| 💰 Dynamic Pricing | Automatic subtotal, tax and delivery calculations. |
| 📦 Checkout | Place orders with unique order ticket IDs. |
| 🚚 Order Tracking | Follow the order through its different stages. |
| 🌙 Dark Mode | Switch between light and dark themes. |
| 🌐 API Integration | Retrieve menu and product information from a REST API. |

---

## 📱 App Screenshots

### Home Screen

Explore coffee, bakery products, categories and featured promotions.

<!-- Add Home screenshot -->

<img src="docs/screenshots/home.png" width="250" alt="Brewkery Home Screen"/>

### Product Details

View product information and customize your preferred drink.

<img src="docs/screenshots/prodect-details.png" width="250" alt="Product Details Screen"/>

### Shopping Cart

Review customized products, update quantities and view the complete price breakdown.

<img src="docs/screenshots/cart.png" width="250" alt="Shopping Cart Screen"/>

### Order Tracking

View your order ticket, current status and order progress.

<img src="docs/screenshots/order-status.png" width="250" alt="Order Tracking Screen"/>

### Dark Theme

A consistent dark experience with carefully selected colors and readable components.

<img src="docs/screenshots/dark-home.png" width="250" alt="Brewkery Dark Home Screen"/>

<img src="docs/screenshots/dark-cart.png" width="250" alt="Brewkery Dark Cart Screen"/>

---

## 🛠️ Technology Stack

| Technology | Implementation |
|---|---|
| Kotlin | Primary programming language |
| XML | Native Android UI development |
| ViewBinding | Type-safe view interaction |
| Retrofit | REST API communication |
| Kotlin Coroutines | Asynchronous operations |
| RecyclerView | Dynamic product and cart lists |
| Glide / Coil | Image loading |
| Material Design 3 | UI components and styling |
| SharedPreferences | Theme preference storage |

---

## 🌐 API Integration

Brewkery retrieves menu and product information using REST APIs.

**Menu Endpoint**

```text
https://raw.githubusercontent.com/VivekShah138/Brewkery/main/data.json
```

**Product Details Endpoint**

```text
https://raw.githubusercontent.com/VivekShah138/Brewkery/main/api/items/{id}.json
```

---

## 💰 Pricing & Checkout

The application calculates the order amount dynamically.

| Component | Calculation |
|---|---:|
| Subtotal | Based on selected products and quantities |
| Delivery Fee | $2.50 |
| Tax | 8% |
| Total | Subtotal + Delivery + Tax |

---

## 🏗️ Development Environment

- **IDE:** Android Studio
- **Language:** Kotlin
- **UI:** XML Layouts
- **Minimum SDK:** Android 6.0 (API 23)
- **Architecture:** Repository-based data access

---

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK 23 or above
- Internet connection

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/HarshHarachkar/Brewkery.git
```

**2. Open the project**

Open the cloned folder in Android Studio.

**3. Sync Gradle**

Allow Android Studio to download dependencies and complete Gradle synchronization.

**4. Run the application**

Connect an Android device or start an emulator and click Run.

---

## 👨‍💻 Developer

<div align="center">

### Harsh Harachkar

BSc Information Technology | Android Developer

Building Android applications with Kotlin.

[![GitHub](https://img.shields.io/badge/GitHub-HarshHarachkar-181717?style=for-the-badge&logo=github)](https://github.com/HarshHarachkar)

</div>

---

<div align="center">

**Brewkery – Crafted for Coffee Lovers. ☕**

</div>
