package com.image.classification.functionaltests.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.util.regex.Pattern;

public class GalleryPage {
    private static final String PATH = "http://localhost:4200/images";
    private Locator image;
    private Locator secondTag;
    private Locator thirdTag;
    private Locator clearTags;
    private Locator firstSelectedTag;
    private Locator firstShownImage;
    private Page page;
    //    Locator submitButton = currPage.locator("button#submit-button");

    public GalleryPage(Page page) {
        this.page = page;
        this.image = page.locator(".gallery-card:first-of-type img");
        this.secondTag = page.locator(".form-recommended-tags > span:nth-of-type(3)");
        this.thirdTag = page.locator(".form-recommended-tags > span:nth-of-type(4)");
        this.clearTags = page.locator("#clear-tags");

    }

    public void navigate() {
        this.page.navigate(PATH);
    }

    public void filterBySecondAndThirdTags() {
        secondTag.click();
        thirdTag.click();
        Pattern pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+,.+,.+");
        PlaywrightAssertions.assertThat(page).hasURL(pattern);
    }

    public void clearTags() {
        clearTags.click();
        Pattern pattern = Pattern.compile("http://localhost:4200/images[\\?order=.*]");
        PlaywrightAssertions.assertThat(page).hasURL(pattern);

        this.firstSelectedTag = page.locator(".selected-tag:nth-of-type(1)");
        PlaywrightAssertions.assertThat(firstSelectedTag).not().isVisible();
    }

    public void clickTheFirstShownImage() {
        this.firstShownImage = page.locator(".gallery-card:first-of-type img");
        firstShownImage.click();

        Pattern pattern = Pattern.compile("http://localhost:4200/images/.*");
        PlaywrightAssertions.assertThat(page).hasURL(pattern);

        Locator image = page.locator("#main-image");
        PlaywrightAssertions.assertThat(image).isVisible();
    }
}
