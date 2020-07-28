import java.io.Serializable;

public class ClassToTransfer implements Serializable {
    private final String someTextBox;

    public ClassToTransfer(String someTextBox) {
        this.someTextBox = someTextBox;
    }

    public String getSomeTextBox() {
        return someTextBox;
    }
}