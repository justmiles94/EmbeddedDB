package tools.spring.scraper;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Scraper {
	
	Logger log = LogManager.getLogger(getClass());
	
	WebDriver driver;
	private StringBuilder verificationErrors;
	static final String menuUrl = "https://ssb.siue.edu/pls/BANPROD/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu";
	
	public Scraper() {
		String userDir = System.getProperty("user.dir")+"/src/main/resources/";
		System.setProperty("webdriver.chrome.driver", userDir+"chromedriver.exe");
    	System.setProperty("webdriver.firefox.marionette",userDir+"geckodriver.exe");
    	ChromeOptions options = new ChromeOptions();
//    	options.addArguments("--headless");
    	DesiredCapabilities capabilities = new DesiredCapabilities(DesiredCapabilities.chrome());
    	capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    	driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}
	
    public void login() throws Exception {
        driver.get(menuUrl);
    	if(isLoggedIn()) return;
//    	driver.get(baseUrl);
//        driver.findElement(By.cssSelector("#contentItem0 > h3")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(System.getProperty("cougarnet.password"));
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(System.getProperty("cougarnet.username"));
        driver.findElement(By.id("btn_submit")).click();
        if(!driver.getCurrentUrl().equals(menuUrl)) {
        	close();
        	throw new Exception("Login Failed. Check username/password.");
        }
    }
    
    public boolean isLoggedIn(){
    	return driver.getCurrentUrl().equals(menuUrl);
    }
    
    public void close() {
    	recordDriverLogs();
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    
    private void recordDriverLogs() {
    	Logs logs = driver.manage().logs();
    	logs.getAvailableLogTypes().forEach(type -> {
    		logs.get(type).forEach(message -> {
    			log.debug(message);
    		});
    	});    	
    }
}
