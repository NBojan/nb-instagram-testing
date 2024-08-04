const { test, expect } = require("@playwright/test");
const path = require("path");

test.use({
  storageState: 'storage/user.json'
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});

test("28-ValidateLogOutByClickingOnTheLogOutOption", async ({ page }) => {
    await page.getByRole("navigation").getByRole("button", { name: "userImg" }).click();
    await page.getByRole("button", { name: "Log out." }).click();
    await expect(page.getByRole("heading", { name: "Sign up" })).toBeVisible();
});

test.only("29-ValidateUpdatingTheProfilePicture", async ({ page }) => {
    const imageBeforeChange = await page.getByAltText("userImg").getAttribute("src");
    await page.getByRole("navigation").getByRole("button", { name: "userImg" }).click();
    await page.getByRole("button", { name: "Update profile picture." }).click();
    const container = page.locator("aside#closeSidebar");
    await expect(container).toBeVisible();
    const fileChooserPromise = page.waitForEvent('filechooser');
    await page.locator("aside label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, '../images/pp/1.png'));
    await page.getByRole("button", { name: "upload profile picture" }).click();
    await expect(container).toBeHidden();
    await page.reload();
    const imageAfterChange = await page.getByAltText("userImg").getAttribute("src");
    await expect(imageBeforeChange).not.toMatch(imageAfterChange);
});

