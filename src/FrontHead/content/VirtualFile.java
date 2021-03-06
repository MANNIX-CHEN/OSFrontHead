package FrontHead.content;

import BackGround.Server;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class VirtualFile implements CatEntry {

    private String name ;
    private String absPath;
    private int firstBlock;
    private int fileLength;
    private String attribute;
    private int ATTRcode;
    private String latestText;
    private String type;

    private Server server;
    private Catalogue catalogue ;
    private TreeItem FxTreeItem ;
    private final Image FILE_IMG = new Image("FrontHead/UI/file_treeView.png");


    public VirtualFile(){

    }

    public VirtualFile(String name , Catalogue catalogue , String absPant , Server server) throws IOException {
        setName(name);
        setCatalogue(catalogue);
        setAbsPath(absPant + "\\" + getName());
        setServer(server);

        setFirstBlock(server.findNextFreeBlock());
        setLatestText(new String());

        getServer().addFile(this);
        setATTRcode(Server.READ_WRITE_FILE);


        setFxTreeItem(new TreeItem<>(name));

    }
    public VirtualFile(String name , int ATTRcode  ,String type, Catalogue catalogue , String absPant , Server server , int firstBlock) throws IOException {
        setName(name);
        setCatalogue(catalogue);
        setAbsPath(absPant + "\\" + getName());

        setServer(server);


        setFirstBlock(firstBlock);
        setLatestText(server.readFile(getFirstBlock()));
        setATTRcode(ATTRcode);

        setFxTreeItem(new TreeItem<>(name));
    }

    public void changeInfo(String newName) throws IOException {
        setName(newName);
        server.changeFileATTR(this);
    }


    public int getATTRcode() {
        return ATTRcode;
    }

    public void setATTRcode(int ATTRcode) throws IOException {
        this.ATTRcode = ATTRcode;
        setAttribute(ATTRcode);
        server.changeFileATTR(this);
    }

    public String getLatestText() {
        return latestText;
    }

    public void setLatestText(String latestText) {
        this.latestText = latestText;
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

    public void setAttribute(int ATTRCode) {
        switch (ATTRCode){
            case 1: this.setAttribute("system");
                return ;
            case 2: this.setAttribute("readOnly");
                return ;
            case 4: this.setAttribute("rw");
                return ;
        }
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

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
