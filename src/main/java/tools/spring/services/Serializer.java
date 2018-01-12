package tools.spring.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tools.spring.domain.Course;

public class Serializer {

	private Logger log = LogManager.getLogger(this.getClass());

	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Serializer(String filename) {
		try {
			out = new ObjectOutputStream(new FileOutputStream(filename));
			in = new ObjectInputStream(new FileInputStream(filename));
		} catch (IOException e) {
			log.error("unable to setup io stream to serializer", e);
		}
	}

	public void serialize(Course object) {
		try {
			out.writeObject(object);
		} catch (IOException e) {
			log.trace("Error serializing object " + object.toString(), e);
		}
	}

	public void serialize(List<Course> objects) throws IOException {
		objects.forEach(t -> {
			try {
				out.writeObject(t);
			} catch (IOException e) {
				log.warn("Error serializing object", e);
			}
		});
	}

	public List<Course> deserialize() throws IOException {
		List<Course> list = new ArrayList<Course>();
		while (in.available() > 0) {
			try {
				list.add((Course) in.readObject());
			} catch (ClassNotFoundException | IOException e) {
				log.warn("Skip reading after {} error occured", list.get(list.size() - 1));
				in.skip(in.available());
			}
		}
		return list;
	}

	public ArrayList<Course> deserializeCourses() throws IOException {
		ArrayList<Course> list = new ArrayList<Course>();
		try {
			while (true) {
				Object raw = in.readObject();
				Course course = (Course)raw;
				list.add(course);
			}
		} catch (ClassNotFoundException | IOException e) {
			log.warn("Skip reading after error occured");
		}
		return list;
	}

	public void close() throws IOException {
		out.close();
		in.close();
	}
}
