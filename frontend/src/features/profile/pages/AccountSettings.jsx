import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useProfile, useUpdateProfile } from '../hooks/useProfile';
import { AvatarUploader } from '../components/AvatarUploader';
import { User, Mail, Phone, Save } from 'lucide-react';

const profileSchema = z.object({
  fullName: z.string().min(2, 'Name must be at least 2 characters'),
  email: z.string().email('Invalid email address'),
  phone: z.string().min(10, 'Phone must be at least 10 digits'),
  bio: z.string().max(500, 'Bio must be less than 500 characters').optional(),
});

export const AccountSettings = () => {
  const { data: profile, isLoading } = useProfile();
  const { mutate: updateProfile, isPending } = useUpdateProfile();

  const { register, handleSubmit, reset, formState: { errors, isDirty } } = useForm({
    resolver: zodResolver(profileSchema),
  });

  useEffect(() => {
    if (profile) {
      reset({
        fullName: profile.fullName || '',
        email: profile.email || '',
        phone: profile.phone || '',
        bio: profile.bio || ''
      });
    }
  }, [profile, reset]);

  const onSubmit = (data) => {
    updateProfile(data);
  };

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading account details...</div>;

  return (
    <div className="space-y-8 max-w-3xl">
      
      {/* Avatar Section */}
      <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
        <div className="p-6 border-b border-border bg-muted/10">
          <h3 className="font-semibold text-lg">Profile Photo</h3>
          <p className="text-sm text-muted-foreground mt-1">This will be displayed on your public profile and dashboards.</p>
        </div>
        <div className="p-6">
          <AvatarUploader currentAvatarUrl={profile?.avatarUrl} />
        </div>
      </div>

      {/* Personal Info Section */}
      <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
        <div className="p-6 border-b border-border bg-muted/10">
          <h3 className="font-semibold text-lg">Personal Information</h3>
          <p className="text-sm text-muted-foreground mt-1">Update your personal details and contact information.</p>
        </div>
        <div className="p-6">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium mb-1 flex items-center gap-1"><User className="w-4 h-4 text-muted-foreground" /> Full Name</label>
                <input 
                  type="text" 
                  {...register('fullName')} 
                  className={`w-full px-3 py-2 bg-background border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary ${errors.fullName ? 'border-destructive' : 'border-input'}`}
                />
                {errors.fullName && <p className="text-xs text-destructive mt-1">{errors.fullName.message}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium mb-1 flex items-center gap-1"><Mail className="w-4 h-4 text-muted-foreground" /> Email Address</label>
                <input 
                  type="email" 
                  {...register('email')} 
                  disabled // usually emails cannot be changed directly without verification
                  className={`w-full px-3 py-2 bg-muted/50 border rounded-lg text-sm focus:outline-none ${errors.email ? 'border-destructive' : 'border-input'}`}
                />
                <p className="text-xs text-muted-foreground mt-1">Contact support to change your email.</p>
              </div>

              <div>
                <label className="block text-sm font-medium mb-1 flex items-center gap-1"><Phone className="w-4 h-4 text-muted-foreground" /> Phone Number</label>
                <input 
                  type="tel" 
                  {...register('phone')} 
                  className={`w-full px-3 py-2 bg-background border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary ${errors.phone ? 'border-destructive' : 'border-input'}`}
                />
                {errors.phone && <p className="text-xs text-destructive mt-1">{errors.phone.message}</p>}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Bio</label>
              <textarea 
                {...register('bio')} 
                rows={4}
                className={`w-full px-3 py-2 bg-background border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary resize-none ${errors.bio ? 'border-destructive' : 'border-input'}`}
                placeholder="Write a few sentences about yourself or your business..."
              />
              {errors.bio && <p className="text-xs text-destructive mt-1">{errors.bio.message}</p>}
            </div>

            <div className="pt-4 flex justify-end">
              <button 
                type="submit" 
                disabled={!isDirty || isPending}
                className="flex items-center gap-2 bg-primary text-primary-foreground px-6 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors disabled:opacity-50"
              >
                <Save className="w-4 h-4" /> Save Changes
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
