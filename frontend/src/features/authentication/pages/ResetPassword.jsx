import React from 'react';
import { useZodForm } from '@/lib/form';
import { resetPasswordSchema } from '../schemas/authSchemas';
import { PasswordStrengthMeter } from '../components/PasswordStrengthMeter';
import { Link } from 'react-router-dom';

export const ResetPassword = () => {
  const { register, handleSubmit, watch, formState: { errors } } = useZodForm(resetPasswordSchema, { token: '', newPassword: '', confirmPassword: '' });
  const password = watch('newPassword');

  return (
    <div className="flex flex-col space-y-6">
      <div className="text-center">
        <h2 className="text-xl font-semibold tracking-tight">Reset Password</h2>
        <p className="text-sm text-muted-foreground mt-1">Enter your new password below</p>
      </div>
      <form onSubmit={handleSubmit((d) => console.log(d))} className="space-y-4">
        {/* Simplified for brevity */}
        <p className="text-sm">Password inputs go here...</p>
        <PasswordStrengthMeter password={password} />
        <button type="submit" className="w-full bg-primary text-primary-foreground h-10 rounded-md">Reset Password</button>
      </form>
      <div className="text-center text-sm">
        <Link to="/login" className="text-primary hover:underline">Back to Login</Link>
      </div>
    </div>
  );
};
