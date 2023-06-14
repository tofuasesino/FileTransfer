module com.example.filetransfer {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.filetransfer to javafx.fxml;
    exports com.example.filetransfer;
}