import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { KeyRound, ShieldAlert } from 'lucide-react';
import { useChangePassword } from '../hooks/useSecurity';

const passwordSchema = z.object({
  currentPassword: z.string().min(1, 'Current password is required'),
  newPassword: z.string().min(8, 'Password must be at least 8 characters'),
  confirmPassword: z.string()
}).refine((data) => data.newPassword === data.confirmPassword, {
  message: "Passwords don't match",
  path: ["confirmPassword"]
});

export const SecurityCard = () => {
  const { mutate: changePassword, isPending } = useChangePassword();
  
  const { register, handleSubmit, formState: { errors }, reset } = useForm({
    resolver: zodResolver(passwordSchema)
  });

  const onSubmit = (data) => {
    changePassword(data, {
      onSuccess: () => reset()
    });
  };

  return (
    <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
      <div className="p-6 border-b border-border bg-muted/10">
        <h3 className="font-semibold text-lg flex items-center gap-2">
          <KeyRound className="w-5 h-5 text-primary" /> Password & Authentication
        </h3>
        <p className="text-sm text-muted-foreground mt-1">Update your password to keep your account secure.</p>
      </div>
      
      <div className="p-6">
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 max-w-md">
          
          <div>
            <label className="block text-sm font-medium mb-1">Current Password</label>
            <input 
              type="password" 
              {...register('currentPassword')} 
              className={`w-full px-3 py-2 bg-background border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary ${errors.currentPassword ? 'border-destructive' : 'border-input'}`}
            />
            {errors.currentPassword && <p className="text-xs text-destructive mt-1">{errors.currentPassword.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">New Password</label>
            <input 
              type="password" 
              {...register('newPassword')} 
              className={`w-full px-3 py-2 bg-background border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary ${errors.newPassword ? 'border-destructive' : 'border-input'}`}
            />
            {errors.newPassword && <p className="text-xs text-destructive mt-1">{errors.newPassword.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Confirm New Password</label>
            <input 
              type="password" 
              {...register('confirmPassword')} 
              className={`w-full px-3 py-2 bg-background border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary ${errors.confirmPassword ? 'border-destructive' : 'border-input'}`}
            />
            {errors.confirmPassword && <p className="text-xs text-destructive mt-1">{errors.confirmPassword.message}</p>}
          </div>

          <div className="pt-4">
            <button 
              type="submit" 
              disabled={isPending}
              className="bg-primary text-primary-foreground px-4 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors disabled:opacity-50"
            >
              {isPending ? 'Updating...' : 'Update Password'}
            </button>
          </div>
        </form>

        {/* 2FA Placeholder */}
        <div className="mt-8 pt-6 border-t border-border">
          <div className="flex items-start gap-4">
            <div className="p-3 bg-yellow-500/10 text-yellow-600 rounded-full shrink-0">
              <ShieldAlert className="w-6 h-6" />
            </div>
            <div>
              <h4 className="font-medium text-foreground mb-1">Two-Factor Authentication (2FA)</h4>
              <p className="text-sm text-muted-foreground mb-3">Add an extra layer of security to your account. We highly recommend enabling 2FA for Fleet Owners and Admins.</p>
              <button className="px-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors">
                Enable 2FA
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
