import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotePad extends JFrame implements ActionListener,DocumentListener,WindowListener, CaretListener {

    private JMenuItem mniNew = null;
    private JMenuItem mniopen = null;

    private JMenuItem mniSave = null;
    private JMenuItem mniSaveAs = null;

    private JMenu mnpZoom = null;
    private JMenuItem mniZoomIn = null;
    private JMenuItem mniZoomOut = null;
    private JMenuItem mniResetZoom = null;

    private JMenuItem mniExit = null;

    private JMenuItem mniAbout = null;

    private JTextArea txt = null;

    private String absolutePath = "/home/jacck0/Untitled.txt";

    private String title = "Untitled";

    private boolean saved = true;
    private boolean savedSmw = false;

    private String savedText = "";

    private int fontSize = 12;

    private JLabel lblLineCol = new JLabel("Ln 1,Col 1");

    public NotePad(){
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setTitle(this.title);
        initUi();
        setVisible(true);
    }

    private void initUi() {

        JMenuBar menuBar = new JMenuBar();
        JMenu mnFile = new JMenu("File");
        mniNew = new JMenuItem("New");
        mniNew.addActionListener(this);
        mniopen = new JMenuItem("Open...");
        mniopen.addActionListener(this);
        mnFile.add(mniNew);
        mnFile.add(mniopen);
        mnFile.addSeparator();
        mniSave = new JMenuItem("Save");
        mniSave.addActionListener(this);
        mniSaveAs = new JMenuItem("Save as...");
        mniSaveAs.addActionListener(this);
        mnFile.add(mniSave);
        mnFile.add(mniSaveAs);
        mnFile.addSeparator();
        mniExit = new JMenuItem("Exit");
        mniExit.addActionListener(this);
        mnFile.add(mniExit);

        JMenu mnView = new JMenu("View");
        mnpZoom = new JMenu("Zoom");
        mniZoomIn = new JMenuItem("Zoom in");
        mniZoomOut = new JMenuItem("Zoom out");
        mniResetZoom = new JMenuItem("Reset zoom");
        mnpZoom.add(mniZoomIn);
        mnpZoom.add(mniZoomOut);
        mnpZoom.add(mniResetZoom);
        mniZoomIn.addActionListener(this);
        mniZoomOut.addActionListener(this);
        mniResetZoom.addActionListener(this);
        mnView.add(mnpZoom);

        JMenu mnHelp = new JMenu("Help");
        mniAbout = new JMenuItem("About");
        mniAbout.addActionListener(this);
        mnHelp.add(mniAbout);
        menuBar.add(mnFile);
        menuBar.add(mnView);
        menuBar.add(mnHelp);
        add(menuBar, BorderLayout.NORTH);

        txt = new JTextArea();
        txt.addCaretListener(this);
        txt.getDocument().addDocumentListener(this);
        JScrollPane scrollPane = new JScrollPane(txt);
        add(scrollPane,BorderLayout.CENTER);

        JMenuBar mnSouth = new JMenuBar();
        JPanel pnlSouth = new JPanel(new GridLayout(1,2));
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        lblLineCol.setFont(new Font(lblLineCol.getFont().getName(),lblLineCol.getFont().getStyle(),10));
        pnlLeft.add(lblLineCol);
        pnlLeft.add(new JSeparator());

        pnlSouth.add(pnlLeft);
        pnlSouth.add(pnlRight);
        mnSouth.add(pnlSouth);
        add(mnSouth,BorderLayout.SOUTH);
    }

    private void save() {
        try {
            PrintWriter pw = new PrintWriter(absolutePath);
            pw.println(txt.getText());
            pw.close();
            savedText = txt.getText();
            setTitle(title);
            saved = true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAs(){
        JFileChooser fileChooser = new JFileChooser(absolutePath);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            absolutePath = fileToSave.getAbsolutePath();
            if(fileToSave.getName().endsWith(".txt"))
                title = fileToSave.getName().substring(0,fileToSave.getName().length()-4);
            else
                title = fileToSave.getName();
            save();
        }
        savedSmw = true;
    }

    private void open(){
        int confirmation = -1;
        if(!savedSmw || !saved) {
            confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if (confirmation == 0) {
            if (savedSmw){
                save();
            }else{
                saveAs();
            }
        }
        if(confirmation != 2){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to open");

            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                absolutePath = fileToSave.getAbsolutePath();
                if(fileToSave.getName().endsWith(".txt"))
                    title = fileToSave.getName().substring(0,fileToSave.getName().length()-4);
                else
                    title = fileToSave.getName();
                setTitle(title);
                saved = true;
                savedSmw = true;
                try {
                    BufferedReader bf = new BufferedReader(new FileReader(absolutePath));
                    txt.read(bf,null);
                    savedText = txt.getText();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void newNotePad() {
        int confirmation = -1;
        if(!savedSmw || !saved) {
            confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if (confirmation == 0){
            saveAs();
            dispose();
        }else if (confirmation == 1){
            dispose();
        }
        if(confirmation!=2 && confirmation!=-1) {
            dispose();
            new NotePad();
        }
    }

    private void isSaved() {
        if(savedText.equals(txt.getText())){
            saved = true;
            setTitle(title);
        }else{
            saved = false;
            setTitle(title+"*");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mniNew){
            newNotePad();
        }
        if (e.getSource() == mniopen){
            open();
        }
        if (e.getSource() == mniSave){
            if(!savedSmw)
                saveAs();
            else
                save();
        }
        if (e.getSource() == mniSaveAs){
            saveAs();
        }
        if (e.getSource() == mniExit){
            int confirmation = -1;
            if(!savedSmw || !saved) {
                confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
            }

            if (confirmation == 0){
                saveAs();
                dispose();
            }else if (confirmation == 1 || confirmation == -1){
                dispose();
            }
        }
        if (e.getSource() == mniAbout){
            JOptionPane.showMessageDialog(this,"Block notes 1.0","About",JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == mniZoomIn){
            if(fontSize < 72) {
                fontSize += 2;
                txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
                System.out.println(fontSize);
                System.out.println(txt.getFont());
            }
        }
        if (e.getSource() == mniZoomOut){
            if(fontSize > 2) {
                fontSize -= 2;
                txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
            }
        }
        if(e.getSource() == mniResetZoom){
            fontSize = 12;
            txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        isSaved();
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        isSaved();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        isSaved();
    }

    //region WindowListener
    @Override
    public void windowClosing(WindowEvent e) {
        int confirmation = -1;
        if (!savedSmw || !saved) {
            confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if (confirmation == 0) {
            saveAs();
            dispose();
        } else if (confirmation == 1) {
            dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int caretpos = txt.getCaretPosition();
        int row = 0;
        try {
            row = txt.getLineOfOffset(caretpos);
            int column = caretpos - txt.getLineStartOffset(row);
            lblLineCol.setText("Ln "+(row+1)+",Col "+(column+1));
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    //endregion
}
