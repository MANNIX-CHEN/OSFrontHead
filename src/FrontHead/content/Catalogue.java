package FrontHead.content;

import BackGround.Server;
import FrontHead.content.component.FilePaneCom;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Vector;

public class Catalogue implements CatEntry{
    //String entries[] ;
    //private CatEntry entries[];
    private Vector<CatEntry> entries;
    private Vector<TreeItem> catItems;
    private Vector<TreeItem> fileItems;
    private String absPath;
    private Server server;
    private int firstBlock;
    private Catalogue parent;
    private String name;
    private final Image CLOSE_FLODER_IMG = new Image("FrontHead/UI/close_floder_treeView.png");
    private final Image OPEN_FLODER_IMG = new Image("FrontHead/UI/open_floder_treeView.png");
    TreeItem <String> fxTreeItem ;//treeItem 为fxtreeView层面上的部件，通过修改这个更新目录

    private static final int CAT_IS_FULL = -1;


    public Catalogue(String rootCatName , Server server) throws IOException {
        /*构造root目录,不需要传入父目录*/
        setParent(null);
        initCat(rootCatName);
        absPath = getName();
        setServer(server);
        setFirstBlock(2);
        server.setReading(true);
        server.readCat(this);
        server.setReading(false);
    }

    public Catalogue (String catName , Catalogue parentCat , int firstBlock){
        //server.readCat调用
        setParent(parentCat);
        setServer(getParent().server);//不是根目录的话，递归寻找server
        setFirstBlock(firstBlock);
        absPath = (parentCat == null)?("C:\\"+catName):
                (parentCat.getAbsPath() + "\\" + catName);
        initCat(catName);
    }

    public Catalogue (String catName , Catalogue parentCat){
        setParent(parentCat);
        setServer(getParent().server);//不是根目录的话，递归寻找server
        setFirstBlock(server.findNextFreeBlock());
        absPath = (parentCat == null)?("C:\\"+catName):
                (parentCat.getAbsPath() + "\\" + catName);
        initCat(catName);
    }

    private void initCat(String catName){
        entries = new Vector<>(8);
        fileItems = new Vector<>(8);
        catItems = new Vector<>(8);
        setName(catName);
        this.setFxTreeItem(new TreeItem<>(catName));
        this.getFxTreeItem().setGraphic(new ImageView(CLOSE_FLODER_IMG));

    }
    public void delCatEntry(Catalogue childrenCat) throws IOException {


        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).toString().matches("(.*)Catalogue(.*)")){
                childrenCat.delCatEntry((Catalogue) entries.get(i));
            }else {
                childrenCat.delFileEntry((VirtualFile) entries.get(i));
            }
        }//使用foreach会报错


        //需要删除子目录的所有内容
        if (!entries.isEmpty())
            entries.remove(childrenCat);
        catItems.remove(childrenCat.getFxTreeItem());
        updateTreeView();

        //逻辑层

        server.delCat(childrenCat);
        //diskFile层删除
    }
    public void delFileEntry(VirtualFile childrenFile) throws IOException {
        entries.remove(childrenFile);
        fileItems.remove(childrenFile.getFxTreeItem());
        updateTreeView();
        //逻辑层删除


        server.delFile(childrenFile);
        //diskFile层删除
    }
    public int addFileEntry(VirtualFile childrenFile) throws IOException {
        /*描述：添加文件目录项
        * 返回 -1 表示添加失败
        * 其余值表示存到目录的第几项
        * */
        if (getEntries().size()!=8){
            //如果仍然有空位
            entries.add(childrenFile);
            fileItems.add(childrenFile.getFxTreeItem());
            updateTreeView();

            return getEntries().size()-1;
        }
        return CAT_IS_FULL;
    }


    public int addCatEntry (Catalogue childrenCat) throws IOException {
        /*描述：添加子目录目录项
         * 返回 -1 表示添加失败
         * 其余值表示存到目录的第几项
         * */

        //在fx层面中添加treeItem

        if (getEntries().size()!=8){
            //如果仍然有空位

            entries.add(childrenCat);
            catItems.add(childrenCat.getFxTreeItem());
            updateTreeView();
            //逻辑层与展示层添加cat

            //server.showMeFat();


            if(!server.isReading())
                 server.addCat(childrenCat);
            //server层添加cat,server初始化的时候不需要添加

            return getEntries().size()-1;
        }
        return CAT_IS_FULL;
        //在程序层面添加目录项


    }
    public void changeName(String newName) throws IOException {
        setName(newName);
        server.changeCat(this);
    }

    public void updateTreeView() {
        this.getFxTreeItem().getChildren().clear();
        for (TreeItem addItem:
             catItems) {
            this.getFxTreeItem().getChildren().add(addItem);
        }
        for (TreeItem addItem:
             fileItems) {
            this.getFxTreeItem().getChildren().add(addItem);
        }
        this.getFxTreeItem().setExpanded(true);
        if(getFxTreeItem().getChildren().isEmpty()){
            closeCat();
        }else {
            openCat();
        }

        this.getFxTreeItem().addEventHandler(TreeItem.branchExpandedEvent(),
            event -> event.getTreeItem().setGraphic(new ImageView(OPEN_FLODER_IMG)));//关闭Item

        this.getFxTreeItem().addEventHandler(TreeItem.branchCollapsedEvent(),
            event -> event.getTreeItem().setGraphic(new ImageView(CLOSE_FLODER_IMG))
        );//打开Item
    }

    public void openCat(){
        getFxTreeItem().setExpanded(true);
        getFxTreeItem().setGraphic(new ImageView(OPEN_FLODER_IMG));
    }
    public void closeCat(){
        getFxTreeItem().setExpanded(false);
        getFxTreeItem().setGraphic(new ImageView(CLOSE_FLODER_IMG));
    }

    public Catalogue getParent() {
        return parent;
    }

    public void setParent(Catalogue parent) {
        this.parent = parent;
    }

    public Vector<CatEntry> getEntries() {
        return entries;
    }

    public void setFxTreeItem(TreeItem<String> fxTreeItem) {
        this.fxTreeItem = fxTreeItem;
    }

    public TreeItem<String> getFxTreeItem() {
        return fxTreeItem;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsPath() {
        return absPath;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(int firstBlock) {
        this.firstBlock = firstBlock;
    }
}
