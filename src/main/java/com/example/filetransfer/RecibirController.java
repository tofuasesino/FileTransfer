package com.example.filetransfer;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.filetransfer.MainController.PORT;


public class RecibirController {

    private ServerSocket serverSocket;
    private boolean serverSocketState = false;
    private ArrayList<Archivo> archivoArrayList = new ArrayList<>();
    private ObservableList<Archivo> archivoObservableList = FXCollections.observableArrayList();

    @FXML
    private TableView<Archivo> tvRec;
    @FXML
    private TableColumn<Archivo, String> tcFile;
    @FXML
    private TableColumn<Archivo, String> tcType;
    @FXML
    private TableColumn<Archivo, Long> tcSize;
    @FXML
    private TextField tfDir;
    @FXML
    private Button btnStartSocket;


    @FXML
    public void initialize() {
        tcFile.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Archivo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Archivo, String> archivoStringCellDataFeatures) {
                return archivoStringCellDataFeatures.getValue().fileNameProperty();
            }
        });

        tcType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Archivo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Archivo, String> archivoStringCellDataFeatures) {
                return archivoStringCellDataFeatures.getValue().fileTypeProperty();
            }
        });

        tcSize.setCellValueFactory(new PropertyValueFactory<Archivo, Long>("FileSize"));
        tvRec.setItems(archivoObservableList);
    }
    @FXML
    protected void btnBackActionEvent(ActionEvent e) throws IOException {
        // Check if the socket is opened. If it is, closes it.
        if (serverSocketState) {
            System.out.println("Closing connection.");
            serverSocket.close();
        }

        Parent root = FXMLLoader.load(getClass().getResource("file-transfer.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void btnStartSocketActionEvent() throws IOException {
        btnStartSocket.setDisable(true);
        serverSocket = new ServerSocket(PORT);
        serverSocketState = true;
        ArrayList<Integer> arrayList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        InputStream inputStream = clientSocket.getInputStream();
                        DataInputStream dis = new DataInputStream(inputStream);
                        String fileName = dis.readUTF();
                        String fileType = dis.readUTF();
                        long fileSize = dis.readLong();
                        Archivo archivo = new Archivo(fileName, fileType, fileSize);
                        archivoArrayList.add(archivo);
                        System.out.println(archivo.getFileName() + archivo.getFileType() + " " + archivo.getFileSize());
                        tvRec.getItems().add(archivo);
                    }



                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        }

    @FXML
    protected void btnChangeDirActionEvent() {

    }
}
