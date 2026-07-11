import React from 'react';
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
import { DriverDashboardHome } from '@/features/driver/pages/DriverDashboardHome';
import { DriverProfile } from '@/features/driver/pages/DriverProfile';
import { DriverTruck } from '@/features/driver/pages/DriverTruck';
import { AvailableShipments } from '@/features/driver/pages/AvailableShipments';
import { DriverTrips } from '@/features/driver/pages/DriverTrips';
import { LiveTracking } from '@/features/driver/pages/LiveTracking';

// Business Owner Pages
import { BusinessDashboardHome } from '@/features/business/pages/BusinessDashboardHome';
import { MyShipments } from '@/features/business/pages/MyShipments';
import { CreateShipmentWizard } from '@/features/business/pages/CreateShipmentWizard';
import { BusinessMatching } from '@/features/business/pages/BusinessMatching';
import { BusinessTracking } from '@/features/business/pages/BusinessTracking';
import { BusinessAnalytics } from '@/features/business/pages/BusinessAnalytics';
import { BusinessProfile } from '@/features/business/pages/BusinessProfile';

// Fleet Owner Pages
import { FleetDashboardHome } from '@/features/fleet/pages/FleetDashboardHome';
import { FleetManagement } from '@/features/fleet/pages/FleetManagement';
import { DriverManagement } from '@/features/fleet/pages/DriverManagement';
import { TripManagement } from '@/features/fleet/pages/TripManagement';
import { FleetAnalytics } from '@/features/fleet/pages/FleetAnalytics';
import { MaintenanceSchedule } from '@/features/fleet/pages/MaintenanceSchedule';
import { FleetProfile } from '@/features/fleet/pages/FleetProfile';

// Admin Pages
import { AdminDashboardHome } from '@/features/admin/pages/AdminDashboardHome';
import { UserManagement } from '@/features/admin/pages/UserManagement';
import { AdminTrucks } from '@/features/admin/pages/AdminTrucks';
import { AdminShipments } from '@/features/admin/pages/AdminShipments';
import { ReviewModeration } from '@/features/admin/pages/ReviewModeration';
import { PlatformAnalytics } from '@/features/admin/pages/PlatformAnalytics';
import { SystemSettings } from '@/features/admin/pages/SystemSettings';
import { SupportTickets } from '@/features/admin/pages/SupportTickets';

// Truck Management Pages
import { TruckDirectory } from '@/features/trucks/pages/TruckDirectory';
import { TruckDetails } from '@/features/trucks/pages/TruckDetails';
import { RegisterTruckWizard } from '@/features/trucks/pages/RegisterTruckWizard';

// Shipment Management Pages
import { ShipmentDirectory } from '@/features/shipments/pages/ShipmentDirectory';
import { ShipmentDetails } from '@/features/shipments/pages/ShipmentDetails';
import { CreateShipmentWizard as NewShipmentWizard } from '@/features/shipments/pages/CreateShipmentWizard';

// Matching & Bidding Pages
import { MatchDashboard } from '@/features/matching/pages/MatchDashboard';
import { RecommendationDirectory } from '@/features/matching/pages/RecommendationDirectory';
import { BidComparison } from '@/features/matching/pages/BidComparison';

// Live Tracking Pages
import { TrackingDashboard } from '@/features/tracking/pages/TrackingDashboard';
import { LiveTripDetails } from '@/features/tracking/pages/LiveTripDetails';

// Notification Pages
import { NotificationCenter } from '@/features/notifications/pages/NotificationCenter';
import { NotificationPreferences } from '@/features/notifications/pages/NotificationPreferences';

// Profile & Settings Pages
import { UserProfile } from '@/features/profile/pages/UserProfile';
import { SettingsLayout } from '@/features/profile/layouts/SettingsLayout';
import { AccountSettings } from '@/features/profile/pages/AccountSettings';
import { SecuritySettings } from '@/features/profile/pages/SecuritySettings';
import { GlobalPreferences } from '@/features/profile/pages/GlobalPreferences';

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
              { path: 'analytics', element: <BusinessAnalytics /> },
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
              { path: 'analytics', element: <FleetAnalytics /> },
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
              { path: 'admin/analytics', element: <PlatformAnalytics /> },
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
