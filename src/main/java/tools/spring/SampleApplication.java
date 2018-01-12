package tools.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tools.spring.domain.Course;
import tools.spring.domain.Extension;
import tools.spring.repositories.CustomerRepository;
import tools.spring.services.CourseCollectorService;


@PropertySource({"file:${user.home}/application.properties", "classpath:application.yml"})
@RestController
@SpringBootApplication
public class SampleApplication implements CommandLineRunner{
	
	Logger log = LogManager.getLogger(getClass());
	
	@Value("${jvm}")
	String jvm;
	
	@Value("${groupId}")
	String groupid;
	
	@Value("${dependency}")
	String depend;
	
	@Value("${cougarnet.username}")
	String username;
	
	@Value("${cougarnet.password}")
	String password;
	
	@Value("${update.courses}")
	boolean updateCourses;
	
	@Autowired
	JdbcTemplate jdbc;
	
	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}
	
	private CourseCollectorService ccs = new CourseCollectorService();

	@Override
	public void run(String... args) throws Exception {
		log.info(jvm);
		log.info(groupid);
		log.info(depend);
//		log.info(Arrays.toString(jdbc.queryForList("select name from TEST", String.class).toArray()));
		log.info("Starting up services");
		System.setProperty("cougarnet.password", password);
		System.setProperty("cougarnet.username", username);
//		PDFReader.read();
//		PDFReader.setCatalog();
//		PDFReader.parseDoc();
		try {
//			ccs.setCourseRepository(courseRepository);
			Course course = new Course();
			course.setSelect(false);
//			course.setCRN(27);
			course.setCmp("hi");
			course.setCredLower(2);
			course.setCredUpper(5);
			course.setCrse("course");
			course.setSec("section");
			course.setSelect(false);
			course.setSubj("subject");
			course.setTitle("title");
			course.setExtension(new Extension());
			course.setPrereqs(new ArrayList<>());
			repo.save(course);
			ccs.build(updateCourses);
			List<Course> courseList = ccs.getCourses();
			repo.saveAll(courseList);
		}catch(Exception e) {
			log.error(e);
		}
		log.info(repo.count());
//		new PDFReader().parseCatalogGeneratedText();
		log.error("Fine");
	}
	
	CustomerRepository repo;
	
	@Bean
	public CommandLineRunner demogog(CustomerRepository repo) {
		this.repo = repo;
		return (args) -> {};
	}

	@RequestMapping("/jdbc")
	public void demo() {
		Course course = new Course();
		course.setCmp("justin");
		course.setCrse("course");
		repo.save(course);
		repo.findAll().forEach(log::info);
	}	
	
	@RequestMapping("/wipe")
	public void wipe() {
		repo.deleteAll();
	}

	String text = "";
	
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
    	jdbc.queryForList("select * from Customer", Course.class).forEach(customer ->{
    		log.info(customer.toString());
    		text += customer.toString() + "\n";
    	});
        return text;
    }
}
