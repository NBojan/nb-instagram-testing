const { test, expect } = require("@playwright/test");

test.use({
  storageState: "storage/user.json",
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});


test("30-ValidateSearchingForAnExistingUsername", async ({ page }) => {
    const existingUsername = "dummies";
    await page.locator("input[id='search']").fill(existingUsername);
    await page.locator("input[id='search']").press("Enter");
    expect(page.url()).toMatch(`https://nb-instagram.vercel.app/search?user=${existingUsername}`);
});