package com.qa.gist.pages;

import com.qa.gist.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class NewGistPage extends BasePage {
    private final By description = By.cssSelector("input[name='gist[description]']");
    private final By fileName = By.cssSelector("input[name='gist[contents][][name]']");
    private final By editor = By.cssSelector(".CodeMirror, .cm-content");


    private final By[] arrowCandidates = {
            By.xpath("//button[contains(normalize-space(.),'Create')]/following::summary[1]"),
            By.xpath("//summary[contains(@class,'btn-primary')]"),
            By.xpath("//summary[.//*[contains(@class,'octicon-triangle-down')]]"),
            By.xpath("//button[@aria-haspopup='true' and contains(@class,'btn-primary')]")
    };

    private final By publicOption = By.xpath(
            "//*[contains(normalize-space(text()),'public gist') or contains(normalize-space(text()),'Public')]");

    private final By createPublicBtn = By.xpath(
            "//button[@type='submit' and contains(normalize-space(.),'Create public gist')]");

    public NewGistPage(WebDriver driver) {
        super(driver);
    }

    public NewGistPage open() {
        driver.get(ConfigReader.get("base.url"));
        waitVisible(description);
        return this;
    }

    public NewGistPage fillForm(String desc, String file, String content) {
        type(description, desc);
        type(fileName, file);
        setEditorContent(editor, content);
        return this;
    }


    private void selectPublicVisibility() {
        if (isDisplayedNow(createPublicBtn)) return; // sudah public

        boolean opened = false;
        for (By arrow : arrowCandidates) {
            List<WebElement> els = driver.findElements(arrow);
            if (!els.isEmpty() && els.get(0).isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(els.get(0))).click();
                opened = true;
                break;
            }
        }
        if (!opened) {
            throw new IllegalStateException("Tombol panah dropdown visibilitas tidak ditemukan. Perbaiki locator arrowCandidates.");
        }

        wait.until(d -> d.findElements(publicOption).stream().anyMatch(WebElement::isDisplayed));
        driver.findElements(publicOption).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow()
                .click();
    }

    public GistDetailPage submitPublic() {
        selectPublicVisibility();
        if (!driver.getCurrentUrl().matches(".*gist\\.github\\.com/[^/]+/[0-9a-f]{20,}.*")
                && isDisplayedNow(createPublicBtn)) {
            click(createPublicBtn);
        }
        return new GistDetailPage(driver);
    }

    public NewGistPage submitPublicExpectingFailure() {
        selectPublicVisibility();
        if (isDisplayedNow(createPublicBtn)) {
            click(createPublicBtn);
        }
        return this;
    }
}
