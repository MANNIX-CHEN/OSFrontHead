package FrontHead.tableData;

import FrontHead.content.VirtualFile;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public final class DataOfOpenFiles {
    /*?????????????????????*/
    /*??????http://www.zzvips.com/article/21976.html*/
    private final SimpleStringProperty fileName = new SimpleStringProperty();
    private final SimpleStringProperty filePath = new SimpleStringProperty();
    private final SimpleIntegerProperty firstBlock = new SimpleIntegerProperty();
    private final SimpleIntegerProperty fileLength = new SimpleIntegerProperty();
    private final SimpleStringProperty operateType = new SimpleStringProperty();

    private String uuid;
    public DataOfOpenFiles(VirtualFile file){
        setFileName(file.getName());
        setFilePath(file.getAbsPath());

        setFirstBlock(file.getFirstBlock());
        setFileLength(file.getFileLength());
        setOperateType(file.getAttribute());

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

    public int getFirstBlock() {
        return firstBlock.get();
    }

    public SimpleIntegerProperty firstBlockProperty() {
        return firstBlock;
    }

    public void setFirstBlock(int firstBlock) {
        this.firstBlock.set(firstBlock);
    }

    public int getFileLength() {
        return fileLength.get();
    }

    public SimpleIntegerProperty fileLengthProperty() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
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
