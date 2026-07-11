# Dashboard Framework

This module provides the core, reusable architecture for all role-based dashboards in the Smart Logistics Optimization Platform.

## Overview
The Dashboard Framework operates as the foundational shell, abstracting away navigation logic, UI state persistence, theme management, and responsive layouts. 

## Features
- **Dashboard Layout**: Integrates a dynamic Sidebar and Topbar.
- **Role-Based Routing**: Centralized via `navigationConfig.js`, which securely manages route access for `Driver`, `Business Owner`, `Fleet Owner`, and `Admin`.
- **Reusable UI Components**:
  - `StatsCard`, `SummaryCard`, `InfoCard`
  - `Timeline`, `QuickActionCard`
  - `NotificationWidget`, `EmptyState`, `LoadingSkeleton`
- **Custom Hooks**:
  - `useSidebar()`: Manages sidebar UI state.
  - `usePermissions()`: Validates user roles.
  - `useBreadcrumb()`: Dynamically parses routes for breadcrumbs.

## Usage
When building a new business feature (e.g., Truck Management), you **do not** need to rebuild the layout. You simply register the new route in `src/routes/index.jsx` and add it to `navigationConfig.js` for the relevant roles. The Dashboard Shell will automatically handle rendering.
