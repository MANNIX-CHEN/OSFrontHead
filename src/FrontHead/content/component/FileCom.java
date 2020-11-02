package FrontHead.content.component;

import FrontHead.content.VirtualFile;
import FrontHead.content.FileTextPane;
import sample.Controller;

public class FileCom extends FilePaneCom {
    VirtualFile file ;
    public FileCom(String name , VirtualFile file , Controller controller) {
        super(name , controller);
        setFile(file);
    }

    @Override
    public void setImg() {
        img.getStyleClass().addAll("imgBtn","fileImg");
    }

    @Override
    public void mouseClickedTiwce() throws Exception {
        controller.openFile(file);
        new FileTextPane(file , controller , controller.getServer()).init();
    }

    public void setFile(VirtualFile file) {
        this.file = file;
    }
}
