import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useCreateShipment } from '../hooks/useCreateShipment';
import { 
  Package, MapPin, Truck, DollarSign, FileText, CheckSquare, 
  ChevronRight, ChevronLeft, CheckCircle 
} from 'lucide-react';

// Massive 7-step Zod Schemas
const schemas = [
  // Step 1: Basic
  z.object({
    shipmentName: z.string().min(3, "Name is required"),
    referenceNumber: z.string().optional(),
    shipmentCategory: z.string().min(2, "Category is required"),
    priority: z.enum(['STANDARD', 'HIGH', 'URGENT'])
  }),
  // Step 2: Pickup
  z.object({
    pickupAddress: z.string().min(5, "Address is required"),
    pickupContact: z.string().min(10, "Valid contact required"),
    pickupDate: z.string().min(8, "Date is required"),
    pickupTime: z.string().optional(),
    loadingInstructions: z.string().optional()
  }),
  // Step 3: Delivery
  z.object({
    destinationAddress: z.string().min(5, "Address is required"),
    receiverContact: z.string().min(10, "Valid contact required"),
    expectedDeliveryDate: z.string().min(8, "Date is required"),
    specialInstructions: z.string().optional()
  }),
  // Step 4: Cargo
  z.object({
    weight: z.string().min(1, "Weight is required"),
    dimensions: z.string().optional(),
    cargoType: z.string().min(2, "Cargo type is required"),
    fragile: z.boolean().default(false),
    hazardous: z.boolean().default(false),
    temperatureRequirement: z.string().optional(),
    insuranceRequired: z.boolean().default(false)
  }),
  // Step 5: Pricing
  z.object({
    budget: z.string().min(1, "Budget is required"),
    preferredVehicle: z.string().optional(),
    paymentTerms: z.string().min(2, "Terms are required")
  }),
  // Step 6: Documents
  z.object({}), // Handled via dropzone
  // Step 7: Review
  z.object({})
];

const steps = [
  { id: 1, title: 'Basic Info', icon: Package },
  { id: 2, title: 'Pickup', icon: MapPin },
  { id: 3, title: 'Delivery', icon: MapPin },
  { id: 4, title: 'Cargo', icon: Truck },
  { id: 5, title: 'Pricing', icon: DollarSign },
  { id: 6, title: 'Documents', icon: FileText },
  { id: 7, title: 'Review', icon: CheckSquare }
];

