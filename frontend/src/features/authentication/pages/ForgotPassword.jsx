import React from 'react';
import { Link } from 'react-router-dom';
import { useZodForm } from '@/lib/form';
import { forgotPasswordSchema } from '../schemas/authSchemas';
import { authService } from '../services/authService';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';
import { Loader2 } from 'lucide-react';

export const ForgotPassword = () => {
  const { register, handleSubmit, formState: { errors } } = useZodForm(forgotPasswordSchema, { email: '' });
  
  const { mutate, isPending, isSuccess } = useMutation({
    mutationFn: (data) => authService.forgotPassword(data),
    onSuccess: () => toast.success('Password reset link sent to your email'),
    onError: () => toast.error('Failed to send reset link'),
  });

  if (isSuccess) {
    return (
      <div className="text-center space-y-4">
        <h2 className="text-xl font-semibold tracking-tight">Check your email</h2>
        <p className="text-sm text-muted-foreground">We have sent a password reset link to your email address.</p>
        <Link to="/login" className="text-sm text-primary hover:underline font-medium block mt-4">Return to Login</Link>
      </div>
    );
  }

  return (
    <div className="flex flex-col space-y-6">
      <div className="text-center">
        <h2 className="text-xl font-semibold tracking-tight">Forgot Password</h2>
        <p className="text-sm text-muted-foreground mt-1">Enter your email to receive a reset link</p>
      </div>
      <form onSubmit={handleSubmit((d) => mutate(d))} className="space-y-4">
        <div className="space-y-2">
          <label className="text-sm font-medium">Email</label>
          <input type="email" {...register('email')} className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring" />
          {errors.email && <p className="text-xs text-destructive">{errors.email.message}</p>}
        </div>
        <button type="submit" disabled={isPending} className="inline-flex w-full items-center justify-center rounded-md text-sm font-medium bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2">
          {isPending && <Loader2 className="w-4 h-4 mr-2 animate-spin" />} Send Reset Link
        </button>
      </form>
      <div className="text-center text-sm">
        <Link to="/login" className="text-primary hover:underline font-medium">Back to Login</Link>
      </div>
    </div>
  );
};
