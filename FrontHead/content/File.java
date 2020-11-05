package FrontHead.content;

public class File implements CatEntry {
    private String name ;

    public File(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
