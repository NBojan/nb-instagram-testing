const { test, expect } = require("@playwright/test");

test.use({
  storageState: 'storage/user.json'
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});

test("46-ValidateUsernameInPostHeaderIsALinkToTheUserPage", async ({ page }) => {
    const article = page.locator("article").filter({ hasText: "MyTestingAccount" }).first();
    await article.getByRole("heading", { name: "MyTestingAccount" }).first().click();
    await expect(page.url()).toMatch("https://nb-instagram.vercel.app/profile/MyTestingAccount");
});

test("47-ValidateUsernameInPostCaptionIsALinkToTheUserPage", async ({ page }) => {
    const article = page.locator("article").filter({ hasText: "MyTestingAccount" }).first();
    await article.getByRole("heading", { name: "MyTestingAccount" }).nth(1).click();
    await expect(page.url()).toMatch("https://nb-instagram.vercel.app/profile/MyTestingAccount");
});
test("48-ValidateUsernameInPostCommentIsALinkToTheUserPage", async ({ page }) => {
    const article = page.locator("article").filter({ hasText: "MyTestingAccountMyTestingAccount29.67928976006091" });
    await article.locator("p.truncate").click();
    await expect(page.url()).toMatch("https://nb-instagram.vercel.app/profile/MyTestingAccount");
});