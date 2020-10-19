package sample;

import javafx.beans.property.SimpleStringProperty;

public class DataOfFat {
    private final SimpleStringProperty block0 = new SimpleStringProperty();
    private final SimpleStringProperty val0 = new SimpleStringProperty();
    private final SimpleStringProperty block1 = new SimpleStringProperty();
    private final SimpleStringProperty val1 = new SimpleStringProperty();
    private final SimpleStringProperty block2 = new SimpleStringProperty();
    private final SimpleStringProperty val2 = new SimpleStringProperty();
    private final SimpleStringProperty block3 = new SimpleStringProperty();
    private final SimpleStringProperty val3 = new SimpleStringProperty();


    public DataOfFat(int block[] , int val[]) {
        setBlock0(String.valueOf(block[0]));
        setVal0(String.valueOf(val[0]));
        setBlock1(String.valueOf(block[1]));
        setVal1(String.valueOf(val[1]));
        setBlock2(String.valueOf(block[2]));
        setVal2(String.valueOf(val[2]));
        setBlock3(String.valueOf(block[3]));
        setVal3(String.valueOf(val[3]));
    }

    public String getBlock0() {
        return block0.get();
    }

    public SimpleStringProperty block0Property() {
        return block0;
    }

    public void setBlock0(String block0) {
        this.block0.set(block0);
    }

    public String getVal0() {
        return val0.get();
    }

    public SimpleStringProperty val0Property() {
        return val0;
    }

    public void setVal0(String val0) {
        this.val0.set(val0);
    }

    public String getBlock1() {
        return block1.get();
    }

    public SimpleStringProperty block1Property() {
        return block1;
    }

    public void setBlock1(String block1) {
        this.block1.set(block1);
    }

    public String getVal1() {
        return val1.get();
    }

    public SimpleStringProperty val1Property() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1.set(val1);
    }

    public String getBlock2() {
        return block2.get();
    }

    public SimpleStringProperty block2Property() {
        return block2;
    }

    public void setBlock2(String block2) {
        this.block2.set(block2);
    }

    public String getVal2() {
        return val2.get();
    }

    public SimpleStringProperty val2Property() {
        return val2;
    }

    public void setVal2(String val2) {
        this.val2.set(val2);
    }

    public String getBlock3() {
        return block3.get();
    }

    public SimpleStringProperty block3Property() {
        return block3;
    }

    public void setBlock3(String block3) {
        this.block3.set(block3);
    }

    public String getVal3() {
        return val3.get();
    }

    public SimpleStringProperty val3Property() {
        return val3;
    }

    public void setVal3(String val3) {
        this.val3.set(val3);
    }
}
