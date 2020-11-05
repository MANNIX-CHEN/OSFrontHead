package FrontHead.content;

import javafx.scene.control.TreeItem;

import java.util.Vector;

public class Catalogue implements CatEntry{
    //String entries[] ;
    //private CatEntry entries[];
    private Vector<CatEntry> entries;
    private Catalogue parent;
    private String name;
    TreeItem <String> fxTreeItem ;//treeItem 为fxtreeView层面上的部件，通过修改这个更新目录

    private static final int CAT_IS_FULL = -1;

    /*以下为未与后台整合的部分*/
    public Catalogue(String rootCatName){
        /*构造目录,不需要传入父目录*/
        setParent(null);
        entries = new Vector<>(8);
        setName(rootCatName);
        this.setFxTreeItem(new TreeItem<>(rootCatName));
    }

    public Catalogue (String catName , Catalogue parentCat){
        entries = new Vector<>(8);
        setName(catName);
        this.setFxTreeItem(new TreeItem<>(catName));
        setParent(parentCat);

    }
    public int addFileEntry(String fileName){
        /*描述：添加文件目录项
        * 返回 -1 表示添加失败
        * 其余值表示存到目录的第几项
        * */
        File childrenFile = new File(fileName);
        if (getEntries().size()!=8){
            //如果仍然有空位
            entries.add(childrenFile);
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
        this.getFxTreeItem().getChildren().add(new TreeItem<>(catName));
        //在fx层面中添加treeItem

        if (getEntries().size()!=8){
            //如果仍然有空位
            entries.add(childrenCat);
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
}
