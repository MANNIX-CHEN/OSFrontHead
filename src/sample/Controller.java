package sample;

import BackGround.Server;
import FrontHead.content.CatEntry;
import FrontHead.content.Catalogue;
import FrontHead.content.VirtualFile;
import FrontHead.content.component.FileCom;
import FrontHead.content.component.FilePaneCom;
import FrontHead.content.component.CatCom;
import FrontHead.content.component.ParentCatCom;
import FrontHead.tableData.DataOfFat;
import FrontHead.tableData.DataOfOpenFiles;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.ImageView;

public class Controller {

    /*���³�Ա�ֱ����Ӧ��FXML�е����*/
    @FXML
    private Button parentCat;
    @FXML
    private TableView<DataOfOpenFiles> openFilesTable;
    @FXML
    private TreeView<String> filesCatView;
    @FXML
    private GridPane diskStatus;
    @FXML
    private TableView fatTable;
    @FXML
    private PieChart pieChart;
    @FXML
    private ImageView test;
    @FXML
    private Label fileLabel;
    //@FXML
    private ContextMenu fileContextMenu;
    private ContextMenu fileContextMenu2;
    private MenuItem newFile,newFolder,openFile,delFile,fileData,renameFile;
    @FXML
    private FlowPane filePane;
    @FXML
    private TextField curAbsPath;

    Server server;


    /*����Ϊ��Ⱦ������Ҫʹ�õ�����س�Ա*/
    private Catalogue rootCat ;
    private Catalogue curCat ;//��ǰĿ¼
    FilePaneCom[] curFPaneComs;
    Vector<VirtualFile> curOpenFiles;
    private FilePaneCom curClickedCom ;

    public boolean contextFlag =false;


    /*����Ϊ�Ѿ����ļ����data����Ҫ���Ӧ�ĵ�table���𿪣�*/
    private ObservableList<DataOfOpenFiles> openFilesData = FXCollections.observableArrayList();
    private ObservableList<DataOfFat> fatData = FXCollections.observableArrayList();

    //TreeItem<String> root = new TreeItem<>("C:");


    /*getter&setter*/

    public FilePaneCom getCurClickedCom() {
        return curClickedCom;
    }

    public void setCurClickedCom(FilePaneCom curClickedCom) {
        this.curClickedCom = curClickedCom;
    }

    public Server getServer() {
        return server;
    }

    public Catalogue getCurCat() {
        return curCat;
    }

    public void setCurCat(Catalogue curCat) {
        this.curCat = curCat;
    }

    public Vector<VirtualFile> getCurOpenFiles() {
        return curOpenFiles;
    }

    public void Init() throws IOException {

        server = new Server(this);


        //server.formatting();
        initContextMenu();//��ʼ���Ҽ������˵�
        setOnActionMenuItem();//�����˵�����ʵ��
        initcatalogue();//��ʼ��Ŀ¼�ṹ
        initOpenFilesTable();//��ʼ�����,��ʼ��curOpenFiles
        updateFilePane();//��ʼˢ�� FilePane
        intDiskPane();//��ʼ�����̷������ҳ��

    }

    public void initContextMenu() {
        fileContextMenu=new ContextMenu();
        newFile = new MenuItem("�½��ļ�");
        newFolder = new MenuItem("�½��ļ���");
        fileContextMenu.getItems().addAll(newFile,newFolder);
        filePane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            if (curClickedCom==null&&event.getButton() == MouseButton.SECONDARY
                    && !fileContextMenu2.isShowing()) {
                fileContextMenu.show(filePane, event.getScreenX(), event.getScreenY());
            } else {
                fileContextMenu.hide();
            }
        });

        /*�ļ��Ҽ��˵�*/
        fileContextMenu2=new ContextMenu();
        openFile = new MenuItem("��");
        delFile = new MenuItem("ɾ��");
        fileData = new MenuItem("����");
        renameFile = new MenuItem("������");
        fileContextMenu2.getItems().addAll(openFile,delFile,fileData,renameFile);
