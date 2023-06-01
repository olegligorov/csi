import { Component } from '@angular/core';
import { TaggingserviceService } from 'src/app/services/taggingservice.service';
import { Tagger } from 'src/app/Tagger';

@Component({
  selector: 'app-tagging-service-page',
  templateUrl: './tagging-service-page.component.html',
  styleUrls: ['./tagging-service-page.component.scss']
})
export class TaggingServicePageComponent {
  taggingServices !: Tagger[]
  users: any = []

  constructor(private taggingService: TaggingserviceService) {}

  ngOnInit(): void {
    const user1 = {
      "id": 1,
      "name": "test",
      "color": "blue",
    }
    this.users.push(user1);


    this.taggingService.getAllTaggingServices().subscribe(result => {
      this.taggingServices = result;
    })
  }

  ceilNumber(num: number) {
    return Math.ceil(num);
  }
}
