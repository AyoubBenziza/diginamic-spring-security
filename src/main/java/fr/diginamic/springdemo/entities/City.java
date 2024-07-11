package fr.diginamic.springdemo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A City entity
 * @see Department
 * @author AyoubBenziza
 */
@Entity
@Table(name = "cities")
public class City {
    /**
     * The city id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The city name
     */
    @Column
    @NotNull
    @Size(min = 2, message = "The name must be at least 2 characters long")
    private String name;

    /**
     * The city population
     */
    @Column
    @NotNull
    @Min(value = 1, message = "The population must be a positive number")
    private int population;

    /**
     * The city department
     */
    @ManyToOne
    @JoinColumn(name = "department_code")
    private Department department;

    /**
     * Constructor
     * @param name the city name
     * @param population the city population
     */
    public City(String name, int population) {
        this.name = name;
        this.population = population;
    }

    /**
     * Default constructor
     */
    public City() {}

    /**
     * Get the city id
     * @return an integer
     */
    public int getId() {
        return id;
    }

    /**
     * Set the city id
     * @return an integer
     */
    public String getName() {
        return name;
    }

    /**
     * Set the city name
     * @param name the city name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the city population
     * @return an integer
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set the city population
     * @param population the city population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Get the city department
     * @return a Department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Set the city department
     * @param department the city department
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Check if two cities are equal
     * @param obj the object to compare
     * @return a boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof City city) {
            return city.getId() == this.getId();
        }
        return false;
    }
}
