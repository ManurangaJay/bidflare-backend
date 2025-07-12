# 🚀 BidFlare Backend

**BidFlare** is a modern, scalable online bidding platform designed to empower small businesses to showcase and auction their products. This backend service is built using **Spring Boot**, integrated with **PostgreSQL**, **AWS services**, and robust security standards.

---

## 📦 Tech Stack

- **Backend:** Spring Boot (Java)
- **Database:** PostgreSQL (via AWS RDS)
- **Authentication:** JWT, Spring Security (OAuth2 + Email/Password)
- **Storage:** AWS S3 (media), RDS (data)
- **Notifications:** AWS SES/SNS
- **Deployment:** AWS EC2 / ECS / Elastic Beanstalk
- **Monitoring:** AWS CloudWatch

---

## 🧾 Core Features

### 🔐 User Authentication & Roles

- OAuth2 + Email/Password login
- Roles: `BUYER`, `SELLER`, `ADMIN`
- JWT-based authentication
- Email verification, password reset
- Profile management

### 🛍️ Product Listings (for Sellers)

- Create, edit, and delete listings
- Upload media to AWS S3
- Define categories, tags, and description
- Set minimum bid, auction start/end time

### 🔨 Bidding Mechanism

- Real-time bidding (WebSocket-ready architecture)
- Bid history & countdown timer
- Optional auto-bidding logic (planned)

### ⏱️ Auction Management

- Scheduled auctions: Upcoming, Ongoing, Closed
- Notifications on bid outcome
- Dashboard for auction results

### 🔎 Search & Filters

- Keyword search
- Category, price range, and status filters
- Sorting options (ending soon, highest bid, etc.)

### 🔔 Notifications

- In-app + Email alerts via AWS SES/SNS
- Outbid alerts, auction updates, product changes

### 👮 Admin Panel (API-level)

- Manage users (ban/delete)
- View stats & platform analytics
- Category/tag moderation
- Content reporting & review

### 💳 Payments & Transactions

- Payment gateway integration (Stripe, PayPal)
- Commission handling
- Transaction history
- (Planned) Escrow system for trust-based bidding

### 💬 User Interactions

- Q&A per product
- Messaging system with moderation
- Ratings & reviews

### 📊 Analytics

- **Sellers**: Views, bid history, engagement
- **Buyers**: Spending, bid history
- **Platform**: Auction activity, user metrics

---

## 🗂️ Database Overview

The database is designed for scalability and modularity.

| Table              | Description                              |
|-------------------|------------------------------------------|
| `users`           | Buyers, sellers, admins                  |
| `products`        | Auctioned items                          |
| `product_images`  | Linked images hosted on S3               |
| `categories`      | Product categories                       |
| `auctions`        | Auction metadata                         |
| `bids`            | All bid submissions                      |
| `transactions`    | Payment and order info                   |
| `notifications`   | In-app/email alerts                      |
| `reviews`         | Buyer feedback (optional)                |

> For detailed schema, refer to the ER diagram.

---

## 🔒 Security

Implemented via **Spring Security + JWT**, including:

- HTTPS
- CSRF/XSS/SQLi protections
- Rate limiting
- 2FA (optional, planned)
- Session expiration & secure logout

---

## ☁️ AWS Infrastructure

| Service          | Purpose                      |
|------------------|------------------------------|
| EC2 / ECS        | Backend hosting              |
| RDS              | PostgreSQL DB                |
| S3               | Product media storage        |
| SES / SNS        | Email and SMS notifications  |
| CloudWatch       | Logging and metrics          |
| CloudFront       | CDN for faster asset access  |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- PostgreSQL
- AWS credentials setup

### Run Locally

```bash
git clone https://github.com/your-org/bidflare-backend.git
cd bidflare-backend
./mvnw clean install
./mvnw spring-boot:run
