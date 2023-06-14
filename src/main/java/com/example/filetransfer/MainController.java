package com.example.filetransfer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public static final int PORT = 1234;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label lblEnv;

    @FXML
    private Label lblRec;

    @FXML
    protected void envOnMouseEntered() throws InterruptedException {
        lblEnv.setStyle("-fx-background-color: LIGHTGRAY");
    }

    @FXML
    protected void envOnMouseExited(){
        lblEnv.setStyle("-fx-background-color: NONE");
    }

    @FXML
    protected void recOnMouseEntered() {
        lblRec.setStyle("-fx-background-color: LIGHTGRAY");
    }

    @FXML
    protected void recOnMouseExited() {
        lblRec.setStyle("-fx-background-color: NONE");
    }
    @FXML
    protected void recOnMouseClicked(MouseEvent event) {
        try {
            switchToRecibir(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void envOnMouseClicked(MouseEvent event){
        try {
            switchToEnviar(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void switchToRecibir(MouseEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("recibir.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    protected void switchToEnviar(MouseEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("enviar.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
