export interface Image {
    id: number;
    url: string;
    checksum: string;
    imagePath: string;
    analysedAt: Date;
    analysedByService: any;
    tags: any;
    width: number;
    height: number;
}