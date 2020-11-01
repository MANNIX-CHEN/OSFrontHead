package FrontHead.content;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.event.ChangeEvent;
import java.util.Vector;

public class Catalogue implements CatEntry{
    //String entries[] ;
    //private CatEntry entries[];
    private Vector<CatEntry> entries;
    private String absPath;
    private Catalogue parent;
    private String name;
    private final Image CLOSE_FLODER_IMG = new Image("FrontHead/UI/close_floder_treeView.png");
    private final Image OPEN_FLODER_IMG = new Image("FrontHead/UI/open_floder_treeView.png");
    TreeItem <String> fxTreeItem ;//treeItem 为fxtreeView层面上的部件，通过修改这个更新目录

    private static final int CAT_IS_FULL = -1;

    /*以下为未与后台整合的部分*/
    public Catalogue(String rootCatName){
        /*构造root目录,不需要传入父目录*/
        setParent(null);
        absPath = getName();
        initCat(rootCatName);
    }

    public Catalogue (String catName , Catalogue parentCat){
        setParent(parentCat);
        absPath = parentCat.getAbsPath() + "\\" + catName;
        initCat(catName);
    }

    private void initCat(String catName){
        entries = new Vector<>(8);
        setName(catName);
        this.setFxTreeItem(new TreeItem<>(catName));
        this.getFxTreeItem().setGraphic(new ImageView(CLOSE_FLODER_IMG));
    }
    public int addFileEntry(String fileName){
        /*描述：添加文件目录项
        * 返回 -1 表示添加失败
        * 其余值表示存到目录的第几项
        * */
        File childrenFile = new File(fileName,this);
        if (getEntries().size()!=8){
            //如果仍然有空位
            entries.add(childrenFile);
            this.getFxTreeItem().getChildren().add(childrenFile.getFxTreeItem());
            return getEntries().size()-1;
        }
        return CAT_IS_FULL;
    }

    public int addCatEntry (String catName){
        /*描述：添加子目录目录项
        * 返回 -1 表示添加失败
        * 其余值表示存到目录的第几项
        * */
        Catalogue childrenCat = new Catalogue(catName , this);

        //在fx层面中添加treeItem

        if (getEntries().size()!=8){
            //如果仍然有空位
            entries.add(childrenCat);

            this.getFxTreeItem().getChildren().add(childrenCat.getFxTreeItem());
            System.out.println("children is "+getFxTreeItem().getChildren());
            return getEntries().size()-1;
        }


        return CAT_IS_FULL;
        //在程序层面添加目录项


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
}
