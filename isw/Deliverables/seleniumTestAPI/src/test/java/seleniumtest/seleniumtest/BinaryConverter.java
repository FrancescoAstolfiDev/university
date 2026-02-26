package seleniumtest.seleniumtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//CRISTINA MIRABELLI
public class BinaryConverter {
    static Integer input;
    static String binary;
    public static void  main(String args[]) {
        input=555;
        binary=Integer.toBinaryString(input);
        String local;
        System.setProperty("webdriver.chrome.driver","src/chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.get("https://www.rapidtables.com/convert/number/decimal-to-binary.html");
        driver.findElement(By.xpath("//*[@id=\"x\"]")).sendKeys(""+input);
        driver.findElement(By.xpath("//*[@id=\"calcform\"]/div[3]/button[1]")).click();
        local=driver.findElement(By.xpath("//*[@id=\"y\"]")).getAttribute("value");
        System.out.println("comparing .:    "+binary+"  ,   "+local+"   return     :"+    binary.equals(local));
    }
}
