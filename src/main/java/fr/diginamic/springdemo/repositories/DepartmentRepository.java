package fr.diginamic.springdemo.repositories;

import fr.diginamic.springdemo.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Repository for the Department entity
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    /**
     * Find a department by its code
     * @param code the code of the department
     * @return the department
     */
    Department findByCode(String code);

    /**
     * Check if a department exists by its code
     * @param code the code of the department
     * @return the department
     */
    boolean existsByCode(String code);

    /**
     * Find a department by its name
     * @param name the name of the department
     * @return the department
     */
    Department findByName(String name);

    /**
     * Find departments by their name starting with a given string
     * @param name the string to search for
     * @return the departments
     */
    Set<Department> findByNameStartingWith(String name);

    /**
     * Delete a department by its code
     * @param code the code of the department
     */
    void deleteByCode(String code);
}
