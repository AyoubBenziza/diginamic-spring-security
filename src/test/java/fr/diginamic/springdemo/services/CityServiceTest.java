package fr.diginamic.springdemo.services;

import fr.diginamic.springdemo.SpringDemoApplication;
import fr.diginamic.springdemo.entities.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = SpringDemoApplication.class)
@ActiveProfiles("test")
public class CityServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    public void insertCity() {
        try {
            departmentService.create(new Department("34"));
            assertEquals(3, departmentService.getDepartments().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
