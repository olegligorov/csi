import { Component, Input } from '@angular/core';
import { Image } from 'src/app/Image';

@Component({
  selector: 'app-gallery-page-container',
  templateUrl: './gallery-page-container.component.html',
  styleUrls: ['./gallery-page-container.component.scss']
})
export class GalleryPageContainerComponent {
  @Input() images!: Image[];

  getImageUrl(imageBytes: any): string {
    const base64 = btoa(String.fromCharCode.apply(null, imageBytes));
    return `data:image/png;base64,${base64}`;
  }
}
