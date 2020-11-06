package FrontHead.content.component;


import java.util.Optional;

import FrontHead.content.Catalogue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sample.Controller;

import java.io.IOException;


public class CatCom extends FilePaneCom {

    Catalogue catalogue;
    protected MenuItem intoFile,delFile,fileData,renameFile;
    protected ContextMenu fileContextMenu2;

    public CatCom(String name , Controller controller , Catalogue catalogue) {
        super(name,controller);
        setCatalogue(catalogue);

        fileContextMenu2=new ContextMenu();
    	intoFile = new MenuItem("����");
    	delFile = new MenuItem("ɾ��");
    	renameFile = new MenuItem("������");
    	fileContextMenu2.getItems().addAll(intoFile,delFile,renameFile);

    	addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
    		if (event.getButton() == MouseButton.SECONDARY) {
    			controller.contextFlag=false;
    			fileContextMenu2.show(img, event.getScreenX(), event.getScreenY());
			}
			else {
				fileContextMenu2.hide();
			}

		});

    	intoFile.setOnAction(ActionEvent -> {
			enter();
		});

    	delFile.setOnAction(ActionEvent -> {
			try {
				catalogue.getParent().delCatEntry(catalogue);
				controller.updateFilePane();
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

    	renameFile.setOnAction(ActionEvent -> {
			TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("�������ļ�");
    		dialog.setHeaderText("�������ļ�");
    		dialog.setContentText("���������ļ���:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			//System.out.println("���ļ�����Ϊ��"+result.get());
				try {
					catalogue.changeName(result.get());
					controller.updateFilePane();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

    }

    @Override
    public void setImg() {
        img.getStyleClass().addAll("imgBtn","catImg");
    }

    @Override
    public void enter() {
        controller.setCurCat(this.catalogue);
        controller.updateFilePane();
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

}
