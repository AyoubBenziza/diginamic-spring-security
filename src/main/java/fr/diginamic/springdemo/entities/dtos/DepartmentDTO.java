package fr.diginamic.springdemo.entities.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fr.diginamic.springdemo.annotations.csv.CSVField;
import fr.diginamic.springdemo.annotations.pdf.PDFList;
import fr.diginamic.springdemo.annotations.pdf.PDFValue;

import java.util.Set;

/**
 * A DTO for the Department entity
 * @see fr.diginamic.springdemo.entities.Department
 * @see fr.diginamic.springdemo.entities.City
 * @see fr.diginamic.springdemo.entities.dtos.CityDTO
 * @author AyoubBenziza
 */
@JsonPropertyOrder({"name", "population", "cities"})
public class DepartmentDTO {
    /**
     * The department name
     */
    @CSVField(name = "Name", order = 1)
    @PDFValue(name = "Name", order = 1)
    private String name;

    /**
     * The department population
     */
    @CSVField(name = "Population", order = 2)
    @PDFValue(name = "Population", order = 2)
    private int population;

    /**
     * The department cities
     */
    @PDFList(name = "Cities", headers = {"Name", "Population"})
    private Set<CityDTO> cities;

    /**
     * Constructor
     * @param population the department population
     */
    public DepartmentDTO(int population) {
        this.population = population;
    }

    /**
     * Default constructor
     */
    public DepartmentDTO() {
    }

    /**
     * Get the department name
     * @return a string
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Set the department name
     * @param name the department name
     */
    @JsonAlias("nom")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the department population (sum of all cities' populations)
     * @return an integer
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set the department population
     * @param population the department population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Get the department cities
     * @return a set of CityDTO
     */
    public Set<CityDTO> getCities() {
        return cities;
    }

    /**
     * Set the department cities
     * @param cities a set of CityDTO
     */
    public void setCities(Set<CityDTO> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", population=" + population +
                ", cities=" + cities +
                '}';
    }
}
