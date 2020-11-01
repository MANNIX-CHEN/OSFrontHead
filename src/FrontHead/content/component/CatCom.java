package FrontHead.content.component;


import FrontHead.content.Catalogue;
import sample.Controller;


public class CatCom extends FilePaneCom {

    Catalogue catalogue;

    public CatCom(String name , Controller controller , Catalogue catalogue) {
        super(name,controller);
        setCatalogue(catalogue);
    }

    @Override
    public void setImg() {
        img.getStyleClass().addAll("imgBtn","catImg");
    }

    @Override
    public void mouseClickedTiwce() {
        controller.updateFilePane(this.catalogue);
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }
}
