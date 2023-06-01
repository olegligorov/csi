import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Image } from 'src/app/Image';
import { ImageService } from 'src/app/services/image.service';

@Component({
  selector: 'app-image-page',
  templateUrl: './image-page.component.html',
  styleUrls: ['./image-page.component.scss']
})
export class ImagePageComponent implements OnInit {

  constructor(private route: ActivatedRoute, private imageService: ImageService) {}
  image!: Image;

  ngOnInit(): void {
    const imageId = this.route.snapshot.paramMap.get('imageId');
    this.imageService.getImage(imageId).subscribe((image) => {
      this.image = image;
      console.log(image)
    });
  }

  public toFloat(value: string): number {
    return parseFloat(value);
 }

}
