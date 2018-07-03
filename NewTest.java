import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewTest 
{
	WebDriver driver= new ChromeDriver();
	  JavascriptExecutor js=(JavascriptExecutor) driver;
  @BeforeClass
  public void launch()
  {
	  driver.get("http://10.0.1.86/tatoc");
	  driver.manage().window().maximize();
  }
  @Test
  public void clickOnBasicCourse()
  {
	  WebElement element =driver.findElement(By.linkText("Basic Course"));
		 js.executeScript("arguments[0].click();", element);
		 assertTrue(js.executeScript("return document.title;").toString().contains("Grid Gate - Basic Course - T.A.T.O.C"));
  }
  @Test(dependsOnMethods = { "clickOnBasicCourse" })
  public void greenBox()
  {
	  js.executeScript("arguments[0].click();", driver.findElement(By.className("greenbox")));
	  assertTrue(js.executeScript("return document.title;").toString().contains("Frame Dungeon - Basic Course - T.A.T.O.C"));
  }
  @Test(dependsOnMethods = { "greenBox" })
  public void repaintBox()
  {
	  driver.switchTo().frame("main");
		WebElement b1=driver.findElement(By.xpath("//*[text()[contains(.,'Box 1')]]"));
		int flag=0;
		while(flag==0)
		{
			String s1 = b1.getAttribute("class");
			driver.switchTo().frame("child");
			WebElement b2=driver.findElement(By.xpath("//*[text()[contains(.,'Box 2')]]"));
			String s2 = b2.getAttribute("class");
			driver.switchTo().defaultContent();
			driver.switchTo().frame("main");
			if(s1.equals(s2))
			{
				flag=1;
				WebElement pro=driver.findElement(By.xpath("//*[text()[contains(.,'Proceed')]]"));
				js.executeScript("arguments[0].click();", pro);
			}
			else
			{
				WebElement re=driver.findElement(By.xpath("//*[text()[contains(.,'Repaint Box 2')]]"));
				js.executeScript("arguments[0].click();", re);
			}
		}
		driver.switchTo().defaultContent();
	  assertTrue(js.executeScript("return document.title;").toString().contains("Drag - Basic Course - T.A.T.O.C"));
  }
  @Test(dependsOnMethods = { "repaintBox" })
  public void drapAround()
  {
	  WebElement drag=driver.findElement(By.id("dragbox"));
		WebElement drop=driver.findElement(By.id("dropbox"));
		Actions builder = new Actions(driver);
		builder.dragAndDrop(drag, drop).perform();
		WebElement pro1=driver.findElement(By.xpath("//*[text()[contains(.,'Proceed')]]"));
		js.executeScript("arguments[0].click();", pro1);
		 assertTrue(js.executeScript("return document.title;").toString().contains("Windows - Basic Course - T.A.T.O.C"));
  }
  @Test(dependsOnMethods = { "drapAround" })
  public void popUp()
  {
	  WebElement heading=driver.findElement(By.tagName("h1"));
		String s1=heading.getText();
		WebElement launch=driver.findElement(By.xpath("//*[text()[contains(.,'Launch Popup Window')]]"));
		js.executeScript("arguments[0].click();", launch);
		ArrayList windowsList =new ArrayList(driver.getWindowHandles());
		String window1= ((String)windowsList.get(1));
	  driver.switchTo().window(window1);
	//  WebElement inputField = driver.findElement(By.id("name"));
	  js.executeScript("document.getElementById('name').value='Prakhar'");
	  WebElement submitButton =driver.findElement(By.id("submit"));
	  js.executeScript("arguments[0].click();", submitButton);
	  String window0= ((String)windowsList.get(0));
	  driver.switchTo().window(window0);
	  WebElement proceedButton =driver.findElement(By.linkText("Proceed"));
	  js.executeScript("arguments[0].click();", proceedButton);
	  assertTrue(js.executeScript("return document.title;").toString().contains("Cookie Handling - Basic Course - T.A.T.O.C"));
  }
  @Test(dependsOnMethods = { "popUp" })
  public void cookieHand()
  {
	  WebElement generateToken=driver.findElement(By.linkText("Generate Token"));
	  js.executeScript("arguments[0].click();", generateToken);
	 // String Token = (String) (js.executeScript("return arguments[0].value;",driver.findElement(By.id("token"))));
		String Token = driver.findElement(By.id("token")).getText();
		System.out.println(Token);
		String substring1=Token.substring(7);
		Cookie name = new Cookie("Token", substring1);
		driver.manage().addCookie(name);
		WebElement proceed=driver.findElement(By.linkText("Proceed"));
		 js.executeScript("arguments[0].click();", proceed);
		 assertTrue(js.executeScript("return document.title;").toString().contains("End - T.A.T.O.C"));
  }
  @AfterClass
  public void quit()
  {
	  driver.quit();
  }
}


