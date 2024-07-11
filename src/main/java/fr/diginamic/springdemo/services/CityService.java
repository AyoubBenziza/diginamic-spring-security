package fr.diginamic.springdemo.services;

import fr.diginamic.springdemo.entities.City;
import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service for the City entity
 * This class is used to interact with the CityRepository
 * @author AyoubBenziza
 */
@Service
public class CityService {

    /**
     * The CityRepository
     */
    @Autowired
    private CityRepository cityRepository;

    /**
     * Get all cities
     * @return a set of cities
     * @throws NotFoundException if no cities are found
     * @see City
     * @see CityRepository
     */
    public Set<City> getCities() throws NotFoundException {
        Set<City> cities = new HashSet<>(cityRepository.findAll());
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found");
        }
        return cities;
    }

    /**
     * Get a city by its id
     * @param id the id of the city
     * @return the city
     * @throws NotFoundException if the city is not found
     * @see City
     * @see CityRepository
     */
    public City getCity(int id) throws NotFoundException {
        City city = cityRepository.findById(id).orElse(null);
        if (city == null) {
            throw new NotFoundException("City with id " + id + " not found");
        }
        return city;
    }

    /**
     * Get a city by its name
     * @param name the name of the city
     * @return the city
     * @throws NotFoundException if the city is not found
     * @see City
     * @see CityRepository
     */
    public City getCityByName(String name) throws NotFoundException {
        City city = cityRepository.findByName(name);
        if (city == null) {
            throw new NotFoundException("City with name " + name + " not found");
        }
        return city;
    }

    /**
     * Get cities by their name starting with a given string
     * @param name the string to search for
     * @return the cities
     * @throws NotFoundException if no cities are found
     * @see City
     * @see CityRepository
     */
    public Set<City> getCitiesByNameStartingWith(String name) throws NotFoundException {
        Set<City> cities = new HashSet<>(cityRepository.findByNameStartingWith(name));
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found starting with " + name);
        }
        return cities;
    }

    /**
     * Get cities by their population
     * @param population the population to search for
     * @return the cities
     * @throws NotFoundException if no cities are found
     * @see City
     * @see CityRepository
     */
    public Set<City> getCitiesByPopulationGreaterThan(int population) throws NotFoundException {
        Set<City> cities = new HashSet<>(cityRepository.findByPopulationIsGreaterThan(population));
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found with a population greater than " + population);
        }
        return cities;
    }

    /**
     * Get cities by range of population
     * @param min the minimum population
     * @param max the maximum population
     * @return the cities
     * @throws NotFoundException if no cities are found
     * @see City
     * @see CityRepository
     */
    public Set<City> getCitiesByPopulationRange(int min, int max) throws NotFoundException {
        Set<City> cities = new HashSet<>(cityRepository.findByPopulationBetween(min, max));
        if (cities.isEmpty()) {
            throw new NotFoundException("No cities found with a population between " + min + " and " + max);
        }
        return cities;
    }

    /**
     * Update a city
     * @param id the id of the city
     * @param city the city
     * @return the updated city
     * @throws NotFoundException if the city is not found
     */
    public City update(int id, City city) throws NotFoundException {
        City cityToUpdate = cityRepository.findById(id).orElse(null);
        if (cityToUpdate == null) {
            throw new NotFoundException("City with id " + id + " not found");
        }
        cityToUpdate.setName(city.getName());
        cityToUpdate.setPopulation(city.getPopulation());
        cityToUpdate.setDepartment(city.getDepartment());
        cityRepository.save(cityToUpdate);
        return cityToUpdate;
    }

    /**
     * Delete a city
     * @param id the id of the city
     * @throws NotFoundException if the city is not found
     */
    public void delete(int id) throws NotFoundException {
        City city = cityRepository.findById(id).orElse(null);
        if (city == null) {
            throw new NotFoundException("City with id " + id + " not found");
        }
        cityRepository.delete(city);
    }
}
