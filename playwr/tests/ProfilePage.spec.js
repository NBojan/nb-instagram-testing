const { test, expect } = require("@playwright/test");

test.use({
  storageState: 'storage/user.json'
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/profile/testing12345");
});

test("49-VerifyProfilePictureUsernameNameAndNumberOfPostsAppear", async ({ page }) => {
    await expect(page.locator("main").getByAltText("userImg")).toBeVisible();
    await expect(page.getByText("testing12345")).toBeVisible();
    await expect(page.locator("p.text-gray-700")).toBeVisible();
    await expect(page.getByText("posts")).toBeVisible(); 
});
test("50-VerifyUsersPostsAreDisplayed", async ({ page }) => {
    await expect(page.locator("div.flex-wrap")).toBeVisible();
    await expect(page.locator("//img[contains(@class, 'aspect-square')]").first()).toBeVisible();
});
test("51-VerifyMessage'ThereAreNoPostsToShow...'ForAUserWithNoPosts", async ({ page }) => {
    await page.goto("https://nb-instagram.vercel.app/profile/helloWorld");
    await expect(page.getByRole("heading", { name: "There are no posts to show..." })).toBeVisible();
});
test("52-VerifyNumberOfLikesAndCommentsAppearWhileHoveringOnAPost", async ({ page }) => {
    await page.locator("//img[contains(@class, 'aspect-square')]").first().hover();
    await expect(page.locator("div.bg-black").first()).toBeVisible();
});
test.only("53-ValidatePostsAreLinksTowardsThePostPage", async ({ page }) => {
    await page.locator("//img[contains(@class, 'aspect-square')]").first().click();
    await expect(page.url()).toContain("post");
});