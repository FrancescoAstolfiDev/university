package seleniumtest.seleniumtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//EDUARD OLENIC
public class ScholarQuotes {
    public static boolean extract(String s,Integer min){
        int start=10;
        String convert=s.substring(start);
        int converted=Integer.parseInt(convert);
        if(converted>min){
            System.out.printf("Number of quotes greater than %d , value: %d",min,converted);
            return true;
        }else{
            System.out.printf("Number of quotes greater than %d , value: %d",min,converted);
            return false;
        }
    }
        //number of quotes
    public static void  main(String [] args ){
        String who="Davide Falessi";
        Integer min=1800;
        System.setProperty("webdriver.chrome.driver","src/chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.get("https://scholar.google.com/citations?hl=it&view_op=search_authors&mauthors=&btnG=");
        driver.findElement(By.xpath("//*[@id=\"gs_hdr_tsi\"]")).sendKeys(who);
        driver.findElement(By.xpath("//*[@id=\"gs_hdr_tsb\"]")).click();
        WebElement TxtBoxContent =driver.findElement(By.xpath("//*[@id=\"gsc_sa_ccl\"]/div/div/div/div[3]"));
        extract(TxtBoxContent.getText(),min);
        }
    }
