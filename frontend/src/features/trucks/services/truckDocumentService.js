import apiClient from '@/api/axios';

export const truckDocumentService = {
  uploadDocument: async (truckId, documentType, file) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentType', documentType);
    
    const response = await apiClient.post(`/v1/trucks/${truckId}/documents`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  },
  getDocuments: async (truckId) => {
    const response = await apiClient.get(`/v1/trucks/${truckId}/documents`);
    return response.data;
  },
  deleteDocument: async (truckId, documentId) => {
    const response = await apiClient.delete(`/v1/trucks/${truckId}/documents/${documentId}`);
    return response.data;
  }
};
