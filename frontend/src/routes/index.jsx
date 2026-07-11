import React, { lazy } from 'react';
import { createBrowserRouter } from 'react-router-dom';
import { ProtectedRoute } from './ProtectedRoute';

import { AuthLayout } from '@/features/authentication/pages/AuthLayout';
import { Login } from '@/features/authentication/pages/Login';
import { Register } from '@/features/authentication/pages/Register';
import { ForgotPassword } from '@/features/authentication/pages/ForgotPassword';
import { ResetPassword } from '@/features/authentication/pages/ResetPassword';
import { VerifyEmail } from '@/features/authentication/pages/VerifyEmail';
import { VerifyOtp } from '@/features/authentication/pages/VerifyOtp';
import { SessionExpired } from '@/features/authentication/pages/SessionExpired';

import { RoleProtectedRoute } from './RoleProtectedRoute';
import { DashboardLayout } from '@/layouts/DashboardLayout';
import { NotFound } from '@/components/common/NotFound';

// Driver Pages
const DriverDashboardHome = lazy(() => import('@/features/driver/pages/DriverDashboardHome').then(module => ({ default: module.DriverDashboardHome })));
const DriverProfile = lazy(() => import('@/features/driver/pages/DriverProfile').then(module => ({ default: module.DriverProfile })));
const DriverTruck = lazy(() => import('@/features/driver/pages/DriverTruck').then(module => ({ default: module.DriverTruck })));
const AvailableShipments = lazy(() => import('@/features/driver/pages/AvailableShipments').then(module => ({ default: module.AvailableShipments })));
const DriverTrips = lazy(() => import('@/features/driver/pages/DriverTrips').then(module => ({ default: module.DriverTrips })));
const LiveTracking = lazy(() => import('@/features/driver/pages/LiveTracking').then(module => ({ default: module.LiveTracking })));

// Business Owner Pages
const BusinessDashboardHome = lazy(() => import('@/features/business/pages/BusinessDashboardHome').then(module => ({ default: module.BusinessDashboardHome })));
const MyShipments = lazy(() => import('@/features/business/pages/MyShipments').then(module => ({ default: module.MyShipments })));
const CreateShipmentWizard = lazy(() => import('@/features/business/pages/CreateShipmentWizard').then(module => ({ default: module.CreateShipmentWizard })));
const BusinessMatching = lazy(() => import('@/features/business/pages/BusinessMatching').then(module => ({ default: module.BusinessMatching })));
const BusinessTracking = lazy(() => import('@/features/business/pages/BusinessTracking').then(module => ({ default: module.BusinessTracking })));
const BusinessAnalytics = lazy(() => import('@/features/business/pages/BusinessAnalytics').then(module => ({ default: module.BusinessAnalytics })));
const BusinessProfile = lazy(() => import('@/features/business/pages/BusinessProfile').then(module => ({ default: module.BusinessProfile })));

// Fleet Owner Pages
const FleetDashboardHome = lazy(() => import('@/features/fleet/pages/FleetDashboardHome').then(module => ({ default: module.FleetDashboardHome })));
const FleetManagement = lazy(() => import('@/features/fleet/pages/FleetManagement').then(module => ({ default: module.FleetManagement })));
const DriverManagement = lazy(() => import('@/features/fleet/pages/DriverManagement').then(module => ({ default: module.DriverManagement })));
const TripManagement = lazy(() => import('@/features/fleet/pages/TripManagement').then(module => ({ default: module.TripManagement })));
const FleetAnalytics = lazy(() => import('@/features/fleet/pages/FleetAnalytics').then(module => ({ default: module.FleetAnalytics })));
const MaintenanceSchedule = lazy(() => import('@/features/fleet/pages/MaintenanceSchedule').then(module => ({ default: module.MaintenanceSchedule })));
const FleetProfile = lazy(() => import('@/features/fleet/pages/FleetProfile').then(module => ({ default: module.FleetProfile })));

// Admin Pages
const AdminDashboardHome = lazy(() => import('@/features/admin/pages/AdminDashboardHome').then(module => ({ default: module.AdminDashboardHome })));
const UserManagement = lazy(() => import('@/features/admin/pages/UserManagement').then(module => ({ default: module.UserManagement })));
const AdminTrucks = lazy(() => import('@/features/admin/pages/AdminTrucks').then(module => ({ default: module.AdminTrucks })));
const AdminShipments = lazy(() => import('@/features/admin/pages/AdminShipments').then(module => ({ default: module.AdminShipments })));
const ReviewModeration = lazy(() => import('@/features/admin/pages/ReviewModeration').then(module => ({ default: module.ReviewModeration })));
const PlatformAnalytics = lazy(() => import('@/features/admin/pages/PlatformAnalytics').then(module => ({ default: module.PlatformAnalytics })));
const SystemSettings = lazy(() => import('@/features/admin/pages/SystemSettings').then(module => ({ default: module.SystemSettings })));
const SupportTickets = lazy(() => import('@/features/admin/pages/SupportTickets').then(module => ({ default: module.SupportTickets })));

