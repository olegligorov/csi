# Image Classification Service
Final project for the VMware Talent Boost academy. 

Used technologies: Spring Boot, MySQL, Angular

The Image Classification Service allows users to submit url and using external API's returns the image's tags. There is also a gallery where all the analysed images are shown.

For testing the backend i used Junit and RestAssured.
For the UI Tests i used PlayWright.

CI/CD Github Actions that run on each commit

## Backend Main Endpoints:
- GET /images :  
    - With optional parameter: tags (Collection of strings)
    - Optional parameter: order (String, asc or desc)  
    - Optional parameter: pageNumber: (int, default=0)  
    - Optional parameter: pageSize: (int, default=20)

- Post /images with ImageDAO entity that has the imageUrl
    - Optional parameter: noCache (if noCache=true then the service will analyse image again although it was already analysed)

- Get /images/{imageId} - returns the image if image with the given id exists, else it returns status code: 404 - Image not found  
- Get /tagger_services - returns the list of used external tagging services
- Get /tags
    - Optional parameter: prefix (returns all the tags that start with the given prefix, if prefix is empty then it returns all the tags)

## Front-End Main Endpoints:
- / - Main page, Image Analysing page where we can insert the url
- /images - Gallery page
- /images/:imageId - Image page for the image with the given imageId
- /tagging_services - Tagging Services page where all the used tagging services are shown with the number of used and left requests 
- /not_found - Image Not Found page to which we are redirected when we try to open the image page with an imageId that doesn't exist  

## Dependencies 
Run in the terminal:  
'npm install @clr/ui@15.1.0 @clr/angular@15.1.0 @cds/core@6.3.1 @cds/angular@6.3.1 @cds/city@1.1.0--legacy-peer-deps'
