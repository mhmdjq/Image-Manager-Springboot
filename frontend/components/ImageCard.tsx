'use client';

import React, { useState } from 'react';
import { Trash2, Edit3, ExternalLink, Plus } from 'lucide-react';
import { ImageDetails } from '../types/image';
import { imageApi } from '../services/api';

interface Props {
  image: ImageDetails;
  onRefresh: () => void;
}

export default function ImageCard({ image, onRefresh }: Props) {
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState(image.title);
  const [editDesc, setEditDesc] = useState(image.description);
  const [overlayText, setOverlayText] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleUpdate = async () => {
    await imageApi.updateMetadata(image.id, editTitle, editDesc);
    setIsEditing(false);
    onRefresh();
  };

  const handleAddOverlay = async () => {
    setError(null);
    try {
      await imageApi.addOverlay(image.id, overlayText);
      setOverlayText('');
      onRefresh();
    } catch (err: any) {
      setError(err.response?.data?.message || "Text too long!");
      setTimeout(() => setError(null), 10000); // Hide error after 10 seconds
    }
  };

  return (
    <div className="bg-slate-900/40 backdrop-blur-xl rounded-[2.5rem] overflow-hidden border border-white/5 shadow-2xl flex flex-col h-full group/card transition-all duration-300 hover:border-blue-500/30">
      
      {/* 1. Main Image - Buttons now hidden until "touched" (hovered) */}
      <div className="relative aspect-[4/5] overflow-hidden">
        <img 
          src={`http://localhost:8080${image.imageUrl}`} 
          className="w-full h-full object-cover transition-transform duration-700 group-hover/card:scale-105" 
          alt={image.title} 
        />
        
        {/* Action Buttons: Opacity-0 by default, shows on group-hover */}
        <div className="absolute top-6 right-6 flex flex-col gap-3 opacity-0 translate-x-4 group-hover/card:opacity-100 group-hover/card:translate-x-0 transition-all duration-300 ease-out">
          <button 
            onClick={() => setIsEditing(!isEditing)}
            className="p-3 bg-white/10 backdrop-blur-md hover:bg-orange-400 rounded-2xl border border-white/10 shadow-xl text-white transition-colors"
          >
            <Edit3 size={30} />
          </button>
          <button 
            onClick={() => confirm('Delete?') && imageApi.deleteImage(image.id).then(onRefresh)}
            className="p-3 bg-white/10 backdrop-blur-md hover:bg-red-900 rounded-2xl border border-white/10 shadow-xl text-white transition-colors"
          >
            <Trash2 size={30} />
          </button>
        </div>
      </div>

      <div className="p-8 space-y-6">
        {/* 2. Metadata Section */}
        {isEditing ? (
          <div className="space-y-3 animate-in fade-in zoom-in-95">
            <input className="w-full bg-slate-800/50 border border-slate-700 rounded-2xl p-3 text-white focus:outline-none focus:border-blue-500" value={editTitle} onChange={(e) => setEditTitle(e.target.value)} />
            <textarea className="w-full bg-slate-800/50 border border-slate-700 rounded-2xl p-3 text-white focus:outline-none focus:border-blue-500 h-24" value={editDesc} onChange={(e) => setEditDesc(e.target.value)} />
            <button onClick={handleUpdate} className="w-full bg-blue-600 hover:bg-blue-500 font-bold py-3 rounded-2xl transition-colors">SAVE CHANGES</button>
          </div>
        ) : (
          <div className="min-h-[80px]">
            <h3 className="text-2xl font-bold text-white tracking-tight leading-tight">{image.title}</h3>
            <p className="text-slate-400 text-sm mt-2 line-clamp-2">{image.description}</p>
          </div>
        )}

        {/* 3. Tech Specs */}
        <div className="flex justify-between border-t border-white/5 pt-6 text-[10px] tracking-[0.2em] font-black text-slate-500">
          <div>DIMENSIONS <span className="block text-blue-400 text-xs mt-1 font-mono tracking-normal">{image.dimensions}</span></div>
          <div className="text-right">FILE SIZE <span className="block text-blue-400 text-xs mt-1 font-mono tracking-normal">{image.fileSizeFormatted}</span></div>
        </div>

        {/* 4. Versions Section */}
        <div className="space-y-4 pt-2">
          <div className="flex items-center justify-between gap-4">
            <span className="text-[10px] font-black text-slate-500 tracking-widest">VERSIONS</span>
            <div className="flex flex-col flex-1 gap-2">
              {error && (
                <p className="text-[10px] font-bold text-red-500 animate-pulse uppercase tracking-widest">
                  ⚠️ {error}
                </p>
              )}
              <div className="flex items-center bg-slate-800/50 rounded-xl border border-white/5 pr-1">
                <input 
                  className="bg-transparent border-none text-xs px-3 py-2 w-full text-white focus:ring-0 placeholder:text-slate-600" 
                  placeholder="New overlay text..." 
                  value={overlayText}
                  onChange={(e) => setOverlayText(e.target.value)}
                />
                <button 
                  onClick={handleAddOverlay}
                  className="bg-green-600 p-1.5 rounded-lg hover:bg-green-500"
                >
                  <Plus size={24} className="text-white" />
                </button>
              </div>
            </div>
          </div>

          {/* 5. The Scrollable Bar - Styled with tailwind-scrollbar */}
          <div className="flex gap-4 overflow-x-auto pb-4 versions-scrollbar">
            {image.overlays.map((ov) => (
              <div key={ov.id} className="group/version relative flex-shrink-0 w-28 h-28 rounded-2xl overflow-hidden border border-white/5 bg-slate-800 shadow-lg">
                <img src={`http://localhost:8080${ov.imageUrl}`} className="w-full h-full object-cover transition-opacity duration-300 group-hover/version:opacity-30" />
                <div className="absolute inset-0 flex items-center justify-center gap-3 opacity-0 group-hover/version:opacity-100 transition-all duration-300">
                  <a href={`http://localhost:8080${ov.imageUrl}`} target="_blank" className="p-2 bg-blue-600 rounded-xl hover:bg-blue-500 shadow-lg"><ExternalLink size={16} /></a>
                  <button onClick={() => imageApi.deleteOverlay(ov.id).then(onRefresh)} className="p-2 bg-red-600 rounded-xl hover:bg-red-500 shadow-lg"><Trash2 size={16} /></button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}