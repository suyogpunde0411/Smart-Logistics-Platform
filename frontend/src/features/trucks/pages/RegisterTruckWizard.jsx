import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useCreateTruck } from '../hooks/useCreateTruck';
import { Truck, FileText, Settings, UserPlus, ShieldCheck, ChevronRight, ChevronLeft } from 'lucide-react';

// Zod Schemas for each step
const schemas = [
  z.object({
    registrationNumber: z.string().min(4, "Registration is required"),
    truckType: z.string().min(2, "Truck type is required"),
    vehicleCategory: z.string().min(2, "Category is required"),
    manufacturer: z.string().min(2, "Manufacturer is required"),
    model: z.string().min(2, "Model is required"),
    manufacturingYear: z.string().min(4, "Valid year required"),
  }),
  z.object({
    weightCapacity: z.string().min(1, "Weight capacity is required"),
    volumeCapacity: z.string().min(1, "Volume capacity is required"),
    axles: z.string().min(1, "Number of axles is required"),
  }),
  z.object({
    assignedDriverId: z.string().optional(),
    fleetOwnerId: z.string().optional(),
  }),
  z.object({
    provider: z.string().min(2, "Provider is required"),
    policyNumber: z.string().min(4, "Policy number is required"),
    expiryDate: z.string().min(8, "Expiry date is required"),
  }),
  z.object({}) // Documents handled separately via Dropzone in real app
];

const steps = [
  { id: 1, title: 'Basic Info', icon: Truck },
  { id: 2, title: 'Capacity', icon: Settings },
  { id: 3, title: 'Assignment', icon: UserPlus },
  { id: 4, title: 'Insurance', icon: ShieldCheck },
  { id: 5, title: 'Documents', icon: FileText }
];

