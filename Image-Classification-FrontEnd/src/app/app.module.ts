import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ClarityModule } from "@clr/angular";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SubmitPageComponent } from './components/submit-page/submit-page.component';
import { ImagePageComponent } from './components/image-page/image-page.component';

const appRoutes: Routes = [
  {path: '', component: SubmitPageComponent},
  {path: 'images/:imageId', component: ImagePageComponent},
]

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SubmitPageComponent,
    ImagePageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    ClarityModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
