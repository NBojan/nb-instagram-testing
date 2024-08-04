const { test, expect } = require("@playwright/test");

test.use({
  storageState: 'storage/user.json'
});
test.beforeEach(async ({ page }) => {
  await page.goto("https://nb-instagram.vercel.app/");
});

const genComment = () => {
    const number = Math.random() * 100;
    return `c-${number.toString()}`;
};

test.describe("Posting", () => {  
    test("42-ValidatePostingACommentOnAnyPost", async ({ page }) => {
        const comment = genComment();
        const article = page.locator("article").filter({ hasText: "testing12345" }).first();
        await article.locator("input").fill(comment);
        await article.getByRole("button", { name: "Post" }).click();
        await expect(article.getByText(comment)).toBeVisible();
    });
    
    test("43-ValidatePostingACommentOnAPersonalPost", async ({ page }) => {
        const comment = genComment();
        const article = page.locator("article").filter({ hasText: "MyTestingAccount" }).first();
        await article.locator("input").fill(comment);
        await article.getByRole("button", { name: "Post" }).click();
        await expect(article.getByText(comment)).toBeVisible();
    });
})

test.describe("VerifyComment", () => {
    test("44-VerifyCommentIncludesUsernameProfilePictureAndTime", async ({ page }) => {
        const commentBox = page.locator("//div[contains(@class, 'overflow-y-scroll')]").first();
        await expect(commentBox).toBeVisible();
        await expect(commentBox.locator("//img[contains(@class, 'comment-img-resp')]").first()).toBeVisible();
        await expect(commentBox.locator("//p[contains(@class, 'truncate')]")).toBeVisible();
        await expect(commentBox.locator("//p[contains(@class, 'whitespace-nowrap')]")).toBeVisible();
    });

    test("45-ValidatePostCommentWontWorkIfOnlyWhiteSpacesInputted", async ({ page }) => {
        const article = page.locator("article").filter({ hasText: "MyTestingAccount" }).first();
        article.locator("input[placeholder='Enter your comment...']").fill(" ");
        await expect(article.getByRole("button", { name: "Post" })).toBeDisabled();
    });
})
