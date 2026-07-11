import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useZodForm } from '@/lib/form';
import { loginSchema } from '../schemas/authSchemas';
import { useLogin } from '../hooks/useLogin';
import { Eye, EyeOff, Loader2 } from 'lucide-react';

export const Login = () => {
  const { register, handleSubmit, formState: { errors } } = useZodForm(loginSchema, { email: '', password: '', rememberMe: false });
  const { mutate: login, isPending } = useLogin();
  const [showPassword, setShowPassword] = useState(false);

  const onSubmit = (data) => {
    login(data);
  };

  return (
    <div className="flex flex-col space-y-6">
      <div className="text-center">
        <h2 className="text-xl font-semibold tracking-tight">Welcome back</h2>
        <p className="text-sm text-muted-foreground mt-1">Enter your credentials to login</p>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="space-y-2">
          <label className="text-sm font-medium">Email</label>
          <input
            type="email"
            {...register('email')}
            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            placeholder="name@example.com"
          />
          {errors.email && <p className="text-xs text-destructive">{errors.email.message}</p>}
        </div>

        <div className="space-y-2">
          <div className="flex justify-between items-center">
            <label className="text-sm font-medium">Password</label>
            <Link to="/forgot-password" className="text-xs text-primary hover:underline font-medium">
              Forgot password?
            </Link>
          </div>
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
          {errors.password && <p className="text-xs text-destructive">{errors.password.message}</p>}
        </div>

        <div className="flex items-center space-x-2">
          <input
            type="checkbox"
            id="rememberMe"
            {...register('rememberMe')}
            className="h-4 w-4 rounded border-gray-300 text-primary focus:ring-primary"
          />
          <label htmlFor="rememberMe" className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
            Remember me
          </label>
        </div>

        <button
          type="submit"
          disabled={isPending}
          className="inline-flex w-full items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2"
        >
          {isPending ? <Loader2 className="w-4 h-4 mr-2 animate-spin" /> : null}
          {isPending ? 'Signing in...' : 'Sign In'}
        </button>
      </form>

      <div className="text-center text-sm">
        <span className="text-muted-foreground">Don't have an account? </span>
        <Link to="/register" className="text-primary hover:underline font-medium">
          Register now
        </Link>
      </div>
    </div>
  );
};
