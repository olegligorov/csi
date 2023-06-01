export interface Image {
    id: number;
    url: string;
    checksum: string;
    imageContent: any;
    // imagePath: string;
    analysedAt: Date;
    analysedByService: any;
    tags: any;
    width: number;
    height: number;
}