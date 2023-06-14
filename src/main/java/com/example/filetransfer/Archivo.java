package com.example.filetransfer;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Archivo {

    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty fileType = new SimpleStringProperty();
    private LongProperty fileSize = new SimpleLongProperty();
    private String filePath;

    public Archivo (String fileName, String fileType, long fileSize) {
        this.fileName.set(fileName);
        this.fileType.set(fileType);
        this.fileSize.set(fileSize);
    }
    public Archivo (String fileName, String fileType, long fileSize, String filePath) {
        this.fileName.set(fileName);
        this.fileType.set(fileType);
        this.fileSize.set(fileSize);
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName.get();
    }

    public final StringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getFileType() {
        return fileType.get();
    }

    public StringProperty fileTypeProperty() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType.set(fileType);
    }

    public long getFileSize() {
        return fileSize.get();
    }

    public LongProperty fileSizeProperty() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize.set(fileSize);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
