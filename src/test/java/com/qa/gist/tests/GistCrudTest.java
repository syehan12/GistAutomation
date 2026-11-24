package com.qa.gist.tests;

import com.qa.gist.base.BaseTest;
import com.qa.gist.pages.EditGistPage;
import com.qa.gist.pages.GistDetailPage;
import com.qa.gist.pages.GistListPage;
import com.qa.gist.pages.NewGistPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class GistCrudTest extends BaseTest {

    private static final String STAMP = String.valueOf(System.currentTimeMillis());
    private static final String DESC = "Auto Gist " + STAMP;
    private static final String FILE = "autotest_" + STAMP + ".txt";
    private static final String CONTENT = "Hello from Selenium " + STAMP;
    private static final String DESC_UPDATED = DESC + " (updated)";
    private static final String CONTENT_UPDATED = "Updated content " + STAMP;

    private String gistUrl;

    // TC-UI-01 : Create Public Gist (positive)
    @Test(priority = 1, description = "TC-UI-01 Create public gist")
    public void createPublicGist() {
        GistDetailPage detail = new NewGistPage(driver)
                .open()
                .fillForm(DESC, FILE, CONTENT)
                .submitPublic()
                .waitUntilLoaded();

        gistUrl = driver.getCurrentUrl();

        Assert.assertEquals(detail.getFileName(), FILE, "Nama file tidak sesuai");
        Assert.assertEquals(detail.getDescription(), DESC, "Deskripsi tidak sesuai");
        Assert.assertTrue(detail.getContent().contains(CONTENT), "Konten gist tidak sesuai");
    }

    // TC-UI-02 : Create gist tanpa konten (negative)
    @Test(priority = 2, description = "TC-UI-02 Create gist tanpa konten harus gagal")
    public void createGistWithoutContentShouldFail() {
        NewGistPage page = new NewGistPage(driver).open();
        page.submitPublicExpectingFailure();

        Assert.assertFalse(driver.getCurrentUrl().matches(".*gist\\.github\\.com/[^/]+/[0-9a-f]{20,}.*"),
                "Gist kosong seharusnya tidak berhasil dibuat");
    }

    // TC-UI-03 : See List of Gists (read)
    @Test(priority = 3, dependsOnMethods = "createPublicGist", description = "TC-UI-03 See list of gists")
    public void seeListOfGists() {
        GistListPage list = new GistListPage(driver).openMyGists(username);

        Assert.assertTrue(list.getItemCount() > 0, "Daftar gist kosong");
        Assert.assertTrue(list.containsGistWithText(FILE),
                "Gist yang baru dibuat tidak muncul di daftar");
    }

    // TC-UI-04 : See List of Gists (discover, publik)
    @Test(priority = 4, description = "TC-UI-04 See list of public gists (discover)")
    public void seeDiscoverGists() {
        GistListPage list = new GistListPage(driver).openDiscover();
        Assert.assertTrue(list.getItemCount() > 0, "Daftar gist discover kosong");
    }

    // TC-UI-05 : Edit Existing Gist
    @Test(priority = 5, dependsOnMethods = "createPublicGist", description = "TC-UI-05 Edit existing gist")
    public void editExistingGist() {
        GistDetailPage detail = new GistDetailPage(driver).openByUrl(gistUrl);
        EditGistPage edit = detail.clickEdit();

        GistDetailPage updated = edit
                .updateDescription(DESC_UPDATED)
                .updateContent(CONTENT_UPDATED)
                .submitUpdate()
                .waitUntilLoaded();

        Assert.assertEquals(updated.getDescription(), DESC_UPDATED, "Deskripsi tidak ter-update");
        Assert.assertTrue(updated.getContent().contains(CONTENT_UPDATED), "Konten tidak ter-update");
    }

    // TC-UI-06 : Delete Existing Gist
    @Test(priority = 6, dependsOnMethods = "editExistingGist", description = "TC-UI-06 Delete existing gist")
    public void deleteExistingGist() {
        GistDetailPage detail = new GistDetailPage(driver).openByUrl(gistUrl);
        detail.clickDeleteAndConfirm();

        // Verifikasi 1: tidak ada di daftar
        GistListPage list = new GistListPage(driver).openMyGists(username);
        Assert.assertFalse(list.containsGistWithText(FILE), "Gist masih muncul di daftar setelah dihapus");

        // Verifikasi 2: URL gist menghasilkan 404
        driver.get(gistUrl);
        Assert.assertTrue(new GistDetailPage(driver).isNotFoundPage(), "URL gist seharusnya 404 setelah dihapus");
    }
}
