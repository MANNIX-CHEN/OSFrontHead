package FrontHead.content.component;

import java.util.Optional;

import FrontHead.content.File;
import FrontHead.content.FileTextPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.Controller;

public class FileCom extends FilePaneCom {
    File file ;
    protected MenuItem openFile,delFile,fileData,renameFile;
    protected ContextMenu fileContextMenu2;
    protected Controller controller;
    public FileCom(String name , File file ,Controller controller) {
        super(name , controller);
        setFile(file);
        
        fileContextMenu2=new ContextMenu();
    	openFile = new MenuItem("打开");
    	delFile = new MenuItem("删除");
    	fileData = new MenuItem("属性");
    	renameFile = new MenuItem("重命名");
    	fileContextMenu2.getItems().addAll(openFile,delFile,fileData,renameFile);
    	    	
    	addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
    		if (event.getButton() == MouseButton.SECONDARY) {
    			controller.contextFlag=false;
    			fileContextMenu2.show(img, event.getScreenX(), event.getScreenY());				
			} 
			else {
				fileContextMenu2.hide();
			}
    		
		});
    	
    	openFile.setOnAction(ActionEvent -> {
			System.out.println("打开文件");
		});
    	
    	delFile.setOnAction(ActionEvent -> {
			System.out.println("删除文件");
		});
    	
    	renameFile.setOnAction(ActionEvent -> {
			System.out.println("重命名文件");
			TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("重命名文件");
    		dialog.setHeaderText("重命名文件");
    		dialog.setContentText("请输入新文件名:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			System.out.println("新文件名为："+result.get());
    		}
		});
    	
    	fileData.setOnAction(ActionEvent -> {
			System.out.println("文件属性");
		});
    	
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
