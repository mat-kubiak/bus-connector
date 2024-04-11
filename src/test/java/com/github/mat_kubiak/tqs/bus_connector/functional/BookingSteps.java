package com.github.mat_kubiak.tqs.bus_connector.functional; // Generated by Selenium IDE

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BookingSteps {
  private WebDriver driver;

  @Given("I'm on the Bus Selector Website")
  public void imOnTheBusSelectorWebsite() {
    driver = new FirefoxDriver();
    driver.get("http://localhost:8080/");
  }

  @When("I search for trips from {string} to {string} on {string}")
  public void iSearchForTrips(String fromCity, String toCity, String date) {
    {
      WebElement dropdownSource = driver.findElement(By.id("originCitySelector"));
      dropdownSource.click();
      dropdownSource.findElement(By.xpath(String.format("./option[. = '%s']", fromCity))).click();
    }

    {
      WebElement dropdownDest = driver.findElement(By.id("destinationCitySelector"));
      dropdownDest.click();
      dropdownDest.findElement(By.xpath(String.format("./option[. = '%s']", toCity))).click();
    }

    driver.findElement(By.cssSelector("#destinationCitySelector > option:nth-child(2)")).click();
    driver.findElement(By.id("dateSelector")).sendKeys(date);

    driver.findElement(By.cssSelector("button")).click();
  }

  @When("I select the first ticket")
  public void iSelectFirstTicket() {
      driver.findElement(By.cssSelector("tr:nth-child(1) button")).click();
  }

  @When("I book a ticket with name {string} and surname {string}")
  public void iBookTicket(String firstName, String lastName) {
    driver.findElement(By.id("firstNameInput")).click();
    driver.findElement(By.id("firstNameInput")).sendKeys(firstName);
    driver.findElement(By.id("lastNameInput")).sendKeys(lastName);

    driver.findElement(By.cssSelector("button")).click();
  }

  @Then("I should see a ticket confirmation")
  public void iVerifyTicketConfirmation() {
    assertThat(driver.findElement(By.id("ticket-id")).getText(), not(""));
    driver.quit();
  }

  @Then("I should be redirected to home page")
  public void iShouldBeRedirectedToHomePage() {
    assertThat(driver.findElement(By.cssSelector("h1")).getText(), is("Bus Selector"));
    driver.quit();
  }
}
