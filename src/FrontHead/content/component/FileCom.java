package FrontHead.content.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import BackGround.Server;
import FrontHead.content.FileTextPane;
import FrontHead.content.VirtualFile;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sample.Controller;

public class FileCom extends FilePaneCom {
    VirtualFile file ;
    protected MenuItem openFile,delFile,fileData,renameFile;
    protected ContextMenu fileContextMenu2;
    public FileCom(String name , VirtualFile file , Controller controller) {
        super(name , controller);
        setFile(file);
        //title.setText(file.getName()+ "." +file.getType() );

        fileContextMenu2=new ContextMenu();
    	openFile = new MenuItem("��");
    	delFile = new MenuItem("ɾ��");
    	fileData = new MenuItem("����");
    	renameFile = new MenuItem("������");
    	fileContextMenu2.getItems().addAll(openFile,delFile,fileData,renameFile);

    	initListener();

    	openFile.setOnAction(ActionEvent -> {
			//System.out.println("���ļ�");
			try {
				enter();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

    	delFile.setOnAction(ActionEvent -> {
			//System.out.println("ɾ���ļ�");

			try {
				file.getCatalogue().delFileEntry(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			controller.updateFilePane();
		});

    	renameFile.setOnAction(ActionEvent -> {
			TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("�������ļ�");
    		dialog.setHeaderText("�������ļ�");
    		dialog.setContentText("���������ļ���:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			//System.out.println("���ļ���Ϊ��"+result.get());

				try {
					file.changeInfo(result.get());
				} catch (IOException e) {
					e.printStackTrace();
				}
				controller.updateFilePane();

			}
		});

    	fileData.setOnAction(ActionEvent -> {

			try {
				showFileATTR();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

    }

	private void showFileATTR() throws IOException {
		List<String> choices = new ArrayList<>();
		choices.add("ϵͳ�ļ�");
		choices.add("ֻ���ļ�");
		choices.add("��д�ļ�");

		ChoiceDialog<String> ATTRdialog;
		switch (file.getATTRcode()){
			case (Server.READ_WRITE_FILE):
				ATTRdialog = new ChoiceDialog<>(choices.get(2), choices);
				break;

			case (Server.READ_ONLY_FILE):
				ATTRdialog = new ChoiceDialog<>(choices.get(1), choices);
				break;

			case (Server.SYS_FILE):
				ATTRdialog = new ChoiceDialog<>(choices.get(0), choices);
				break;

			default:
				throw new IllegalStateException("Unexpected value: " + file.getATTRcode());
		}

		ATTRdialog.setTitle("�ļ�����");

		ATTRdialog.setHeaderText(String.format("" +
				"%-6s%c  %-3s\n" +
				"%-6s%c  %-3s\n" +
				"%-6s%c  %-3d\n" +
				"%-6s%c  %-3d\n",
				"�ļ�����",':',file.getName(),
				"�ļ�λ��",':',file.getAbsPath(),
				"��ʼ�̿�",':',file.getFirstBlock(),
				"�ļ�����",':',file.getLatestText().length()/64 + 1));
		ATTRdialog.setContentText("��ѡ���ļ�����");
		ATTRdialog.showAndWait();

		switch (ATTRdialog.getSelectedItem()){
			case ("ϵͳ�ļ�"):
				file.setATTRcode(Server.SYS_FILE);
				break;
			case ("ֻ���ļ�"):
				file.setATTRcode(Server.READ_ONLY_FILE);
				break;
			case ("��д�ļ�"):
				file.setATTRcode(Server.READ_WRITE_FILE);
				break;
		}
	}

	public void initListener(){
    	img.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				controller.contextFlag=false;
				fileContextMenu2.show(img, event.getScreenX(), event.getScreenY());
			}
			else {
				fileContextMenu2.hide();
			}
			controller.updatePaneStyle(event);

		});

		addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				controller.contextFlag=false;
				fileContextMenu2.show(img, event.getScreenX(), event.getScreenY());
			}
			else {
				fileContextMenu2.hide();
			}

		});
	}

    @Override
    public void setImg() {
        img.getStyleClass().addAll("imgBtn","fileImg");
    }

    @Override
    public void enter() throws Exception {
        controller.openFile(file);
        new FileTextPane(file , controller , controller.getServer()).init();
    }

    public void setFile(VirtualFile file) {
        this.file = file;
    }
}
