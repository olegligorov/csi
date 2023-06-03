package com.image.classification.functionaltests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.nio.file.Paths;
import java.util.regex.Pattern;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionalUITests {
    private static Browser browser;
    BrowserContext context;

    @BeforeAll
    public static void setUp() {
        var playwright = Playwright.create();
        browser = playwright.chromium().launch(
//                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
                new BrowserType.LaunchOptions().setHeadless(true)
        );
    }

    @BeforeEach
    void createContext() {
//        context = browser.newContext();
        context = browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("test-videos/")));
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    @Order(1)
    public void testNavigationBar() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/");

        Locator analyseLink = currPage.locator(".nav-link").getByText("Analyse");
        Locator galleryLink = currPage.locator(".nav-link").getByText("Gallery");
        Locator taggingServicesLink = currPage.locator(".nav-link").getByText("Tagging Services");
        analyseLink.click();
        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/");
        galleryLink.click();
        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/images");
        taggingServicesLink.click();
        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/tagging_services");
    }

    @Test
    @Order(2)
    public void testSubmitUrlWithCacheRedirectsToPhotoPage() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/");

        Locator searchBarLocator = currPage.locator("input#searchbar");
        searchBarLocator.type("https://i.pinimg.com/564x/86/49/f5/8649f5cbfc573b1c5d7f190dcf52b96c.jpg");
        Locator cacheCheckboxLocator = currPage.locator("input#cache_checkbox");
        cacheCheckboxLocator.click();

        Locator submitButton = currPage.locator("button#submit-button");
        submitButton.click();

        Pattern pattern = Pattern.compile("http://localhost:4200/images/.*");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        Locator image = currPage.locator("#main-image");
        PlaywrightAssertions.assertThat(image).isVisible();
        currPage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("test-screenshots/imagePage.png")));
    }

    @Test
    @Order(3)
    public void testSubmitInvalidUrl() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/");

        Locator searchBarLocator = currPage.locator("input#searchbar");
        searchBarLocator.type("invalidimageurl");

        Locator submitButton = currPage.locator("button#submit-button");
        submitButton.click();

        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/");

        Locator errorText = currPage.locator("#error-message");
        PlaywrightAssertions.assertThat(errorText).isVisible();
        currPage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("test-screenshots/invalidImageSubmission.png")));
    }

    @Test
    @Order(4)
    public void testClickingOnAGalleryImageRedirectsToImage() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/images");

        Locator firstImage = currPage.locator(".gallery-card:first-of-type img");
        firstImage.click();
        Pattern pattern = Pattern.compile("http://localhost:4200/images/.*");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        Locator firstTag = currPage.locator(".image-page-tag:first-of-type a");
        firstTag.click();

        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        Locator image = currPage.locator(".gallery-card:first-of-type img");
        PlaywrightAssertions.assertThat(image).isVisible();
    }

    @Test
    @Order(5)
    public void testFilteringByMultipleTagsInGalleryPage() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/images");

        Locator firstTag = currPage.locator(".form-recommended-tags > span:nth-of-type(2)");
        Locator secondTag = currPage.locator(".form-recommended-tags > span:nth-of-type(3)");
        Locator thirdTag = currPage.locator(".form-recommended-tags > span:nth-of-type(4)");

        firstTag.click();
        Pattern pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        Locator selectedTagIsPresent = currPage.locator(".selected-tag:nth-of-type(1)");
        PlaywrightAssertions.assertThat(selectedTagIsPresent).isVisible();

        Locator image = currPage.locator(".gallery-card:first-of-type img");
        PlaywrightAssertions.assertThat(image).isVisible();

        secondTag.click();
        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+,.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        selectedTagIsPresent = currPage.locator(".selected-tag:nth-of-type(2)");
        PlaywrightAssertions.assertThat(selectedTagIsPresent).isVisible();

        thirdTag.click();
        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+,.+,.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        selectedTagIsPresent = currPage.locator(".selected-tag:nth-of-type(3)");
        PlaywrightAssertions.assertThat(selectedTagIsPresent).isVisible();

        Locator searchingByText = currPage.locator("#showing-results-text");
        String expectedText = "Showing results for tags: " + firstTag.textContent() + "," + secondTag.textContent() + "," + thirdTag.textContent();
        PlaywrightAssertions.assertThat(searchingByText).containsText(expectedText);

        currPage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("test-screenshots/searchingByMultipleTags.png")));

        Locator clearTags = currPage.locator("#clear-tags");
        clearTags.click();
        pattern = Pattern.compile("http://localhost:4200/images[\\?order=.*]");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        selectedTagIsPresent = currPage.locator(".selected-tag:nth-of-type(1)");
        PlaywrightAssertions.assertThat(selectedTagIsPresent).not().isVisible();
    }

    @Test
    @Order(6)
    public void testGallerySettingsSortAndPageSize() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/images");
        Locator sortBy = currPage.locator("#order");
        sortBy.click();
        sortBy.selectOption("desc");

        Locator pageSize = currPage.locator("#pageSize");
        pageSize.click();
        pageSize.selectOption("10");
        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/images?order=desc&pageSize=10");

        sortBy.click();
        sortBy.selectOption("asc");
        pageSize.click();
        pageSize.selectOption("20");
        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/images?order=asc&pageSize=20");

//        assert that there is at least one image shown that matches the tags
        Locator image = currPage.locator(".gallery-card:first-of-type img");
        PlaywrightAssertions.assertThat(image).isVisible();
    }

    @Test
    @Order(7)
    public void testCreatingCustomTags() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/images");
        Locator inputTags = currPage.locator("#submit-tags-search-input");
        String tagInput = "custominvalidtag";
        inputTags.type(tagInput);
        Locator addTagBtn = currPage.locator("#add-tag-btn");
        addTagBtn.click();

        Locator selectedTagIsPresent = currPage.locator(".selected-tag:first-of-type");
        PlaywrightAssertions.assertThat(selectedTagIsPresent).isVisible();

        tagInput = "customtag2invalid";
        inputTags.type(tagInput);
        addTagBtn.click();

        Locator image = currPage.locator(".gallery-card:first-of-type img");
        PlaywrightAssertions.assertThat(image).not().isVisible();

        selectedTagIsPresent = currPage.locator(".selected-tag:nth-of-type(2)");
        PlaywrightAssertions.assertThat(selectedTagIsPresent).isVisible();
    }

    @Test
    @Order(8)
    public void testViewingTaggingServices() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/tagging_services");

        Locator hasAtLeastOneTaggingService = currPage.locator("clr-dg-row:first-of-type");
        PlaywrightAssertions.assertThat(hasAtLeastOneTaggingService).isVisible();
    }

    @Test
    public void testOpeningInvalidImageId() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/images/-5");

        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/not_found");

        Locator noResultsFound = currPage.getByText("No results found");
        PlaywrightAssertions.assertThat(noResultsFound).isVisible();
        currPage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("test-screenshots/ImageNotFound404 .png")));
    }

}