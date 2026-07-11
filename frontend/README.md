# Smart Logistics Optimization Platform - Frontend

This is the production-grade, optimized enterprise React.js frontend for the **Smart Logistics Optimization Platform**. It connects Truck Drivers, Fleet Owners, Business Owners, and Admins under a unified dashboard structure.

## Tech Stack
* **Core:** React.js, JavaScript (ES2023), Vite (dev server & bundle building)
* **State Management:** Redux Toolkit (UI state), TanStack Query v5 (Server-side API caching & polling)
* **Routing:** React Router DOM v7 (Role-based route guarding & lazy loading)
* **Validation & Forms:** React Hook Form & Zod Schemas
* **Styling:** Tailwind CSS & shadcn/ui styles
* **Charts:** Recharts
* **Maps:** React Leaflet
* **Exports:** `jspdf` & `xlsx`

---

## Folder Structure

```
frontend/
├── public/                 # Static assets
├── src/
│   ├── api/                # Core HTTP clients
│   │   └── axios.js        # Global Axios client with token refresh & interceptors
│   ├── components/         # Global shared components (NotFound, loaders)
│   ├── config/             # Application configs (navigation menus, constants)
│   ├── features/           # Feature-based modular structure
│   │   ├── admin/          # Admin dashboard & platform moderation
│   │   ├── analytics/      # Role-based KPI widgets, charts, and downloadable reports
│   │   ├── authentication/ # Login, Register, Forget Password, and Session Expiry
│   │   ├── business/       # Business Owner dashboard & shipping volume summaries
│   │   ├── dashboard/      # Main layout wrappers and sidebar services
│   │   ├── driver/         # Driver profile, trips, and available matching boards
│   │   ├── fleet/          # Fleet management, managed drivers, and maintenance logs
│   │   ├── matching/       # Shared matching and bidding details
│   │   ├── notifications/  # Notification center, drawers, and preferences
│   │   ├── profile/        # Personal settings, active logins table, and avatar uploads
│   │   ├── shipments/      # Shared shipment directory and creation wizards
│   │   └── tracking/       # Live trip ETA trackers, waypoints, and Leaflet Maps
│   ├── layouts/            # Global layouts (Topbar, Sidebar, DashboardLayout)
│   ├── redux/              # Central Redux stores and slices (Auth, Theme)
│   ├── routes/             # Guarded router trees (RoleProtectedRoute)
│   ├── test/               # Setup files & API mocks for Vitest
│   ├── App.jsx             # Root React wrapper
│   └── main.jsx            # Entry mount point
├── package.json            # Dependencies and npm script registers
├── vite.config.js          # Vite configurations
└── vitest.config.js        # Test configurations
```

---

## Environment Variables
Create a `.env` file in the root of the `frontend/` directory:

```env
# Base URL for API requests. Defaults to '/api' if left blank.
VITE_API_URL=http://localhost:8080
```

---

## Frontend Setup Guide

### 1. Installation
Install project dependencies:
```bash
npm install
```

### 2. Development Run
Start the local Vite development server:
```bash
npm run dev
```

### 3. Run Test Suite
Execute the Vitest suite synchronously:
```bash
npm run test
```

### 4. Build for Production
Compile the optimized production bundles:
```bash
npm run build
```

---

## API Integration Guide

All service layers connect to the backend microservices using the unified `apiClient` defined in `src/api/axios.js`.

### 1. Token Refresh Interceptor
The Axios configuration automatically attaches the Bearer JWT token to all outgoing requests. If a request returns a `401 Unauthorized` error, the client attempts to refresh the access token via `/v1/auth/refresh`.

### 2. Role Guards & Authorization
React Router routes are guarded using `<RoleProtectedRoute allowedRoles={['...']} />` to block access to unauthorized endpoints and redirect to `/unauthorized`.
