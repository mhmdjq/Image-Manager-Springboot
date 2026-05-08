export interface Overlay {
    id: string;
    overlayText: string;
    imageUrl: string;
    createdAt: string;
}

export interface ImageDetails {
    id: string;
    title: string;
    description: string;
    originalFileName: string;
    format: string;
    dimensions: string;
    fileSizeFormatted: string;
    imageUrl: string;
    overlayCount: number;
    overlays: Overlay[];
    createdAt: string;
}