export const RegisterTruckWizard = () => {
  const [currentStep, setCurrentStep] = useState(1);
  const navigate = useNavigate();
  const { mutate: createTruck, isPending } = useCreateTruck();

  const formOptions = { resolver: zodResolver(schemas[currentStep - 1]), mode: 'onChange' };
  const { register, handleSubmit, formState: { errors }, trigger, getValues } = useForm(formOptions);

  // Accumulate all form data here
  const [formData, setFormData] = useState({});

  const nextStep = async () => {
    const isStepValid = await trigger();
    if (isStepValid) {
      setFormData(prev => ({ ...prev, ...getValues() }));
      setCurrentStep(p => Math.min(p + 1, 5));
    }
  };

  const prevStep = () => setCurrentStep(p => Math.max(p - 1, 1));

  const onSubmit = (data) => {
    const finalData = { ...formData, ...data };
    createTruck(finalData, {
      onSuccess: () => navigate('/trucks')
    });
  };

  return (
    <PageContainer>
      <DashboardHeader title="Register New Truck" description="Add a new vehicle to the global fleet directory." />

      <div className="w-full max-w-4xl mx-auto bg-card rounded-xl border border-border shadow-sm p-8 mt-6">
        
        {/* Progress Bar */}
        <div className="flex justify-between mb-8 relative">
          <div className="absolute top-1/2 left-0 h-1 bg-muted -z-10 -translate-y-1/2 w-full rounded-full"></div>
          <div className={`absolute top-1/2 left-0 h-1 bg-primary -z-10 -translate-y-1/2 rounded-full transition-all duration-300`} style={{ width: `${((currentStep - 1) / 4) * 100}%` }}></div>
          {steps.map(step => {
            const Icon = step.icon;
            const isActive = step.id === currentStep;
            const isCompleted = step.id < currentStep;
            return (
              <div key={step.id} className="flex flex-col items-center">
                <div className={`w-10 h-10 rounded-full flex items-center justify-center transition-colors ${isActive ? 'bg-primary text-primary-foreground shadow-md scale-110' : isCompleted ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground border border-border'}`}>
                  <Icon className="w-5 h-5" />
                </div>
                <span className={`text-xs mt-2 font-medium ${isActive ? 'text-primary' : 'text-muted-foreground'}`}>{step.title}</span>
              </div>
            );
          })}
        </div>

        {/* Form Content */}
        <form onSubmit={handleSubmit(currentStep === 5 ? onSubmit : (e) => { e.preventDefault(); nextStep(); })} className="space-y-6">
          
          {currentStep === 1 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 animate-in fade-in slide-in-from-right-4">
              <div>
                <label className="text-sm font-medium mb-1 block">Registration Number</label>
                <input {...register('registrationNumber')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="e.g. MH 12 AB 1234" />
                {errors.registrationNumber && <p className="text-destructive text-xs mt-1">{errors.registrationNumber.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Truck Type</label>
                <select {...register('truckType')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary">
                  <option value="">Select Type</option>
                  <option value="FLATBED">Flatbed</option>
                  <option value="REFRIGERATED">Refrigerated</option>
                  <option value="DRY_VAN">Dry Van</option>
                </select>
                {errors.truckType && <p className="text-destructive text-xs mt-1">{errors.truckType.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Vehicle Category</label>
                <input {...register('vehicleCategory')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="e.g. Heavy Commercial" />
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Manufacturer</label>
                <input {...register('manufacturer')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="e.g. Tata Motors" />
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Model</label>
                <input {...register('model')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="e.g. Prima 4028" />
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Manufacturing Year</label>
                <input type="number" {...register('manufacturingYear')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="2023" />
              </div>
            </div>
          )}

          {currentStep === 2 && (
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 animate-in fade-in slide-in-from-right-4">
              <div>
                <label className="text-sm font-medium mb-1 block">Weight Capacity (Tons)</label>
                <input type="number" {...register('weightCapacity')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" />
                {errors.weightCapacity && <p className="text-destructive text-xs mt-1">{errors.weightCapacity.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Volume Capacity (Cubic Ft)</label>
                <input type="number" {...register('volumeCapacity')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" />
                {errors.volumeCapacity && <p className="text-destructive text-xs mt-1">{errors.volumeCapacity.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Number of Axles</label>
                <input type="number" {...register('axles')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" />
                {errors.axles && <p className="text-destructive text-xs mt-1">{errors.axles.message}</p>}
              </div>
            </div>
          )}

          {currentStep === 3 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 animate-in fade-in slide-in-from-right-4">
              <div>
                <label className="text-sm font-medium mb-1 block">Assign Driver (Optional)</label>
                <input {...register('assignedDriverId')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="Search driver by name or ID" />
                <p className="text-xs text-muted-foreground mt-1">Leave blank to assign later.</p>
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Fleet Owner (Optional)</label>
                <input {...register('fleetOwnerId')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="Search fleet owner" />
              </div>
            </div>
          )}

          {currentStep === 4 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 animate-in fade-in slide-in-from-right-4">
              <div>
                <label className="text-sm font-medium mb-1 block">Insurance Provider</label>
                <input {...register('provider')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="e.g. HDFC Ergo" />
                {errors.provider && <p className="text-destructive text-xs mt-1">{errors.provider.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Policy Number</label>
                <input {...register('policyNumber')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" placeholder="POL-12345678" />
                {errors.policyNumber && <p className="text-destructive text-xs mt-1">{errors.policyNumber.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Expiry Date</label>
                <input type="date" {...register('expiryDate')} className="w-full px-4 py-2 rounded-md border border-input bg-background focus:outline-none focus:ring-2 focus:ring-primary" />
                {errors.expiryDate && <p className="text-destructive text-xs mt-1">{errors.expiryDate.message}</p>}
              </div>
            </div>
          )}

          {currentStep === 5 && (
            <div className="animate-in fade-in slide-in-from-right-4">
              <div className="p-8 border-2 border-dashed rounded-xl flex flex-col items-center justify-center text-center bg-muted/30">
                <FileText className="w-12 h-12 text-muted-foreground mb-4" />
                <h3 className="font-semibold text-lg mb-2">Upload Truck Documents</h3>
                <p className="text-sm text-muted-foreground max-w-md mb-6">
                  Please upload the RC Book, Fitness Certificate, Permit, and Insurance Policy. 
                  (In a full implementation, you would use the TruckDocumentDropzone component here).
                </p>
                <button type="button" className="px-4 py-2 bg-secondary text-secondary-foreground rounded-lg font-medium">Browse Files</button>
              </div>
            </div>
          )}

          <div className="flex justify-between pt-6 border-t border-border mt-8">
            <button
              type="button"
              onClick={prevStep}
              disabled={currentStep === 1}
              className={`flex items-center gap-2 px-6 py-2 rounded-lg font-medium transition-colors ${currentStep === 1 ? 'opacity-50 cursor-not-allowed text-muted-foreground' : 'hover:bg-muted text-foreground border border-border'}`}
            >
              <ChevronLeft className="w-4 h-4" /> Back
            </button>
            
            {currentStep < 5 ? (
              <button
                type="button"
                onClick={nextStep}
                className="flex items-center gap-2 px-6 py-2 bg-primary text-primary-foreground rounded-lg font-medium hover:bg-primary/90 transition-colors"
              >
                Next Step <ChevronRight className="w-4 h-4" />
              </button>
            ) : (
              <button
                type="submit"
                disabled={isPending}
                className="flex items-center gap-2 px-6 py-2 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition-colors disabled:opacity-50"
              >
                {isPending ? 'Registering...' : 'Complete Registration'} <CheckCircle className="w-4 h-4" />
              </button>
            )}
          </div>

        </form>
      </div>
    </PageContainer>
  );
};

// Extracted from Lucide since we used CheckCircle at the end
import { CheckCircle } from 'lucide-react';
