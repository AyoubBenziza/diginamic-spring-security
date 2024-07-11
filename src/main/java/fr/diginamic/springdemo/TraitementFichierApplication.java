package fr.diginamic.springdemo;

import fr.diginamic.springdemo.utils.ImportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

/**
 * Main class for the TraitementFichier application
 */
@SpringBootApplication
public class TraitementFichierApplication implements CommandLineRunner {

    /**
     * The path to the cities CSV file
     */
    private static final String CITIES_CSV_PATH = Objects.requireNonNull(TraitementFichierApplication.class.getClassLoader().getResource("recensement.csv")).getPath();

    /**
     * The headers of the cities CSV file
     */
    private static final String[] CITIES_CSV_HEADERS = {"codeRegion", "nameRegion", "codeDepartment", "codeArrondissement", "codeCanton", "codeCommune", "nameCommune", "populationMunicipale", "populationComptéeAPart", "populationTotale"};

    /**
     * The ImportUtils
     */
    @Autowired
    private ImportUtils importUtils;

    /**
     * Main method
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.out.println("Application started");
        SpringApplication app = new SpringApplication(TraitementFichierApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    /**
     * Run the application
     * @param args the arguments
     */
    @Override
    public void run(String... args) {
        importUtils.mostPopulatedCitiesCSV(CITIES_CSV_PATH, CITIES_CSV_HEADERS, 1000);
    }
}
