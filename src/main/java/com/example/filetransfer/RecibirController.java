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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
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
    private TableColumn<Archivo, String> tcPath;
    @FXML
    private TextField tfDir;
    @FXML
    private Button btnStartSocket;
    @FXML
    private Label lblErr;


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
        tcPath.setCellValueFactory(new PropertyValueFactory<Archivo, String>("FilePath"));
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
    protected void btnStartSocketActionEvent() throws IOException{
        serverSocket = new ServerSocket(PORT);
        serverSocketState = true;

        if (tfDir.getText().isEmpty()) {
            lblErr.setText("Introduce la ruta para\nguardar los archivos");
            lblErr.setVisible(true);
        } else {
            lblErr.setVisible(false);
            btnStartSocket.setDisable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Socket clientSocket = serverSocket.accept();
                            InputStream in = clientSocket.getInputStream();
                            DataInputStream dis = new DataInputStream(in);

                            String fileName = dis.readUTF();
                            String fileType = dis.readUTF();
                            long fileSize = dis.readLong();
                            OutputStream out = new FileOutputStream(tfDir.getText() + File.separator + fileName + "." + fileType);

                            byte[] buffer = new byte[16*1024];
                            int count;
                            while ((count = in.read(buffer)) != -1) {
                                out.write(buffer, 0, count);
                            }

                            out.close();
                            in.close();


                            tvRec.getItems().add(new Archivo(fileName, fileType, fileSize, tfDir.getText()));
                        }



                    } catch (FileNotFoundException e) {
                        lblErr.setText("ERROR: No se puede crear el archivo");
                        lblErr.setVisible(true);
                    } catch (IOException e) {
                        lblErr.setText("Error de I/O");
                        lblErr.setVisible(true);
                    }
                }
            }).start();
        }


        }

}
