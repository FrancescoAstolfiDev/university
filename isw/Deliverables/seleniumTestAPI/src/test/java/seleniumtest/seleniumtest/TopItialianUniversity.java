package seleniumtest.seleniumtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//FRANCESCO D'AMATA
public class TopItialianUniversity {
    private static String which;
    private static int numUniversity;//max number is 8
    private static String [] topUniversities;
    private static void setTopUniversities(){
        System.setProperty("webdriver.chrome.driver","src/chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.get("https://www.google.com/search?q=top+universita+italiane+2021&rlz=1C1GEWG_itIT949IT949&oq=top+universita+italiane+2021&aqs=chrome..69i57.8432j0j7&sourceid=chrome&ie=UTF-8");
        String inner;
        for(int i=1;i<=numUniversity;i++){
            inner=String.format("//*[@id=\"rso\"]/div[1]/div/div[1]/div/div[1]/div/div/div/div/div[1]/div/div[2]/ul/li[%d]",i);
            topUniversities[i-1]=driver.findElement(By.xpath(inner)).getText();
        }
    }
    public static boolean check(){
        boolean bool=false;
        int index=0;
        while(!bool && index<topUniversities.length){
            bool=topUniversities[index++].contains(which);
        }
        return bool;
    }
    public static void  main(String [] args ){
        which="Roma";
        numUniversity=8;
        topUniversities=new String[numUniversity];
        setTopUniversities();
        System.out.println(check());
    }
}
