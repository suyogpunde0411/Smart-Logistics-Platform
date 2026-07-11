import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useZodForm } from '@/lib/form';
import { registerSchema } from '../schemas/authSchemas';
import { useRegister } from '../hooks/useRegister';
import { Eye, EyeOff, Loader2 } from 'lucide-react';
import { PasswordStrengthMeter } from '../components/PasswordStrengthMeter';
import { RoleSelector } from '../components/RoleSelector';

export const Register = () => {
  const { register, handleSubmit, watch, setValue, formState: { errors } } = useZodForm(registerSchema, { 
    email: '', 
    password: '', 
    confirmPassword: '', 
    role: '', 
    termsAccepted: false 
  });
  
  const { mutate: registerUser, isPending } = useRegister();
  const [showPassword, setShowPassword] = useState(false);
  
  const password = watch('password');
  const role = watch('role');

  const onSubmit = (data) => {
    registerUser(data);
  };

  return (
    <div className="flex flex-col space-y-6">
      <div className="text-center">
        <h2 className="text-xl font-semibold tracking-tight">Create an account</h2>
        <p className="text-sm text-muted-foreground mt-1">Join the Smart Logistics Platform</p>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        
        <RoleSelector 
          value={role} 
          onChange={(val) => setValue('role', val, { shouldValidate: true })} 
          error={errors.role?.message} 
        />

        <div className="space-y-2">
          <label className="text-sm font-medium">Email</label>
          <input
            type="email"
            {...register('email')}
            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
            placeholder="name@example.com"
          />
          {errors.email && <p className="text-xs text-destructive">{errors.email.message}</p>}
        </div>

        <div className="space-y-2">
          <label className="text-sm font-medium">Password</label>
          <div className="relative">
            <input
              type={showPassword ? 'text' : 'password'}
              {...register('password')}
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 pr-10"
              placeholder="••••••••"
            />
            <button
              type="button"
              className="absolute right-3 top-2.5 text-muted-foreground hover:text-foreground"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>
          <PasswordStrengthMeter password={password} />
          {errors.password && <p className="text-xs text-destructive">{errors.password.message}</p>}
        </div>

        <div className="space-y-2">
          <label className="text-sm font-medium">Confirm Password</label>
          <input
            type={showPassword ? 'text' : 'password'}
            {...register('confirmPassword')}
            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
            placeholder="••••••••"
          />
          {errors.confirmPassword && <p className="text-xs text-destructive">{errors.confirmPassword.message}</p>}
        </div>

        <div className="flex items-start space-x-2 pt-2">
          <input
            type="checkbox"
            id="termsAccepted"
            {...register('termsAccepted')}
            className="mt-1 h-4 w-4 rounded border-gray-300 text-primary focus:ring-primary"
          />
          <label htmlFor="termsAccepted" className="text-sm font-medium leading-tight peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
            I accept the Terms of Service and Privacy Policy
          </label>
        </div>
        {errors.termsAccepted && <p className="text-xs text-destructive">{errors.termsAccepted.message}</p>}

        <button
          type="submit"
          disabled={isPending}
          className="inline-flex w-full items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 mt-4"
        >
          {isPending ? <Loader2 className="w-4 h-4 mr-2 animate-spin" /> : null}
          {isPending ? 'Registering...' : 'Create Account'}
        </button>
      </form>

      <div className="text-center text-sm">
        <span className="text-muted-foreground">Already have an account? </span>
        <Link to="/login" className="text-primary hover:underline font-medium">
          Sign In
        </Link>
      </div>
    </div>
  );
};
