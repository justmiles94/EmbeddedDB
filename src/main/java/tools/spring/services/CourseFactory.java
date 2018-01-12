package tools.spring.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tools.spring.domain.Course;
import tools.spring.domain.Extension;

public class CourseFactory {

	Logger log = LogManager.getLogger(getClass());

	static String c = "C";
	static String[] timeSplit, dateSplit, reqSplit;
	static SimpleDateFormat timer = new SimpleDateFormat("HH:mm aa");
	static SimpleDateFormat dater = new SimpleDateFormat("mm/dd");
	static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a'-'hh:mm a");
	static SimpleDateFormat dateFormat = new SimpleDateFormat("MM '/' dd '-' MM '/' dd");

	public List<Course> builder(List<String[]> rawCourseList) {
		List<Course> courseList = new ArrayList<Course>();
		Course currentCourse = courseMain(rawCourseList.get(0));
		currentCourse.extension.add(courseExt(rawCourseList.get(0)));
		int i = 1;
		for (; i < rawCourseList.size(); i++) {
			if (isExtension(rawCourseList.get(i))) {
				currentCourse.extension.add(courseExt(rawCourseList.get(i)));
			} else {
				courseList.add(currentCourse);
				currentCourse = courseMain(rawCourseList.get(i));
				currentCourse.extension.add(courseExt(rawCourseList.get(i)));
			}
		}
		if (isExtension(rawCourseList.get(i - 1))) {
			courseList.add(currentCourse);
		}
		return courseList;
	}

	private Course currentCourse;

	public Course builder(String[] rawCourseList) {
		if (isExtension(rawCourseList)) {
			currentCourse.extension.add(courseExt(rawCourseList));
		} else {
			currentCourse = courseMain(rawCourseList);
			currentCourse.extension.add(courseExt(rawCourseList));
		}
		return currentCourse;
	}

	private boolean isExtension(String[] arr) {
		return StringUtils.isAllBlank(Arrays.copyOfRange(arr, 0, 8));
	}

	public Course courseMain(String[] s) {
		return courseMain(new Course(), s);
	}

	public Course courseMain(Course course, String[] s) {
		course.setSelect(s[0].contains("C"));
		course.setCRN(s[1].isEmpty() ? null : Integer.parseInt(s[1]));
		course.setSubj(s[2]);
		course.setCrse(s[3]);
		course.setSec(s[4]);
		course.setCmp(s[5]);
		if (!s[6].isEmpty()) {
			if (s[6].contains("-")) {
				course.setCredLower(Double.parseDouble(s[6].split("-")[0]));
				course.setCredUpper(Double.parseDouble(s[6].split("-")[1]));
			} else if (s[6].contains("/")) {
				course.setCredLower(Double.parseDouble(s[6].split("/")[0]));
				course.setCredUpper(Double.parseDouble(s[6].split("/")[1]));
			} else {
				course.setCredLower(Double.parseDouble(s[6]));
				course.setCredUpper(Double.parseDouble(s[6]));
			}
		}
		course.setTitle(s[7]);
		return course;
	}

	public Extension courseExt(String[] s) {
		return courseExt(new Extension(), s);
	}

	public Extension courseExt(Extension ext, String[] s) {
		ext.setCap(Integer.parseInt(StringUtils.isBlank(s[10]) ? "-1" : s[10]));
		ext.setAct(Integer.parseInt(StringUtils.isBlank(s[11]) ? "-1" : s[11]));
		ext.setRem(Integer.parseInt(StringUtils.isBlank(s[12]) ? "-1" : s[12]));
		ext.setWLCap(Integer.parseInt(StringUtils.isBlank(s[13]) ? "-1" : s[13]));
		ext.setWLAct(Integer.parseInt(StringUtils.isBlank(s[14]) ? "-1" : s[14]));
		ext.setWLRem(Integer.parseInt(StringUtils.isBlank(s[15]) ? "-1" : s[15]));
		ext.setXLCap(Integer.parseInt(StringUtils.isBlank(s[16]) ? "-1" : s[16]));
		ext.setXLAct(Integer.parseInt(StringUtils.isBlank(s[17]) ? "-1" : s[17]));
		ext.setXLRem(Integer.parseInt(StringUtils.isBlank(s[18]) ? "-1" : s[18]));
		ext.setDays(s[8]);
		try {
			if (!s[9].isEmpty() && !"TBA".equals(s[20])) {
				timeSplit = s[9].split("-");
				ext.setStart(timer.parse(timeSplit[0]));
				ext.setEnd(timer.parse(timeSplit[1]));
			}
		} catch (ParseException e) {
			log.trace("unable to parse time", e);
		}catch (NullPointerException e) {
			log.trace("time has thrown a null pointer exception", e);
		}
		ext.setInstructor(s[19]);
		try {
			dateSplit = s[20].isEmpty() ? null : s[20].split("-");
			ext.setBegin(dater.parse(dateSplit[0]));
			ext.setFinish(dater.parse(dateSplit[1]));
		} catch (ParseException e) {
			log.trace("unable to parse date", e);
		} catch (NullPointerException e) {
			log.trace("date has thrown a null pointer exception", e);
		}
		ext.setLocation(s[21]);
		if (s.length > 22)
			ext.setAttribute(s[22]);
		return ext;
	}

}
