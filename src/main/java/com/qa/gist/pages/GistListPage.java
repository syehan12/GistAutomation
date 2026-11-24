package com.qa.gist.pages;

import com.qa.gist.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GistListPage extends BasePage {
    private final By gistItems = By.cssSelector(".gist-snippet");

    public GistListPage(WebDriver driver) {
        super(driver);
    }

    public GistListPage openMyGists(String username) {
        driver.get(ConfigReader.get("base.url") + "/" + username);
        return this;
    }

    public GistListPage openDiscover() {
        driver.get(ConfigReader.get("base.url") + "/discover");
        waitVisible(gistItems);
        return this;
    }

    public List<WebElement> getItems() {
        return driver.findElements(gistItems);
    }

    public int getItemCount() {
        return getItems().size();
    }

    public boolean containsGistWithText(String text) {
        return getItems().stream().anyMatch(e -> e.getText().contains(text));
    }
}
