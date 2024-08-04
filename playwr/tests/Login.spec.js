const { test, expect } = require("@playwright/test")

test.beforeEach( async ({ page }) => {
    await page.goto("https://nb-instagram.vercel.app/registration");
})

test.describe("ValidTests", () => {
    test("15-ValidateLoginByEnteringValidEmailAndPassword @smoke", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.locator("[name='email']").fill("mytesting@testing.com");
        await page.locator("[name='password']").fill("Testing123");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.getByRole("img", { name: "userImg" })).toBeVisible();
    });

    test("16-ValidateRedirectToDashboardAfterLogin @smoke", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.locator("[name='email']").fill("mytesting@testing.com");
        await page.locator("[name='password']").fill("Testing123");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.getByRole("img", { name: "userImg" })).toBeVisible();
        await expect(page.getByRole("img", { name: "postImage" }).first()).toBeVisible();
    });

    test("17-ValidateLoginButtonDisabledIfAtLeastOneFieldIsEmpty", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.locator("[name='email']").fill("mytesting@testing.com");
        await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();
    });

    test("18-ValidateLoginButtonDisabledIfOnlySpacesAreEnteredInTheFields", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.locator("[name='email']").fill("        ");
        await page.locator("[name='password']").fill("        ");
        await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();
    });

    test("19-ValidateSwtichToRegisterLinkSwitchesToTheRegistrationForm @smoke", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.getByText("Sign Up").click();
        await expect(page.getByRole("heading", { name: "Sign up" })).toBeVisible();
    });     
    
    test("20-ValidateLoginByUsingTheDummyAccount @smoke", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.getByRole("button", { name: 'use dummy account' }).click();
        await expect(page.getByRole("img", { name: "userImg" })).toBeVisible();
    });  
});

test.describe("InvalidTests", () => {
    test("21-ValidateErrorMessageIfValidEmailButWrongPassword", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.locator("[name='email']").fill("mytesting@testing.com");
        await page.locator("[name='password']").fill("WrongPassword");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText("Invalid email or password.");
    });

    test("22-ValidateErrorMessageIfWrongEmail", async ({ page }) => {
        await page.getByText("Log In").click();
        await page.locator("[name='email']").fill("nonexistant@gmail.com");
        await page.locator("[name='password']").fill("Password123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText("Invalid email or password.");
    });  
});