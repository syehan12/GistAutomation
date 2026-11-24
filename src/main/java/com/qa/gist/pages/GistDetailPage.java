package com.qa.gist.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class GistDetailPage extends BasePage {
    private final By gistDescription = By.cssSelector("[itemprop='about']");
    private final By gistFileName = By.cssSelector(".gist-blob-name");
    private final By gistContent = By.cssSelector(".blob-code-inner, .js-file-line");
    private final By editLink = By.cssSelector("a.Button[href$='/edit']");
    private final By deleteButton = By.cssSelector("button.Button--danger[data-confirm]");

    public GistDetailPage(WebDriver driver) {
        super(driver);
    }

    public GistDetailPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlMatches(".*gist\\.github\\.com/[^/]+/[0-9a-f]{20,}.*"));
        waitVisible(gistFileName);
        return this;
    }

    public GistDetailPage openByUrl(String url) {
        driver.get(url);
        return waitUntilLoaded();
    }

    public String getDescription() { return getText(gistDescription); }

    public String getFileName() { return getText(gistFileName); }

    public String getContent() {
        waitVisible(gistContent);
        StringBuilder sb = new StringBuilder();
        driver.findElements(gistContent).forEach(e -> sb.append(e.getText()).append("\n"));
        return sb.toString().trim();
    }

    public EditGistPage clickEdit() {
        click(editLink);
        return new EditGistPage(driver);
    }

    public void clickDeleteAndConfirm() {
        click(deleteButton);
        try {
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (TimeoutException ignored) {
            By confirm = By.xpath("//dialog//button[contains(normalize-space(.),'Delete') or contains(normalize-space(.),'Confirm')]");
            if (isDisplayedNow(confirm)) click(confirm);
        }
    }

    public boolean isNotFoundPage() {
        String title = driver.getTitle().toLowerCase();
        return title.contains("404") || title.contains("not found");
    }
}
