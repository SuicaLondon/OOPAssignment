import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextFieldWithPlaceHolder extends TextField {
    private String placeholder;

    public TextFieldWithPlaceHolder(int columns, String placeholder) throws HeadlessException {
        super(columns);
        this.placeholder = placeholder;
        initPlaceholderOfTextfield(placeholder);
        // proof display black when running in command line
        setBackground(Color.white);
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                if (getText().equals(placeholder)) {
                    clearPlaceHolderOfTextfield();
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (getText().isEmpty()) {
                    initPlaceholderOfTextfield(placeholder);
                }
            }
        });
    }

    public String getText() {
        return super.getText();
    }

    /**
     * get the user inputted text (ignore placeholder)
     * @return inputted text
     */
    public String getRawText() {
        String text = super.getText();
        return text.equals(placeholder) ? "" : text;
    }

    /**
     * set the user inputted text (ignore placeholder)
     * @param text
     */
    public void setRawText(String text) {
        super.setText(text);
        setForeground(Color.BLACK);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void initPlaceholderOfTextfield(String placeholder) {
        setText(placeholder);
        setForeground(Color.GRAY);
    }

    public void clearPlaceHolderOfTextfield() {
        setText("");
        setForeground(Color.BLACK);
    }
}
