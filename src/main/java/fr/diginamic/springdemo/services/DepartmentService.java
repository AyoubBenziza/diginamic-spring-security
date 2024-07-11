package fr.diginamic.springdemo.services;

import fr.diginamic.springdemo.entities.City;
import fr.diginamic.springdemo.entities.Department;
import fr.diginamic.springdemo.entities.dtos.DepartmentDTO;
import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.repositories.CityRepository;
import fr.diginamic.springdemo.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for the Department entity
 * This class is used to interact with the DepartmentRepository
 */
@Service
public class DepartmentService {

    private final String apiUrl = "https://geo.api.gouv.fr/departements";

    /**
     * The RestTemplate
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * The DepartmentRepository
     */
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * The CityRepository
     */
    @Autowired
    private CityRepository cityRepository;

    /**
     * Get all departments
     * @return a set of departments
     * @throws NotFoundException if no departments are found
     * @see Department
     * @see DepartmentRepository
     */
    public Set<Department> getDepartments() throws NotFoundException {
        Set<Department> departments = new HashSet<>(departmentRepository.findAll());
        if (departments.isEmpty()) {
            throw new NotFoundException("No departments found");
        }
        return departments;
    }

    /**
     * Get a department by its code
     * @param code the code of the department
     * @return the department
     * @throws NotFoundException if the department is not found
     * @see Department
     * @see DepartmentRepository
     */
    public Department getDepartment(String code) throws NotFoundException {
        Department department = departmentRepository.findByCode(code);
        if (department == null) {
            throw new NotFoundException("Department with code " + code + " not found");
        }
        return department;
    }

    /**
     * Get a department by its name
     * @param name the name of the department
     * @return the department
     * @throws NotFoundException if the department is not found
     * @see Department
     * @see DepartmentRepository
     */
    public Department getDepartmentByName(String name) throws NotFoundException {
        Department department = departmentRepository.findByName(name);
        if (department == null) {
            throw new NotFoundException("Department with name " + name + " not found");
        }
        return department;
    }

    /**
     * Get a set of departments starting with a given name
     * @param name the name
     * @return a set of departments
     * @throws NotFoundException if no departments are found
     * @see Department
     * @see DepartmentRepository
     * @see NotFoundException
     */
    public Set<Department> getDepartmentsStartingWith(String name) throws NotFoundException {
        Set<Department> departments = departmentRepository.findByNameStartingWith(name);
        if (departments.isEmpty()) {
            throw new NotFoundException("No departments found starting with " + name);
        }
        return departments;
    }

    /**
     * Get the cities in a department
     * @param code the code of the department
     * @return a set of cities
     * @throws NotFoundException if no cities are found
     * @see City
     * @see CityRepository
     */
    public Set<City> getCities(String code) throws NotFoundException {
        Set<City> cities = cityRepository.findCitiesByDepartment_Code(code);
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found in department with code " + code);
        }
        return cities;
    }

    /**
     * Get the top N cities in a department
     * @param code the code of the department
     * @param nbCities the number of cities to get
     * @return a list of cities
     * @throws NotFoundException if no cities are found
     * @see City
     * @see CityRepository
     * @see Limit
     */
    public List<City> getTopNCities(String code, int nbCities) throws NotFoundException {
        Page<City> citiesPage = cityRepository.findAllByDepartment_CodeOrderByPopulationDesc(code, PageRequest.of(0, nbCities));
        List<City> cities = (citiesPage.getContent());
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found in department with code " + code);
        }
        return cities;
    }

    public Set<City> getCitiesWithPopulationRange(String code, int minPopulation, int maxPopulation) throws NotFoundException {
        Set<City> cities = cityRepository.findCitiesByPopulationBetweenAndDepartment_Code(minPopulation, maxPopulation, code);
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found in department with code " + code);
        }
        return cities;
    }

    /**
     * Add a list of cities to a department
     * @param code the code of the department
     * @param cities the list of cities
     * @return the department
     */
    public Department addCities(String code, Set<City> cities) throws NotFoundException {
        Department department = departmentRepository.findByCode(code);
        if (department == null) {
            throw new NotFoundException("Department with code " + code + " not found");
        }
        cities.forEach(city -> city.setDepartment(department));
        cityRepository.saveAll(cities);
        return department;
    }

    /**
     * Update a department
     * @param code the code of the department
     * @param department the department
     */
    public Department update(String code, Department department) throws NotFoundException {
        Department departmentToUpdate = departmentRepository.findByCode(code);
        if (departmentToUpdate == null) {
            throw new NotFoundException("Department with code " + code + " not found");
        }
        departmentToUpdate.setCode(department.getCode());
        departmentToUpdate.setCities(department.getCities());
        departmentRepository.save(departmentToUpdate);
        return departmentToUpdate;
    }

    public void delete(String code) throws NotFoundException {
        if (!departmentRepository.existsByCode(code)) {
            throw new NotFoundException("Department with code " + code + " not found");
        }
        departmentRepository.deleteByCode(code);
    }

    /**
     * Add the name of a department based on its code.
     * This method fetches the department name using an external API and sets it on the provided Department entity.
     *
     * @param department The department entity to update.
     * @param code       The code of the department to fetch the name for.
     */
    public void addName(Department department, String code) {
        if (department != null) {
            String url = apiUrl + "?code=" + code + "&fields=nom,code";

            try {
                DepartmentDTO[] departmentDTOs = restTemplate.getForObject(url, DepartmentDTO[].class);
                if (departmentDTOs != null && departmentDTOs.length > 0) {
                    DepartmentDTO firstDepartmentDTO = departmentDTOs[0];
                    department.setName(firstDepartmentDTO.getName());
                } else {
                    // Handle case where no department name is returned
                    throw new NotFoundException("No department found with code " + code);
                }
            } catch (Exception e) {
                // Handle errors during API call
                throw new RuntimeException("Failed to fetch department name for code " + code, e);
            }
        }
    }

    /**
     * Create a department
     * @param department the department
     * @return the department
     * @throws NotFoundException if the department is not found
     */
    public Department create(Department department) throws NotFoundException {
        addName(department, department.getCode()); // Set the department name using the addName method
        departmentRepository.save(department); // Save the department to the database
        return department;
    }
}
