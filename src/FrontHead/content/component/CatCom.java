package FrontHead.content.component;


import FrontHead.content.Catalogue;
import javafx.scene.input.MouseEvent;
import sample.Controller;

import java.io.IOException;


public class CatCom extends FilePaneCom {

    Catalogue catalogue;

    public CatCom(String name , Controller controller , Catalogue catalogue) {
        super(name,controller);
        setCatalogue(catalogue);
        addEventHandler(MouseEvent.MOUSE_CLICKED , event->{
            try {
                delCat();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
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
    public void delCat() throws IOException {

        //逻辑层

        server.delCat(catalogue);
    }
}
