import javax.print.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

public class NotePad extends JFrame implements ActionListener, DocumentListener, WindowListener, CaretListener {

    private JMenuItem mniNew = null;
    private JMenuItem mniopen = null;

    private JMenuItem mniSave = null;
    private JMenuItem mniSaveAs = null;

    private JMenuItem mniPrint = null;

    private JMenuItem mniExit = null;

    private JMenuItem mniUndo = null;

    private JMenuItem mniCut = null;
    private JMenuItem mniCopy = null;
    private JMenuItem mniPaste = null;
    private JMenuItem mniDelete = null;

    private JMenu mnpZoom = null;
    private JMenuItem mniZoomIn = null;
    private JMenuItem mniZoomOut = null;
    private JMenuItem mniResetZoom = null;

    private JCheckBoxMenuItem mncStatusBar = null;
    private JCheckBoxMenuItem mncWordWrap = null;

    private JMenuItem mniAbout = null;

    private JTextArea txt = null;

    private String absolutePath = System.getProperty("user.home") + File.separator + "Desktop/Untitled.txt";

    private String title = "Untitled";

    private boolean saved = true;
    private boolean savedSmw = false;

    private String savedText = "";

    private int fontSize = 10;

    private JMenuBar mnSouth = new JMenuBar();

    private JLabel lblLineCol = new JLabel("Ln 1,Col 1   ");
    private JLabel lblCharNumber = new JLabel("   0 characters");

    private JLabel lblZoom = new JLabel("100%   ");

    public NotePad() {
        setSize(500, 600);
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
        mniPrint = new JMenuItem("Print");
        mniPrint.addActionListener(this);
        mnFile.add(mniPrint);
        mnFile.addSeparator();
        mniExit = new JMenuItem("Exit");
        mniExit.addActionListener(this);
        mnFile.add(mniExit);

        JMenu mnEdit = new JMenu("Edit");
        mniCut = new JMenuItem("Cut");
        mniCut.addActionListener(this);
        mnEdit.add(mniCut);
        mniCopy = new JMenuItem("Copy");
        mniCopy.addActionListener(this);
        mnEdit.add(mniCopy);
        mniPaste = new JMenuItem("Paste");
        mniPaste.addActionListener(this);
        mnEdit.add(mniPaste);
        mniDelete = new JMenuItem("Delete");
        mniDelete.addActionListener(this);
        mnEdit.add(mniDelete);

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

        mncStatusBar = new JCheckBoxMenuItem("Status bar");
        mncStatusBar.setState(true);
        mncStatusBar.addActionListener(this);
        mnView.add(mncStatusBar);

        mncWordWrap = new JCheckBoxMenuItem("Word wrap");
        mncWordWrap.setState(true);
        mncWordWrap.addActionListener(this);
        mnView.add(mncWordWrap);

        JMenu mnHelp = new JMenu("Help");
        mniAbout = new JMenuItem("About");
        mniAbout.addActionListener(this);
        mnHelp.add(mniAbout);
        menuBar.add(mnFile);
        menuBar.add(mnEdit);
        menuBar.add(mnView);
        menuBar.add(mnHelp);
        add(menuBar, BorderLayout.NORTH);

        txt = new JTextArea();
        txt.addCaretListener(this);
        txt.getDocument().addDocumentListener(this);
        txt.setLineWrap(true);
        txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
        JScrollPane scrollPane = new JScrollPane(txt);
        add(scrollPane, BorderLayout.CENTER);

        mnSouth = new JMenuBar();
        JPanel pnlSouth = new JPanel(new GridLayout(1, 2));
        JPanel pnlLeftGrid = new JPanel(new GridBagLayout());
        GridBagConstraints layoutContraintsLeft = new GridBagConstraints();
        JPanel pnlRightGrid = new JPanel(new GridBagLayout());
        GridBagConstraints layoutContraintsRight = new GridBagConstraints();

        lblLineCol.setFont(new Font(lblLineCol.getFont().getName(), lblLineCol.getFont().getStyle(), 10));
        lblCharNumber.setFont(new Font(lblCharNumber.getFont().getName(), lblCharNumber.getFont().getStyle(), 10));

        layoutContraintsLeft.gridy = 0;
        layoutContraintsLeft.gridx = 0;

        layoutContraintsLeft.weightx = 1.0;

        pnlLeftGrid.add(lblLineCol, layoutContraintsLeft);
        JSeparator sepLeft = new JSeparator(SwingConstants.VERTICAL);
        layoutContraintsLeft.gridy = 0;
        layoutContraintsLeft.gridx = 1;
        layoutContraintsLeft.fill = GridBagConstraints.VERTICAL;
        pnlLeftGrid.add(sepLeft, layoutContraintsLeft);
        layoutContraintsLeft.gridy = 0;
        layoutContraintsLeft.gridx = 2;
        pnlLeftGrid.add(lblCharNumber, layoutContraintsLeft);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLeft.add(pnlLeftGrid);

        layoutContraintsRight.gridy = 0;
        layoutContraintsRight.gridx = 0;

        layoutContraintsRight.weightx = 1.0;

        JLabel lblUTF = new JLabel("   UTF-16");
        lblUTF.setFont(new Font(lblUTF.getFont().getName(), lblUTF.getFont().getStyle(), 10));
        lblZoom.setFont(new Font(lblZoom.getFont().getName(), lblZoom.getFont().getStyle(), 10));
        pnlRightGrid.add(lblZoom, layoutContraintsRight);
        JSeparator sepRight = new JSeparator(SwingConstants.VERTICAL);
        layoutContraintsRight.gridy = 0;
        layoutContraintsRight.gridx = 1;
        layoutContraintsRight.fill = GridBagConstraints.VERTICAL;
        pnlRightGrid.add(sepRight, layoutContraintsRight);
        layoutContraintsRight.gridy = 0;
        layoutContraintsRight.gridx = 2;
        pnlRightGrid.add(lblUTF, layoutContraintsRight);

        JPanel pnlLright = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlLright.add(pnlRightGrid);

        pnlSouth.add(pnlLeft);
        pnlSouth.add(pnlLright);
        mnSouth.add(pnlSouth);
        add(mnSouth, BorderLayout.SOUTH);

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

    private void saveAs() {
        JFileChooser fileChooser = new JFileChooser(absolutePath);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            absolutePath = fileToSave.getAbsolutePath();
            if (fileToSave.getName().endsWith(".txt"))
                title = fileToSave.getName().substring(0, fileToSave.getName().length() - 4);
            else
                title = fileToSave.getName();
            save();
        }
        savedSmw = true;
    }

