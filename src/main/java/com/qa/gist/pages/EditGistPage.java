package com.qa.gist.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EditGistPage extends BasePage {
    private final By description = By.cssSelector("input[name='gist[description]']");
    private final By editor = By.cssSelector(".cm-content, .CodeMirror");
    private final By updateButton = By.xpath("//button[contains(normalize-space(.),'Update')]");

    public EditGistPage(WebDriver driver) {
        super(driver);
    }

    public EditGistPage updateDescription(String newDesc) {
        type(description, newDesc);
        return this;
    }

    public EditGistPage updateContent(String newContent) {
        setEditorContent(editor, newContent);
        return this;
    }

    public GistDetailPage submitUpdate() {
        click(updateButton);
        return new GistDetailPage(driver);
    }
}
