package fr.diginamic.springdemo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * A Department entity
 * @see City
 * @author AyoubBenziza
 */
@Entity
@Table(name = "department")
public class Department {
    /**
     * The department code
     */
    @Id
    @NotNull
    @Size(min = 2, max = 5)
    private String code;

    /**
     * The department name
     */
    @Column
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    /**
     * The department cities
     */
    @OneToMany(mappedBy = "department",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<City> cities = new HashSet<>();

    /**
     * Constructor
     * @param code the department code
     */
    public Department(String code) {
        this.code = code;
    }

    /**
     * Default constructor
     */
    public Department() {
    }

    /**
     * Get the department code
     * @return a string
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the department code
     * @param code the department code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get the department name
     * @return a string
     */
    public String getName() {
        return name;
    }

    /**
     * Set the department name
     * @param name the department name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the department population (sum of all cities' populations)
     * @return an integer
     */
    public int getPopulation() {
        return cities.stream().mapToInt(City::getPopulation).sum();
    }

    /**
     * Get the department cities
     * @return a set of City
     */
    public Set<City> getCities() {
        return cities;
    }

    /**
     * Set the department cities
     * @param cities a set of City
     */
    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    /**
     * Add a city to the department
     * @param city the city to add
     */
    public void addCity(City city) {
        cities.add(city);
    }

    /**
     * Remove a city from the department
     * @param city the city to remove
     */
    public void removeCity(City city) {
        cities.remove(city);
    }

    /**
     * Get the department cities count
     * @param obj the object to compare
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Department department = (Department) obj;
        return code.equals(department.code);
    }
}