//        parentCat.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
//
//            if (event.getButton() == MouseButton.SECONDARY && !fileContextMenu.isShowing()) {
//                fileContextMenu2.show(parentCat, event.getScreenX(), event.getScreenY());
//            } else {
//                fileContextMenu2.hide();
//            }
//        });
    }


    public void addFile(){
        //System.out.println("�½��ļ�");
        TextInputDialog dialog=new TextInputDialog();
        dialog.setTitle("�½��ļ�");
        dialog.setHeaderText("�½��ļ�");
        dialog.setContentText("�������ļ���:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()&& check(result.get())){
            try {
                curCat.addFileEntry(new VirtualFile(result.get(),curCat,curCat.getAbsPath(),curCat.getServer()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateFilePane();
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("�½��ļ�ʧ��");
            alert.headerTextProperty().set("ע�����\n" +
                    "1. �ļ����������Ϊ3\n" +
                    "2. �ļ������ܺ���\"$\",\".\",\"\\\"�ַ�\n" +
                    "3. �ļ����Լ��ļ�����֮�䲻���ظ�\n" +
                    "4. ��ǰĿ¼���ļ��Լ��ļ����������Ϊ8\n" +
                    "5. �����̿ռ��Ƿ�д��");
            alert.showAndWait();
        }
    }
    public void addFloder(){
        //System.out.println("�½��ļ���");
        TextInputDialog dialog=new TextInputDialog();
        dialog.setTitle("�½��ļ���");
        dialog.setHeaderText("�½��ļ���");
        dialog.setContentText("�������ļ�����:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()&& check(result.get())){ //�½��ļ����¼�
            try {
                curCat.addCatEntry(new Catalogue(result.get(),curCat));
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateFilePane();//���Ŀ¼����½���
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("�½��ļ���ʧ��");
            alert.headerTextProperty().set("ע�����\n" +
                    "1. �ļ������������Ϊ3\n" +
                    "2. �ļ��������ܺ���\"$\",\".\",\"\\\"�ַ�\n" +
                    "3. �ļ����Լ��ļ�����֮�䲻���ظ�\n" +
                    "4. ��ǰĿ¼���ļ��Լ��ļ����������Ϊ8\n" +
                    "5. �����̿ռ��Ƿ�д��");
            alert.showAndWait();
        }
    }
    /*�����˵�����ʵ��*/
    public void setOnActionMenuItem() {
        newFile.setOnAction(ActionEvent -> {
            addFile();
        });

        newFolder.setOnAction(ActionEvent -> {
            addFloder();

        });
    }

    private boolean check(String s) {
        //Pattern pattern$ = Pattern.compile("(\\$)+") ;
        if(curCat.getEntries().size() == 8)
            return false;

        for (CatEntry entry :
                curCat.getEntries()) {
            if (s.equals(entry.getName())) {
                return false;
            }
        }


        Matcher mDoller = Pattern.compile("(\\$)+").matcher(s);
        Matcher mDot = Pattern.compile("(\\.)+").matcher(s);
        Matcher mSlash = Pattern.compile("(\\\\)+").matcher(s);
        //�Ƿ�Ϸ�
        if(mDoller.find() || mDot.find() || mSlash.find()
                 || s.length()>3 ||server.findNextFreeBlock()==-1){
            return false;
        }


        return true ;
    }


    public void updateFilePane() {
        /*����filePane����clear���������Ȼ�����curCat������
         * */
        curAbsPath.setText(curCat.getAbsPath());
        filePane.getChildren().clear();//����������

        if ((curCat != rootCat)) {
            filePane.getChildren().add(new ParentCatCom("...", this));
        }


        Vector<CatEntry> curCatEntries  = curCat.getEntries();
        /*����forѭ�������� ���ļ��У�Ȼ�����ļ�*/
        for (CatEntry cat :
                curCatEntries) {
            if (cat.toString().matches("(.*)Catalogue(.*)")){
                //������ʽƥ�� catalogue
                CatCom addCom = new CatCom(cat.getName(), this, (Catalogue) cat);
                filePane.getChildren().add(addCom);
            }
        }
        for (CatEntry file :
                curCatEntries) {
            if (file.toString().matches("(.*)File(.*)")){
                //������ʽƥ�� File
                FileCom addCom = new FileCom(file.getName(), (VirtualFile) file,this);
                filePane.getChildren().add(addCom);
            }
        }
        updateOpenFilesTable();
    }



    public void updateFilePane(Catalogue nextCurCat){

        setCurCat(nextCurCat);
        curCat.updateTreeView();
        updateFilePane();
    }
    public void closeFile(VirtualFile file){
        getCurOpenFiles().remove(file);
        updateOpenFilesTable();
    }
    public void openFile(VirtualFile file){
        getCurOpenFiles().add(file);
        updateOpenFilesTable();
    }

    public void updateOpenFilesTable() {
        openFilesData.clear();
        for (VirtualFile addFile:
                curOpenFiles) {
            openFilesData.add(new DataOfOpenFiles(addFile));
        }
        openFilesTable.setItems(openFilesData);
    }
    private void intDiskPane() throws IOException {
        double testwidth;

        /*�������ݳ�ʼ��fat��*/
        ObservableList<TableColumn<DataOfFat, ?>> observableList = fatTable.getColumns();

        for (int i = 0 ; i < 4 ; i++){
            TableColumn curBlockCol = observableList.get(2*i);
            TableColumn curValCol = observableList.get(2*i+1);

            /*����*/
            curBlockCol.setText("��");
            curBlockCol.setCellValueFactory(new PropertyValueFactory("block"+i));
            curBlockCol.getStyleClass().add("blockCol");//������߽磬��ǿ�۸�

            /*ֵ��*/
            curValCol.setText("ֵ");
            curValCol.setCellValueFactory(new PropertyValueFactory("val"+i));
        }//��ʼ��fatTable����Ҫ�õ��Ķ���

        /*��������Ϊ��ʼ����ͼ*/

        server.updateFat();
    }
    public void updateFatShow(int[] newFat){
        /*����Ϊˢ��fat��*/
        fatData.clear();
        for (int i = 0 ; i < 32 ; i++ ){
            fatData.add(new DataOfFat(
                    new int[]{4*i,4*i+1,4*i+2,4*i+3},
                    new int[]{newFat[4*i] , newFat[4*i+1],
                            newFat[4*i+2] , newFat[4*i+3] }));
        }

        int numOfused = 0;
        for (int curVal :
                newFat) {
            if (curVal != 0)
                numOfused++;
        }

        fatTable.setItems(fatData);

        /*��������Ϊˢ��pieͼ*/
        if (pieChart.getData().size()!=0){
            pieChart.getData().set(0,new PieChart.Data("used",numOfused));
            pieChart.getData().set(1,new PieChart.Data("unused",128-numOfused));
        }else {
            pieChart.getData().add(0,new PieChart.Data("used",numOfused));
            pieChart.getData().add(1,new PieChart.Data("unused",128-numOfused));
        }



    }

    private void initcatalogue() throws IOException {
        rootCat = new Catalogue("C:" , getServer());
        curCat = rootCat;
        filesCatView.setRoot(rootCat.getFxTreeItem());
        filesCatView.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> {
                    //System.out.println("info is " + observableValue +" @@@ " + number+" @@@ " +t1);
                }
        );

    }

    private void initOpenFilesTable() {
        curOpenFiles = new Vector<VirtualFile>(5);

        ObservableList<TableColumn<DataOfOpenFiles, ?>> observableList = openFilesTable.getColumns();
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("fileName"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("filePath"));
        observableList.get(2).setCellValueFactory(new PropertyValueFactory("firstBlock"));
        observableList.get(3).setCellValueFactory(new PropertyValueFactory("fileLength"));
        observableList.get(4).setCellValueFactory(new PropertyValueFactory("operateType"));
        //�趨��Ҫ��ӵ���Ϣ��ʲô


//        for (String[] curFileInfo:
//             rd.openFilesInfo) {
//            openFilesData.add(new DataOfOpenFiles(curFileInfo));
//        }
//        //����Ҫ���ڱ�����Ϣ����data��ͷ
//
//        openFilesTable.setItems(openFilesData);
//        //setItem��data�����ݷŵ������

    }

    @FXML//�¼�������
    public void onButtonClick(ActionEvent event){
    }

    @FXML
    public void panePress(MouseEvent mouseEvent) {
    }

/*    @FXML
    public void paneSecondary(MouseEvent mouseEvent) {
        if(mouseEvent.getButton()==MouseButton.SECONDARY) {
        	System.out.println("�Ҽ�����հ׵ط�");
        	updateFilePane();
        }
    }*/

    public void updatePaneStyle(MouseEvent event){

        if (curClickedCom!=null){
            if(curClickedCom.isClickedflag){
                curClickedCom.isClickedflag = false;
            }else {
                curClickedCom.turnWhite();
                curClickedCom = null;
            }
        }

    }

    @FXML
    public void onButtonClickTwice(MouseEvent event) {
        if(event.getClickCount() == 2) {
            System.out.println("˫�������ļ����У�");
        }
    }

    @FXML
    public void onButtonSecondary(MouseEvent event) {
        if(event.getButton()==MouseButton.SECONDARY) {
            System.out.println("�Ҽ�����ļ����У�");
        }
    }

    public void formattingDisk(ActionEvent event) throws IOException {
        /* ��ʽ�����̣����ڲ���
         *  �����ڲ˵��� -> Help -> format��ֱ�ӵ���
         * */
        rootCat.getEntries().clear();
        setCurCat(rootCat);
        updateFilePane();
        rootCat.clearTreeView();
        server.formatting();
    }
    public void showMeFat(ActionEvent event) throws IOException {
        server.showMeFat();
    }
    public void findNextBlock(ActionEvent event) throws IOException {
        System.out.println("next block is " + server.findNextFreeBlock());
    }

    public void showAboutDialog(ActionEvent event){

        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("ABOUT");
        aboutDialog.setHeaderText(null);
        aboutDialog.setContentText("ʹ�÷�����");
        aboutDialog.showAndWait();
    }
}