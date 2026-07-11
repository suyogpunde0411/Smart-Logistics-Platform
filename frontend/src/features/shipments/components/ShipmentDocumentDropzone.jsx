import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { UploadCloud, File, X, CheckCircle } from 'lucide-react';
import { useUploadDocument, useDeleteDocument } from '../hooks/useShipmentDocuments';

export const ShipmentDocumentDropzone = ({ shipmentId, documentType, existingDocument }) => {
  const { mutate: uploadDoc, isPending: isUploading } = useUploadDocument();
  const { mutate: deleteDoc, isPending: isDeleting } = useDeleteDocument();

  const onDrop = useCallback((acceptedFiles) => {
    if (acceptedFiles?.length) {
      uploadDoc({ shipmentId, documentType, file: acceptedFiles[0] });
    }
  }, [shipmentId, documentType, uploadDoc]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'application/pdf': ['.pdf'],
      'image/jpeg': ['.jpg', '.jpeg'],
      'image/png': ['.png']
    },
    maxSize: 5242880, // 5MB
    multiple: false
  });

  if (existingDocument) {
    return (
      <div className="p-4 border rounded-xl flex items-center justify-between bg-card">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-green-500/10 text-green-600 rounded-lg flex items-center justify-center">
            <CheckCircle className="w-5 h-5" />
          </div>
          <div>
            <p className="font-medium text-sm">{documentType.replace('_', ' ')}</p>
            <p className="text-xs text-muted-foreground">Uploaded successfully</p>
          </div>
        </div>
        <button 
          onClick={() => deleteDoc({ shipmentId, documentId: existingDocument.id })}
          disabled={isDeleting}
          className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-md transition-colors"
        >
          <X className="w-4 h-4" />
        </button>
      </div>
    );
  }

  return (
    <div 
      {...getRootProps()} 
      className={`p-6 border-2 border-dashed rounded-xl flex flex-col items-center justify-center text-center cursor-pointer transition-colors ${
        isDragActive ? 'border-primary bg-primary/5' : 'border-border hover:bg-muted/50'
      }`}
    >
      <input {...getInputProps()} />
      <UploadCloud className={`w-8 h-8 mb-3 ${isDragActive ? 'text-primary' : 'text-muted-foreground'}`} />
      <p className="font-medium text-sm">
        {isUploading ? 'Uploading...' : `Upload ${documentType.replace('_', ' ')}`}
      </p>
      <p className="text-xs text-muted-foreground mt-1">PDF, JPG, PNG up to 5MB</p>
    </div>
  );
};