// Truck Management Pages
const TruckDirectory = lazy(() => import('@/features/trucks/pages/TruckDirectory').then(module => ({ default: module.TruckDirectory })));
const TruckDetails = lazy(() => import('@/features/trucks/pages/TruckDetails').then(module => ({ default: module.TruckDetails })));
const RegisterTruckWizard = lazy(() => import('@/features/trucks/pages/RegisterTruckWizard').then(module => ({ default: module.RegisterTruckWizard })));

// Shipment Management Pages
const ShipmentDirectory = lazy(() => import('@/features/shipments/pages/ShipmentDirectory').then(module => ({ default: module.ShipmentDirectory })));
const ShipmentDetails = lazy(() => import('@/features/shipments/pages/ShipmentDetails').then(module => ({ default: module.ShipmentDetails })));
const NewShipmentWizard = lazy(() => import('@/features/shipments/pages/CreateShipmentWizard').then(module => ({ default: module.CreateShipmentWizard })));

// Matching & Bidding Pages
const MatchDashboard = lazy(() => import('@/features/matching/pages/MatchDashboard').then(module => ({ default: module.MatchDashboard })));
const RecommendationDirectory = lazy(() => import('@/features/matching/pages/RecommendationDirectory').then(module => ({ default: module.RecommendationDirectory })));
const BidComparison = lazy(() => import('@/features/matching/pages/BidComparison').then(module => ({ default: module.BidComparison })));

// Live Tracking Pages
const TrackingDashboard = lazy(() => import('@/features/tracking/pages/TrackingDashboard').then(module => ({ default: module.TrackingDashboard })));
const LiveTripDetails = lazy(() => import('@/features/tracking/pages/LiveTripDetails').then(module => ({ default: module.LiveTripDetails })));

// Notification Pages
const NotificationCenter = lazy(() => import('@/features/notifications/pages/NotificationCenter').then(module => ({ default: module.NotificationCenter })));
const NotificationPreferences = lazy(() => import('@/features/notifications/pages/NotificationPreferences').then(module => ({ default: module.NotificationPreferences })));

// Profile & Settings Pages
const UserProfile = lazy(() => import('@/features/profile/pages/UserProfile').then(module => ({ default: module.UserProfile })));
const SettingsLayout = lazy(() => import('@/features/profile/layouts/SettingsLayout').then(module => ({ default: module.SettingsLayout })));
const AccountSettings = lazy(() => import('@/features/profile/pages/AccountSettings').then(module => ({ default: module.AccountSettings })));
const SecuritySettings = lazy(() => import('@/features/profile/pages/SecuritySettings').then(module => ({ default: module.SecuritySettings })));
const GlobalPreferences = lazy(() => import('@/features/profile/pages/GlobalPreferences').then(module => ({ default: module.GlobalPreferences })));

// Analytics & Reports Pages
const AnalyticsDashboard = lazy(() => import('@/features/analytics/pages/AnalyticsDashboard').then(module => ({ default: module.AnalyticsDashboard })));
const ReportsDirectory = lazy(() => import('@/features/analytics/pages/ReportsDirectory').then(module => ({ default: module.ReportsDirectory })));
const DetailedReport = lazy(() => import('@/features/analytics/pages/DetailedReport').then(module => ({ default: module.DetailedReport })));

