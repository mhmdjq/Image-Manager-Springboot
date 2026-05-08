import axios from 'axios';
import { ImageDetails } from '../types/image';

const API_BASE = 'http://localhost:8080/api/images';

export const imageApi = {
    // List all images for the gallery
    getImages: () => axios.get<ImageDetails[]>(API_BASE),
    
    // Initial Upload (Button 2 logic)
    uploadImage: (formData: FormData) => axios.post<ImageDetails>(`${API_BASE}/upload`, formData),
    
    // Button 1: Delete OG Image
    deleteImage: (id: string) => axios.delete(`${API_BASE}/${id}`),
    
    // Button 3: Add Text Overlay
    addOverlay: (id: string, text: string) => 
        axios.post(`${API_BASE}/${id}/overlays`, { overlayText: text }),
        
    // Button 3: Delete specific overlay from the nested list
    deleteOverlay: (overlayId: string) => 
        axios.delete(`${API_BASE}/overlays/${overlayId}`),
    
    // Button 4: Update Title/Desc
    updateMetadata: (id: string, title: string, description: string) =>
        axios.patch<ImageDetails>(`${API_BASE}/${id}`, { title, description })
};