'use client';

import { useEffect, useState } from 'react';
import { imageApi } from '../services/api';
import { ImageDetails } from '../types/image';
import ImageCard from '../components/ImageCard';
import UploadForm from '../components/UploadForm';

export default function GalleryPage() {
  const [images, setImages] = useState<ImageDetails[]>([]);

  const fetchImages = async () => {
    try {
      const response = await imageApi.getImages();
      setImages(response.data);
    } catch (error) {
      console.error("Connection failed", error);
    }
  };

  useEffect(() => { fetchImages(); }, []);

  return (
    <main className="flex min-h-screen bg-[#020617]">
      {/* Static Sidebar */}
      <aside className="w-96 border-r border-white/5 p-8 bg-slate-900/20 h-screen sticky top-0 overflow-y-auto">        
        <UploadForm onUploadSuccess={fetchImages} />
      </aside>

      {/* 3-Column Content Area */}
      <section className="flex-1 p-12">
        <div className="grid grid-cols-3 gap-8">
          {images.map((img) => (
            <ImageCard 
              key={img.id} 
              image={img} 
              onRefresh={fetchImages} 
            />
          ))}
        </div>
      </section>
    </main>
  );
}