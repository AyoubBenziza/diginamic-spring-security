package fr.diginamic.springdemo.entities.dtos;

import fr.diginamic.springdemo.annotations.csv.CSVField;
import fr.diginamic.springdemo.annotations.pdf.PDFValue;
import fr.diginamic.springdemo.entities.City;

/**
 * A DTO for the City entity
 * @see fr.diginamic.springdemo.entities.City
 * @author AyoubBenziza
 */
public class CityDTO {
    /**
     * The city name
     */
    @CSVField(name = "Name", order = 1)
    @PDFValue(name = "Name", order = 1)
    private final String name;

    /**
     * The city population
     */
    @CSVField(name = "Population", order = 2)
    @PDFValue(name = "Population", order = 2)
    private final int population;

    /**
     * The city department code
     */
    @CSVField(name = "Department Code", order = 3)
    private final String departmentCode;

    /**
     * Constructor
     * @param name the city name
     * @param population the city population
     * @param departmentCode the city department code
     */
    public CityDTO(String name, int population, String departmentCode) {
        this.name = name;
        this.population = population;
        this.departmentCode = departmentCode;
    }

    /**
     * Constructor
     * @param city the city
     */
    public CityDTO(City city) {
        this.name = city.getName();
        this.population = city.getPopulation();
        this.departmentCode = city.getDepartment().getCode();
    }

    /**
     * Get the city name
     * @return a string
     */
    public String getName() {
        return name;
    }

    /**
     * Get the city population
     * @return an integer
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Get the city department code
     * @return a string
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", population=" + population +
                ", departmentCode='" + departmentCode + '\'' +
                '}';
    }
}
