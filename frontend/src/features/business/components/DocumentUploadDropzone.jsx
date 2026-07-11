import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { UploadCloud, File, X } from 'lucide-react';

export const DocumentUploadDropzone = ({ files, setFiles, maxSize = 5242880, accept }) => {
  const onDrop = useCallback(acceptedFiles => {
    setFiles(prev => [...prev, ...acceptedFiles]);
  }, [setFiles]);

  const removeFile = (index) => {
    setFiles(prev => prev.filter((_, i) => i !== index));
  };

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop, maxSize, accept });

  return (
    <div className="space-y-4">
      <div 
        {...getRootProps()} 
        className={`p-8 border-2 border-dashed rounded-xl flex flex-col items-center justify-center text-center cursor-pointer transition-colors ${
          isDragActive ? 'border-primary bg-primary/5' : 'border-border bg-card hover:bg-muted/30'
        }`}
      >
        <input {...getInputProps()} />
        <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center mb-4">
          <UploadCloud className="w-6 h-6 text-primary" />
        </div>
        <p className="text-sm font-medium">Drag & drop files here, or click to select files</p>
        <p className="text-xs text-muted-foreground mt-1">Maximum file size: {Math.round(maxSize / 1024 / 1024)}MB</p>
      </div>

      {files.length > 0 && (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          {files.map((file, idx) => (
            <div key={idx} className="flex items-center justify-between p-3 bg-muted/30 border border-border rounded-lg">
              <div className="flex items-center gap-3 overflow-hidden">
                <File className="w-5 h-5 text-muted-foreground flex-shrink-0" />
                <div className="truncate">
                  <p className="text-sm font-medium truncate">{file.name}</p>
                  <p className="text-xs text-muted-foreground">{(file.size / 1024).toFixed(1)} KB</p>
                </div>
              </div>
              <button onClick={(e) => { e.stopPropagation(); removeFile(idx); }} className="p-1 hover:bg-muted rounded-md text-muted-foreground hover:text-destructive">
                <X className="w-4 h-4" />
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
