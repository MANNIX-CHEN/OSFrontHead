package FrontHead.content.component;

import FrontHead.content.File;
import FrontHead.content.FileTextPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.Controller;

public class FileCom extends FilePaneCom {
    File file ;
    public FileCom(String name , File file ,Controller controller) {
        super(name , controller);
        setFile(file);
    }

    @Override
    public void setImg() {
        img.getStyleClass().addAll("imgBtn","fileImg");
    }

    @Override
    public void mouseClickedTiwce() throws Exception {
        new FileTextPane(file).init();
    }

    public void setFile(File file) {
        this.file = file;
    }
}
