import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Image } from 'src/app/Image';
import { ImageService } from 'src/app/services/image.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-image-page',
  templateUrl: './image-page.component.html',
  styleUrls: ['./image-page.component.scss']
})
export class ImagePageComponent implements OnInit {

  constructor(private route: ActivatedRoute, private imageService: ImageService, private router: Router) { }
  image!: Image;

  ngOnInit(): void {
    const imageId = this.route.snapshot.paramMap.get('imageId');
    this.imageService.getImage(imageId).subscribe((image) => {
      this.image = image;
    }, error => {
        this.router.navigateByUrl(`not_found`)
    });
  }

  public toFloat(value: string): number {
    return parseFloat(value);
  }

}
