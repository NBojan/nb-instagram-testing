const { test, expect } = require("@playwright/test");

test.use({
  storageState: 'storage/user.json'
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});


test("23-VerifyInstagramLogoVisible", async ({ page }) => {
  await expect(page.getByRole("link", { name: "Instagram" })).toBeVisible();
});
test("24-VerifyInstagramLogoIsALinkToTheDashboard", async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/profile/dummies");
  await expect(page.url()).toMatch("https://nb-instagram.vercel.app/profile/dummies");
  await page.getByRole("link", { name: "Instagram" }).click();
  await expect(page.url()).toMatch("https://nb-instagram.vercel.app/");
});
test("25-VerifyTheUploadPostButtonIsVisible", async ({ page }) => {
  await expect(page.getByRole("navigation").locator("//button[contains(@class, 'transition-transform')]")).toBeVisible();
});
test("26-VerifyTheProfilePictureIsVisible", async ({ page }) => {
  await expect(page.getByRole("navigation").getByRole("button", { name: "userImg" })).toBeVisible();
});
test("27-VerifyTheProfilePictureIsClickableAndTogglesTheProfileMenu", async ({ page }) => {
  const profileButton = page.getByRole("navigation").getByRole("button", { name: "userImg" });
  const profileMenu = page.locator("//div[contains(@class, 'right-0')]");
  await profileButton.click();
  await expect(profileMenu).toBeVisible();
  await profileButton.click();
  await expect(profileMenu).toBeHidden();
});


