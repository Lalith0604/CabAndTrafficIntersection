# 🚕 Hyderabad Smart Traffic Signal & Cab Route Tracker

Demo video: https://www.loom.com/share/09434530623549a7a3e619ac0580c1ec

A full-stack **microservice-based smart routing simulation system** that visualizes how traffic signals affect a cab’s movement in real time.  
Built using **Spring Boot**, **React + Mapbox**, and **Mapbox Directions API**, this project demonstrates how integrating traffic data can improve user experience and route transparency.

---

## 🧭 Purpose

When users book a cab, sometimes it takes longer than expected for the cab to arrive — not because of driver inefficiency, but due to **traffic lights or congestion**.  
Users often get frustrated without knowing the reason for the delay.  

This project solves that by showing the **exact cause of delay (RED signals)** in real-time on the map — helping users understand delays visually and reducing frustration.

---

## ⚙️ System Overview

The project consists of **two backend microservices** and **one frontend application**:

### 🟢 1. Traffic Signal Service (Spring Boot - Port 8080)
- Simulates **40+ Hyderabad city traffic signals**.  
- Each signal automatically cycles between **RED → GREEN → RED** every 60 seconds.  
- Provides APIs to fetch the nearest signal’s status, name, and remaining time.

### 🚕 2. Route Tracking Service (Spring Boot - Port 8081)
- Fetches a real route from **Mapbox Directions API** between cab and user.
- Simulates cab movement every few seconds.
- If the cab is within **100m of a RED signal**, it stops and waits until the light turns **GREEN**.

### 🗺️ 3. Frontend (React + Vite + MapboxGL + TailwindCSS + Framer Motion)
- Detects the user’s live location with animation.
- Sends coordinates to backend (`/api/route/set-location`).
- Displays:
  - Cab movement with smooth animation.
  - Route line in black.
  - Traffic signals (🚦) when nearby and RED.
  - Countdown timer inside the traffic light marker.
  - “Cab has arrived” modal when destination is reached.

---

## 🧩 Architecture Diagram

![System Architecture](A_flowchart-style_diagram_illustrates_a_system_arc.png)

**Flow:**
Frontend (React @5173)
↓
Route Service (Spring Boot @8081)
↓
Traffic Signal Service (Spring Boot @8080)
↑
Mapbox Directions API

yaml
Copy code

---

## 🚦 APIs

### 🚦 Traffic Signal Service (Port 8080)
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/api/signal-status?lat={lat}&lon={lon}` | Returns nearest signal with name, status, and countdown |
| `GET` | `/api/all-signals` | Returns all signals and current states |

### 🚕 Route Tracking Service (Port 8081)
| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/api/route/set-location` | Sets user location (latitude, longitude) |
| `POST` | `/api/route/start` | Starts route simulation |
| `POST` | `/api/route/stop` | Stops route simulation |
| `GET` | `/api/route/current` | Returns current cab location |

---

## 🧠 Tech Stack

| Layer | Technologies |
|--------|--------------|
| **Frontend** | React, TypeScript, TailwindCSS, Vite, Mapbox GL, Framer Motion |
| **Backend (Route)** | Java 17, Spring Boot, Mapbox Directions API |
| **Backend (Signal)** | Java 17, Spring Boot, Scheduled Tasks |
| **Map API** | Mapbox Directions API |
| **Ports** | Frontend → `5173`, Route Service → `8081`, Traffic Signal Service → `8080` |

---

## 🛠️ Setup Instructions

### ✅ Prerequisites
- Node.js (v18 or newer)
- Java 17+
- Maven
- Mapbox Access Token (replace your own if needed)

---

### ⚙️ Backend Setup

#### 🟠 Run Traffic Signal Service
```bash
cd traffic-signal-service
mvn spring-boot:run
Runs on port 8080

🟣 Run Route Tracking Service
bash
Copy code
cd route-tracking-service
mvn spring-boot:run
Runs on port 8081

🧩 Frontend Setup
bash
Copy code
cd frontend
npm install
npm run dev
Runs on port 5173 (http://localhost:5173)

🚗 How It Works
1️⃣ User’s current location is detected and sent to backend (/set-location).
2️⃣ Cab route is fetched from Mapbox between user and destination.
3️⃣ Cab (🚕) moves along the black route line on the map.
4️⃣ If the cab reaches a nearby RED signal, it stops and a 🚦 marker appears with a countdown timer.
5️⃣ Once signal turns GREEN, cab resumes.
6️⃣ Upon reaching the user, a modal appears: “Cab has arrived” ✅

📸 Screenshots
Cab Stopped at RED Light	Cab Moving on Route

💡 Key Features
🚦 Live signal simulation for Hyderabad city

🚕 Real-time cab movement visualization

⏱️ Dynamic signal countdown updates

📍 Automatic user location detection

🗺️ Smooth route animation (MapboxGL + Framer Motion)

💬 Realistic delay explanation (why cab is slow)

💼 Use Cases
Smart City Traffic Simulation

Cab/Delivery Delay Visualization

Education/Research on IoT and Mobility

Intelligent Transport System Prototype

📈 Future Enhancements
Integrate live Hyderabad Traffic API

Add real cab data integration

Machine Learning–based ETA prediction

Multi-cab tracking and live dashboards

Integration with EV charging route optimization

👨‍💻 Developer
M. Lalith Kumar
📍 Full Stack Developer (Spring Boot + React)
🚀 Passionate about building real-time mobility systems

🏁 Conclusion
This project bridges the gap between real-world cab tracking and smart traffic visualization, helping users understand route delays better.
By simulating real signal behavior and cab movement, it demonstrates how simple data integration can drastically improve user experience in transportation platforms.

⭐ If you found this project interesting, give it a star on GitHub!

markdown
Copy code

---

✅ **Tips for GitHub Presentation:**
- Save your architecture image as `A_flowchart-style_diagram_illustrates_a_system_arc.png` in the project root.  
- Add both screenshots (`Screenshot (290).png`, `Screenshot (293).png`) inside the same folder.  
- You can preview how it looks using **GitHub’s Markdown Preview (Ctrl+Shift+V)** in VS Code.

Would you like me to also generate a **GitHub repository description + tags + topics section** (for the right sidebar)? That helps your project rank higher when shared.

