import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Image } from 'src/app/Image';
import { ImageService } from 'src/app/services/image.service';
import { Router } from '@angular/router';
import { TagService } from 'src/app/services/tag.service';
import { Tag } from 'src/app/Tag';

@Component({
  selector: 'app-gallery-page',
  templateUrl: './gallery-page.component.html',
  styleUrls: ['./gallery-page.component.scss']
})
export class GalleryPageComponent {
  constructor(
    private route: ActivatedRoute,
    private imageService: ImageService,
    private tagService: TagService,
    private router: Router
  ) { }

  images !: Image[];
  tags: string | null | undefined;
  tagsSearch: string | null = "";
  recommendedTags !: Tag[];
  filterByTags : Set<string> = new Set<string>();

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      this.tags = params.get('tags');

      this.filterByTags = new Set(this.tags?.split(','));
      // this.tagsSearch = !this.tags ? '' : this.tags;

      this.imageService.getAllImages(this.tags).subscribe((images) => {
        this.images = images;
      });
    });

    this.getRecommendedTags('');

  }

  searchImages(): void {
    const searchForTags = [...this.filterByTags].join(',');

    if (searchForTags) {
      this.router.navigateByUrl(`images?tags=${searchForTags}`)
    } else {
      this.router.navigateByUrl('images')
    }
  }

  getRecommendedTags(prefix: string | null | undefined): void {
    this.tagService.getAllTags(prefix).subscribe(tags => {
      this.recommendedTags = tags;
      // console.log(this.recommendedTags);
    })
  }

  private searchTimeout: any;

  onTagsSearchChange(): void {
    clearTimeout(this.searchTimeout); // Clear any existing timeout

    this.searchTimeout = setTimeout(() => {
      console.log('Input value has not changed for 1 seconds');
      const last_tag = this.tagsSearch?.split(',').pop()?.trim();
      console.log(last_tag);
      this.getRecommendedTags(last_tag);
    }, 1000);
  }

  addTagToInput(tag: string | null): void {
    if (tag) {
      this.filterByTags.add(tag);
    }

    this.tagsSearch = '';
    this.getRecommendedTags(this.tagsSearch);

    // console.log("FILTER BY TAGS!");
    // console.log(this.filterByTags);

    this.searchImages();
  }

  deleteTag(tag: string): void {
    this.filterByTags.delete(tag);
    
    console.log("FILTER BY TAGS!");
    console.log(this.filterByTags);

    this.searchImages();
  }

  clearTags(): void {
    this.filterByTags.clear();

    this.searchImages();
  }

  addCustomTag() {
    console.log(this.tagsSearch);

    this.addTagToInput(this.tagsSearch);

    this.tagsSearch = '';
  }

}
