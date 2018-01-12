package tools.spring.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class CourseArchiveParser {
	
	private final String fs = File.separator;
	
	public ArrayList<String[]> parse(){
		
		String fileName = System.getProperty("user.dir")+fs+"courseList.txt";
		ArrayList<String[]> rawCourses = new ArrayList<String[]>();

		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(line -> {
				String[] arr = line.split("\\t");
				if(!(arr[0].length() > 2) && StringUtils.isNumericSpace(arr[10])){
					rawCourses.add(arr);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rawCourses;
			
	}

}
