package BackGround;

import FrontHead.content.VirtualFile;
import sample.Controller;

import java.io.*;
import java.util.Vector;

public class Server {
    Controller controller;
    private File diskFile;
    private int[] fatTable;
    private String bufferIn;  		//读缓冲
    private String bufferOut;  		//写缓冲

    private Vector<byte[]> curCatalogue;
    //private String[] curCatalogue;

    private final static int ONE_BLOCK_LENGTH = 64;
    public final static int SAVED_SUCESS = 0;
    public final static int ADD_SUCESS = 0;
    public final static int CAT_IS_FULL = 1;
    public final static int NO_MORE_DISK_SPACE = 2;
    public final static int FILE_EXISTED = 3;


    public Server(Controller controller) throws IOException {
        fatTable = new int[128];
        setController(controller);
        init();
        //readFile();
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    private void init() throws IOException {
        curCatalogue = new Vector<>(8);
        int ftLength = fatTable.length;
        this.diskFile = new File("src/BackGround/diskFile");
        if(diskFile.exists()) {
            InputStream inFat = new FileInputStream(diskFile);
            byte[] inBytes = new byte[ftLength];
            inFat.read(inBytes);
            for(int i=0; i<ftLength; i++) {
                fatTable[i] = (int)inBytes[i];
            }

            for(int i=0; i<8; i++) {
                curCatalogue.add(new byte[8]);
                inFat.read(curCatalogue.get(i),0,8);
            }
            inFat.close();
        }else {
            fatTable[0] = -1;
            fatTable[1] = -1;
            fatTable[2] = -1;//第三个盘块放根目录
            //System.out.println("fl is"+fatTable.length);
            for(int i=3; i<ftLength; i++) {
                fatTable[i] = 0;
            }
            diskFile.createNewFile();
            OutputStream outFat = new FileOutputStream(diskFile);

            for(int i=0; i<ftLength; i++) {
                int temp = fatTable[i];
                outFat.write(temp);
            }
            addCat(outFat);

            //创建好64*128字节的文件
            byte[] areaByte = new byte[64*128-192];
            outFat.write(areaByte);
            outFat.close();
        }
        //初始化fatTable
    }

    private void readCat(){

    }
    private void addCat(OutputStream outFat) throws IOException {
        for (int i = 0 ; i < 8 ; i++ ){
            curCatalogue.add(new byte[]{'$','0','0','0','0','0','0','0'});
            outFat.write(curCatalogue.get(0));
        }
        //初始化diskFile的时候添加根目录

    }
    private void addCat(String name) {

    }
    public int findNextFreeBlock() {
        int blockNum = -1;
        for(int i=3; i<fatTable.length; i++) {
            if(fatTable[i] == 0) {
                System.out.println("i is " + i);
                return i;
            }
        }
        return blockNum;
    }

    public int addFile(VirtualFile file) throws IOException {


        int curBlock = findNextFreeBlock();//当前所在的盘块号
        //int needBlock = text.length()/64+1;
        int haveBlock = 0;

        file.setFirstBlock(curBlock);
        //curFile.setFileLength(needBlock);
        file.setAttribute("1");//文件属性怎么规定
        {
            //将文件属性的内容写到curCatalogue
        }
        int i = 0;

//        for (i = 0; i < 8; i++) {
//            System.out.println(curCatalogue[i].substring(7, 8));
//            if ("0".equals(curCatalogue[i].substring(7, 8))) {
//                freeCatalogue = i;
//                break;
//            }
//        }
        if(i == 8) {
            System.out.println("当前目录已满");
            return CAT_IS_FULL;
        }//判断当前目录项是否还有空间

        for(i=0; i<fatTable.length; i++) {
            if(fatTable[i] == 0) {
                haveBlock++;
            }
        }
//        if(haveBlock < needBlock) {
//            System.out.println("磁盘容量不足");
//            return NO_MORE_DISK_SPACE;
//        }//判断是否还有足够的空闲块

//        for (i = 0; i < 8; i++) {
//            if (fileName.equals(curCatalogue[i].substring(0, 2))) {
//                System.out.println("文件已存在");
//                return FILE_EXISTED;
//            }
//        }

        return ADD_SUCESS;
    }
    public int saveFile(VirtualFile file) throws IOException {
        byte[] curByte = null;//多次写入磁盘文件的byte数组
        String text = file.getLatestText();
        int curBlock = file.getFirstBlock();
        int needBlock = text.length()/64+1;
        RandomAccessFile rdf = new RandomAccessFile(diskFile,"rw");

        System.out.println("curblock is " + curBlock);
        int lastFatNum = curBlock;//记录上一次的盘块号
        int textNum = 0;//第几个text剪切段，因为存在缓冲区，text可能要分多次写入磁盘
        int freeCatalogue;//第几个目录项时空闲 的

        rdf.seek((long)curBlock*64);
        if(textNum*64+64 < text.length()) {
            bufferIn = text.substring(textNum*64, textNum*64+64);//0=64?0-63?
            textNum++;
        }else {
            bufferIn = text.substring(textNum*64, text.length());
        }
        rdf.write(bufferIn.getBytes());
        fatTable[curBlock] = -1; //先将fatTable中该项标为已用
        needBlock--;

        while(needBlock > 0) {
            curBlock = findNextFreeBlock();
            fatTable[lastFatNum] = curBlock;
            fatTable[curBlock] = -1;
            rdf.seek((long)curBlock*64);
            if(textNum*64+64 > text.length()) {
                bufferIn = text.substring(textNum*64, textNum*64+64);//0=64?0-63?
                textNum++;
            }else {
                bufferIn = text.substring(textNum*64, text.length());
            }
            rdf.write(bufferIn.getBytes());
            needBlock--;
        }
        //将更新后的fat表写进磁盘
        rdf.seek(0);
        for(int i=0; i<fatTable.length; i++) {
            int temp = fatTable[i];
            rdf.write(temp);
        }

        return SAVED_SUCESS;
    }
    public String readFile() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(diskFile,"r");
        raf.seek(64);
        return new String();
    }
}
