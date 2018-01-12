package tools.spring.repositories;

import tools.spring.domain.Course;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Course, Long>{

}
