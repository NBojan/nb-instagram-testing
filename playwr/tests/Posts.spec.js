const { test, expect } = require("@playwright/test");
const path = require("path");

test.use({
  storageState: "storage/user.json",
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});

const genCaption = () => {
    const number = Math.random() * 100;
    return number.toString();
}

test.skip("31-ValidateUploadingAPostByUsingAnImageAndACaption", async ({ page }) => {
    const caption = genCaption();
    const imageNr = Math.floor(Math.random() * 5) + 1;
    await page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]").click();
    const container = page.locator("aside#closeSidebar");
    await expect(container).toBeVisible();
    const fileChooserPromise = page.waitForEvent('filechooser');
    await page.locator("aside label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/post/${imageNr}.jpg`));
    await expect(page.getByAltText("selectedFile")).toBeVisible();
    await page.locator("input[name='caption']").fill(caption);
    await page.getByRole("button", { name: "upload post" }).click();
    await expect(container).toBeHidden();
    await expect(page.getByText(caption)).toBeVisible();
});


test("32-ValidateRemovingAnImageAfterChoosing", async ({ page }) => {
    const imageNr = Math.floor(Math.random() * 5) + 1;
    await page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]").click();
    const container = page.locator("aside#closeSidebar");
    await expect(container).toBeVisible();
    const fileChooserPromise = page.waitForEvent('filechooser');
    await page.locator("aside label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/post/${imageNr}.jpg`));
    await expect(page.getByAltText("selectedFile")).toBeVisible();
    await page.locator("aside div.group").hover();
    await page.locator("aside div.group").click();
    await expect(page.getByAltText("selectedFile")).toHaveCount(0);
});

test("33-ValidateUploadButtonIsDisabledIfNoImageSelected", async ({ page }) => {
    await page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]").click();
    await expect(page.locator("aside#closeSidebar")).toBeVisible();
    await expect(page.getByAltText("selectedFile")).toHaveCount(0);
    await expect(page.getByRole("button", { name: "upload post" })).toBeDisabled();
});

test("34-ValidateLinkToSwitchToProfilePictureUploadIsWorking", async ({ page }) => {
    await page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]").click();
    await expect(page.locator("aside#closeSidebar")).toBeVisible();
    await page.getByText('Upload your profile picture instead.').click();
    await expect(page.getByRole("button", { name: "upload profile picture" })).toBeVisible();
});

test("35-ValidateCloseButtonClosesTheUploadBox", async ({ page }) => {
    await page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]").click();
    await expect(page.locator("aside#closeSidebar")).toBeVisible();
    await page.locator("aside button.absolute").click();
    await expect(page.locator("aside#closeSidebar")).toBeHidden();
});

test("36-ValidateUploadButtonIsDisabledIfOtherTypeOfFileIsSelected", async ({ page }) => {
    await page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]").click();
    const container = page.locator("aside#closeSidebar");
    await expect(container).toBeVisible();
    const fileChooserPromise = page.waitForEvent('filechooser');
    await page.locator("aside label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/wrongformat/1.pdf`));
    await expect(page.getByAltText("selectedFile")).toBeVisible();
    await expect(page.getByRole("button", { name: "upload post" })).toBeDisabled();
});