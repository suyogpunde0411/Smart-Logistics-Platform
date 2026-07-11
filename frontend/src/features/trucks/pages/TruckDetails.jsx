import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { TruckImageGallery } from '../components/TruckImageGallery';
import { TruckDocumentDropzone } from '../components/TruckDocumentDropzone';
import { TruckMaintenanceLog } from '../components/TruckMaintenanceLog';
import { TruckInsuranceStatus } from '../components/TruckInsuranceStatus';
import { useTruck } from '../hooks/useTrucks';
import { MapPin, User, Activity, Edit3, Trash2 } from 'lucide-react';

export const TruckDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('overview');

  const { data: truck, isLoading } = useTruck(id);

  // Mock data fallback
  const mockTruck = truck || {
    id: id,
    registrationNumber: 'MH 12 AB 1234',
    truckType: 'Flatbed',
    vehicleCategory: 'Heavy Commercial',
    manufacturer: 'Tata Motors',
    model: 'Prima 4028',
    manufacturingYear: 2023,
    weightCapacity: 40,
    volumeCapacity: 1200,
    axles: 3,
    status: 'AVAILABLE',
    lastLocation: 'Mumbai, Maharashtra',
    assignedDriver: { name: 'Rahul Sharma', phone: '+91 9876543210' },
    insurance: { provider: 'HDFC Ergo', policyNumber: 'POL-12345678', issueDate: '2023-01-15', expiryDate: '2024-01-14' },
    images: [{ id: 1, url: 'https://placehold.co/600x400/1e293b/fff?text=Truck+Front', isPrimary: true }],
    maintenanceLogs: [{ date: '2023-05-10', serviceType: 'Oil Change & Brake Inspection', provider: 'Tata Authorized Service', cost: '₹12,500' }],
    documents: [{ id: 1, type: 'RC_BOOK' }, { id: 2, type: 'FITNESS_CERTIFICATE' }]
  };

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading truck details...</div>;

  return (
    <PageContainer>
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">{mockTruck.registrationNumber}</h1>
          <p className="text-muted-foreground">{mockTruck.manufacturer} {mockTruck.model} • {mockTruck.manufacturingYear}</p>
        </div>
        <div className="flex gap-2">
          <button className="flex items-center gap-2 px-4 py-2 bg-secondary text-secondary-foreground rounded-lg font-medium hover:bg-secondary/80 transition-colors">
            <Edit3 className="w-4 h-4" /> Edit
          </button>
          <button className="flex items-center gap-2 px-4 py-2 bg-destructive/10 text-destructive rounded-lg font-medium hover:bg-destructive hover:text-white transition-colors">
            <Trash2 className="w-4 h-4" /> Delete
          </button>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex border-b border-border mb-6 overflow-x-auto">
        {['overview', 'images', 'documents', 'maintenance', 'insurance'].map(tab => (
          <button 
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-3 font-medium text-sm whitespace-nowrap border-b-2 transition-colors ${activeTab === tab ? 'border-primary text-primary' : 'border-transparent text-muted-foreground hover:text-foreground'}`}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      <div className="animate-in fade-in slide-in-from-bottom-4">
        {activeTab === 'overview' && (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2 space-y-6">
              
              {/* Specs Card */}
              <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
                <h3 className="font-semibold text-lg mb-4 flex items-center gap-2"><Activity className="w-5 h-5 text-muted-foreground" /> Specifications</h3>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div><p className="text-sm text-muted-foreground mb-1">Type</p><p className="font-medium">{mockTruck.truckType}</p></div>
                  <div><p className="text-sm text-muted-foreground mb-1">Category</p><p className="font-medium">{mockTruck.vehicleCategory}</p></div>
                  <div><p className="text-sm text-muted-foreground mb-1">Weight</p><p className="font-medium">{mockTruck.weightCapacity} Tons</p></div>
                  <div><p className="text-sm text-muted-foreground mb-1">Axles</p><p className="font-medium">{mockTruck.axles}</p></div>
                </div>
              </div>

              {/* Status & Location Card */}
              <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
                <h3 className="font-semibold text-lg mb-4 flex items-center gap-2"><MapPin className="w-5 h-5 text-muted-foreground" /> Current Status</h3>
                <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">Availability</p>
                    <span className={`px-2.5 py-1 text-xs font-bold rounded-full ${mockTruck.status === 'AVAILABLE' ? 'bg-green-500/10 text-green-600' : 'bg-yellow-500/10 text-yellow-600'}`}>
                      {mockTruck.status}
                    </span>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">Last Known Location</p>
                    <p className="font-medium">{mockTruck.lastLocation}</p>
                  </div>
                  <button className="px-4 py-2 bg-primary/10 text-primary rounded-lg font-medium text-sm">View on Map</button>
                </div>
              </div>

            </div>

            {/* Sidebar Cards */}
            <div className="space-y-6">
              <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
                <h3 className="font-semibold text-lg mb-4 flex items-center gap-2"><User className="w-5 h-5 text-muted-foreground" /> Assigned Driver</h3>
                {mockTruck.assignedDriver ? (
                  <div className="flex items-center gap-3">
                    <div className="w-12 h-12 bg-muted rounded-full flex items-center justify-center">
                      <User className="w-6 h-6 text-muted-foreground" />
                    </div>
                    <div>
                      <p className="font-medium">{mockTruck.assignedDriver.name}</p>
                      <p className="text-sm text-muted-foreground">{mockTruck.assignedDriver.phone}</p>
                    </div>
                  </div>
                ) : (
                  <p className="text-sm text-muted-foreground">No driver currently assigned.</p>
                )}
                <button className="w-full mt-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors">Reassign Driver</button>
              </div>

              <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
                <h3 className="font-semibold text-lg mb-4">Quick Stats</h3>
                <div className="space-y-3 text-sm">
                  <div className="flex justify-between"><span className="text-muted-foreground">Total Trips</span><span className="font-medium">142</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Total Distance</span><span className="font-medium">45,200 km</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Fuel Efficiency</span><span className="font-medium">4.2 km/L</span></div>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'images' && (
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <h3 className="font-semibold text-lg mb-6">Truck Image Gallery</h3>
            <TruckImageGallery images={mockTruck.images} />
          </div>
        )}

        {activeTab === 'documents' && (
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <h3 className="font-semibold text-lg mb-6">Verification Documents</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              <TruckDocumentDropzone truckId={id} documentType="RC_BOOK" existingDocument={mockTruck.documents.find(d => d.type === 'RC_BOOK')} />
              <TruckDocumentDropzone truckId={id} documentType="FITNESS_CERTIFICATE" existingDocument={mockTruck.documents.find(d => d.type === 'FITNESS_CERTIFICATE')} />
              <TruckDocumentDropzone truckId={id} documentType="PERMIT" existingDocument={mockTruck.documents.find(d => d.type === 'PERMIT')} />
              <TruckDocumentDropzone truckId={id} documentType="POLLUTION_CERTIFICATE" existingDocument={mockTruck.documents.find(d => d.type === 'POLLUTION_CERTIFICATE')} />
            </div>
          </div>
        )}

        {activeTab === 'maintenance' && (
          <div className="space-y-6">
            <div className="flex justify-end">
              <button className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors">
                Log Maintenance
              </button>
            </div>
            <TruckMaintenanceLog logs={mockTruck.maintenanceLogs} />
          </div>
        )}

        {activeTab === 'insurance' && (
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm max-w-3xl">
            <h3 className="font-semibold text-lg mb-6">Insurance Policy Status</h3>
            <TruckInsuranceStatus insurance={mockTruck.insurance} />
            <div className="mt-6 flex justify-end">
              <button className="flex items-center gap-2 px-4 py-2 border border-border rounded-lg font-medium hover:bg-muted transition-colors">
                Update Policy Details
              </button>
            </div>
          </div>
        )}
      </div>
    </PageContainer>
  );
};
