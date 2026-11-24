package com.qa.gist.pages;

import com.qa.gist.utils.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("timeout.seconds", 15)));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.click();
        el.sendKeys(Keys.chord(modifierKey(), "a"));
        el.sendKeys(Keys.BACK_SPACE);
        el.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitVisible(locator).getText().trim();
    }

    protected boolean isDisplayedNow(By locator) {
        List<WebElement> els = driver.findElements(locator);
        return !els.isEmpty() && els.get(0).isDisplayed();
    }

    protected Keys modifierKey() {
        return System.getProperty("os.name").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL;
    }

    protected void setEditorContent(By editorLocator, String content) {
        WebElement editor = wait.until(ExpectedConditions.presenceOfElementLocated(editorLocator));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", editor);

        Boolean isCm5 = (Boolean) js.executeScript("return !!arguments[0].CodeMirror;", editor);
        if (Boolean.TRUE.equals(isCm5)) {
            js.executeScript("arguments[0].CodeMirror.setValue(arguments[1]);", editor, content);
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(editor)).click();
            editor.sendKeys(Keys.chord(modifierKey(), "a"));
            editor.sendKeys(Keys.BACK_SPACE);
            editor.sendKeys(content);
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
