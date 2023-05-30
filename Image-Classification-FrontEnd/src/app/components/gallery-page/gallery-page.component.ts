import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Image } from 'src/app/Image';
import { ImageService } from 'src/app/services/image.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-gallery-page',
  templateUrl: './gallery-page.component.html',
  styleUrls: ['./gallery-page.component.scss']
})
export class GalleryPageComponent {
  constructor(private route: ActivatedRoute, private imageService: ImageService, private router: Router) {

  }

  images !: Image[];
  tags: string | null | undefined;
  tagsSearch !: string | null;

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      this.tags = params.get('tags');
      
      this.tagsSearch = this.tags;
      
      this.imageService.getAllImages(this.tags).subscribe((images) => {
        this.images = images;
      });
    });
  }

  onSubmitSearchForTags(): void {
    if(this.tagsSearch) {
      this.router.navigateByUrl(`images?tags=${this.tagsSearch}`)
    } else {
      this.router.navigateByUrl('images')
    }
  }
}
