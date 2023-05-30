import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { ImageUrl } from "../ImageUrl";
import { Image } from "../Image";

@Injectable({
  providedIn: 'root'
})

export class ImageService {
  private apiUrl = "http://localhost:8080/images";

  constructor(private http: HttpClient) { }

  addImage(imageUrl: ImageUrl, noCache: boolean): Observable<Image> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      params: new HttpParams().set("noCache", noCache)
    };
    
    return this.http.post<Image>(this.apiUrl, imageUrl, httpOptions);
  }

  getImage(imageId: string | null): Observable<Image> {
    const endpoint = `${this.apiUrl}/${imageId}`;
    return this.http.get<Image>(endpoint);
  }

  getAllImages(tags: string | null): Observable<Image[]> {
    if (tags) {
      return this.getAllImagesWithTags(tags);
    }
    return this.http.get<Image[]>(this.apiUrl);
  }

  getAllImagesWithTags(tags: string | null): Observable<Image[]> {
    const endpoint = `${this.apiUrl}?tags=${tags}`;
    return this.http.get<Image[]>(endpoint);
  }
}
