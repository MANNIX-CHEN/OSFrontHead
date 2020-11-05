package FrontHead.content;

import javafx.scene.control.TreeItem;

import java.util.Vector;

public class Catalogue implements CatEntry{
    //String entries[] ;
    //private CatEntry entries[];
    private Vector<CatEntry> entries;
    private Catalogue parent;
    private String name;
    TreeItem <String> fxTreeItem ;//treeItem ΪfxtreeView�����ϵĲ�����ͨ���޸��������Ŀ¼

    private static final int CAT_IS_FULL = -1;

    /*����Ϊδ���̨���ϵĲ���*/
    public Catalogue(String rootCatName){
        /*����Ŀ¼,����Ҫ���븸Ŀ¼*/
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
        /*����������ļ�Ŀ¼��
        * ���� -1 ��ʾ���ʧ��
        * ����ֵ��ʾ�浽Ŀ¼�ĵڼ���
        * */
        File childrenFile = new File(fileName);
        if (getEntries().size()!=8){
            //�����Ȼ�п�λ
            entries.add(childrenFile);
            return getEntries().size()-1;
        }
        return CAT_IS_FULL;
    }

    public int addCatEntry (String catName){
        /*�����������Ŀ¼Ŀ¼��
        * ���� -1 ��ʾ���ʧ��
        * ����ֵ��ʾ�浽Ŀ¼�ĵڼ���
        * */
        Catalogue childrenCat = new Catalogue(catName , this);
        this.getFxTreeItem().getChildren().add(new TreeItem<>(catName));
        //��fx���������treeItem

        if (getEntries().size()!=8){
            //�����Ȼ�п�λ
            entries.add(childrenCat);
            return getEntries().size()-1;
        }


        return CAT_IS_FULL;
        //�ڳ���������Ŀ¼��
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
