package BackGround;

import FrontHead.content.Catalogue;
import FrontHead.content.VirtualFile;
import sample.Controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    Controller controller;
    private File diskFile;
    private int[] fatTable;
    private byte[] writeBuffer;  		//写缓冲
    private byte[] readBuffer = new byte[64];  		//读缓冲
    private boolean Reading = false;

    private Vector<byte[]> curCatalogue;
    //private String[] curCatalogue;

    private final static int ONE_BLOCK_LENGTH = 64;
    public final static int SAVED_SUCESS = 0;
    public final static int ADD_SUCESS = 0;
    public final static int CAT_IS_FULL = 1;
    public final static int NO_MORE_DISK_SPACE = 2;
    public final static int FILE_EXISTED = 3;

    public final static String DISK_FILE_PATH = "src/BackGround/diskFile";
    public final static String TEST_FILE_PATH = "src/BackGround/testFile";
    public final static String EMPTY_ENTRY = "$0000000";//空目录项

    public boolean isReading() {
        return Reading;
    }

    public void setReading(boolean reading) {
        Reading = reading;
    }

    public Server () throws IOException {
        init();
    }
    public Server(Controller controller) throws IOException {
        setController(controller);
        init();
        //readFile();
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    private void init() throws IOException {
        fatTable = new int[128];
        curCatalogue = new Vector<>(8);
        int ftLength = fatTable.length;
        this.diskFile = new File(DISK_FILE_PATH);
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
            initRootCat(outFat);

            //创建好64*128字节的文件
            byte[] areaByte = new byte[64*128-192];
            outFat.write(areaByte);
            outFat.close();
        }
        //初始化fatTable
    }

    public void readCat(Catalogue catalogue) throws IOException {
        /*
        * 该功能仅在程序启动时调用，需要递归遍历各个目录
        * 根据 catalogue 在底层的bytes生成逻辑层的各个内容
        * */

        byte[] blockContent = readBlock(catalogue.getFirstBlock());
        for (int i = 0; i < 8; i++) {
            byte[] entryBytes =  new byte[8];
            for (int j = 0; j < 8; j++) {
                entryBytes[j] = blockContent[i*8+j];
            }//读取下一个目录项

            if (entryBytes[0]=='$'){
                continue;
            }
            else if(isFileEntry(entryBytes)){
                //如果是文件目录项
                Map<String,byte[]> fm = decodeFileEntry(entryBytes);
                VirtualFile file =
                        new VirtualFile(new String(fm.get("name")),catalogue,
                                catalogue.getAbsPath(),this,fm.get("firstBlock")[0]);
                catalogue.addFileEntry(file);
            }else {
                //如果是登记目录项
                Map<String,byte[]> cm = decodeCatEntry(entryBytes);
                Catalogue childrenCat =
                        new Catalogue(new String(cm.get("name")),catalogue,cm.get("firstBlock")[0]);
                catalogue.addCatEntry(childrenCat);
                readCat(childrenCat);
            }

        }

    }
    private void initRootCat(OutputStream outFat) throws IOException {
        for (int i = 0 ; i < 8 ; i++ ){
            curCatalogue.add(new byte[]{'$','0','0','0','0','0','0','0'});
            outFat.write(curCatalogue.get(0));
        }
        //初始化diskFile的时候添加根目录

    }


    public int addFile(VirtualFile file) throws IOException {
        /*
         * 1. 根据传入的file，获取对应的控制信息（文件目录项需要的信息有 1.文件名 2.文件类型 3.文件属性 4.文件初始盘块号 5.文件长度 ）
         *   其中 1.文件名 4.文件初始盘块号 可以调用 file.geName 和 getBlock 调用
         *    2.文件类型名，即表示该文件是.xx 默认为 ".rw"所以这个就直接 typeBytes = new bytes[]{'r','w'}即可
         *    3.文件属性默认表示可读可写的文件，即 ATTRBytes = new bytes[]{4}
         *    5. 文件长度，新建的文件text为空，但仍然占用一个盘块，所以这个属性默认为 1 即可， 即lenBytes = new bytes[]{1}
         *  根据以上属性拼接得到目录项 bytes[] ( 名称为entryBytes )，写入当前目录中
         *  2. 更新fat表
         *  */

        /*
         * cat目录项信息 (null表示未使用，填写 0 )
         * 内容目录   : | 文  件  名 |  类型名 | 属性 | 起始盘块号 |  文件长度 |
         * byteskey  : | [0][1][2] | [3][4] | [5] |    [6]    |    [7]   |
         * */
        byte[] entryBytes = new byte[0];
        entryBytes = mergeBytes(entryBytes, file.getName().getBytes(), 3);
        entryBytes = mergeBytes(entryBytes, new byte[] {'r','w'}, 2);
        entryBytes = mergeBytes(entryBytes, new byte[] {4}, 1);//
        entryBytes = mergeBytes(entryBytes, new byte[]{(byte) file.getFirstBlock()}, 1);
        entryBytes = mergeBytes(entryBytes, new byte[] {1}, 1);

        fatTable[file.getFirstBlock()] = -1;
        int status = writeEntry(file.getCatalogue(),entryBytes);

        updateFat();

        //int needBlock = text.length()/64+1;
        int haveBlock = 0;
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


        return ADD_SUCESS;
    }


    public String readFile (int firstBlock){
        StringBuffer text = new StringBuffer();
        /*步骤
         * 1. 根据传入firstBlock查询fat表
         * 2. 根据fat表读取text，伪代码如下:
         * int curBlock = firstBlock
         * while(curBlock != -1){
         *   String termText = getDiskText (curBlock) 用readBlock读取后再转换回字符串
         *   text += termText
         *   curBlock = fatTable[curBlock]
         * }
         * 3. 返回text值
         * */
        int curBlock = firstBlock;
        while(curBlock != -1) {
            try {
                byte[] thisBytes = readBlock(curBlock);
                text.append(new String(thisBytes));
                curBlock = fatTable[curBlock];
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return text.toString();
    }
    public void addCat (Catalogue catalogue) throws IOException {
        /*需要完成的功能
        * 1. server根据传入的catalougue ，获取对应的控制信息（目录项需要的控制信息有1.目录名2.目录属性3.起始盘块号
        * 目录名和目录属性 getName 和 getBlock 即可，目录属性查阅指导书在这个方法可以自己计算），拼接得到
        * 将登记目录项的信息以byte[ ]的形式写入diskFile（写到程序当前的目录的位置）
        * 2. 更新fat表
        * 3. 初始化新增的文件夹在diskFile的目录项（就是写入8个空目录项，即"$0000000"）到catalougue的diskFile区域
        * ************ #1步 和 #3 步写到diskFile的位置应该是不一致的，需要加以区分 **********
        *  */

        /*
        * cat目录项信息 (null表示未使用，填写 0 )
        * 内容目录   : | 目  录  名 |  null  | 属性 | 起始盘块号 |  null |
        * byteskey  : | [0][1][2] | [3][4] | [5] |    [6]    |  [7]  |
        * */
        byte[] entryBytes  = new byte[0];//准备写入的目录项（写到当前目录下）
        entryBytes = mergeBytes(entryBytes,catalogue.getName().getBytes(),3); //目录名
        entryBytes = mergeBytes(entryBytes,new byte[2],2);  //null
        entryBytes = mergeBytes(entryBytes,new byte[]{8},1);//目录属性
        entryBytes = mergeBytes(entryBytes,new byte[]{(byte) catalogue.getFirstBlock()},1);
        entryBytes = mergeBytes(entryBytes,new byte[1],1);
        // 生成目录项

        int status = writeEntry(catalogue.getParent(),entryBytes);
        //将目录项写到diskFile中的对应位置

        System.out.println("this cat fb is " + catalogue.getFirstBlock());

        fatTable[catalogue.getFirstBlock()] = -1;
        updateFat();
        //更新fat表

        for (int i = 0; i < 8; i++) {
            removeEntry(catalogue,i);
        }
        //将所有目录项置空

    }

    public void delCat (Catalogue catalogue) throws IOException {
        /*
         * 1. 获取父目录catalougue.getParent()
         * 2. 获取父目录在diskFile的信息删除对应的目录项
         * （可以使用正则表达式匹配目录名的初始位置，并计算目录项的起始，并清空）
         * 3. 获取起始盘块号码catalougue.getFirstBlock()
         * 4. 将fat表对应的val置0（表示该盘块空闲）
         * */

        Catalogue parent = catalogue.getParent();
        //获取父目录

        byte[] parentInfo = readBlock(parent.getFirstBlock());
        //获取父目录在diskFile信息


        for (int i = 0; i < 8; i++) {
            byte [] entryBytes = new byte[8];
            for (int j = 0; j < 8; j++) {
                entryBytes[j] = parentInfo[i*8+j];
            }//获取单个目录项
            String curCatName = new String(decodeCatEntry(entryBytes).get("name"));
            //获取当前目录项的名字

            if (curCatName.equals(catalogue.getName())){
                //如果当前目录项匹配到需要del的目录
                removeEntry(parent,i);
                //移除匹配到的目录项
            }else {
                //不必考虑找不到cat的情形，因为传入了一个catlogue就说明该目录一定存在了
            }
        }

        fatTable[catalogue.getFirstBlock()] = 0;
        updateFat();
        //获取起始盘块号


        //fatTable[val] = null;
        //fat表val置0
    }

//    public void delCat (Catalogue catalogue){
//        /*
//         * 1. 获取父目录catalougue.getParent()
//         * 2. 获取父目录在diskFile的信息删除对应的目录项
//         * （可以使用正则表达式匹配目录名的初始位置，并计算目录项的起始，并清空）
//         * 3. 获取起始盘块号码catalougue.getFirstBlock()
//         * 4. 将fat表对应的val置0（表示该盘块空闲）
//         * */
//        String parentcat = new string();
//        parentcat = catalogue.getParent();
//        //获取父目录
//
//        //获取父目录在diskFile信息
//
//        int block = catalogue.getFirstBlock();
//        //获取起始盘块号
//
//        fatTable[val] = null;
//        //fat表val置0
//
//    }

    public void changeCat (Catalogue catalogue) throws IOException {
        /*
        * 一般只有变更文件名的需求
        * 所以找到对应的目录项，将对应的目录名字的部分修改即可
        * */

        byte[] parentInfo = readBlock(catalogue.getParent().getFirstBlock());
        for (int i = 0; i < 8; i++) {
            byte [] curEntryBytes = new byte[8];
            for (int j = 0; j < 8; j++) {
                curEntryBytes[j] = parentInfo[i*8+j];
            }//获取单个目录项
            int curBLock = decodeCatEntry(curEntryBytes).get("firstBlock")[0];
            //获取当前目录项的盘块号

            if (curBLock == catalogue.getFirstBlock()){
                //如果当前目录项匹配到需要change的目录项
                byte[] entryBytes  = new byte[0];//准备写入的目录项（写到当前目录下）
                entryBytes = mergeBytes(entryBytes,catalogue.getName().getBytes(),3); //目录名
                entryBytes = mergeBytes(entryBytes,new byte[2],2);  //null
                entryBytes = mergeBytes(entryBytes,new byte[]{8},1);//目录属性
                entryBytes = mergeBytes(entryBytes,new byte[]{(byte) catalogue.getFirstBlock()},1);
                entryBytes = mergeBytes(entryBytes,new byte[1],1);
                int status = writeEntry(catalogue.getParent(),entryBytes);
                //生成目录项，并加入父目录
            }else {
                //不必考虑找不到cat的情形，因为传入了一个catlogue就说明该目录一定存在了
            }
        }





        //将目录项写到diskFile中的对应位置



    }
    public void delFile(VirtualFile file){
        /*
         * 这个方法和delCat结构是差不多的，可以互相参照
         * 1. 获取父录file.getCatalogue()
         * 2. 获取父目录在diskFile的信息删除对应的目录项
         * （可以使用正则表达式匹配目录名的初始位置，并计算目录项的起始，并清空）
         * 3. 获取起始盘块号码file.getFirstBlock()
         * 4. 将fat表对应的val置0（表示该盘块空闲）
         * */
    }
    public void changeFileATTR (VirtualFile file){
        /* 这个方法需要与saveFile区分开，一个是修改文件的内容，一个是修改文件的属性
         * 同理这个方法和changeCat结构是差不多的，可以互相参照
         *
         * 实现方法1：直接先调用一次 delFile，再调用一次addFile
         *（还需要更新file的firstBlock）
         *
         * 实现方法2：先依旧按更该文件名的功能来做，后续可能有更多的需求
         * 所以找到对应的目录项，将对应的目录名字的部分修改即可
         * */
    }

    public int saveFile(VirtualFile file) throws IOException {
        String text = file.getLatestText();
        int curBlock = file.getFirstBlock();
        file.setFileLength(text.length()/64);
        int needBlock = text.length()/64+1;
        RandomAccessFile rdf = new RandomAccessFile(diskFile,"rw");

        int termBlock = fatTable[curBlock];
        while (termBlock!=-1){
            int x = fatTable[termBlock];
            fatTable[termBlock] = 0;
            updateFat();
            termBlock = x;
        }//将磁盘块先释放

        int lastFatNum = curBlock;//记录上一次的盘块号
        int textNum = 0;//第几个text剪切段，因为存在缓冲区，text可能要分多次写入磁盘

        writeBuffer = new byte[64];
        while(needBlock > 0) {
            rdf.seek((long)curBlock*64);
            if(textNum >= text.length()/64 ) {
                //写入最后一个剪切段
                for (int i = 0; i < 64; i++) {
                    writeBuffer[i] = (textNum*64+i<text.length())?
                            (text.substring(textNum*64).getBytes()[i]):(0);
                    fatTable[curBlock] = -1;
                    updateFat();
                }
            }else {
                //写入整整64个剪切段
                writeBuffer = text.substring(textNum*64, textNum*64+64).getBytes();
                textNum++;
                fatTable[curBlock] = findNextFreeBlock();
                updateFat();
            }
            rdf.write(writeBuffer);
            curBlock = findNextFreeBlock();
            needBlock--;

        }
        rdf.close();
        return SAVED_SUCESS;
    }




    /*工具方法集*/
    public boolean isFileEntry (byte[] entryBytes){
        /*(使用前需要先判断是不是空目录才能继续判断！！！！！！！！！！！！)
        * 判断是文件目录项还是等级目录项，登记目录项
        * 文件目录项为true 登记目录项为fallse*/
        return (entryBytes[5]!=8)?true:false;
    }
    public Map<String , byte[]> decodeCatEntry(byte[] entryBytes){
        /*将目录项字节留分解成key val对
        * 可选key：(括号为对应bytes长度)
        * name(3),ATTR(1),firstBlock（1）
        *
        * */
        Map <String, byte[]> catBytesMap = new HashMap<>();
        byte[] name = new byte[3];
        for (int i = 0; i < 3; i++) {
            name[i] = entryBytes[i];
        }//获取name
        byte[] ATTR = new byte[]{entryBytes[5]};
        byte[] firstBlock = new byte[]{entryBytes[6]};
        catBytesMap.put("name",name);
        catBytesMap.put("ATTR",ATTR);
        catBytesMap.put("firstBlock",firstBlock);
        return catBytesMap;
    }
    public Map<String , byte[]> decodeFileEntry(byte[] entryBytes){
        /*将目录项字节留分解成key val对
         * 可选key：
         * name,type,ATTR,firstBlock,length
         * */
        Map <String, byte[]> fileBytesMap = new HashMap<>();
        byte[] name = new byte[3];
        for (int i = 0; i < 3; i++) {
            name[i] = entryBytes[i];
        }//获取name
        byte[] type = new byte[2];
        for (int i = 0; i < 2; i++) {
            name[i] = entryBytes[i];
        }//获取type
        byte[] ATTR = new byte[]{entryBytes[5]};
        byte[] firstBlock = new byte[]{entryBytes[6]};
        byte[] length = new byte[]{entryBytes[7]};
        fileBytesMap.put("name",name);
        fileBytesMap.put("type",type);
        fileBytesMap.put("ATTR",ATTR);
        fileBytesMap.put("firstBlock",firstBlock);
        fileBytesMap.put("length",length);
        return fileBytesMap;
    }
    public int findNextFreeBlock() {
        /*字如其意*/
        int blockNum = -1;
        for(int i=3; i<fatTable.length; i++) {
            if(fatTable[i] == 0) {
                //System.out.println("i is " + i);
                return i;
            }
        }
        return blockNum;
    }
    public void formatting() throws IOException {
        /*格式化磁盘，用于测试*/
        new File(DISK_FILE_PATH).delete();
        init();
    }

    public void showMeFat(){
        /*在控制台直接输出 4*32 fat表*/
        System.out.println("输出FAT表");
        for (int i = 0; i < 4; i++) {
            System.out.print("#"+i*32+" ~ #"+ (i*32+31) +" : ");
            for (int j = 0; j < 32; j++) {
                System.out.print(fatTable[i*32+j]);
            }
            System.out.println();
        }
    }

    public byte[] mergeBytes (byte[] bytesA , byte[] bytesB ,int offset){
        /*连接两个byte数组，在生成目录项的时候可能会用到，如果想连接 String str，传入 str.getBytes() 即可
        * offset表示bytesB应该的长度，比如生成文件目录项，文件名为 “a” , bytesB 的长度仅仅为 1
        * 但需要的bytesB长度应该为 3 （目录项文件名的字节长度为3）
        */
        byte[] sumBytes = new byte[bytesA.length + offset];
        int index = 0;
        for (int i = 0; i < bytesA.length; i++) {
            sumBytes[index++] = bytesA[i];
        }
        for (int i = 0; i < offset; i++) {
            sumBytes[index++] = (i < bytesB.length) ? bytesB[i] : 0;
        }
        return sumBytes;
    }
    public void removeEntry (Catalogue catalogue , int index) throws IOException {
        /*删除catalogue中第index个目录项（从0开始计数）*/
        byte[] blankBytes = EMPTY_ENTRY.getBytes();
        RandomAccessFile raf = new RandomAccessFile (new File(DISK_FILE_PATH),"rw");

        long catOffset = catalogue.getFirstBlock()*64 + index*8;
        raf.seek(catOffset);
        //获取偏移量

        raf.write(blankBytes);

        raf.close();

    }
    public int writeEntry (Catalogue catalogue , byte[] entryBytes ,int index) throws IOException {
        /*将 entryBytes(目录项信息，不能是空目录项) 写到 catalogue 指定的index中（diskFile层）
         * */
        RandomAccessFile raf = new RandomAccessFile (new File(DISK_FILE_PATH),"rw");
        long catOffset = catalogue.getFirstBlock()*64;
        raf.seek(catOffset);
        raf.read(readBuffer,0,64);
        //将对应的目录项读取出来读到读缓冲

        raf.seek(catOffset + index*8);
        //找到目录项应该插入的位置
        raf.write(entryBytes);

        raf.close();
        return ADD_SUCESS;
    }
    public int writeEntry (Catalogue catalogue , byte[] entryBytes) throws IOException {
        /*将 entryBytes(目录项信息，不能是空目录项) 写到 catalogue 中（diskFile层）
        * */
        RandomAccessFile raf = new RandomAccessFile (new File(DISK_FILE_PATH),"rw");
        long catOffset = catalogue.getFirstBlock()*64;
        raf.seek(catOffset);
        raf.read(readBuffer,0,64);
        //将对应的目录项读取出来读到读缓冲

        Pattern p = Pattern.compile("\\"+ EMPTY_ENTRY );
        Matcher m = p.matcher(new String(readBuffer));
        //匹配空目录项

        if (m.find()){
            raf.seek(catOffset + m.start());
            //找到目录项应该插入的位置
            raf.write(entryBytes);
        }else{
            raf.close();
            return CAT_IS_FULL;
        }

        raf.close();
        return ADD_SUCESS;
    }
    public void updateFat() throws IOException {
        /*将 ！已经修改好！ 的fat表更新到diskFile中*/
        RandomAccessFile raf = new RandomAccessFile(new File(DISK_FILE_PATH),"rw");
        byte[] wirteBuf = new byte[128];
        for (int i = 0; i < 128; i++) {
            wirteBuf[i] = (byte) fatTable[i];
        }
        raf.write(wirteBuf);
        raf.close();
    }
    public byte[] readBlock(int blocksnum) throws IOException {
        /*读取对应blocknum的块号内容*/
        byte[] readBuf = new byte[64];
        RandomAccessFile raf = new RandomAccessFile(new File(DISK_FILE_PATH),"rw");
        raf.seek(blocksnum*64);
        raf.read(readBuf,0,64);
        raf.close();
        return readBuf;

    }

}