    private void open() {
        int confirmation = -1;
        if (!savedSmw || !saved) {
            confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if (confirmation == 0) {
            if (savedSmw) {
                save();
            } else {
                saveAs();
            }
        }
        if (confirmation != 2) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to open");

            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                absolutePath = fileToSave.getAbsolutePath();
                if (fileToSave.getName().endsWith(".txt"))
                    title = fileToSave.getName().substring(0, fileToSave.getName().length() - 4);
                else
                    title = fileToSave.getName();
                setTitle(title);
                saved = true;
                savedSmw = true;
                try {
                    BufferedReader bf = new BufferedReader(new FileReader(absolutePath));
                    txt.read(bf, null);
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
        if (!savedSmw || !saved) {
            confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if (confirmation == 0) {
            saveAs();
            dispose();
        } else if (confirmation == 1) {
            dispose();
        }
        if (confirmation != 2 && confirmation != -1) {
            dispose();
            new NotePad();
        }
    }

    private void isSaved() {
        if (savedText.equals(txt.getText())) {
            saved = true;
            setTitle(title);
        } else {
            saved = false;
            setTitle(title + "*");
        }
    }

    private void printToPrinter()
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new OutputPrinter(txt.getText()));
        boolean doPrint = job.printDialog();
        if (doPrint)
        {
            try
            {
                job.print();
            }
            catch (PrinterException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mniNew) {
            newNotePad();
        }
        if (e.getSource() == mniopen) {
            open();
        }
        if (e.getSource() == mniSave) {
            if (!savedSmw)
                saveAs();
            else
                save();
        }
        if (e.getSource() == mniSaveAs) {
            saveAs();
        }
        if (e.getSource() == mniExit) {
            int confirmation = -1;
            if (!savedSmw || !saved) {
                confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
            }

            if (confirmation == 0) {
                saveAs();
                dispose();
            } else if (confirmation == 1 || confirmation == -1) {
                dispose();
            }
        }
        if (e.getSource() == mniAbout) {
            JOptionPane.showMessageDialog(this, "Block notes 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == mniZoomIn) {
            if (fontSize < 50) {
                fontSize += 1;
                txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
            }
            lblZoom.setText((fontSize * 10) + "%   ");
        }
        if (e.getSource() == mniZoomOut) {
            if (fontSize > 1) {
                fontSize -= 1;
                txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
            }
            lblZoom.setText((fontSize * 10) + "%   ");
        }
        if (e.getSource() == mniResetZoom) {
            fontSize = 10;
            txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
            lblZoom.setText("100%   ");
        }
        if (e.getSource() == mncStatusBar)
            mnSouth.setVisible(mncStatusBar.getState());
        if (e.getSource() == mncWordWrap)
            txt.setLineWrap(mncWordWrap.getState());
        if (e.getSource() == mniPrint) {
            printToPrinter();
        }
        if (e.getSource() == mniCut){
            txt.cut();
        }
        if (e.getSource() == mniCopy){
            txt.copy();
        }
        if (e.getSource() == mniPaste){
            txt.paste();
        }
        if (e.getSource() == mniDelete){
            int start = txt.getSelectionStart();
            int end = txt.getSelectionEnd();
            StringBuilder newTxt = new StringBuilder(txt.getText());
            newTxt.delete(start,end);
            txt.setText(newTxt.toString());
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
            lblLineCol.setText("Ln " + (row + 1) + ",Col " + (column + 1) + "   ");
            lblCharNumber.setText("   " + txt.getText().length() + " characters");
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
    //endregion
}
