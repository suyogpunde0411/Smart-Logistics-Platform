import React, { createContext, useContext, useState } from 'react';

const WizardContext = createContext();

export const ShipmentWizardProvider = ({ children }) => {
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState({
    title: '', description: '', cargoType: '',
    weight: '', dimensions: '', fragile: false, hazardous: false,
    pickupLocation: '', pickupDate: '', deliveryLocation: '', deliveryDate: '',
    estimatedBudget: '', instructions: ''
  });
  const [files, setFiles] = useState([]);

  const nextStep = () => setCurrentStep(prev => Math.min(prev + 1, 7));
  const prevStep = () => setCurrentStep(prev => Math.max(prev - 1, 1));
  const updateFormData = (data) => setFormData(prev => ({ ...prev, ...data }));

  return (
    <WizardContext.Provider value={{ currentStep, setCurrentStep, formData, updateFormData, files, setFiles, nextStep, prevStep }}>
      {children}
    </WizardContext.Provider>
  );
};

export const useShipmentWizard = () => useContext(WizardContext);
