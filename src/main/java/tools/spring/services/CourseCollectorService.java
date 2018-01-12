package tools.spring.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

import tools.spring.domain.Course;
import tools.spring.scraper.CourseScraper;


public class CourseCollectorService {

	final Logger log = LogManager.getLogger(CourseCollectorService.class);

//	@Autowired
//	CourseRepo courseRepository;
	CourseScraper courseScraper;
	CourseFactory factory = new CourseFactory();
	Serializer ser;

	ArrayList<Course> allCourses = new ArrayList<Course>();
	ArrayList<Course> coursesFullList = new ArrayList<Course>();	

	Map<String, List<String>> prereqs = new HashMap<String, List<String>>();
	Map<String, List<String>> prereqsFullList = new HashMap<String, List<String>>();

	boolean RANDOM_DEPARTMENT_START = true;
	int departmentIndexStartRange = 93;
	int departmentIndexEndRange = 94;//94 total
	int randStartPoint = RANDOM_DEPARTMENT_START ? 0 : (int)(Math.random()*95);
	String departmentName = "";//blank assumes all departments
	
	// @Scheduled(cron = "0 */4 * * * *")
	public void build(boolean updateCourses) throws Exception{
		//getSerializedFileData();
		allCourses.addAll(readCourseFile());
		setPreReqs();
		if(updateCourses) {
			courseScraper = new CourseScraper();
			for(; departmentIndexStartRange < departmentIndexEndRange; departmentIndexStartRange++) {
				courseScraper.login();
				courseScraper.setCourses(allCourses);
				int index = (departmentIndexStartRange+randStartPoint)%departmentIndexEndRange;
				departmentName = courseScraper.goToCourseListingPage(index);
				try {
					log.trace("Parsing " + departmentName + " courses and prerequisites now..");
					prereqs = courseScraper.getPrereqs();
					grabCourses();
					setPreReqs();
//					courseRepository.saveAll(allCourses);
					String filename = writeDotFileInverted(departmentName, allCourses);
					graphviz(filename);
					graphvizNewest(filename);
					writeCourseFile();
					allCourses.clear();
					prereqs.clear();
				}catch(NoSuchElementException e) {
					log.warn("If "+departmentIndexStartRange+" Education: department has no entries", e);
				}
			}
			courseScraper.close();
		}
		graphviz("super.dot");
	}

	public void getSerializedFileData() {
		ser = new Serializer("courseListFileSerialized.txt");
		try {
			allCourses = ser.deserializeCourses();
		} catch (IOException e) {
			log.warn("Unable to parse the serialized file of courses", e);
		}
	}
	
