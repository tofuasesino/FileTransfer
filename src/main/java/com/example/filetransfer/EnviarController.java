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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.example.filetransfer.MainController.PORT;

public class EnviarController {



    @FXML
    private Button btnEnviar;
    @FXML
    private Button btnDir;
    @FXML
    private TableView<Archivo> tvFiles;
    @FXML
    private TableColumn<Archivo, String> tcFileName;
    @FXML
    private TableColumn<Archivo, String> tcFileType;
    @FXML
    private TableColumn<Archivo, Long> tcFileSize;
    @FXML
    private TextField tfDestIP;
    @FXML
    private Label lblErrMsg;

    private List<Archivo> finalArchivoList = new ArrayList<>();
    private ObservableList<Archivo> archivoObservablelist = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        tcFileName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Archivo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Archivo, String> archivoStringCellDataFeatures) {
                return archivoStringCellDataFeatures.getValue().fileNameProperty();
            }
        });
        tcFileType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Archivo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Archivo, String> archivoStringCellDataFeatures) {
                return archivoStringCellDataFeatures.getValue().fileTypeProperty();
            }
        });
        tcFileSize.setCellValueFactory(new PropertyValueFactory<Archivo, Long>("FileSize"));
        tvFiles.setItems(archivoObservablelist);
    }

    @FXML
    protected void btnBackActionEvent(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("file-transfer.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // When button is clicked checks if the TextField is empty
    // if it's not, fetches the IP into the socket as well as the port,
    // creates a byte array and loads the file within.
    // After that, the socket sends the file
    @FXML
    protected void btnEnviarActionEvent(ActionEvent e) {
        lblErrMsg.setVisible(false);

        if (tfDestIP.getText().length() == 0) {
            tfDestIP.setPromptText("Introduce una IP válida");
        } else {
            try {

                for(Archivo archivo : finalArchivoList) {
                    Socket socket = new Socket(tfDestIP.getText(), PORT);
                    OutputStream out = socket.getOutputStream();
                    DataOutputStream dout = new DataOutputStream(out);

                    dout.writeUTF(archivo.getFileName());
                    dout.writeUTF(archivo.getFileType());
                    dout.writeLong(archivo.getFileSize());
                    byte[] byteArray = new byte[(int)archivo.getFileSize()];
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivo.getFilePath()));
                    bis.read(byteArray, 0, byteArray.length);
                    System.out.println("Starting to send file");
                    dout.write(byteArray, 0, byteArray.length);
                    dout.flush();
                    finalArchivoList.remove(archivo);
                    socket.close();

                }


            } catch (FileNotFoundException ex) {
                lblErrMsg.setText("ERROR: Archivo no encontrado");
                lblErrMsg.setVisible(true);
            } catch (SecurityException ex) {
                lblErrMsg.setText("ERROR: Acceso al archivo denegado.");
                lblErrMsg.setVisible(true);
            } catch (IOException ex) {
                lblErrMsg.setText("ERROR: Fallo de I/O");
                lblErrMsg.setVisible(true);
            }

        }
    }

    // Uses a FileChooser to select multiple files, adding them to an ArrayList<Archivo>
    // and sending it to the TableView.
    @FXML
    protected void btnDirActionEvent(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        List<File> fileList = fileChooser.showOpenMultipleDialog(btnDir.getScene().getWindow());
        if (!fileList.isEmpty()) {
            List<Archivo> archivoList = new ArrayList<>();

            // This loop splits the extension of the file name and creates a new Archivo instance.
            for(File file : fileList) {
                System.out.println(file.getPath());
                String nombre = file.getName().split("[.]")[0];
                String tipo = file.getName().split("\\.",2)[1];
                archivoList.add(new Archivo(nombre, tipo, file.length() / 1000, file.getPath()));
                finalArchivoList.add(new Archivo(nombre, tipo, file.length() / 1000, file.getPath()));
            }
            // Foreach loop to add instances of Archivo within the arraylist archivoList to the table view.
            for (Archivo archivo : archivoList) {
                tvFiles.getItems().add(archivo);
            }
            // Unset disabled state of the send button.
            btnEnviar.setDisable(false);

        }

    }
}
