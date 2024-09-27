package com.jdojo.intro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos; // Pour centrer l'interface
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

public class ExamenApp extends Application {

    public TextField inputMatricule;  // Ancien matriculeField
    public Label resultatLabel;       // Ancien resultLabel
    public Button boutonDetails;      // Ancien detailsButton
    public ProgressIndicator barreDeProgression; // Ancien progressBarSearchUser

    @Override
    public void start(Stage stagePrincipal) {
        stagePrincipal.setTitle("Consultation de Résultats");

        inputMatricule = new TextField();
        inputMatricule.setPromptText("Entrez votre matricule");
        inputMatricule.getStyleClass().add("text-field");

        Button boutonRechercher = new Button("Rechercher");
        boutonRechercher.setOnAction(e -> {
            boutonRechercher.setVisible(false);
            boutonRechercher.setManaged(false);
            barreDeProgression.setVisible(true);
            barreDeProgression.setManaged(true);

            new Thread(() -> {
                effectuerRecherche();

                Platform.runLater(() -> {
                    barreDeProgression.setManaged(false);
                    barreDeProgression.setVisible(false);
                    boutonRechercher.setVisible(true);
                    boutonRechercher.setManaged(true);
                });
            }).start();
        });

        boutonRechercher.getStyleClass().add("button");

        resultatLabel = new Label();
        resultatLabel.getStyleClass().add("label");

        boutonDetails = new Button("Afficher les détails");
        boutonDetails.setVisible(false);
        boutonDetails.setOnAction(e -> afficherDetails());
        boutonDetails.getStyleClass().add("button");

        barreDeProgression = new ProgressIndicator();
        barreDeProgression.setPrefWidth(57);
        barreDeProgression.setPrefHeight(38);
        barreDeProgression.setVisible(false);

        // Le résultat apparaît avant le bouton de détails
        HBox resultBtnAffiResult = new HBox();
        resultBtnAffiResult.getChildren().addAll(resultatLabel, boutonDetails);
        HBox.setMargin(boutonDetails, new Insets(75, 30, 30, 10));
        HBox.setMargin(resultatLabel, new Insets(60, 30, 30, 10));
        
        HBox conteneurBoutons = new HBox();
        conteneurBoutons.getChildren().addAll(boutonRechercher, barreDeProgression);
        conteneurBoutons.setAlignment(Pos.CENTER); // Centrage horizontal
        HBox.setMargin(boutonRechercher, new Insets(5, 10, 5, 0));
        HBox.setMargin(barreDeProgression, new Insets(5, 0, 5, 10));
       

        VBox conteneurPrincipal = new VBox();
        conteneurPrincipal.getChildren().addAll(inputMatricule, conteneurBoutons, resultBtnAffiResult);
        conteneurPrincipal.setAlignment(Pos.CENTER); // Centrage vertical
        conteneurPrincipal.getStyleClass().add("conteneurPrincipal");

        VBox layoutGlobal = new VBox();
        layoutGlobal.getChildren().addAll(new Label("RESULTAT D'EXAMEN"), conteneurPrincipal);
        layoutGlobal.setPadding(new Insets(10));
        layoutGlobal.setAlignment(Pos.CENTER); // Centrage dans la scène
        layoutGlobal.getStyleClass().add("layoutGlobal");

     // Créer un conteneur vide pour capturer le focus
        Pane conteneurVide = new Pane();
        layoutGlobal.getChildren().add(conteneurVide); // Ajouter le conteneur vide

        Scene scenePrincipale = new Scene(layoutGlobal, 1200, 600);
        scenePrincipale.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stagePrincipal.setScene(scenePrincipale);
        stagePrincipal.show();

        // Demander le focus sur le conteneur vide pour annuler la sélection
        Platform.runLater(() -> conteneurVide.requestFocus());
    }

    public void effectuerRecherche() {
        String matricule = inputMatricule.getText();
        System.out.println("Recherche pour le matricule : " + matricule);

        try (Connection cnx = DbConnexion.getConnection()) {
            String sql = "SELECT etudiants.* FROM etudiants WHERE matricule = ?";
            PreparedStatement pstmt = cnx.prepareStatement(sql);
            pstmt.setString(1, matricule);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String resultat = rs.getString("resultat");
                resultatLabel.getStyleClass().add("resultLabel");
                System.out.println("Résultat trouvé : " + resultat);
                boutonDetails.setVisible(true);
                
                Platform.runLater(() -> {
                    resultatLabel.setText("Résultat : " + resultat);
                });

            } else {
                boutonDetails.setVisible(false);
                System.out.println("Aucun résultat trouvé pour ce matricule.");
                
                Platform.runLater(() -> {
                    resultatLabel.setText("Aucun résultat trouvé pour ce matricule.");
                    resultatLabel.getStyleClass().add("resultLabel");
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                resultatLabel.setText("Erreur lors de la recherche.");
            });
        }
    }

    public void afficherDetails() {
        String matricule = inputMatricule.getText();

        try (Connection cnx = DbConnexion.getConnection()) {
            String sql = "SELECT * FROM etudiants WHERE matricule = ?";
            PreparedStatement pstmt = cnx.prepareStatement(sql);
            pstmt.setString(1, matricule);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String dateNaissance = rs.getString("date_naissance");
                String ecole = rs.getString("ecole");
                float moyenne = rs.getFloat("moyenne");

                Stage fenetreDetails = new Stage();
                fenetreDetails.setTitle("Détails de l'étudiant");

                Label labelMatricule = new Label("Matricule: " + matricule);
                Label labelNom = new Label("Nom: " + nom);
                Label labelPrenom = new Label("Prénom: " + prenom);
                Label labelDateNaissance = new Label("Date de naissance: " + dateNaissance);
                Label labelEcole = new Label("École: " + ecole);
                Label labelMoyenne = new Label("Moyenne: " + moyenne + " / 20");

                VBox vboxDetails = new VBox(10);
                vboxDetails.getChildren().addAll(labelMatricule, labelNom, labelPrenom, labelDateNaissance, labelEcole, labelMoyenne);
                vboxDetails.setPadding(new Insets(10));
                vboxDetails.getStyleClass().add("vboxDetails");

                Scene sceneDetails = new Scene(vboxDetails, 1200, 600);
                sceneDetails.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                fenetreDetails.setScene(sceneDetails);
                fenetreDetails.show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
