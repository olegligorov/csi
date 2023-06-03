package com.image.classification.functionaltests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.regex.Pattern;

public class E2EUITests {
    private static Browser browser;
    BrowserContext context;

    @BeforeAll
    public static void setUp() {
        var playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
//                new BrowserType.LaunchOptions().setHeadless(true)
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

    /**
     * open the main page
     * enter an image URL
     * select the checkbox
     * submit the image for analysis
     * after analysing the image we should be redirected to a new page with
     * endpoint image/:imageId where we can view the image and the image's tags
     * click the first tag, should redirect us to images/tags?*tagname*
     * verify that the image is present in the shown images
     * add more tags from the recommended ones and verify that it is searching for them
     * clear the tags
     * click on the first image and verify if it will redirect to the image page
     */
    @Test
    public void testSubmitUrlWithCacheRedirectsToPhotoPage() {
//        open the main page
        Page currPage = context.newPage();
        currPage.navigate("http://localhost:4200/");
// enter image URL and check the checkbox
        Locator searchBarLocator = currPage.locator("input#searchbar");
        searchBarLocator.type("https://i.pinimg.com/564x/86/49/f5/8649f5cbfc573b1c5d7f190dcf52b96c.jpg");
        Locator cacheCheckboxLocator = currPage.locator("input#cache_checkbox");
//      TODO uncomment it
//        cacheCheckboxLocator.click();

// submit the image for analysis
        Locator submitButton = currPage.locator("button#submit-button");
        submitButton.click();
// assert that we are redirected correctly
        Pattern pattern = Pattern.compile("http://localhost:4200/images/.*");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);
// assert that the image is present
        Locator image = currPage.locator("#main-image");
        PlaywrightAssertions.assertThat(image).isVisible();
// click the first tag
        Locator firstTag = currPage.locator(".image-page-tag:first-of-type a");
        firstTag.click();
// assert that we are redirected correctly to the gallery page
        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);

//  assert that images are shown when searching by tags
        image = currPage.locator(".gallery-card:first-of-type img");
        PlaywrightAssertions.assertThat(image).isVisible();

//  click on the second and third tag in the recommended tags
        Locator secondTag = currPage.locator(".form-recommended-tags > span:nth-of-type(3)");
        Locator thirdTag = currPage.locator(".form-recommended-tags > span:nth-of-type(4)");
        secondTag.click();
        thirdTag.click();

// assert that the endpoint changed and is searching for tags

        pattern = Pattern.compile("http://localhost:4200/images\\?tags=.+,.+,.+");
        PlaywrightAssertions.assertThat(currPage).hasURL(pattern);
    }
}
