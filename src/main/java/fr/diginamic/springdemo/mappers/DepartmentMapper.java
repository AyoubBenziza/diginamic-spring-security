package fr.diginamic.springdemo.mappers;

import fr.diginamic.springdemo.entities.Department;
import fr.diginamic.springdemo.entities.dtos.CityDTO;
import fr.diginamic.springdemo.entities.dtos.DepartmentDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the Department entity
 */
@Component
public class DepartmentMapper {
    /**
     * Convert a Department to a DepartmentDTO
     * @param department the department
     * @return the DepartmentDTO
     */
    public static DepartmentDTO convertToDTO(Department department) {
        if (department != null) {
            DepartmentDTO departmentDTO = new DepartmentDTO(department.getPopulation());
            departmentDTO.setName(department.getName());
            Set<CityDTO> cityDTOs = department.getCities().stream()
                    .map(city -> new CityDTO(city.getName(), city.getPopulation(), city.getDepartment().getCode()))
                    .collect(Collectors.toSet());
            departmentDTO.setCities(cityDTOs);
            return departmentDTO;
        }
        return null;
    }
}
