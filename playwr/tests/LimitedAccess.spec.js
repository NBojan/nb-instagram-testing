const { test, expect } = require('@playwright/test');

test("1-UserNotLoggedInOpensTheRegistrationPage", async ({ page }) => {
    await page.goto("https://nb-instagram.vercel.app/registration");
    await expect(page.getByRole('heading', { name: 'Sign up' })).toBeVisible();
});

test("2-UserNotLoggedInTryingToAccessThePageRedirectedToTheRegisterPage", async ({ page }) => {
    await page.goto("https://nb-instagram.vercel.app");
    await expect(page.getByRole('heading', { name: 'Sign up' })).toBeVisible();
})