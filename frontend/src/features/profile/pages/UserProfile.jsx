import React from 'react';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ProfileHeader } from '../components/ProfileHeader';
import { useProfile } from '../hooks/useProfile';
import { Edit, Truck, Map, Shield } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const UserProfile = () => {
  const navigate = useNavigate();
  const { data: profile, isLoading } = useProfile();

  // Mock data fallback
  const mockProfile = profile || {
    id: 'USR-101',
    fullName: 'Ramesh Singh',
    email: 'ramesh.singh@example.com',
    phone: '+91 98765 43210',
    role: 'DRIVER',
    isVerified: true,
    joinedDate: 'Jan 15, 2025',
    avatarUrl: null,
    address: { city: 'Mumbai', state: 'Maharashtra', zip: '400001' },
    bio: 'Experienced long-haul driver with a clean safety record spanning 12 years.',
    
    // Role specific mock data
    licenseNumber: 'MH-12-2015-0012345',
    experienceYears: 12,
    rating: 4.8,
    completedTrips: 142
  };

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading profile...</div>;

  return (
    <PageContainer>
      <div className="flex justify-end mb-4">
        <button 
          onClick={() => navigate('/settings/account')}
          className="flex items-center gap-2 px-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors bg-card shadow-sm"
        >
          <Edit className="w-4 h-4" /> Edit Profile
        </button>
      </div>

      <ProfileHeader profile={mockProfile} />

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        
        {/* Left Column: About & Bio */}
        <div className="md:col-span-1 space-y-6">
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <h3 className="font-semibold text-lg mb-4">About</h3>
            <p className="text-sm text-muted-foreground leading-relaxed">
              {mockProfile.bio || 'No bio provided.'}
            </p>
          </div>
        </div>

        {/* Right Column: Role Specific Details */}
        <div className="md:col-span-2 space-y-6">
          <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
            <div className="p-6 border-b border-border bg-muted/10">
              <h3 className="font-semibold text-lg flex items-center gap-2">
                {mockProfile.role === 'DRIVER' && <Truck className="w-5 h-5 text-primary" />}
                {mockProfile.role === 'BUSINESS_OWNER' && <Map className="w-5 h-5 text-primary" />}
                {mockProfile.role === 'ADMIN' && <Shield className="w-5 h-5 text-primary" />}
                Role Specific Information
              </h3>
            </div>
            
            <div className="p-6">
              {mockProfile.role === 'DRIVER' && (
                <div className="grid grid-cols-2 gap-y-6 gap-x-4">
                  <div>
                    <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">License Number</p>
                    <p className="font-medium mt-1">{mockProfile.licenseNumber}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Experience</p>
                    <p className="font-medium mt-1">{mockProfile.experienceYears} Years</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Current Rating</p>
                    <p className="font-medium mt-1 text-primary text-xl">{mockProfile.rating} ⭐</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Completed Trips</p>
                    <p className="font-medium mt-1 text-xl">{mockProfile.completedTrips}</p>
                  </div>
                </div>
              )}
              {/* Other roles can have their specific blocks rendered here similarly */}
            </div>
          </div>
        </div>

      </div>
    </PageContainer>
  );
};
