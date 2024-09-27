package ExamTest;
import javafx.application.Platform;

import javafx.stage.Stage;
import javafx.stage.Window;

import com.jdojo.intro.DbConnexion;
import com.jdojo.intro.ExamenApp;

import static org.assertj.core.api.Assertions.assertThat;


import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestAssertJ {
	
	    private static ExamenApp app;

	    @BeforeAll
	    public static void initToolkit() {
	        Platform.startup(() -> {});
	        app = new ExamenApp();
	    }

	    
	     // Test AssertJ pour vérifier que le label affiche le bon résultat après une recherche avec un matricule valide.
	     
	    
	    @Test
	    public void testLabelResultatAvecMatriculeValideAssertJ() {
	        Platform.runLater(() -> {
	        	app.inputMatricule.setText("001");
		        app.effectuerRecherche();
		        assertThat(app.resultatLabel.getText()).isEqualTo("Résultat :");
	        });
	    }

	    
	     // Test AssertJ pour s'assurer que le bouton "Détails" devient visible après une recherche réussie.
	    
	    @Test
	    public void testBoutonDetailsVisibleAssertJ() {
	        
	        
	        Platform.runLater(() -> {
	        	app.inputMatricule.setText("ABC123");
		        app.effectuerRecherche();
		        assertThat(app.boutonDetails.isVisible()).isTrue();
	        });
	    }

	   
	      //Test AssertJ pour vérifier que la barre de progression est masquée après la recherche.
	   
	    
	    @Test
	    public void testProgressBarMasqueeApresRechercheAssertJ() {
	        
	        
	        Platform.runLater(() -> {
	        	app.inputMatricule.setText("ABC123");
		        app.effectuerRecherche();
		        assertThat(app.barreDeProgression.isVisible()).isFalse();
	        });
	    }

	    
	    
	     //Test AssertJ pour vérifier que la fenêtre de détails s'affiche correctement après la recherche.
	    
	    
	    @Test
	    public void testAfficherDetailsEtudiantValideAssertJ() {
	        
	        
	        Platform.runLater(() -> {
	        	app.inputMatricule.setText("ABC123");
		        app.afficherDetails();
		        assertThat(Stage.getWindows()).anyMatch(Window::isShowing);
	        });
	    }

	    
	    
	     /* Test AssertJ pour vérifier que le label affiche une erreur en cas d'exception dans la base de données.
	      @throws SQLException */
	     
	    
	    @Test
	    public void testLabelErreurAfficheeAssertJ() throws SQLException {
	    	
	    	Platform.runLater(() -> {
	    		try {
					DbConnexion.getConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        app.effectuerRecherche();
		        assertThat(app.resultatLabel.getText()).isEqualTo("Erreur lors de la recherche.");
	        });
	        
	  
	    }
	}
	




