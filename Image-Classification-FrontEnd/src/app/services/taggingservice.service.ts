import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Tagger } from '../Tagger';

@Injectable({
  providedIn: 'root'
})
export class TaggingserviceService {
  private apiUrl = "http://localhost:8080/tagger_services";

  constructor(private http: HttpClient) { }

  getAllTaggingServices(): Observable<Tagger[]>  {
    return this.http.get<Tagger[]>(this.apiUrl);
  }
}
