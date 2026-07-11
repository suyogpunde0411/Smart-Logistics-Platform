import apiClient from '@/api/axios';

export const shipmentDocumentService = {
  uploadDocument: async (shipmentId, documentType, file) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentType', documentType);
    
    const response = await apiClient.post(`/v1/shipments/${shipmentId}/documents`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  },
  getDocuments: async (shipmentId) => {
    const response = await apiClient.get(`/v1/shipments/${shipmentId}/documents`);
    return response.data;
  },
  deleteDocument: async (shipmentId, documentId) => {
    const response = await apiClient.delete(`/v1/shipments/${shipmentId}/documents/${documentId}`);
    return response.data;
  }
};
