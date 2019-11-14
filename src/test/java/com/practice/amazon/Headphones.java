package com.practice.amazon;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Slf4j
public class Headphones {
    String url;
    WebDriver driver;

    @Before
    public void setUp() throws Exception {
        url="https://www.amazon.com";
        System.setProperty("webdriver.chrome.driver", "src/test/resources/BrowserDrivers/ChromeDriverMac");
        driver = new ChromeDriver();
        driver.get(url);
    }

    /**
     *  open amazon.com
     *  Enter the text “com.amazon.Headphones” in the search box. Hit enter
     *  From the results displayed on page1 add all the items marked as “Best seller” to the cart.
     */
    @Test
    public void test() {
        driver.findElement(By.xpath("//*[@id=\"twotabsearchtextbox\"]")).sendKeys("Headphones" + Keys.ENTER);
        WebDriverWait wait = new WebDriverWait(driver, 20);
        List<WebElement> bestSellers = driver.findElements(
                By.xpath("//span[text()='Best Seller']" +
                        "/ancestor::div[@data-asin and not(.//span[.='Sponsored'])][1]" +
                        "//span[@data-component-type='s-product-image']//a"));
        List<String> bestSellersLinks = bestSellers.stream()
                .map(element -> element.getAttribute("href")).collect(Collectors.toList());

        bestSellersLinks.forEach(link -> {
            driver.get(link);
            wait.until(elementToBeClickable(By.id("add-to-cart-button"))).click();
            wait.until(or(
                    visibilityOfElementLocated(By.className("success-message")),
                    visibilityOfElementLocated(By.xpath("//div[@id='attachDisplayAddBaseAlert']//h4[normalize-space(.)='Added to Cart']")),
                    visibilityOfElementLocated(By.xpath("//h1[normalize-space(.)='Added to Cart']"))
            ));
        });


    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