export const router = createBrowserRouter([
  {
    element: <AuthLayout />,
    children: [
      { path: '/login', element: <Login /> },
      { path: '/register', element: <Register /> },
      { path: '/forgot-password', element: <ForgotPassword /> },
      { path: '/reset-password', element: <ResetPassword /> },
      { path: '/verify-email', element: <VerifyEmail /> },
      { path: '/verify-otp', element: <VerifyOtp /> },
      { path: '/session-expired', element: <SessionExpired /> },
    ],
  },
  {
    path: '/',
    element: <ProtectedRoute />,
    children: [
      {
        element: <DashboardLayout />,
        children: [
          // Driver Routes
          {
            element: <RoleProtectedRoute allowedRoles={['Driver']} />,
            children: [
              { index: true, element: <DriverDashboardHome /> },
              { path: 'profile', element: <DriverProfile /> },
              { path: 'truck/me', element: <DriverTruck /> },
              { path: 'shipments/available', element: <AvailableShipments /> },
              { path: 'trips', element: <DriverTrips /> },
              { path: 'tracking', element: <LiveTracking /> },
            ]
          },
          // Business Owner Routes
          {
            element: <RoleProtectedRoute allowedRoles={['Business Owner']} />,
            children: [
              { index: true, element: <BusinessDashboardHome /> },
              // Old business shipment routes replaced by shared /shipments module
              // { path: 'shipments', element: <MyShipments /> },
              // { path: 'shipments/new', element: <CreateShipmentWizard /> },
              { path: 'matching', element: <BusinessMatching /> },
              { path: 'tracking', element: <BusinessTracking /> },
              { path: 'analytics', element: <AnalyticsDashboard /> },
              { path: 'profile', element: <BusinessProfile /> },
            ]
          },
          // Fleet Owner Routes
          {
            element: <RoleProtectedRoute allowedRoles={['Fleet Owner']} />,
            children: [
              { index: true, element: <FleetDashboardHome /> },
              { path: 'fleet', element: <FleetManagement /> },
              { path: 'drivers', element: <DriverManagement /> },
              { path: 'trips', element: <TripManagement /> },
              { path: 'analytics', element: <AnalyticsDashboard /> },
              { path: 'maintenance', element: <MaintenanceSchedule /> },
              { path: 'profile', element: <FleetProfile /> },
            ]
          },
          // Admin Routes
          {
            element: <RoleProtectedRoute allowedRoles={['Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <AdminDashboardHome /> },
              { path: 'admin/users', element: <UserManagement /> },
              { path: 'admin/trucks', element: <AdminTrucks /> },
              { path: 'admin/shipments', element: <AdminShipments /> },
              { path: 'admin/reviews', element: <ReviewModeration /> },
              { path: 'admin/analytics', element: <AnalyticsDashboard /> },
              { path: 'admin/settings', element: <SystemSettings /> },
              { path: 'admin/support', element: <SupportTickets /> },
            ]
          },
          // Shared Truck Routes
          {
            path: 'trucks',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <TruckDirectory /> },
              { path: 'new', element: <RegisterTruckWizard /> },
              { path: ':id', element: <TruckDetails /> },
            ]
          },
          // Shared Shipment Routes
          {
            path: 'shipments',
            element: <RoleProtectedRoute allowedRoles={['Business Owner', 'Admin', 'Super Admin', 'Driver', 'Fleet Owner']} />,
            children: [
              { index: true, element: <ShipmentDirectory /> },
              { path: 'new', element: <NewShipmentWizard /> },
              { path: ':id', element: <ShipmentDetails /> },
            ]
          },
          // Shared Matching & Bidding Routes
          {
            path: 'matching',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Business Owner', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <MatchDashboard /> },
              { path: 'recommendations', element: <RecommendationDirectory /> },
              { path: 'bids/:shipmentId', element: <BidComparison /> },
            ]
          },
          // Shared Live Tracking Routes
          {
            path: 'tracking',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Business Owner', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <TrackingDashboard /> },
              { path: ':tripId', element: <LiveTripDetails /> },
            ]
          },
          // Shared Notification Routes
          {
            path: 'notifications',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Business Owner', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <NotificationCenter /> }
            ]
          },
          // Shared Reports Routes
          {
            path: 'reports',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Business Owner', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <ReportsDirectory /> },
              { path: ':reportType', element: <DetailedReport /> }
            ]
          },
          // Shared Profile & Settings Routes
          {
            path: 'profile',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Business Owner', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              { index: true, element: <UserProfile /> }
            ]
          },
          {
            path: 'settings',
            element: <RoleProtectedRoute allowedRoles={['Driver', 'Business Owner', 'Fleet Owner', 'Admin', 'Super Admin']} />,
            children: [
              {
                element: <SettingsLayout />,
                children: [
                  { path: 'account', element: <AccountSettings /> },
                  { path: 'security', element: <SecuritySettings /> },
                  { path: 'notifications', element: <NotificationPreferences /> },
                  { path: 'preferences', element: <GlobalPreferences /> },
                ]
              }
            ]
          },
          {
            path: '*',
            element: <div>Module not implemented yet.</div>,
          }
        ]
      },
    ],
  },
  {
    path: '/unauthorized',
    element: <div>403 Unauthorized</div>,
  },
  {
    path: '*',
    element: <NotFound />,
  },
]);
