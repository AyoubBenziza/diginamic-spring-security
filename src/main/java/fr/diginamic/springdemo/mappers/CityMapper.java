package fr.diginamic.springdemo.mappers;

import fr.diginamic.springdemo.entities.City;
import fr.diginamic.springdemo.entities.dtos.CityDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper for the City entity
 */
@Component
public class CityMapper {
    /**
     * Convert a City to a CityDTO
     * @param city the city
     * @return the CityDTO
     */
    public static CityDTO convertToDTO(City city) {
        if (city != null) {
            String departmentCode = city.getDepartment() != null ? city.getDepartment().getCode() : "N/A";
            return new CityDTO(city.getName(), city.getPopulation(), departmentCode);
        }
        return null;
    }
}