export const CreateShipmentWizard = () => {
  const [currentStep, setCurrentStep] = useState(1);
  const navigate = useNavigate();
  const { mutate: createShipment, isPending } = useCreateShipment();
  const [formData, setFormData] = useState({ priority: 'STANDARD' });

  const formOptions = { resolver: zodResolver(schemas[currentStep - 1]), mode: 'onChange', defaultValues: formData };
  const { register, handleSubmit, formState: { errors }, trigger, getValues } = useForm(formOptions);

  const nextStep = async () => {
    const isStepValid = await trigger();
    if (isStepValid) {
      setFormData(prev => ({ ...prev, ...getValues() }));
      setCurrentStep(p => Math.min(p + 1, 7));
    }
  };

  const prevStep = () => setCurrentStep(p => Math.max(p - 1, 1));

  const onSubmit = () => {
    const finalData = { ...formData, ...getValues() };
    createShipment(finalData, { onSuccess: () => navigate('/shipments') });
  };

  return (
    <PageContainer>
      <DashboardHeader title="Create Shipment" description="Publish a new shipment to the marketplace." />

      <div className="w-full max-w-5xl mx-auto bg-card rounded-xl border border-border shadow-sm p-8 mt-6">
        
        {/* Progress Stepper */}
        <div className="hidden md:flex justify-between mb-10 relative">
          <div className="absolute top-1/2 left-0 h-1 bg-muted -z-10 -translate-y-1/2 w-full rounded-full"></div>
          <div className={`absolute top-1/2 left-0 h-1 bg-primary -z-10 -translate-y-1/2 rounded-full transition-all duration-300`} style={{ width: `${((currentStep - 1) / 6) * 100}%` }}></div>
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
        
        {/* Mobile Stepper */}
        <div className="md:hidden flex justify-between items-center mb-8 border-b pb-4">
          <span className="font-semibold text-lg">Step {currentStep} of 7</span>
          <span className="text-primary font-medium">{steps[currentStep-1].title}</span>
        </div>

        {/* Form Components */}
        <form onSubmit={handleSubmit(currentStep === 7 ? onSubmit : (e) => { e.preventDefault(); nextStep(); })} className="space-y-6">
          
          {currentStep === 1 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 animate-in fade-in slide-in-from-right-4">
              <div className="col-span-1 md:col-span-2">
                <label className="text-sm font-medium mb-1 block">Shipment Name</label>
                <input {...register('shipmentName')} className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="e.g. Electronic Goods to Mumbai" />
                {errors.shipmentName && <p className="text-destructive text-xs mt-1">{errors.shipmentName.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Reference Number (Optional)</label>
                <input {...register('referenceNumber')} className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="PO-123456" />
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Shipment Category</label>
                <select {...register('shipmentCategory')} className="w-full px-4 py-2 rounded-md border border-input bg-background">
                  <option value="">Select Category</option>
                  <option value="ELECTRONICS">Electronics</option>
                  <option value="MACHINERY">Machinery</option>
                  <option value="AGRICULTURE">Agriculture</option>
                  <option value="FMCG">FMCG</option>
                </select>
                {errors.shipmentCategory && <p className="text-destructive text-xs mt-1">{errors.shipmentCategory.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Priority</label>
                <select {...register('priority')} className="w-full px-4 py-2 rounded-md border border-input bg-background">
                  <option value="STANDARD">Standard</option>
                  <option value="HIGH">High</option>
                  <option value="URGENT">Urgent</option>
                </select>
              </div>
            </div>
          )}

          {currentStep === 2 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 animate-in fade-in slide-in-from-right-4">
              <div className="col-span-1 md:col-span-2">
                <label className="text-sm font-medium mb-1 block">Pickup Address</label>
                <textarea {...register('pickupAddress')} rows="3" className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="Full pickup address..." />
                {errors.pickupAddress && <p className="text-destructive text-xs mt-1">{errors.pickupAddress.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Pickup Contact Name & Phone</label>
                <input {...register('pickupContact')} className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="John Doe, +91 9876543210" />
                {errors.pickupContact && <p className="text-destructive text-xs mt-1">{errors.pickupContact.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Pickup Date</label>
                <input type="date" {...register('pickupDate')} className="w-full px-4 py-2 rounded-md border border-input bg-background" />
                {errors.pickupDate && <p className="text-destructive text-xs mt-1">{errors.pickupDate.message}</p>}
              </div>
              <div className="col-span-1 md:col-span-2">
                <label className="text-sm font-medium mb-1 block">Loading Instructions (Optional)</label>
                <input {...register('loadingInstructions')} className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="e.g. Forklift required" />
              </div>
            </div>
          )}

          {currentStep === 3 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 animate-in fade-in slide-in-from-right-4">
              <div className="col-span-1 md:col-span-2">
                <label className="text-sm font-medium mb-1 block">Destination Address</label>
                <textarea {...register('destinationAddress')} rows="3" className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="Full destination address..." />
                {errors.destinationAddress && <p className="text-destructive text-xs mt-1">{errors.destinationAddress.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Receiver Contact Name & Phone</label>
                <input {...register('receiverContact')} className="w-full px-4 py-2 rounded-md border border-input bg-background" />
                {errors.receiverContact && <p className="text-destructive text-xs mt-1">{errors.receiverContact.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Expected Delivery Date</label>
                <input type="date" {...register('expectedDeliveryDate')} className="w-full px-4 py-2 rounded-md border border-input bg-background" />
                {errors.expectedDeliveryDate && <p className="text-destructive text-xs mt-1">{errors.expectedDeliveryDate.message}</p>}
              </div>
              <div className="col-span-1 md:col-span-2">
                <label className="text-sm font-medium mb-1 block">Special Unloading Instructions (Optional)</label>
                <input {...register('specialInstructions')} className="w-full px-4 py-2 rounded-md border border-input bg-background" />
              </div>
            </div>
          )}

          {currentStep === 4 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 animate-in fade-in slide-in-from-right-4">
              <div>
                <label className="text-sm font-medium mb-1 block">Weight (Tons)</label>
                <input type="number" {...register('weight')} className="w-full px-4 py-2 rounded-md border border-input bg-background" />
                {errors.weight && <p className="text-destructive text-xs mt-1">{errors.weight.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Dimensions (L x W x H meters)</label>
                <input {...register('dimensions')} className="w-full px-4 py-2 rounded-md border border-input bg-background" placeholder="e.g. 5 x 2.5 x 2.5" />
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Cargo Type</label>
                <select {...register('cargoType')} className="w-full px-4 py-2 rounded-md border border-input bg-background">
                  <option value="">Select Type</option>
                  <option value="PALLETIZED">Palletized</option>
                  <option value="LOOSE">Loose</option>
                  <option value="LIQUID">Liquid/Bulk</option>
                </select>
              </div>
              
              <div className="col-span-1 md:col-span-2 flex flex-wrap gap-6 mt-2">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input type="checkbox" {...register('fragile')} className="w-4 h-4 rounded border-input" />
                  <span className="text-sm font-medium">Fragile Cargo</span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input type="checkbox" {...register('hazardous')} className="w-4 h-4 rounded border-input" />
                  <span className="text-sm font-medium">Hazardous Materials</span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input type="checkbox" {...register('insuranceRequired')} className="w-4 h-4 rounded border-input" />
                  <span className="text-sm font-medium">Insurance Required</span>
                </label>
              </div>
            </div>
          )}

          {currentStep === 5 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 animate-in fade-in slide-in-from-right-4">
              <div>
                <label className="text-sm font-medium mb-1 block">Estimated Budget (₹)</label>
                <input type="number" {...register('budget')} className="w-full px-4 py-2 rounded-md border border-input bg-background" />
                {errors.budget && <p className="text-destructive text-xs mt-1">{errors.budget.message}</p>}
              </div>
              <div>
                <label className="text-sm font-medium mb-1 block">Preferred Vehicle Type (Optional)</label>
                <select {...register('preferredVehicle')} className="w-full px-4 py-2 rounded-md border border-input bg-background">
                  <option value="">Any Vehicle</option>
                  <option value="FLATBED">Flatbed</option>
                  <option value="REFRIGERATED">Refrigerated</option>
                </select>
              </div>
              <div className="col-span-1 md:col-span-2">
                <label className="text-sm font-medium mb-1 block">Payment Terms</label>
                <select {...register('paymentTerms')} className="w-full px-4 py-2 rounded-md border border-input bg-background">
                  <option value="ADVANCE">50% Advance, 50% on Delivery</option>
                  <option value="POST_DELIVERY">100% Post Delivery</option>
                  <option value="NET_30">Net 30 Days</option>
                </select>
              </div>
            </div>
          )}

          {currentStep === 6 && (
            <div className="animate-in fade-in slide-in-from-right-4 space-y-4">
              <div className="p-8 border-2 border-dashed rounded-xl flex flex-col items-center justify-center text-center bg-muted/30">
                <FileText className="w-12 h-12 text-muted-foreground mb-4" />
                <h3 className="font-semibold text-lg mb-2">Upload Shipping Documents</h3>
                <p className="text-sm text-muted-foreground max-w-md mb-6">
                  Please upload the Invoice, E-Way Bill, and Purchase Orders.
                </p>
                <button type="button" className="px-4 py-2 bg-secondary text-secondary-foreground rounded-lg font-medium">Browse Files</button>
              </div>
              <p className="text-xs text-center text-muted-foreground">Note: Document upload is optional for saving a draft, but required before publishing.</p>
            </div>
          )}

          {currentStep === 7 && (
            <div className="animate-in fade-in slide-in-from-right-4 bg-muted/20 p-6 rounded-xl border border-border">
              <h3 className="font-bold text-xl mb-6">Review Shipment Details</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-8 text-sm">
                <div>
                  <h4 className="font-semibold text-muted-foreground mb-3 uppercase text-xs">Pickup</h4>
                  <p className="font-medium">{getValues('pickupAddress') || formData.pickupAddress}</p>
                  <p className="mt-1 text-muted-foreground">Date: {getValues('pickupDate') || formData.pickupDate}</p>
                  <p className="text-muted-foreground">Contact: {getValues('pickupContact') || formData.pickupContact}</p>
                </div>
                <div>
                  <h4 className="font-semibold text-muted-foreground mb-3 uppercase text-xs">Delivery</h4>
                  <p className="font-medium">{getValues('destinationAddress') || formData.destinationAddress}</p>
                  <p className="mt-1 text-muted-foreground">Date: {getValues('expectedDeliveryDate') || formData.expectedDeliveryDate}</p>
                </div>
                <div>
                  <h4 className="font-semibold text-muted-foreground mb-3 uppercase text-xs">Cargo & Pricing</h4>
                  <p><strong>Weight:</strong> {getValues('weight') || formData.weight} Tons</p>
                  <p><strong>Type:</strong> {getValues('cargoType') || formData.cargoType}</p>
                  <p><strong>Budget:</strong> ₹{getValues('budget') || formData.budget}</p>
                </div>
              </div>
            </div>
          )}

          {/* Navigation Buttons */}
          <div className="flex justify-between pt-6 border-t border-border mt-8">
            <button
              type="button"
              onClick={prevStep}
              disabled={currentStep === 1 || isPending}
              className={`flex items-center gap-2 px-6 py-2 rounded-lg font-medium transition-colors ${currentStep === 1 ? 'opacity-50 cursor-not-allowed text-muted-foreground' : 'hover:bg-muted text-foreground border border-border'}`}
            >
              <ChevronLeft className="w-4 h-4" /> Back
            </button>
            
            {currentStep < 7 ? (
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
                {isPending ? 'Publishing...' : 'Publish Shipment'} <CheckCircle className="w-4 h-4" />
              </button>
            )}
          </div>

        </form>
      </div>
    </PageContainer>
  );
};
