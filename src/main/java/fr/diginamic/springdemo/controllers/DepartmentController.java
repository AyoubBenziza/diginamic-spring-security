package fr.diginamic.springdemo.controllers;

import com.itextpdf.text.DocumentException;
import fr.diginamic.springdemo.entities.City;
import fr.diginamic.springdemo.entities.Department;
import fr.diginamic.springdemo.entities.dtos.CityDTO;
import fr.diginamic.springdemo.entities.dtos.DepartmentDTO;
import fr.diginamic.springdemo.exceptions.InvalidException;
import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.mappers.CityMapper;
import fr.diginamic.springdemo.mappers.DepartmentMapper;
import fr.diginamic.springdemo.repositories.DepartmentRepository;
import fr.diginamic.springdemo.services.DepartmentService;
import fr.diginamic.springdemo.utils.ExportsUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A controller for the Department entity
 * @see Department
 * @see DepartmentDTO
 * @see DepartmentService
 *
 * @author AyoubBenziza
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    /**
     * The DepartmentService instance
     * @see DepartmentService
     */
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PagedResourcesAssembler<DepartmentDTO> departmentDTOPagedResourcesAssembler;

    /**
     * Get all departments
     * @return a set of DepartmentDTO
     * @throws NotFoundException if no departments are found
     * @see Department
     * @see DepartmentDTO
     * @see DepartmentService
     */
    @Operation(summary = "Get all departments")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of departments in format JSON",
                        content = {@Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = DepartmentDTO.class)
                        )}
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "No departments found",
                        content = @Content
                )
            }
    )
    @GetMapping
    public ResponseEntity<Set<DepartmentDTO>> getDepartments() throws NotFoundException {
        Set<Department> departments = departmentService.getDepartments();
        Set<DepartmentDTO> departmentDTOS = departments.stream()
                .map(DepartmentMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(departmentDTOS);
    }

    /**
     * Get departments with pagination
     * @param page the page number
     * @param size the page size
     * @return a page of DepartmentDTO
     * @see Page
     * @see PageRequest
     * @see Department
     * @see DepartmentDTO
     * @see DepartmentRepository
     * @see DepartmentMapper
     */
    @Operation(summary = "Get departments with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Page of departments in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No departments found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/pagination")
    public PagedModel<EntityModel<DepartmentDTO>> getDepartmentsPagination(@RequestParam @Min(0) int page, @RequestParam int size) {
        return departmentDTOPagedResourcesAssembler.toModel(departmentRepository.findAll(PageRequest.of(page, size)).map(DepartmentMapper::convertToDTO));
    }

    /**
     * Get a department by its code
     * @param code the department code
     * @return a DepartmentDTO
     * @throws NotFoundException if the department is not found
     */
    @Operation(summary = "Get a department by its code")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{code}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable String code) throws NotFoundException {
        Department department = departmentService.getDepartment(code);
        DepartmentDTO departmentDTO = DepartmentMapper.convertToDTO(department);
        return ResponseEntity.ok(departmentDTO);
    }

    /**
     * Get a department by its name
     * @param name the department name
     * @return a DepartmentDTO
     * @throws NotFoundException if the department is not found
     */
    @Operation(summary = "Get a department by its name")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/search/name")
    public ResponseEntity<DepartmentDTO> getDepartmentByName(@RequestParam String name) throws NotFoundException {
        Department department = departmentService.getDepartmentByName(name);
        DepartmentDTO departmentDTO = DepartmentMapper.convertToDTO(department);
        return ResponseEntity.ok(departmentDTO);
    }

    /**
     * Get departments by name starting with a given string
     * @param name the department name
     * @return a set of DepartmentDTO
     * @throws NotFoundException if no departments are found
     * @see Department
     * @see DepartmentDTO
     * @see DepartmentService
     */
    @Operation(summary = "Get departments by name starting with a given string")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of departments with names starting with the given string in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No departments found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/search/name/start")
    public ResponseEntity<Set<DepartmentDTO>> getDepartmentsByNameStartingWith(@RequestParam String name) throws NotFoundException {
        Set<Department> departments = departmentService.getDepartmentsStartingWith(name);
        Set<DepartmentDTO> departmentDTOS = departments.stream()
                .map(DepartmentMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(departmentDTOS);
    }

    /**
     * Get cities in a department
     * @param code the department code
     * @return a set of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get cities in a department")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of cities in the department in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No cities found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{code}/cities")
    public ResponseEntity<Set<CityDTO>> getCitiesInDepartment(@PathVariable String code) throws NotFoundException {
        Set<City> cities = departmentService.getCities(code);
        Set<CityDTO> cityDTOS = cities.stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(cityDTOS);
    }

    /**
     * Get the most populated cities in a department
     * @param code the department code
     * @param nbCities the number of cities to return
     * @return a list of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get the most populated cities in a department")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of the most populated cities in the department in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No cities found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{code}/cities/mostPopulated")
    public ResponseEntity<Set<CityDTO>> getTopNCitiesInDepartment(@PathVariable String code, @RequestParam int nbCities) throws NotFoundException {
        List<City> cities = departmentService.getTopNCities(code, nbCities);
        Set<CityDTO> cityDTOS = cities.stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(cityDTOS);
    }

    /**
     * Get cities in a department with a population range
     * @param code the department code
     * @param min the minimum population
     * @param max the maximum population
     * @return a set of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get cities in a department with a population range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of cities in the department with a population range in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No cities found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{code}/cities/search/population/range")
    public ResponseEntity<Set<CityDTO>> getCitiesInDepartmentWithPopulationBetween(@PathVariable String code, @RequestParam int min, @RequestParam int max) throws NotFoundException {
        Set<City> cities = departmentService.getCitiesWithPopulationRange(code, min, max);
        Set<CityDTO> cityDTOS = cities.stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(cityDTOS);
    }

    /**
     * Add a department
     * @param department the department to add
     * @param result the binding result
     * @return a DepartmentDTO
     * @throws InvalidException if the request is invalid
     * @throws NotFoundException if the department is not found
     */
    @Operation(summary = "Add a department")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department added in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(@Valid @RequestBody Department department, BindingResult result) throws InvalidException, NotFoundException {
        if (result.hasErrors()) {
            throw new InvalidException(result.getAllErrors().getFirst().getDefaultMessage());
        }
        Department newDepartment = departmentService.create(department);
        return ResponseEntity.ok(DepartmentMapper.convertToDTO(newDepartment));
    }

    /**
     * Add cities to a department
     * @param code the department code
     * @param cities the cities to add
     * @return a DepartmentDTO
     * @throws InvalidException if the request is invalid
     * @throws NotFoundException if the department is not found
     */
    @Operation(summary = "Add cities to a department")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cities added to the department in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{code}/cities")
    public ResponseEntity<DepartmentDTO> addCitiesToDepartment(@PathVariable String code, @Valid @RequestBody Set<City> cities, BindingResult result) throws InvalidException, NotFoundException {
        if (result.hasErrors()) {
            throw new InvalidException(result.getAllErrors().getFirst().getDefaultMessage());
        }
        Department updatedDepartment = departmentService.addCities(code, cities);
        return ResponseEntity.ok(DepartmentMapper.convertToDTO(updatedDepartment));
    }

    /**
     * Update a department
     * @param code the department code
     * @param department the department to update
     * @return a DepartmentDTO
     * @throws InvalidException if the request is invalid
     * @throws NotFoundException if the department is not found
     */
    @Operation(summary = "Update a department")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department updated in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{code}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable String code, @Valid @RequestBody Department department, BindingResult result) throws InvalidException, NotFoundException {
        if (result.hasErrors()) {
            throw new InvalidException(result.getAllErrors().getFirst().getDefaultMessage());
        }
        Department updatedDepartment = departmentService.update(code, department);
        return ResponseEntity.ok(DepartmentMapper.convertToDTO(updatedDepartment));
    }

    /**
     * Delete a department
     * @param code the department code
     * @return a response entity
     * @throws NotFoundException if the department is not found
     */
    @Operation(summary = "Delete a department")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department deleted",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteDepartment(@PathVariable String code) throws NotFoundException {
        departmentService.delete(code);
        return ResponseEntity.ok("Department deleted");
    }

    /**
     * Export all departments to a PDF file
     * @param response the HttpServletResponse
     * @throws NotFoundException if no departments are found
     * @throws DocumentException if an error occurs during the document creation
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     */
    @Operation(summary = "Export all departments to a PDF file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF file of all departments",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No departments found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws NotFoundException, DocumentException, IOException, IllegalAccessException {
        Set<DepartmentDTO> departments = departmentService.getDepartments().stream()
                .map(DepartmentMapper::convertToDTO)
                .collect(Collectors.toSet());
        ExportsUtils.toPDFFile(departments, "departments", response);
    }

    /**
     * Export a department to a PDF file
     * @param code the department code
     * @param response the HttpServletResponse
     * @throws NotFoundException if the department is not found
     * @throws DocumentException if an error occurs during the document creation
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     */
    @Operation(summary = "Export a department to a PDF file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF file of the department",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("{code}/export/pdf")
    public void exportOneToPDF(@PathVariable String code, HttpServletResponse response) throws NotFoundException, DocumentException, IOException, IllegalAccessException {
        DepartmentDTO department = DepartmentMapper.convertToDTO(departmentService.getDepartment(code));
        ExportsUtils.toPDFFile(Set.of(department), "department", response);
    }

    /**
     * Export all departments to a CSV file
     * @param response the HttpServletResponse
     * @throws NotFoundException if no departments are found
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     */
    @Operation(summary = "Export all departments to a CSV file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CSV file of all departments",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No departments found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws NotFoundException, IOException, IllegalAccessException {
        Set<DepartmentDTO> departments = departmentService.getDepartments().stream()
                .map(DepartmentMapper::convertToDTO)
                .collect(Collectors.toSet());
        ExportsUtils.toCSVFile(departments, "departments", response);
    }

    /**
     * Export a department to a CSV file
     * @param code the department code
     * @param response the HttpServletResponse
     * @throws NotFoundException if the department is not found
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     */
    @Operation(summary = "Export a department to a CSV file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CSV file of the department",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Department not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("{code}/export/csv")
    public void exportOneToCSV(@PathVariable String code, HttpServletResponse response) throws NotFoundException, IOException, IllegalAccessException {
        DepartmentDTO department = DepartmentMapper.convertToDTO(departmentService.getDepartment(code));
        ExportsUtils.toCSVFile(Set.of(department), "department", response);
    }
}
