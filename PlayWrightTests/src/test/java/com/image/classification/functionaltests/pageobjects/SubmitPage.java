package com.image.classification.functionaltests.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.util.regex.Pattern;

public class SubmitPage {
    private static final String PATH = "http://localhost:4200/";
    private Locator searchBarLocator;
    private Locator cacheCheckboxLocator;
    private Locator submitButton;
    private Page page;
    //    Locator submitButton = currPage.locator("button#submit-button");

    public SubmitPage(Page page) {
        this.page = page;
        this.searchBarLocator = page.locator("input#searchbar");
        this.cacheCheckboxLocator = page.locator("input#cache_checkbox");
        this.submitButton = page.locator("button#submit-button");
    }

    public void navigate() {
        this.page.navigate(PATH);
    }

    public void analyseImage(String imageUrl) {
        searchBarLocator.type(imageUrl);
        cacheCheckboxLocator.click();
        submitButton.click();
    }

    public void assertImageWasAnalysed() {
        Pattern pattern = Pattern.compile("http://localhost:4200/images/.*");
        PlaywrightAssertions.assertThat(page).hasURL(pattern);
        Locator image = page.locator("#main-image");
        PlaywrightAssertions.assertThat(image).isVisible();
    }

    public void clickFirstTagToRedirectToGallery() {
        Locator firstTag = page.locator(".image-page-tag:first-of-type a");
        firstTag.click();
        Pattern pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+");
        PlaywrightAssertions.assertThat(page).hasURL(pattern);
    }

}
