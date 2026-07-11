import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ShipmentWizardProvider, useShipmentWizard } from '../components/ShipmentWizardProvider';
import { DocumentUploadDropzone } from '../components/DocumentUploadDropzone';
import { useCreateShipment } from '../hooks/useShipments';
import { useNavigate } from 'react-router-dom';

const WizardForm = () => {
  const { currentStep, nextStep, prevStep, formData, updateFormData, files, setFiles } = useShipmentWizard();
  const { mutate: createShipment, isPending } = useCreateShipment();
  const navigate = useNavigate();

  const handleSubmit = () => {
    // Convert to FormData if there are files
    const payload = new FormData();
    Object.keys(formData).forEach(key => payload.append(key, formData[key]));
    files.forEach(f => payload.append('documents', f));

    createShipment(payload, {
      onSuccess: () => navigate('/shipments')
    });
  };

  return (
    <div className="max-w-3xl mx-auto bg-card border border-border rounded-xl shadow-sm p-6 md:p-8 mt-8">
      <div className="mb-8 flex justify-between items-center relative">
        <div className="absolute top-1/2 left-0 w-full h-1 bg-muted -z-10 -translate-y-1/2 rounded-full" />
        <div className="absolute top-1/2 left-0 h-1 bg-primary -z-10 -translate-y-1/2 rounded-full transition-all duration-300" style={{ width: `${(currentStep - 1) / 6 * 100}%` }} />
        
        {[1,2,3,4,5,6,7].map(step => (
          <div key={step} className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm transition-colors ${currentStep >= step ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground border-2 border-border'}`}>
            {step}
          </div>
        ))}
      </div>

      <div className="min-h-[300px]">
        {currentStep === 1 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Basic Information</h3>
            <div>
              <label className="block text-sm font-medium mb-1">Shipment Title</label>
              <input value={formData.title} onChange={e => updateFormData({ title: e.target.value })} className="w-full p-2 rounded-md border bg-background" placeholder="E.g., Steel pipes delivery" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Cargo Type</label>
              <input value={formData.cargoType} onChange={e => updateFormData({ cargoType: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
          </div>
        )}
        
        {currentStep === 2 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Pickup Details</h3>
            <div>
              <label className="block text-sm font-medium mb-1">Pickup Location</label>
              <input value={formData.pickupLocation} onChange={e => updateFormData({ pickupLocation: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Pickup Date</label>
              <input type="date" value={formData.pickupDate} onChange={e => updateFormData({ pickupDate: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
          </div>
        )}

        {currentStep === 3 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Drop Details</h3>
            <div>
              <label className="block text-sm font-medium mb-1">Delivery Location</label>
              <input value={formData.deliveryLocation} onChange={e => updateFormData({ deliveryLocation: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Delivery Date</label>
              <input type="date" value={formData.deliveryDate} onChange={e => updateFormData({ deliveryDate: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
          </div>
        )}

        {currentStep === 4 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Cargo Specifics</h3>
            <div>
              <label className="block text-sm font-medium mb-1">Weight (kg)</label>
              <input type="number" value={formData.weight} onChange={e => updateFormData({ weight: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
            <div className="flex items-center gap-4 mt-4">
              <label className="flex items-center gap-2 text-sm cursor-pointer">
                <input type="checkbox" checked={formData.fragile} onChange={e => updateFormData({ fragile: e.target.checked })} /> Fragile
              </label>
              <label className="flex items-center gap-2 text-sm cursor-pointer">
                <input type="checkbox" checked={formData.hazardous} onChange={e => updateFormData({ hazardous: e.target.checked })} /> Hazardous
              </label>
            </div>
          </div>
        )}

        {currentStep === 5 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Pricing Options</h3>
            <div>
              <label className="block text-sm font-medium mb-1">Estimated Budget (USD)</label>
              <input type="number" value={formData.estimatedBudget} onChange={e => updateFormData({ estimatedBudget: e.target.value })} className="w-full p-2 rounded-md border bg-background" />
            </div>
          </div>
        )}

        {currentStep === 6 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Documents</h3>
            <p className="text-sm text-muted-foreground mb-4">Upload invoices, e-way bills, or permits.</p>
            <DocumentUploadDropzone files={files} setFiles={setFiles} />
          </div>
        )}

        {currentStep === 7 && (
          <div className="space-y-4">
            <h3 className="text-xl font-bold">Review & Submit</h3>
            <div className="p-4 bg-muted/50 rounded-lg text-sm space-y-2">
              <p><strong>Title:</strong> {formData.title}</p>
              <p><strong>Route:</strong> {formData.pickupLocation} to {formData.deliveryLocation}</p>
              <p><strong>Weight:</strong> {formData.weight} kg</p>
              <p><strong>Budget:</strong> ${formData.estimatedBudget}</p>
              <p><strong>Documents:</strong> {files.length} attached</p>
            </div>
          </div>
        )}
      </div>

      <div className="flex justify-between mt-8 pt-6 border-t border-border">
        <button 
          onClick={prevStep} 
          disabled={currentStep === 1}
          className="px-6 py-2 rounded-md font-medium text-sm border border-border hover:bg-muted disabled:opacity-50 transition-colors"
        >
          Back
        </button>
        {currentStep < 7 ? (
          <button 
            onClick={nextStep}
            className="px-6 py-2 bg-primary text-primary-foreground rounded-md font-medium text-sm hover:bg-primary/90 transition-colors"
          >
            Next Step
          </button>
        ) : (
          <button 
            onClick={handleSubmit}
            disabled={isPending}
            className="px-8 py-2 bg-primary text-primary-foreground rounded-md font-medium text-sm hover:bg-primary/90 transition-colors"
          >
            {isPending ? 'Submitting...' : 'Submit Shipment'}
          </button>
        )}
      </div>
    </div>
  );
};

export const CreateShipmentWizard = () => {
  return (
    <ShipmentWizardProvider>
      <PageContainer>
        <DashboardHeader title="Create New Shipment" description="Follow the steps to configure and list a new shipment." />
        <WizardForm />
      </PageContainer>
    </ShipmentWizardProvider>
  );
};
