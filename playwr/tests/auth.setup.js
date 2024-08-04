import { test as setup, expect } from '@playwright/test';

const authFile = 'storage/user.json';

setup('authenticate', async ({ page }) => {
    await page.goto("https://nb-instagram.vercel.app/registration");
    await page.getByText("Log In").click();
    await page.locator("[name='email']").fill("mytesting@testing.com");
    await page.locator("[name='password']").fill("Testing123");
    await page.getByRole("button", { name: "Submit" }).click();

    await expect(page.getByRole("img", { name: "userImg" })).toBeVisible();

    await page.context().storageState({ path: authFile });
});

// setup('authenticate', async ({ page }) => {
//     await page.goto("https://nb-instagram.vercel.app/registration");
//     await page.getByText("Log In").click();
//     await page.locator("[name='email']").fill("dummy@dummy.com");
//     await page.locator("[name='password']").fill("Dummy123");
//     await page.getByRole("button", { name: "Submit" }).click();

//     await expect(page.getByRole("img", { name: "userImg" })).toBeVisible();

//     await page.context().storageState({ path: authFile });
// });