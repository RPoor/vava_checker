package sk.stuba.xpoor.regextester;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TesterWindow extends JFrame {
    private final Set<Preprocessor> preprocessorSet = new HashSet<>(Preprocessor.values().length);

    private final TextArea textEntry;
    private final JTextField regexEntry;
    private final JTextField successBox;

    private final JCheckBox caseInsensitiveToggle;
    private final JCheckBox specialCharacterIgnoreToggle;

    private void recheckCallback() {
        boolean matches;
        try {
            var p = Pattern.compile(regexEntry.getText());
            var preprocessed = textEntry.getText();
            for (var preprocessor : preprocessorSet) {
                preprocessed = preprocessor.apply(preprocessed);
            }
            matches = p.matcher(preprocessed).matches();
        } catch (PatternSyntaxException e) {
            successBox.setText("ERR");
            return;
        }
        if (matches) {
            successBox.setText("OK");
        } else {
            successBox.setText("NG");
        }
    }

    private void toggle(JCheckBox box, Preprocessor p) {
        boolean s = box.isSelected();
        if (s) {
            preprocessorSet.add(p);
        } else {
            preprocessorSet.remove(p);
        }
        recheckCallback();
    }

    private void caseInsensitiveCallback() {
        toggle(caseInsensitiveToggle, Preprocessor.CaseInsensitive);
    }

    private void specialCharacterIgnoreCallback() {
        toggle(specialCharacterIgnoreToggle, Preprocessor.IgnoreSpecialCharacters);
    }

    private void trimCleanCallback() {
        textEntry.setText(Preprocessor.TrimClean.apply(textEntry.getText()));
    }

    public TesterWindow() {
        setLayout(new GridBagLayout());
        var c = new GridBagConstraints();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textEntry = new TextArea(20, 45);
        regexEntry = new JTextField();
        successBox = new JTextField(3);

        caseInsensitiveToggle = new JCheckBox("Case Insensitive");
        specialCharacterIgnoreToggle = new JCheckBox("Ignore special characters");
        var trimCleanButton = new JButton("Trim and clean text");

        var textEntryLabel = new JLabel("Text to test");
        var regexEntryLabel = new JLabel("Regex");
        var successLabel = new JLabel("Matches");

        var refreshButton = new JButton("Refresh");

        textEntry.addTextListener((event) -> recheckCallback());
        regexEntry.addCaretListener((event) -> recheckCallback());

        caseInsensitiveToggle.addActionListener((event) -> caseInsensitiveCallback());
        specialCharacterIgnoreToggle.addActionListener((event) -> specialCharacterIgnoreCallback());
        trimCleanButton.addActionListener((event) -> trimCleanCallback());
        refreshButton.addActionListener((event) -> recheckCallback());

        successBox.setEditable(false);
        c.gridx = 0;
        c.gridy = 0;
        add(textEntryLabel, c);
        c.gridx = 1;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textEntry, c);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 0;
        add(regexEntryLabel, c);
        c.gridx = 1;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(regexEntry, c);
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 2;
        add(caseInsensitiveToggle, c);
        c.gridwidth = 1;
        c.gridx = 3;
        add(specialCharacterIgnoreToggle, c);
        c.gridx = 4;
        add(trimCleanButton, c);
        c.gridx = 0;
        c.gridy = 3;
        add(successLabel, c);
        c.gridx = 1;
        add(successBox, c);
        c.gridx = 2;
        add(refreshButton, c);
        pack();
        recheckCallback();
    }

    public static void main(String[] args) {
        var win = new TesterWindow();
        win.setVisible(true);
    }
}