	public List<Course> getCourses(){
		return allCourses;
	}
	public void graphvizNewest(String filename) {
		String output = "";
		Process p;
		try {
			String newestFile = filename.split("-")[0];
			String cmd = String.format("grapher.bat %s %s.png", filename, newestFile);
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor(3, TimeUnit.SECONDS);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.concat(line + "\n");
			}
			log.trace(output);
			reader.close();
		} catch (IOException e) {
			log.error("unable to execute graphviz to generate png", e);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	public void graphviz(String filename) {
		String output = "";
		Process p;
		try {
			String cmd = String.format("grapher.bat %s %s.png", filename, filename.replace(".dot", ""));
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor(3, TimeUnit.SECONDS);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.concat(line + "\n");
			}
			log.trace(output);
			reader.close();
		} catch (IOException e) {
			log.error("unable to execute graphviz to generate png", e);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	public void grabCourses() {
		int courseElementRowListCount = courseScraper.courseListRowCount();
		for (int i = 0; i < courseElementRowListCount; i++) {
			String[] rawCourse = courseScraper.getCourseData(i);
			if (!(null == rawCourse)) {
				Course course = factory.builder(rawCourse);
				//ser.serialize(course);
				allCourses.add(course);
			}
		}
		//try {
			//ser.close();
		//} catch (IOException e) {
			//log.warn(e);
		//}
	}
	
	public void writeCourseFile() {
		allCourses.forEach(course -> {
			try {
				Files.write(Paths.get("persistentCourseList.txt"), course.toString().getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				log.trace("unable to write course to file " + course.toString(), e);
			}
		});
	}
	
	public ArrayList<Course> readCourseFile() {
		ArrayList<Course> courseList = new ArrayList<Course>();
		try {
			Files.readAllLines(Paths.get("persistentCourseList.txt")).forEach(courseString -> {
				courseList.add(factory.builder(courseString.split(",")));
			});
		} catch (IOException e) {
			log.trace("unable to read course file except for {} courses", courseList.size(), e);
		} catch(Exception e) {
			
		}
		return courseList;
	}

	public void printSoutCourseInfo() {
		List<String> courseStrings = new ArrayList<String>();
		for (Course course : allCourses) {
			courseStrings.add(course.toString());
		}
		courseStrings.forEach(System.out::println);
		for (Entry<String, List<String>> entry : prereqs.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().toString());
		}
	}

	public void setPreReqs() {
		prereqsFullList.putAll(prereqs);
		for (String crn : prereqs.keySet()) {
			for (Course course : allCourses) {
				if (course.getCRN() == Integer.parseInt(crn))
					course.setPrereqs(prereqs.get(crn));
			}
		}
		coursesFullList.addAll(allCourses);
	}

	public static final String DATETIME_FORMAT = "-yyyy_MM_dd-HH_mm_ss";

	public String writeDotFile(String departmentName, List<Course> courseList) {
		String filename = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
			departmentName = departmentName.replaceAll("&", "").replaceAll(" ", "_");
			filename = String.format("%s%s.dot", departmentName, dateFormat.format(new Date()));
			PrintWriter dot = new PrintWriter(new File("graphs/" + filename));
			dot.println("digraph {");
			Set<String> cds = new HashSet<String>();
			for (Course course : courseList) {
				String cd = course.getSubj() + course.getCrse();
				if (!cds.contains(cd)) {
					dot.println(String.format("\t%s -> { %s };", cd, StringUtils.join(course.getPrereqs(), " ")));
					dot.flush();
					cds.add(cd);
				}
			}
			dot.println("}");
			dot.flush();
			dot.close();
			Files.write(Paths.get("graphs/super.dot"), Files.readAllBytes(Paths.get("graphs/"+filename)), StandardOpenOption.APPEND);
		} catch (FileNotFoundException e) {
			log.warn("unable to write to graphs folder or the graphs file", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}

	public Map<String, List<String>> inverted(List<Course> courseList) {
		Map<String, List<String>> inversion = new HashMap<String, List<String>>();
		for (Course course : courseList) {
			for (String prereq : course.getPrereqs()) {
				if (!inversion.containsKey(prereq)) {
					inversion.put(prereq, new ArrayList<String>());
				}
				inversion.get(prereq).add(course.getSubj() + course.getCrse());
			}
		}
		return inversion;
	}

	public String writeDotFileInverted(String departmentName, List<Course> courseList) {
		String filename = "";
		try {
			Map<String, List<String>> invertedMap = inverted(courseList);
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT);
			departmentName = departmentName.replaceAll("&", "").replaceAll(" ", "_");
			filename = String.format("%s%s.dot", departmentName, dateFormat.format(new Date()));
			PrintWriter dot = new PrintWriter("graphs/" + filename);
			dot.println("digraph {");
			Set<String> cds = new HashSet<String>();
			for (String course : invertedMap.keySet()) {
				if (!cds.contains(course)) {
					dot.println(
							String.format("\t%s -> { %s };", course, StringUtils.join(invertedMap.get(course), " ")));
					dot.flush();
					cds.add(course);
				}
			}
			dot.println("}");
			dot.flush();
			dot.close();
		} catch (FileNotFoundException e) {
			log.error("file not found for dot file", e);
		}
		return filename;
	}

//	public void setCourseRepository(CourseRepo courseRepository) {
//		this.courseRepository = courseRepository;
//	}

}