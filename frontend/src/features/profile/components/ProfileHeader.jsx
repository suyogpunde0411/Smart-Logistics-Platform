import React from 'react';
import { Mail, Phone, MapPin, Calendar, ShieldCheck, AlertCircle } from 'lucide-react';

export const ProfileHeader = ({ profile }) => {
  if (!profile) return null;

  return (
    <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden mb-6">
      {/* Cover Photo */}
      <div className="h-32 bg-gradient-to-r from-primary/80 to-primary w-full"></div>
      
      <div className="px-6 sm:px-8 pb-8 relative">
        <div className="flex flex-col sm:flex-row gap-6 items-start sm:items-end -mt-12 sm:-mt-16 mb-6">
          
          {/* Avatar */}
          <div className="w-24 h-24 sm:w-32 sm:h-32 rounded-full border-4 border-background bg-muted shrink-0 overflow-hidden shadow-lg relative">
            {profile.avatarUrl ? (
              <img src={profile.avatarUrl} alt={profile.fullName} className="w-full h-full object-cover" />
            ) : (
              <div className="w-full h-full flex items-center justify-center bg-primary text-primary-foreground font-bold text-3xl sm:text-4xl">
                {profile.fullName?.charAt(0) || '?'}
              </div>
            )}
          </div>

          {/* Core Info */}
          <div className="flex-1 pb-2">
            <div className="flex items-center gap-3 mb-1">
              <h1 className="text-2xl font-bold tracking-tight">{profile.fullName}</h1>
              {profile.isVerified ? (
                <ShieldCheck className="w-5 h-5 text-green-500" title="Verified Account" />
              ) : (
                <AlertCircle className="w-5 h-5 text-yellow-500" title="Pending Verification" />
              )}
            </div>
            <div className="flex flex-wrap items-center gap-2 text-sm text-muted-foreground">
              <span className="px-2.5 py-0.5 rounded-full bg-primary/10 text-primary font-medium border border-primary/20">
                {profile.role?.replace('_', ' ')}
              </span>
              <span>•</span>
              <span className="flex items-center gap-1"><Calendar className="w-3.5 h-3.5" /> Joined {profile.joinedDate}</span>
            </div>
          </div>
        </div>

        {/* Contact Info Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 p-4 bg-muted/30 rounded-lg border border-border">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-background rounded-md shadow-sm border border-border shrink-0"><Mail className="w-4 h-4 text-muted-foreground" /></div>
            <div className="truncate">
              <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Email</p>
              <p className="text-sm font-medium truncate">{profile.email}</p>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <div className="p-2 bg-background rounded-md shadow-sm border border-border shrink-0"><Phone className="w-4 h-4 text-muted-foreground" /></div>
            <div>
              <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Phone</p>
              <p className="text-sm font-medium">{profile.phone || 'Not provided'}</p>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <div className="p-2 bg-background rounded-md shadow-sm border border-border shrink-0"><MapPin className="w-4 h-4 text-muted-foreground" /></div>
            <div>
              <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Location</p>
              <p className="text-sm font-medium">{profile.address?.city || 'Not provided'}</p>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};
