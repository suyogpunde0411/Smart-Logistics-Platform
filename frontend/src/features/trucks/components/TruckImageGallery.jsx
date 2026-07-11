import React from 'react';
import { Camera, Trash2, Star } from 'lucide-react';

export const TruckImageGallery = ({ images = [] }) => {
  return (
    <div className="space-y-4">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {/* Mock Add Image Card */}
        <div className="aspect-video bg-muted border-2 border-dashed border-border rounded-xl flex flex-col items-center justify-center cursor-pointer hover:bg-muted/80 transition-colors">
          <Camera className="w-8 h-8 text-muted-foreground mb-2" />
          <span className="text-sm font-medium text-muted-foreground">Add Image</span>
        </div>

        {images.map((img) => (
          <div key={img.id} className="relative aspect-video rounded-xl overflow-hidden group border border-border">
            <img src={img.url} alt="Truck" className="w-full h-full object-cover transition-transform group-hover:scale-105" />
            <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
              <button className={`p-2 rounded-full ${img.isPrimary ? 'bg-yellow-500 text-white' : 'bg-white/20 text-white hover:bg-white/40'}`}>
                <Star className="w-4 h-4" />
              </button>
              <button className="p-2 rounded-full bg-destructive/80 text-white hover:bg-destructive">
                <Trash2 className="w-4 h-4" />
              </button>
            </div>
            {img.isPrimary && (
              <div className="absolute top-2 left-2 bg-yellow-500 text-white text-[10px] font-bold px-2 py-0.5 rounded-full uppercase tracking-wider">
                Primary
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
