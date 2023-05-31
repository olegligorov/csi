import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { Tag } from "../Tag";

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private apiUrl = "http://localhost:8080/tags";

  constructor(private http: HttpClient) { }

  getAllTags(prefix: string | null | undefined): Observable<Tag[]> {
    console.log("fetching tags");
    return this.http.get<Tag[]>(`${this.apiUrl}?prefix=${prefix}`);
  }
}
