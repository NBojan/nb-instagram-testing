const { test, expect } = require('@playwright/test');

test.beforeEach(async ({ page }) => {
    await page.goto("https://nb-instagram.vercel.app/registration");
})

test.describe("ValidTests", () => {
    test.skip("3-ValidateRegistrationWithValidDataInAllFields @smoke", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Test");
        await page.locator("[name='lastName']").fill("Testovski");
        await page.locator("[name='username']").fill("testot1");
        await page.locator("[name='email']").fill("testot1@test.com");
        await page.locator("[name='password']").fill("Testot123");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.getByRole("button", { name: "userImg" })).toBeVisible();
    });

    test.skip("4-ValidateRegistrationWithCyrilicLettersUsedInFirst&lastNameAndUsername @smoke", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Бојан");
        await page.locator("[name='lastName']").fill("Новески");
        await page.locator("[name='username']").fill("бокобоко");
        await page.locator("[name='email']").fill("testot2@test.com");
        await page.locator("[name='password']").fill("Testot123");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.getByRole("button", { name: "userImg" })).toBeVisible();
    });

    test("5-ValidateSubmitButtonDisabledIfAtLeastOneFieldIsEmpty", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Test");
        await page.locator("[name='lastName']").fill("Testovski");
        await page.locator("[name='username']").fill("testot1");
        await page.locator("[name='email']").fill("testot1@test.com");
        await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();

    });

    test("6-ValidateSubmitButtonDisabledIfOnlySpacesAreEnteredInTheFields", async ({ page }) => {
        await page.locator("[name='firstName']").fill(" ");
        await page.locator("[name='lastName']").fill(" ");
        await page.locator("[name='username']").fill(" ");
        await page.locator("[name='email']").fill(" ");
        await page.locator("[name='password']").fill(" ");
        await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();

    });

    test("7-ValidateEmailPlaceholderISemail@address.com", async ({ page }) => {
        await expect(page.locator("[name='email']")).toHaveAttribute("placeholder", "email@address.com")
    });

    test("8-ValidateSwtichToLoginLinkSwitchesToTheLoginForm @smoke", async ({ page }) => {
        await page.getByText("Log In").click();
        await expect(page.getByRole("heading", { name: "Log in" })).toBeVisible();
    });

});

test.describe("InvalidTests", () => {
    test("9-ValidateErrorMessageDisplaysIfEnteringUsernameAlreadyInUse", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Existing");
        await page.locator("[name='lastName']").fill("Username");
        await page.locator("[name='username']").fill("dummies");
        await page.locator("[name='email']").fill("randomemail@gmail.com");
        await page.locator("[name='password']").fill("Testot123");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.locator("p.text-red-500")).toHaveText("Username is already in use.");
    });

    test("10-ValidateErrorMessageDisplaysIfEnteringEmailAlreadyInUse", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Existing");
        await page.locator("[name='lastName']").fill("Email");
        await page.locator("[name='username']").fill("dumbAndDumber");
        await page.locator("[name='email']").fill("digmark27@gmail.com");
        await page.locator("[name='password']").fill("Testot123");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.locator("p.text-red-500")).toHaveText("Email is already in use.");
    });

    test("11-ValidateErrorMessageDisplaysIfEnteringPasswordLessThan8Characters", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Bad");
        await page.locator("[name='lastName']").fill("Password");
        await page.locator("[name='username']").fill("dumbAndDumber");
        await page.locator("[name='email']").fill("randomemail@gmail.com");
        await page.locator("[name='password']").fill("Bojan12");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.locator("p.text-red-500")).toHaveText("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.");
    });

    test("12-ValidateErrorMessageDisplaysIfEnteringPasswordWithoutLowercaseLetters", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Existing");
        await page.locator("[name='lastName']").fill("Email");
        await page.locator("[name='username']").fill("dumbAndDumber");
        await page.locator("[name='email']").fill("randomemail@gmail.com");
        await page.locator("[name='password']").fill("BOJAN12345");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.locator("p.text-red-500")).toHaveText("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.");
    });

    test("13-ValidateErrorMessageDisplaysIfEnteringPasswordWithoutUppercaseLetters", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Existing");
        await page.locator("[name='lastName']").fill("Email");
        await page.locator("[name='username']").fill("dumbAndDumber");
        await page.locator("[name='email']").fill("randomemail@gmail.com");
        await page.locator("[name='password']").fill("bojan12345");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.locator("p.text-red-500")).toHaveText("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.");
    });

    test("14-ValidateErrorMessageDisplaysIfEnteringPasswordWithoutNumbers", async ({ page }) => {
        await page.locator("[name='firstName']").fill("Existing");
        await page.locator("[name='lastName']").fill("Email");
        await page.locator("[name='username']").fill("dumbAndDumber");
        await page.locator("[name='email']").fill("randomemail@gmail.com");
        await page.locator("[name='password']").fill("bojanNov");
        await page.getByRole("button", { name: "Submit" }).click();

        await expect(page.locator("p.text-red-500")).toHaveText("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.");
    });

});