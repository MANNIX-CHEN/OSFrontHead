package FrontHead.content;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class File implements CatEntry {
    private String name ;
    private Catalogue catalogue ;
    private TreeItem FxTreeItem ;
    private final Image FILE_IMG = new Image("FrontHead/UI/file_treeView.png");

    public File(String name , Catalogue catalogue) {
        setName(name);
        setCatalogue(catalogue);
        setFxTreeItem(new TreeItem<>(name));
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
