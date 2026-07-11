import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { UploadCloud, X } from 'lucide-react';
import { useAvatarUpload, useDeleteAvatar } from '../hooks/useProfile';

export const AvatarUploader = ({ currentAvatarUrl, onUploadSuccess }) => {
  const { mutate: uploadAvatar, isPending: isUploading } = useAvatarUpload();
  const { mutate: deleteAvatar, isPending: isDeleting } = useDeleteAvatar();

  const onDrop = useCallback((acceptedFiles) => {
    if (acceptedFiles.length > 0) {
      const file = acceptedFiles[0];
      uploadAvatar(file, {
        onSuccess: () => {
          if (onUploadSuccess) onUploadSuccess();
        }
      });
    }
  }, [uploadAvatar, onUploadSuccess]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { 'image/*': ['.jpeg', '.jpg', '.png'] },
    maxFiles: 1,
    maxSize: 5242880 // 5MB
  });

  return (
    <div className="flex flex-col sm:flex-row items-center gap-6">
      {/* Current Avatar Display */}
      <div className="relative shrink-0">
        <div className="w-24 h-24 rounded-full border-4 border-background shadow-lg overflow-hidden bg-muted">
          {currentAvatarUrl ? (
            <img src={currentAvatarUrl} alt="Avatar" className="w-full h-full object-cover" />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-muted-foreground bg-primary/10 font-bold text-2xl">
              ?
            </div>
          )}
        </div>
        {currentAvatarUrl && (
          <button 
            onClick={() => deleteAvatar()}
            disabled={isDeleting}
            className="absolute top-0 right-0 bg-destructive text-destructive-foreground p-1 rounded-full shadow-sm hover:scale-110 transition-transform"
            title="Remove Avatar"
          >
            <X className="w-3 h-3" />
          </button>
        )}
      </div>

      {/* Dropzone */}
      <div 
        {...getRootProps()} 
        className={`flex-1 border-2 border-dashed rounded-xl p-6 text-center cursor-pointer transition-colors ${isDragActive ? 'border-primary bg-primary/5' : 'border-border hover:border-primary/50 hover:bg-muted/30'}`}
      >
        <input {...getInputProps()} />
        <div className="flex flex-col items-center justify-center gap-2">
          <UploadCloud className={`w-8 h-8 ${isDragActive ? 'text-primary' : 'text-muted-foreground'}`} />
          {isUploading ? (
            <p className="text-sm font-medium animate-pulse text-primary">Uploading your avatar...</p>
          ) : isDragActive ? (
            <p className="text-sm font-medium text-primary">Drop the image here...</p>
          ) : (
            <>
              <p className="text-sm font-medium">Click to upload or drag and drop</p>
              <p className="text-xs text-muted-foreground">SVG, PNG, JPG or GIF (max. 5MB)</p>
            </>
          )}
        </div>
      </div>
    </div>
  );
};
