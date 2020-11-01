package FrontHead.content;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class File implements CatEntry {

    private String name ;
    private String absPath;
    private int firstBlock;
    private int fileLength;
    private String attribute;


    private Catalogue catalogue ;
    private TreeItem FxTreeItem ;
    private final Image FILE_IMG = new Image("FrontHead/UI/file_treeView.png");

    public File(String name , Catalogue catalogue , String absPant) {
        setName(name);
        setCatalogue(catalogue);
        setAbsPath(absPant);
        setFirstBlock(0);
        setFileLength(0);
        setAttribute("readOnly");

        setFxTreeItem(new TreeItem<>(name));
    }

    public int getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(int firstBlock) {
        this.firstBlock = firstBlock;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public TreeItem getFxTreeItem() {
        return FxTreeItem;
    }

    public void setFxTreeItem(TreeItem fxTreeItem) {
        FxTreeItem = fxTreeItem;
        fxTreeItem.setGraphic(new ImageView(FILE_IMG));

    }

    @Override
    public String getName() {
        return this.name;
    }

}
