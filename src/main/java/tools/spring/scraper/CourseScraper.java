package tools.spring.scraper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import tools.spring.domain.Course;

public class CourseScraper extends Scraper {

	Logger log = LogManager.getLogger(getClass());

	private static ArrayList<String[]> courses = new ArrayList<>();

	static PrintWriter prereqs, courseList;
	
	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("-yyyy_MM_dd-HH_mm_ss");
		String prereqName = String.format("output/prereq-output%s.txt", dateFormat.format(new Date()));
		String courseName = String.format("output/course-output%s.txt", dateFormat.format(new Date()));
		try {
			prereqs = new PrintWriter(new File(prereqName));
			courseList = new PrintWriter(new File(courseName));
		} catch (IOException e) {
			//log.warn("unable to write the courselist or prereq output files or to their folder", e);
		}
	}

	public CourseScraper() {
		super();
	}
	
	List<Integer> crnsWithPrereqs = new ArrayList<Integer>();

	public void setCourses(ArrayList<Course> courseListing) {
		courseListing.forEach(course -> {
			crns.add(course.getCRN());
			if(!course.getPrereqs().isEmpty()) {
				crnsWithPrereqs.add(course.getCRN());
			}
		});
		
	}

	public String goToCourseListingPage(String department) {
		getCourseListingAdvanceSearch();
		// subject box
		Select selectBox = new Select(driver.findElement(By.cssSelector("#subj_id")));
		List<WebElement> options = selectBox.getOptions();

		if (department.equals("ALL")) {
			options.forEach(option -> selectBox.selectByVisibleText(option.getText()));
			// section search with all options (~30seconds)
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else {
			selectBox.selectByVisibleText(department);
		}
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection |
		// id=subj_id | label=Anthropology]]
		// ERROR: Caught exception [ERROR: Unsupported command [removeSelection |
		// id=subj_id | label=Anthropology]]
		driver.findElement(By.id("id____UID6")).click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		return department;
	}
	
	public void getCourseListingAdvanceSearch(){
		// student module
		driver.get("https://ssb.siue.edu/pls/BANPROD/twbkwbis.P_GenMenu?name=bmenu.P_StuMainMnu");
		// register
		// driver.get(domainUrl+"/pls/BANPROD/bwskagre.P_Agreement");
		driver.findElement(By.cssSelector("#bwskagre--P_Agreement___UID1")).click();
		// lookup classes
		// driver.get("https://ssb.siue.edu/pls/BANPROD/bwskfcls.p_sel_crse_search");
		driver.findElement(By.cssSelector("#contentItem14")).click();
		// pick 3rd dropdown option
		// html > body > div#content.level4 > div#bodyContainer > div#pagebody.level4 >
		// div
		WebElement element = driver.findElement(By.cssSelector("#pagebody"));
		element = element
				.findElement(By.cssSelector(".pagebodydiv > form > .dataentrytable > tbody > tr > .dedefault"));
		Select selectTerm = new Select(
				element.findElement(By.cssSelector("tbody > tr > td.dedefault > select#term_input_id")));
		selectTerm.selectByValue("201735");// td.dedefault:nth-child(1)
		// submit search term
		driver.findElement(By.id("id____UID8")).click();
		// advanced search
		driver.findElement(By.id("id____UID7")).click();
	}
	
	public String goToCourseListingPage(int numberOfDepartments) {
		getCourseListingAdvanceSearch();
		// subject box
		Select selectBox = new Select(driver.findElement(By.cssSelector("#subj_id")));
		List<WebElement> options = selectBox.getOptions();

		if (numberOfDepartments == -1) {
			options.forEach(option -> selectBox.selectByVisibleText(option.getText()));
			// section search with all options (~30seconds)
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else {
			selectBox.selectByIndex(numberOfDepartments);
		}
		String department = selectBox.getFirstSelectedOption().getText();
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection |
		// id=subj_id | label=Anthropology]]
		// ERROR: Caught exception [ERROR: Unsupported command [removeSelection |
		// id=subj_id | label=Anthropology]]
		driver.findElement(By.id("id____UID6")).click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		return department;
	}

	List<WebElement> elementList;
	Set<Integer> crns = new HashSet<Integer>();

	public int courseListRowCount() {
		elementList = driver.findElements(By.cssSelector(".datadisplaytable > tbody > tr"));
		return elementList.size();
	}

	public String[] getCourseData(int index) {
		String[] courseInfo = null;
		// #contentHolder > div.pagebodydiv > form > table > tbody > tr:nth-child(3) >
		// td:nth-child(23)
		WebElement row = elementList.get(index);
		if (23 == row.findElements(By.cssSelector("td")).size()) {
			try{
				String crnStr = row.findElement(By.cssSelector("td:nth-child(2)")).getText().trim();
				Integer crn = Integer.parseInt(crnStr);
				if(!crnStr.equals("") && !crns.contains(crn)){
					courseInfo = new String[23];
					List<WebElement> tableData = row.findElements(By.tagName("td"));
					for (int i = 0; i < 23; i++) {
						courseInfo[i] = tableData.get(i).getText();
					}
					log.info(index+Arrays.deepToString(courseInfo));
					courses.add(courseInfo);
					// courseList.println(Arrays.toString(courseInfo));
					crns.add(crn);
				}
			}catch(NumberFormatException e) {	}
		}
		return courseInfo;
	}

	public Map<String, List<String>> getPrereqs() throws NoSuchElementException{
		Map<String, List<String>> prereqMap = new HashMap<String, List<String>>();
		By crnList = By
				.cssSelector("#contentHolder > div.pagebodydiv > form > table > tbody > tr > td:nth-child(2) > a");
		int numberOfLinks = driver.findElements(crnList).size();
		for (int i = 0; i < numberOfLinks+3; i++) {
			try {
				String cssselector = "#contentHolder > div.pagebodydiv > form > table > tbody > tr:nth-child(" + i
						+ ") > td:nth-child(2) > a";
				String crnStr = driver.findElement(By.cssSelector(cssselector)).getText();
				Integer crn = Integer.parseInt(crnStr);
				if(!crnsWithPrereqs.contains(crn)) {
					// course description page
					driver.findElement(By.cssSelector(cssselector)).click();
					// goto prereq page
					driver.findElement(By.cssSelector(
							"#contentHolder > div.pagebodydiv > table:nth-child(6) > tbody > tr:nth-child(1) > th > a"))
							.click();
					List<WebElement> linkList = driver.findElements(By.cssSelector(
							"#contentHolder > div.pagebodydiv > table:nth-child(1) > tbody > tr:nth-child(2) > td > a"));
					List<String> goodList = new ArrayList<String>();
					for (WebElement prerequi : linkList) {
						if (!prerequi.getText().equalsIgnoreCase("Syllabus Available")
								&& !prerequi.getText().equalsIgnoreCase("View Catalog Entry")) {
							goodList.add(prerequi.getText().replace(" ", ""));
						}
					}
					log.trace("Current Prereq CRN: " + crn);
					prereqMap.put(crn.toString(), goodList);
					crnsWithPrereqs.add(crn);
					driver.navigate().back();
					driver.navigate().back();
				}
			} catch (NoSuchElementException e) {
				log.trace("header section", e);
			}
		}
		return prereqMap;
	}

	@Override
	public void close() {
		super.close();
	}
}