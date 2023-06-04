package com.image.classification.functionaltests;

import com.image.classification.functionaltests.pageobjects.GalleryPage;
import com.image.classification.functionaltests.pageobjects.SubmitPage;
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

        SubmitPage submitPage = new SubmitPage(currPage);
        submitPage.navigate();
        submitPage.analyseImage("https://i.pinimg.com/564x/86/49/f5/8649f5cbfc573b1c5d7f190dcf52b96c.jpg");
        submitPage.assertImageWasAnalysed();

        submitPage.clickFirstTagToRedirectToGallery();

        GalleryPage galleryPage = new GalleryPage(currPage);
        galleryPage.filterBySecondAndThirdTags();
        galleryPage.clearTags();
        galleryPage.clickTheFirstShownImage();
    }
}
