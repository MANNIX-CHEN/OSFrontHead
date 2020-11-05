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
    	openFile = new MenuItem("��");
    	delFile = new MenuItem("ɾ��");
    	fileData = new MenuItem("����");
    	renameFile = new MenuItem("������");
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
			System.out.println("���ļ�");
		});
    	
    	delFile.setOnAction(ActionEvent -> {
			System.out.println("ɾ���ļ�");
		});
    	
    	renameFile.setOnAction(ActionEvent -> {
			System.out.println("�������ļ�");
			TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("�������ļ�");
    		dialog.setHeaderText("�������ļ�");
    		dialog.setContentText("���������ļ���:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			System.out.println("���ļ���Ϊ��"+result.get());
    		}
		});
    	
    	fileData.setOnAction(ActionEvent -> {
			System.out.println("�ļ�����");
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
