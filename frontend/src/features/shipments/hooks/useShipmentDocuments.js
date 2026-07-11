import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { shipmentDocumentService } from '../services/shipmentDocumentService';
import toast from 'react-hot-toast';

export const useShipmentDocuments = (shipmentId) => {
  return useQuery({
    queryKey: ['shipmentDocuments', shipmentId],
    queryFn: () => shipmentDocumentService.getDocuments(shipmentId),
    enabled: !!shipmentId,
  });
};

export const useUploadDocument = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ shipmentId, documentType, file }) => shipmentDocumentService.uploadDocument(shipmentId, documentType, file),
    onSuccess: (_, { shipmentId }) => {
      toast.success('Document uploaded successfully');
      queryClient.invalidateQueries({ queryKey: ['shipmentDocuments', shipmentId] });
    },
    onError: () => toast.error('Failed to upload document')
  });
};

export const useDeleteDocument = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ shipmentId, documentId }) => shipmentDocumentService.deleteDocument(shipmentId, documentId),
    onSuccess: (_, { shipmentId }) => {
      toast.success('Document deleted');
      queryClient.invalidateQueries({ queryKey: ['shipmentDocuments', shipmentId] });
    },
    onError: () => toast.error('Failed to delete document')
  });
};
