import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { truckDocumentService } from '../services/truckDocumentService';
import toast from 'react-hot-toast';

export const useTruckDocuments = (truckId) => {
  return useQuery({
    queryKey: ['truckDocuments', truckId],
    queryFn: () => truckDocumentService.getDocuments(truckId),
    enabled: !!truckId,
  });
};

export const useUploadDocument = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ truckId, documentType, file }) => truckDocumentService.uploadDocument(truckId, documentType, file),
    onSuccess: (_, { truckId }) => {
      toast.success('Document uploaded successfully');
      queryClient.invalidateQueries({ queryKey: ['truckDocuments', truckId] });
    },
    onError: () => toast.error('Failed to upload document')
  });
};

export const useDeleteDocument = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ truckId, documentId }) => truckDocumentService.deleteDocument(truckId, documentId),
    onSuccess: (_, { truckId }) => {
      toast.success('Document deleted');
      queryClient.invalidateQueries({ queryKey: ['truckDocuments', truckId] });
    },
    onError: () => toast.error('Failed to delete document')
  });
};
