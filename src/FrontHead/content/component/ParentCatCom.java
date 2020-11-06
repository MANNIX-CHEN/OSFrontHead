package FrontHead.content.component;

import sample.Controller;

public class ParentCatCom extends FilePaneCom {

    public ParentCatCom(String name, Controller controller) {
        super(name , controller);
    }

    @Override
    public void setImg() {
        img.getStyleClass().addAll("imgBtn","parentCatImg");
    }

    @Override
    public void enter() {
        controller.getCurCat().closeCat();
        controller.updateFilePane(controller.getCurCat().getParent());
    }

}
