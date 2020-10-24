package sample;

import javafx.beans.property.SimpleStringProperty;

public final class DataOfTable {
    /*该类用来加载表格中的数据*/
    /*参考网址http://www.zzvips.com/article/21976.html*/
    private final SimpleStringProperty fileName = new SimpleStringProperty();
    private final SimpleStringProperty filePath = new SimpleStringProperty();
    private final SimpleStringProperty firstBlock = new SimpleStringProperty();
    private final SimpleStringProperty fileLength = new SimpleStringProperty();
    private final SimpleStringProperty operateType = new SimpleStringProperty();
    private String uuid;

    public DataOfTable(String[] curFileInfo){
        setFileName(curFileInfo[0]);
        setFilePath(curFileInfo[1]);
        setFirstBlock(curFileInfo[2]);
        setFileLength(curFileInfo[3]);
        setOperateType(curFileInfo[4]);
    }
    /*getter&setter*/
    public String getFileName() {
        return fileName.get();
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getFilePath() {
        return filePath.get();
    }

    public SimpleStringProperty filePathProperty() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public String getFirstBlock() {
        return firstBlock.get();
    }

    public SimpleStringProperty firstBlockProperty() {
        return firstBlock;
    }

    public void setFirstBlock(String firstBlock) {
        this.firstBlock.set(firstBlock);
    }

    public String getFileLength() {
        return fileLength.get();
    }

    public SimpleStringProperty fileLengthProperty() {
        return fileLength;
    }

    public void setFileLength(String fileLength) {
        this.fileLength.set(fileLength);
    }

    public String getOperateType() {
        return operateType.get();
    }

    public SimpleStringProperty operateTypeProperty() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType.set(operateType);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
