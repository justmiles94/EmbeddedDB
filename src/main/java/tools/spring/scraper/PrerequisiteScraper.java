package tools.spring.scraper;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tools.spring.domain.Course;

public class PrerequisiteScraper extends Scraper {
	
	public PrerequisiteScraper() {
		super();
	}

    public ArrayList<?> getPrereqs(WebElement element) {
        driver.findElement(By.cssSelector(".ddtitle > a:nth-child(1)")).click();
        if(driver.findElement(By.cssSelector("span.fieldlabeltext:nth-child(20)")).isDisplayed()){
            ArrayList<String> course = new ArrayList<>();
            String location = "table.datadisplaytable:nth-child(1) > tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1) > br:nth-child(";//break
            for (int i = 21; driver.findElement(By.cssSelector(location + i + ")")).isDisplayed(); i++) {
                course.add(driver.findElement(By.cssSelector(location + i + ")")).getText());
                
            }
            return course;
        }else{
            return new ArrayList<>();
        }
    }
    
    public String[] getPrereqs(Course course){
    	return null;//course.preReqMap.get(0).split("");
    }
}
