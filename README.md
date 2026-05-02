# 🏥 MediConnect — Telemedicine Platform

> A production-grade full-stack telemedicine platform built with Java 21 + Spring Boot 3

## 🚀 Live Demo
[Deploy link — https://mediconnect-api-fvp2.onrender.com/]

## ✨ Features

| Feature | Technology |
|---------|-----------|
| JWT Authentication | Spring Security + JJWT |
| Role-based Access | PATIENT / DOCTOR / ADMIN |
| Real-time Chat | WebSocket + STOMP |
| Video Consultation | WebRTC peer-to-peer |
| Smart Slot Booking | Redis-cached availability |
| PDF Prescription | iText 5 |
| Email Notifications | Spring Mail |
| OTP Password Reset | Redis TTL (5 min) |
| Payment Gateway | Razorpay |
| AI Symptom Checker | Rule-based specialist recommendation |
| Doctor Online Status | Redis heartbeat |
| Analytics Dashboard | Doctor earnings + stats |
| Admin Panel | Full platform management |
| Notification Bell | Real-time WebSocket alerts |
| Appointment Reschedule | Slot rebooking system |
| Doctor Calendar | Monthly appointment view |
| Medical Report Upload | File storage system |
| Unit Testing | JUnit 5 + Mockito |

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Security | Spring Security + JWT |
| Database | PostgreSQL 16 |
| Cache | Redis |
| Real-time | WebSocket (STOMP) |
| Video | WebRTC |
| PDF | iText 5 |
| Payment | Razorpay |
| Build | Maven |
| Testing | JUnit 5, Mockito |

## 📦 Setup & Run

### Prerequisites
- Java 21
- PostgreSQL 16
- Redis

### Steps

```bash
# 1. Clone karo
git clone https://github.com/YOUR_USERNAME/mediconnect.git
cd mediconnect

# 2. PostgreSQL database banao
createdb mediconnect_db

# 3. application.yml update karo
# DB password, mail credentials set karo

# 4. Redis start karo

# 5. Run karo
mvn spring-boot:run

# 6. Browser mein kholo
http://localhost:8081
```

## 🔗 API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/register | Register | Public |
| POST | /api/auth/login | Login | Public |
| POST | /api/auth/forgot-password | Send OTP | Public |
| POST | /api/auth/reset-password | Reset password | Public |
| GET | /api/doctors | All doctors | All |
| GET | /api/doctors/filter | Search+filter | All |
| POST | /api/appointments/book | Book slot | Patient |
| GET | /api/appointments/slots/{id} | Available slots | All |
| PATCH | /api/appointments/{id}/reschedule | Reschedule | All |
| POST | /api/prescriptions/generate | PDF generate | Doctor |
| GET | /api/analytics/doctor/{id} | Doctor stats | Doctor |
| GET | /api/admin/stats | Platform stats | Admin |
| POST | /api/symptoms/check | AI symptom check | All |
| POST | /api/payment/create-order | Razorpay order | Patient |
| POST | /api/reports/upload/{id} | Upload report | All |

## 🏗️ Project Structure
