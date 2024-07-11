package fr.diginamic.springdemo.repositories;

import fr.diginamic.springdemo.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Repository for the City entity
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    /**
     * Find a city by its name
     * @param name the name of the city
     * @return the city
     */
    City findByName(String name);

    /**
     * Find cities by their name starting with a given string
     * @param name the string to search for
     * @return the cities
     */
    Set<City> findByNameStartingWith(String name);

    /**
     * Find cities by their population
     * @param population the population to search for
     * @return the cities
     */
    Set<City> findByPopulationIsGreaterThan(int population);

    /**
     * Find cities by their population
     * @param min the minimum population
     * @param max the maximum population
     * @return the cities
     */
    Set<City> findByPopulationBetween(int min, int max);

    /**
     * Find cities in a department
     * @param departmentCode the code of the department
     * @return the cities
     */
    Set<City> findCitiesByDepartment_Code(String departmentCode);

    /**
     * Find cities in a department by their population
     * @param departmentCode the code of the department
     * @param minPopulation the minimum population
     * @param maxPopulation the maximum population
     * @return the cities
     */
    Set<City> findCitiesByPopulationBetweenAndDepartment_Code(int minPopulation, int maxPopulation, String departmentCode);

    Page<City> findAllByDepartment_CodeOrderByPopulationDesc(String departmentCode, Pageable pageable);
}
