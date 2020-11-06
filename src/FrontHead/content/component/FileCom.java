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
    	openFile = new MenuItem("打开");
    	delFile = new MenuItem("删除");
    	fileData = new MenuItem("属性");
    	renameFile = new MenuItem("重命名");
    	fileContextMenu2.getItems().addAll(openFile,delFile,fileData,renameFile);

    	initListener();

    	openFile.setOnAction(ActionEvent -> {
			//System.out.println("打开文件");
			try {
				enter();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

    	delFile.setOnAction(ActionEvent -> {
			//System.out.println("删除文件");

			try {
				file.getCatalogue().delFileEntry(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			controller.updateFilePane();
		});

    	renameFile.setOnAction(ActionEvent -> {
			TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("重命名文件");
    		dialog.setHeaderText("重命名文件");
    		dialog.setContentText("请输入新文件名:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			//System.out.println("新文件名为："+result.get());

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
		choices.add("系统文件");
		choices.add("只读文件");
		choices.add("读写文件");

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

		ATTRdialog.setTitle("文件属性");

		ATTRdialog.setHeaderText(String.format("" +
				"%-6s%c  %-3s\n" +
				"%-6s%c  %-3s\n" +
				"%-6s%c  %-3d\n" +
				"%-6s%c  %-3d\n",
				"文件名称",':',file.getName(),
				"文件位置",':',file.getAbsPath(),
				"起始盘块",':',file.getFirstBlock(),
				"文件长度",':',file.getLatestText().length()/64 + 1));
		ATTRdialog.setContentText("请选择文件属性");
		ATTRdialog.showAndWait();

		switch (ATTRdialog.getSelectedItem()){
			case ("系统文件"):
				file.setATTRcode(Server.SYS_FILE);
				break;
			case ("只读文件"):
				file.setATTRcode(Server.READ_ONLY_FILE);
				break;
			case ("读写文件"):
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
