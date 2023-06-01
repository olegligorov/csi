export interface Tagger {
    id: number;
    imageTaggerName: string;
    currentRequests: number;
    taggerLimit: number;
    lastUsed: Date;
}