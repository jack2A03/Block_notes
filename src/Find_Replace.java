import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class Find_Replace extends JDialog implements ActionListener{

    private JButton btnReplaceAll = null;
    private JButton btnCancel = null;

    private JTextField txtReplace = new JTextField(20);

    private JCheckBox ckbMatchCase = null;
    private JCheckBox ckbWords = null;

    private String strFind = "";

    private String currentTxt;

    private final JTextArea parentTxt;

    public Find_Replace(JTextArea parentTxt){
        this.currentTxt = parentTxt.getText();
        this.parentTxt = parentTxt;
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Find and Replace");
        setModal(true);
        initUI();
    }

    public void initUI(){
        JPanel tabFindReplace = new JPanel(new GridLayout(5,1));
        JLabel lblFind = new JLabel("Find what:      ");
        JTextField txtFind = new JTextField(20);
        txtFind.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                strFind = txtFind.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                strFind = txtFind.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                strFind = txtFind.getText();
            }
        });
        JLabel lblNumFound = new JLabel("   0/0");
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
        ckbMatchCase.setToolTipText("Finds matches only if they have the same cases");
        JLabel lblMatchCase = new JLabel("Match cases   ");
        lblMatchCase.setToolTipText("Finds matches only if they have the same cases");
        pnlMatchCase.add(lblMatchCase);
        pnlMatchCase.add(ckbMatchCase);
        JPanel pnlWords = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ckbWords = new JCheckBox();
        ckbWords.setToolTipText("Finds matches only if the whole word is the same as the searching word");
        JLabel lblWords = new JLabel("Words              ");
        lblWords.setToolTipText("Finds matches only if the whole word is the same as the searching word");
        pnlWords.add(lblWords);
        pnlWords.add(ckbWords);
        JButton btnReplace = new JButton("Replace");
        btnReplace.addActionListener(this);
        btnReplaceAll = new JButton("Replace All");
        btnReplaceAll.addActionListener(this);
        JButton btnFindNext = new JButton("Find Next");
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

    public void replaceAll(String replacement){
        if(ckbMatchCase.isSelected()){
            if(!ckbWords.isSelected()) {
                currentTxt = currentTxt.replaceAll(strFind, replacement);
                parentTxt.setText(currentTxt);
            }
                //TODO replace all matching case and whole words
        }else {
            if(!ckbWords.isSelected()) {
                String replaceString = "(?i)" + Pattern.quote(strFind);
                currentTxt = currentTxt.replaceAll(replaceString, replacement);
                parentTxt.setText(currentTxt);
            }
                //TODO replace all whole words
        }
    }

    //TODO find single section

    //TODO replace single section

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel){
            dispose();
        }
        if (e.getSource() == btnReplaceAll){
            replaceAll(txtReplace.getText());
        }
    }
}
