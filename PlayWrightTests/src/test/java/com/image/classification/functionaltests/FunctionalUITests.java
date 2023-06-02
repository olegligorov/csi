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

    private static Page currentPage;
    BrowserContext context;

    @BeforeAll
    public static void setUp() {
        var playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
        );
    }

    @BeforeEach
    void createContext() {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        context = browser.newContext();
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
        PlaywrightAssertions.assertThat(currPage).hasURL("http://localhost:4200/");

        Locator submitButton = currPage.locator("button#submit-button");
        submitButton.click();

        Pattern pattern = Pattern.compile("http://localhost:4200/images/.*");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);
    }

    @Test
    @Order(3)
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
    }

    @Test
    @Order(4)
    public void testFilteringByMultipleTagsInGalleryPage() {
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/images");

        Locator firstTag = currPage.locator(".form-recommended-tags > span:nth-of-type(2)");
        Locator secondTag = currPage.locator(".form-recommended-tags > span:nth-of-type(3)");
        Locator thirdTag = currPage.locator(".form-recommended-tags > span:nth-of-type(4)");

        firstTag.click();
        Pattern pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        secondTag.click();
        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+,.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        thirdTag.click();
        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+,.+,.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

        Locator searchingByText = currPage.locator("#showing-results-text");
        String expectedText = "Showing results for tags: " + firstTag.textContent() + "," + secondTag.textContent() + "," + thirdTag.textContent();
        PlaywrightAssertions.assertThat(searchingByText).containsText(expectedText);

        Locator clearTags = currPage.locator("#clear-tags");
        clearTags.click();
        pattern = Pattern.compile("http://localhost:4200/images[\\?order=.*]");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);
    }

    @Test
    @Order(5)
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
    }



}