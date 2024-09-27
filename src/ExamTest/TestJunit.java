package ExamTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import com.jdojo.intro.DbConnexion;
import com.jdojo.intro.ExamenApp;


import javafx.application.Platform;
//import javafx.stage.Stage;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class TestJunit {

    static ExamenApp app;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {});
        app = new ExamenApp();
    }

    // Test de la connexion à la base de données
    
    @Test
    void testDatabaseConnection() throws SQLException {
        Connection cnx = DbConnexion.getConnection();
        assertThat(cnx).as("La connexion à la base de données doit être établie").isNotNull();
    }

    // Test de la recherche avec un matricule valide
    
    @Test
    void testEffectuerRechercheValidMatricule() {
        Platform.runLater(() -> {
            app.inputMatricule.setText("001");
            app.effectuerRecherche();
            assertThat(app.resultatLabel.getText()).contains("Résultat :");
        });
    }
    

    // Test de la recherche avec un matricule invalide
    
    @Test
    void testEffectuerRechercheInvalidMatricule() {
        
        Platform.runLater(() -> {
        	app.inputMatricule.setText("999999"); // Matricule qui n'existe pas
            app.effectuerRecherche();
            assertThat(app.resultatLabel.getText()).isEqualTo("Aucun résultat trouvé pour ce matricule.");
        });
    }

    // Test de l'affichage des détails
    
    @Test
    void testAfficherDetails() {
     
        Platform.runLater(() -> {
        	app.inputMatricule.setText("001"); // Matricule valide
        	app.effectuerRecherche();
            app.boutonDetails.fire(); // Simuler un clic sur le bouton
            assertThat(app.boutonDetails.isVisible()).isTrue();
        });
    }

    
    // Test de la visibilité du bouton de détails
   
    @Test
    void testDetailsButtonHiddenByDefault() {
        Platform.runLater(() -> {
            ExamenApp app = new ExamenApp();
            app.start(new Stage());  // Démarrer l'application
            // Vérifie si le bouton est caché par défaut
            assertFalse(app.boutonDetails.isVisible(), 
                "Le bouton Détails doit être caché par défaut.");
        });
    }
}
