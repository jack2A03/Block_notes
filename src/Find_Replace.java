import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Find_Replace extends JDialog implements ActionListener {

    private JButton btnFindNext = null;
    private JButton btnReplaceAll = null;
    private JButton btnReplace = null;
    private JButton btnCancel = null;

    private JTextField txtReplace = new JTextField(20);

    private JCheckBox ckbMatchCase = null;
    private JCheckBox ckbWords = null;

    private final JLabel lblNumFound = new JLabel("   0/0");;

    private String strFind = "";

    private String currentTxt;

    private final JTextArea parentTxt;

    private int currentPos = 0;

    ArrayList<Integer> position = new ArrayList<>();

    public Find_Replace(JTextArea parentTxt) {
        this.currentTxt = parentTxt.getText();
        this.parentTxt = parentTxt;
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Find and Replace");
        setModal(true);
        initUI();
    }

    public void initUI() {
        JPanel tabFindReplace = new JPanel(new GridLayout(5, 1));
        JLabel lblFind = new JLabel("Find what:      ");
        JTextField txtFind = new JTextField(20);
        txtFind.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                strFind = txtFind.getText();
                populatePosition();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                strFind = txtFind.getText();
                populatePosition();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                strFind = txtFind.getText();
                populatePosition();
            }
        });
        JLabel lblReplace = new JLabel("Replace with: ");
        txtReplace = new JTextField(20);
        JPanel pnlFind = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFind.add(lblFind);
        pnlFind.add(txtFind);
        pnlFind.add(lblNumFound);
        JPanel pnlReplace = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlReplace.add(lblReplace);
        pnlReplace.add(txtReplace);
        JPanel pnlMatchCase = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ckbMatchCase = new JCheckBox();
        ckbMatchCase.addActionListener(this);
        ckbMatchCase.setToolTipText("Finds matches only if they have the same cases");
        JLabel lblMatchCase = new JLabel("Match cases   ");
        lblMatchCase.setToolTipText("Finds matches only if they have the same cases");
        pnlMatchCase.add(lblMatchCase);
        pnlMatchCase.add(ckbMatchCase);
        JPanel pnlWords = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ckbWords = new JCheckBox();
        ckbWords.addActionListener(this);
        ckbWords.setToolTipText("Finds matches only if the whole word is the same as the searching word");
        JLabel lblWords = new JLabel("Words              ");
        lblWords.setToolTipText("Finds matches only if the whole word is the same as the searching word");
        pnlWords.add(lblWords);
        pnlWords.add(ckbWords);
        btnReplace = new JButton("Replace");
        btnReplace.addActionListener(this);
        btnReplaceAll = new JButton("Replace All");
        btnReplaceAll.addActionListener(this);
        btnFindNext = new JButton("Find Next");
        btnFindNext.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.add(btnReplace);
        pnlBtn.add(btnReplaceAll);
        pnlBtn.add(btnFindNext);
        pnlBtn.add(btnCancel);
        tabFindReplace.add(pnlFind);
        tabFindReplace.add(pnlReplace);
        tabFindReplace.add(pnlMatchCase);
        tabFindReplace.add(pnlWords);
        tabFindReplace.add(pnlBtn);
        add(tabFindReplace);
    }

    public void replaceAll(String replacement) {
        if (ckbMatchCase.isSelected()) {
            if (!ckbWords.isSelected()) {
                currentTxt = currentTxt.replaceAll(strFind, replacement);
                parentTxt.setText(currentTxt);
            } else {
                replaceAllWholeWord();
            }
        } else {
            if (!ckbWords.isSelected()) {
                String replaceString = "(?i)" + Pattern.quote(strFind);
                currentTxt = currentTxt.replaceAll(replaceString, replacement);
                parentTxt.setText(currentTxt);
            } else {
                replaceAllWholeWord();
            }
        }
    }

    private void replaceAllWholeWord(){
        populatePosition();
        Object[] positionsArray = position.toArray();
        int strDiff = txtReplace.getText().length() - strFind.length();
        for (int i = 0; i<positionsArray.length;i++){
            parentTxt.replaceRange(txtReplace.getText(), Integer.parseInt(positionsArray[i]+"")+(i*strDiff), Integer.parseInt(positionsArray[i]+"")+(i*strDiff) + strFind.length());
            System.out.println(i);
        }
    }

    private void populatePosition() {
        currentTxt = parentTxt.getText();
        position = new ArrayList<>();
        String strToFind;
        if (strFind.length() < currentTxt.length()) {
            char[] chars;
            if (!ckbMatchCase.isSelected()) {
                chars = currentTxt.toLowerCase().toCharArray();
                strToFind = strFind.toLowerCase();
            } else {
                strToFind = strFind;
                chars = currentTxt.toCharArray();
            }
            StringBuilder createdString = new StringBuilder();
            for (int i = 0; i < currentTxt.length() - strToFind.length() + 1; i++) {
                for (int j = 0; j < strToFind.length(); j++) {
                    createdString.append(chars[i + j]);
                }
                if (createdString.toString().equals(strToFind)) {
                    if ((ckbWords.isSelected() && isWholeWord(i))) {
                        position.add(i);
                    } else if (!ckbWords.isSelected()) {
                        position.add(i);
                    }
                }
                createdString = new StringBuilder();
            }
            if (position.isEmpty()) {
                parentTxt.select(0, 0);
                lblNumFound.setText("   0/0");
            }else{
                if (currentPos == 0 && position.getFirst() != 0)
                    currentPos = position.getFirst()+strFind.length();
                parentTxt.select(currentPos+strFind.length(),currentPos);
                if ((position.indexOf(currentPos - strFind.length())+1) != position.size())
                    lblNumFound.setText("   " + (position.indexOf(currentPos - strFind.length())+2) + "/" + position.size());
                else
                    lblNumFound.setText("   " + 1 + "/" + position.size());
            }
        }
    }

    private void findNext() {
        populatePosition();
        if (!position.isEmpty()) {
            if ((currentPos == 0 || currentPos == position.getLast() + strFind.length())) {
                parentTxt.select(position.getFirst(), strFind.length() + position.getFirst());
                currentPos = strFind.length() + position.getFirst();
            } else {
                int index = position.indexOf(currentPos - strFind.length());
                parentTxt.select(position.get(index + 1), strFind.length() + position.get(index + 1));
                currentPos = strFind.length() + position.get(index + 1);
            }
        }
    }

    private void replace() {
        if (parentTxt.getSelectedText().equals(strFind) && !ckbMatchCase.isSelected()) {
            parentTxt.replaceRange(txtReplace.getText(), parentTxt.getSelectionStart(), parentTxt.getSelectionEnd());
        } else if (parentTxt.getSelectedText().equalsIgnoreCase(strFind)) {
            parentTxt.replaceRange(txtReplace.getText(), parentTxt.getSelectionStart(), parentTxt.getSelectionEnd());
        }
        findNext();
    }

    private boolean isWholeWord(int pos) {
        String charBefore = " ";
        if (pos != 0)
            charBefore = String.valueOf(currentTxt.charAt(pos - 1));
        String charAfter = String.valueOf(currentTxt.charAt(pos + strFind.length()));
        String regex = "^[^<>{}\"/|;:.,~!?@#$%^=&*\\]\\\\()\\[¿§«»ω⊙¤°℃℉€¥£¢¡®©0-9_+]*$";
        return ((!(charBefore.matches(regex)) || charBefore.equals(" ")) && ((!(charAfter.matches(regex)) || charAfter.equals(" "))));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel) {
            dispose();
        }
        if (e.getSource() == btnReplaceAll) {
            replaceAll(txtReplace.getText());
            populatePosition();
            currentPos = 0;
        }
        if (e.getSource() == btnFindNext) {
            findNext();
        }
        if (e.getSource() == ckbMatchCase) {
            populatePosition();
            currentPos = 0;
        }
        if (e.getSource() == ckbWords) {
            position = new ArrayList<>();
            currentPos = 0;
        }
        if (e.getSource() == btnReplace) {
            replace();
        }
    }
}
