'use client';

import React, { useState, useEffect } from 'react';
import { imageApi } from '../services/api';

export default function UploadForm({ onUploadSuccess }: { onUploadSuccess: () => void }) {
  const [file, setFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  // Handle Image Preview
  useEffect(() => {
    if (!file) {
      setPreview(null);
      return;
    }
    const objectUrl = URL.createObjectURL(file);
    setPreview(objectUrl);
    return () => URL.revokeObjectURL(objectUrl);
  }, [file]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', title);
    formData.append('description', description);

    await imageApi.uploadImage(formData);
    setFile(null); setTitle(''); setDescription('');
    onUploadSuccess();
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="space-y-4">
        {/* Image Preview Area */}
        <div className="w-full aspect-square rounded-2xl border-2 border-dashed border-slate-700 overflow-hidden bg-slate-800/30 flex items-center justify-center relative">
          {preview ? (
            <img src={preview} className="w-full h-full object-cover" alt="Preview" />
          ) : (
            <span className="text-slate-600 text-xs font-bold uppercase">No Image Selected</span>
          )}
        </div>

        {/* Secondary Button: Choose File */}
        <label className="block w-full bg-green-700 hover:bg-green-500 text-white text-center py-3 rounded-xl cursor-pointer font-bold text-sm transition-colors border border-white/5">
          CHOOSE IMAGE
          <input type="file" className="hidden" onChange={(e) => setFile(e.target.files?.[0] || null)} required />
        </label>
      </div>

      <div className="space-y-3">
        <input 
          type="text" placeholder="Title" 
          className="w-full bg-slate-900 border border-white/5 rounded-xl p-3 text-sm text-white focus:outline-none focus:border-blue-500"
          value={title} onChange={(e) => setTitle(e.target.value)} required
        />
        <textarea 
          placeholder="Description" 
          className="w-full bg-slate-900 border border-white/5 rounded-xl p-3 text-sm text-white focus:outline-none focus:border-blue-500 h-24"
          value={description} onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      {/* Primary Button: Publish */}
      <button 
        type="submit" 
        className="w-full bg-blue-600 hover:bg-blue-500 text-white font-black py-4 rounded-xl shadow-lg shadow-blue-500/20 transition-all active:scale-95"
      >
        PUBLISH IMAGE
      </button>
    </form>
  );
}