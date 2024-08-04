const { test, expect } = require("@playwright/test");

test.use({
  storageState: 'storage/user.json'
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});

test("37-ValidateLikingAPostFromAnotherUser", async ({ page }) => {
  const preLikeBtn = page.locator("article").filter({ hasText: "testing12345testing12345dada" }).getByRole("button").first();
  const postLikeBtn = page.locator("article").filter({ hasText: "testing123451 liketesting12345dada" }).getByRole("button").first();
  await preLikeBtn.click();
  await expect(postLikeBtn).toHaveClass(/text-red-500/);
  await postLikeBtn.click();
  await expect(preLikeBtn).not.toHaveClass(/text-red-500/);
});

test("38-ValidateLikingAPersonalPost", async ({ page }) => {
  const preLikeBtn = page.locator("article").filter({ hasText: "MyTestingAccountMyTestingAccount29.67928976006091" }).getByRole("button").first();
  const postLikeBtn = page.locator("article").filter({ hasText: "MyTestingAccount1 likeMyTestingAccount29.67928976006091" }).getByRole("button").first();
  await preLikeBtn.click();
  await expect(postLikeBtn).toHaveClass(/text-red-500/);
  await postLikeBtn.click();
  await expect(preLikeBtn).not.toHaveClass(/text-red-500/);
});

test("39-ValidateRemovingALikeFromAPost", async ({ page }) => {
  const postLikeBtn = page.locator("article").filter({ hasText: "testing123451 liketesting12345testing12345" }).getByRole("button").first();
  const preLikeBtn = page.locator("article").filter({ hasText: "testing12345testing12345testing12345" }).getByRole("button").first();
  await postLikeBtn.click();
  await expect(preLikeBtn).not.toHaveClass(/text-red-500/);
  await preLikeBtn.click();
  await expect(postLikeBtn).toHaveClass(/text-red-500/);
});

test("40-ValidateNumberOfLikesIsDisplayedForAPostThatHas1OrMoreLikes", async ({ page }) => {
  await expect(page.locator("article").filter({ hasText: "testing123451 liketesting12345testing12345" }).getByRole("heading", { name: "1 like" })).toBeVisible();
});

test("41-ValidateClickingOnTheLikesOpensABoxShowingTheUsersThatLiked", async ({ page }) => {
  await expect(page.locator("article").filter({ hasText: "testing123451 liketesting12345testing12345" }).getByRole("heading", { name: "1 like" })).toBeVisible();
  await page.locator("article").filter({ hasText: "testing123451 liketesting12345testing12345" }).getByRole("heading", { name: "1 like" }).click();
  await expect(page.locator("aside#closeLikes")).toBeVisible();
  await page.locator("aside#closeLikes").getByRole("button").click();
  await expect(page.locator("aside#closeLikes")).toBeHidden();
});