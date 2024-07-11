package fr.diginamic.springdemo.controllers;

import com.itextpdf.text.DocumentException;
import fr.diginamic.springdemo.entities.City;
import fr.diginamic.springdemo.entities.dtos.CityDTO;
import fr.diginamic.springdemo.exceptions.InvalidException;
import fr.diginamic.springdemo.exceptions.NotFoundException;
import fr.diginamic.springdemo.mappers.CityMapper;
import fr.diginamic.springdemo.repositories.CityRepository;
import fr.diginamic.springdemo.services.CityService;
import fr.diginamic.springdemo.utils.ExportsUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A controller for the City entity
 * @see City
 * @see CityDTO
 * @see CityService
 * @see CityRepository
 * @see CityMapper
 * @see ExportsUtils
 * @see BindingResult
 * @see ResponseEntity
 *
 * @author AyoubBenziza
 */
@RestController
@RequestMapping("/cities")
public class CityController {

    /**
     * The CityService instance
     * @see CityService
     */
    @Autowired
    private CityService cityService;

    /**
     * The CityRepository instance
     * @see CityRepository
     */
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PagedResourcesAssembler<CityDTO> cityDTOPagedResourcesAssembler;

    /**
     * Get all cities
     * @return a set of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get all cities")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of cities in format JSON",
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
    @GetMapping
    public ResponseEntity<Set<CityDTO>> getCities() throws NotFoundException {
        Set<CityDTO> cities = cityService.getCities().stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(cities);
    }

    /**
     * Get cities with pagination
     *
     * @param page the page number
     * @param size the page size
     * @return a page of CityDTO
     * @see Page
     * @see Pageable
     */
    @Operation(summary = "Get cities with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Page of cities in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    )
            }
    )
    @GetMapping("/pagination")
    public PagedModel<EntityModel<CityDTO>> getCitiesWithPagination(@RequestParam @Min(0) int page, @RequestParam int size) {
        return cityDTOPagedResourcesAssembler.toModel(cityRepository.findAll(PageRequest.of(page, size)).map(CityMapper::convertToDTO));
    }

    /**
     * Get a city by its id
     * @param id the city id
     * @return a response entity
     * @throws NotFoundException if the city is not found
     */
    @Operation(summary = "Get a city by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "City not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable int id) throws NotFoundException {
        City city = cityService.getCity(id);
        CityDTO cityDTO = CityMapper.convertToDTO(city);
        return ResponseEntity.ok(cityDTO);
    }

    /**
     * Get a city by its name
     * @param name the city name
     * @return a city DTO
     * @throws NotFoundException if the city is not found
     */
    @Operation(summary = "Get a city by its name")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "City not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/search/name")
    public ResponseEntity<CityDTO> getCityByName(@RequestParam @Size(min = 1) String name) throws NotFoundException {
        City city = cityService.getCityByName(name);
        CityDTO cityDTO = CityMapper.convertToDTO(city);
        return ResponseEntity.ok(cityDTO);
    }

    /**
     * Get cities by their name starting with a given value
     * @param name the name value
     * @return a set of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get cities by their name starting with a given value")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of cities starting with the given value in format JSON",
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
    @GetMapping("/search/name/start")
    public ResponseEntity<Set<CityDTO>> getCitiesByNameStartingWith(@RequestParam String name) throws NotFoundException {
        Set<City> cities = cityService.getCitiesByNameStartingWith(name);
        Set<CityDTO> citiesDTO = cities.stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(citiesDTO);
    }

    /**
     * Get cities with a population greater than a given value
     * @param population the population value
     * @return a set of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get cities with a population greater than a given value")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of cities with a population greater than the given value in format JSON",
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
    @GetMapping("/search/population/greater")
    public ResponseEntity<Set<CityDTO>> getCitiesByPopulationGreaterThan(@RequestParam @Min(0) int population) throws NotFoundException {
        Set<City> cities = cityService.getCitiesByPopulationGreaterThan(population);
        Set<CityDTO> citiesDTO = cities.stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(citiesDTO);
    }

    /**
     * Get cities with a population range
     * @param min the minimum population value
     * @param max the maximum population value
     * @return a set of CityDTO
     * @throws NotFoundException if no cities are found
     */
    @Operation(summary = "Get cities with a population range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of cities with a population range in format JSON",
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
    @GetMapping("/search/population/range")
    public ResponseEntity<Set<CityDTO>> getCitiesByPopulationRange(@RequestParam int min, @RequestParam int max) throws NotFoundException {
        Set<City> cities = cityService.getCitiesByPopulationRange(min, max);
        Set<CityDTO> citiesDTO = cities.stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(citiesDTO);
    }

    /**
     * Add a city
     * @param city the city data
     * @param result the binding result
     * @return a response entity
     */
    @Operation(summary = "Add a city")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City added in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CityDTO> addCity(@Valid @RequestBody City city, BindingResult result) throws InvalidException {
        if (result.hasErrors()) {
            throw new InvalidException(result.getAllErrors().getFirst().getDefaultMessage());
        }
        City savedCity = cityRepository.save(city);
        CityDTO savedCityDTO = CityMapper.convertToDTO(savedCity);
        return ResponseEntity.ok(savedCityDTO);
    }

    /**
     * Update a city
     * @param id the city id
     * @param city the city data
     * @param result the binding result
     * @return a response entity
     */
    @Operation(summary = "Update a city")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City updated in format JSON",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDTO.class)
                            )}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "City not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable @Min(0) int id, @Valid @RequestBody City city, BindingResult result) throws InvalidException, NotFoundException {
        if (result.hasErrors()) {
            throw new InvalidException(result.getAllErrors().getFirst().getDefaultMessage());
        }
        City updatedCity = cityService.update(id, city);
        CityDTO updatedCityDTO = CityMapper.convertToDTO(updatedCity);
        return ResponseEntity.ok(updatedCityDTO);
    }

    /**
     * Delete a city by its id
     * @param id the city id
     * @return a response entity
     */
    @Operation(summary = "Delete a city by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City deleted",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "City not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable int id) throws NotFoundException {
        cityService.delete(id);
        return ResponseEntity.ok("City deleted");
    }

    /**
     * Export cities to a CSV file
     * @param response the HTTP response
     * @see ExportsUtils
     * @see HttpServletResponse
     */
    @Operation(summary = "Export cities to a CSV file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cities exported to a CSV file",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No cities found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/export/csv")
    public void exportCities(HttpServletResponse response) throws NotFoundException, IOException, IllegalAccessException {
        Set<CityDTO> cities = cityService.getCities().stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        ExportsUtils.toCSVFile(cities, "cities", response);
    }

    /**
     * Export a city to a CSV file
     * @param id the city id
     * @param response the HTTP response
     * @see ExportsUtils
     * @see HttpServletResponse
     * @throws NotFoundException if the city is not found
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     */
    @Operation(summary = "Export a city to a CSV file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City exported to a CSV file",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "City not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}/export/csv")
    public void exportCity(@PathVariable int id, HttpServletResponse response) throws NotFoundException, IOException, IllegalAccessException {
        City city = cityService.getCity(id);
        Set<CityDTO> cities = Set.of(CityMapper.convertToDTO(city));
        ExportsUtils.toCSVFile(cities, "city", response);
    }

    /**
     * Export cities to a PDF file
     * @param response the HTTP response
     * @throws NotFoundException if no cities are found
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     * @throws DocumentException if an error occurs during the document processing
     */
    @Operation(summary = "Export cities to a PDF file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cities exported to a PDF file",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No cities found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/export/pdf")
    public void exportCitiesToPDF(HttpServletResponse response) throws NotFoundException, IOException, IllegalAccessException, DocumentException {
        Set<CityDTO> cities = cityService.getCities().stream()
                .map(CityMapper::convertToDTO)
                .collect(Collectors.toSet());
        ExportsUtils.toPDFFile(cities, "cities", response);
    }

    /**
     * Export a city to a PDF file
     * @param id the city id
     * @param response the HTTP response
     * @throws NotFoundException if the city is not found
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if an illegal access operation is attempted
     * @throws DocumentException if an error occurs during the document processing
     */
    @Operation(summary = "Export a city to a PDF file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City exported to a PDF file",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "City not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}/export/pdf")
    public void exportCityToPDF(@PathVariable int id, HttpServletResponse response) throws NotFoundException, IOException, IllegalAccessException, DocumentException {
        City city = cityService.getCity(id);
        Set<CityDTO> cities = Set.of(CityMapper.convertToDTO(city));
        ExportsUtils.toPDFFile(cities, "city", response);
    }
}
