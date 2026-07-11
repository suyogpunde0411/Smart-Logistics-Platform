import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix for default Leaflet markers missing icons in React
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

// Custom Icons
const truckIconHtml = `<div class="bg-primary text-primary-foreground p-1.5 rounded-full shadow-lg border-2 border-white"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10 17h4V5H2v12h3"/><path d="M20 17h2v-9l-3.5-2H14v11h3"/><circle cx="8.5" cy="17.5" r="1.5"/><circle cx="18.5" cy="17.5" r="1.5"/></svg></div>`;
const TruckIcon = L.divIcon({ html: truckIconHtml, className: 'custom-leaflet-icon', iconSize: [32, 32], iconAnchor: [16, 16] });

const destinationIconHtml = `<div class="bg-destructive text-destructive-foreground p-1.5 rounded-full shadow-lg border-2 border-white"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg></div>`;
const DestinationIcon = L.divIcon({ html: destinationIconHtml, className: 'custom-leaflet-icon', iconSize: [32, 32], iconAnchor: [16, 32] });

// Component to recenter map dynamically when truck moves
const RecenterMap = ({ lat, lng }) => {
  const map = useMap();
  useEffect(() => {
    if (lat && lng) map.setView([lat, lng], map.getZoom());
  }, [lat, lng, map]);
  return null;
};

export const TripMap = ({ 
  currentLocation, 
  origin, 
  destination, 
  routeCoordinates = [] 
}) => {
  const defaultCenter = currentLocation || origin || [20.5937, 78.9629]; // Default to India if no data

  return (
    <div className="h-full w-full rounded-xl overflow-hidden border border-border shadow-sm z-0 relative">
      <MapContainer center={defaultCenter} zoom={6} className="h-full w-full" zoomControl={false}>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {/* Route Path */}
        {routeCoordinates.length > 0 && (
          <Polyline 
            positions={routeCoordinates} 
            color="#3b82f6" 
            weight={4} 
            opacity={0.7} 
            dashArray="10, 10" 
          />
        )}

        {/* Origin Marker */}
        {origin && (
          <Marker position={origin}>
            <Popup>
              <strong>Pickup Location</strong>
            </Popup>
          </Marker>
        )}

        {/* Destination Marker */}
        {destination && (
          <Marker position={destination} icon={DestinationIcon}>
            <Popup>
              <strong>Delivery Destination</strong>
            </Popup>
          </Marker>
        )}

        {/* Current Truck Location */}
        {currentLocation && (
          <>
            <Marker position={currentLocation} icon={TruckIcon}>
              <Popup>
                <strong>Current Location</strong><br/>
                Speed: 65 km/h<br/>
                Updated: Just now
              </Popup>
            </Marker>
            <RecenterMap lat={currentLocation[0]} lng={currentLocation[1]} />
          </>
        )}
      </MapContainer>

      {/* Embedded Map Controls Overlay */}
      <div className="absolute top-4 right-4 z-[1000] flex flex-col gap-2">
        <button className="bg-background border border-border rounded-lg p-2 shadow-md hover:bg-muted transition-colors" onClick={() => document.querySelector('.leaflet-control-zoom-in').click()}>+</button>
        <button className="bg-background border border-border rounded-lg p-2 shadow-md hover:bg-muted transition-colors" onClick={() => document.querySelector('.leaflet-control-zoom-out').click()}>-</button>
      </div>
    </div>
  );
};
