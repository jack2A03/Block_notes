import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

public class NotePad extends JFrame implements ActionListener, DocumentListener, WindowListener, CaretListener, MouseWheelListener {

    //region atr
    //region GUI atr
    private JMenuItem mniNew = null;
    private JMenuItem mniOpen = null;

    private JMenuItem mniSave = null;
    private JMenuItem mniSaveAs = null;

    private JMenuItem mniPrint = null;

    private JMenuItem mniExit = null;

    private JMenuItem mniUndo = null;

    private JMenuItem mniCut = null;
    private JMenuItem mniCopy = null;
    private JMenuItem mniPaste = null;
    private JMenuItem mniDelete = null;

    private JMenuItem mniFindReplace = null;

    private JMenuItem mniZoomIn = null;
    private JMenuItem mniZoomOut = null;
    private JMenuItem mniResetZoom = null;

    private JCheckBoxMenuItem mncStatusBar = null;
    private JCheckBoxMenuItem mncWordWrap = null;

    private JMenuItem mniAbout = null;

    private JTextArea txt = null;

    private JMenuBar mnSouth = new JMenuBar();

    private final JLabel lblLineCol = new JLabel("Ln 1,Col 1   ");
    private final JLabel lblCharNumber = new JLabel("   0 characters");

    private final JLabel lblZoom = new JLabel("100%   ");

    //endregion

    //region normal atr
    private String absolutePath = System.getProperty("user.home") + File.separator + "Desktop/Untitled.txt";

    private String title = "Untitled";

    private boolean saved = false;
    private boolean savedSmw = false;

    private String savedText = "";

    private int fontSize = 10;

    private final UndoManager manager = new UndoManager();

    private final static String SOME_ACTION = "control 1";

    //endregion
    //endregion

    //region cost
    public NotePad() {
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setTitle(this.title);
        initUi();
        setVisible(true);
    }
    //endregion

    private void initUi() {

        //region mnFile
        JMenuBar menuBar = new JMenuBar();
        JMenu mnFile = new JMenu("File");
        mniNew = new JMenuItem("New");
        mniNew.addActionListener(this);
        mniNew.setToolTipText("CTRL+N");
        mniOpen = new JMenuItem("Open...");
        mniOpen.addActionListener(this);
        mniOpen.setToolTipText("CTRL+O");
        mnFile.add(mniNew);
        mnFile.add(mniOpen);
        mnFile.addSeparator();
        mniSave = new JMenuItem("Save");
        mniSave.addActionListener(this);
        mniSave.setToolTipText("CTRL+S");
        mniSaveAs = new JMenuItem("Save as...");
        mniSaveAs.addActionListener(this);
        mniSaveAs.setToolTipText("CTRL+SHIFT+S");
        mnFile.add(mniSave);
        mnFile.add(mniSaveAs);
        mnFile.addSeparator();
        mniPrint = new JMenuItem("Print");
        mniPrint.addActionListener(this);
        mniPrint.setToolTipText("CTRL+P");
        mnFile.add(mniPrint);
        mnFile.addSeparator();
        mniExit = new JMenuItem("Exit");
        mniExit.addActionListener(this);
        mnFile.add(mniExit);
        //endregion

        //region mnEdit
        JMenu mnEdit = new JMenu("Edit");
        mniUndo = new JMenuItem("Undo");
        mniUndo.setToolTipText("CTRL+Z");
        mniUndo.addActionListener(this);
        mnEdit.add(mniUndo);
        mnEdit.addSeparator();
        mniCut = new JMenuItem("Cut");
        mniCut.setToolTipText("CTRL+X");
        mniCut.addActionListener(this);
        mnEdit.add(mniCut);
        mniCopy = new JMenuItem("Copy");
        mniCopy.setToolTipText("CTRL+C");
        mniCopy.addActionListener(this);
        mnEdit.add(mniCopy);
        mniPaste = new JMenuItem("Paste");
        mniPaste.setToolTipText("CTRL+V");
        mniPaste.addActionListener(this);
        mnEdit.add(mniPaste);
        mniDelete = new JMenuItem("Delete");
        mniDelete.setToolTipText("CANC");
        mniDelete.addActionListener(this);
        mnEdit.add(mniDelete);
        mnEdit.addSeparator();
        mniFindReplace = new JMenuItem("Find and Replace");
        mniFindReplace.setToolTipText("CTRL+H");
        mniFindReplace.addActionListener(this);
        mnEdit.add(mniFindReplace);
        mnEdit.addSeparator();
        JMenuItem mniSelectAll = new JMenuItem("Select All");
        mniSelectAll.addActionListener(e -> txt.selectAll());
        mniSelectAll.setToolTipText("CTRL+A");
        mnEdit.add(mniSelectAll);
        //endregion

        //region mnView
        JMenu mnView = new JMenu("View");
        JMenu mnpZoom = new JMenu("Zoom");
        mniZoomIn = new JMenuItem("Zoom in");
        mniZoomOut = new JMenuItem("Zoom out");
        mniResetZoom = new JMenuItem("Reset zoom");
        mniZoomOut.setToolTipText("CTRL + Minus");
        mniZoomIn.setToolTipText("CTRL + Plus");
        mniResetZoom.setToolTipText("CTRL + 0");
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
        //endregion

        //region mnHelp
        JMenu mnHelp = new JMenu("Help");
        mniAbout = new JMenuItem("About");
        mniAbout.addActionListener(this);
        mnHelp.add(mniAbout);
        //endregion

        //region menuBar
        menuBar.add(mnFile);
        menuBar.add(mnEdit);
        menuBar.add(mnView);
        menuBar.add(mnHelp);
        add(menuBar, BorderLayout.NORTH);
        //endregion

        //region txt + keyStrokes

        //region txt
        txt = new JTextArea();
        txt.addCaretListener(this);
        txt.getDocument().addDocumentListener(this);
        txt.setLineWrap(true);
        txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
        txt.addMouseWheelListener(this);
        //endregion

        //region KeyStrokeActions
        Document document = txt.getDocument();
        document.addUndoableEditListener(e -> manager.addEdit(e.getEdit()));

        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        KeyStroke newNotePadKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        KeyStroke openNotePadKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        KeyStroke printKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK);
        KeyStroke resetZoomKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_0, InputEvent.CTRL_DOWN_MASK);
        KeyStroke zoomOutKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK);
        KeyStroke zoomInKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK);

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(undoKeyStroke, "undoKeyStroke");
        txt.getActionMap().put("undoKeyStroke", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    manager.undo();
                } catch (CannotUndoException ignored) {
                }
            }
        });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(redoKeyStroke, "redoKeyStroke");
        txt
                .getActionMap().put("redoKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            manager.redo();
                        } catch (CannotRedoException ignored) {
                        }
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(newNotePadKeyStroke, "newNotePadKeyStroke");
        txt
                .getActionMap().put("newNotePadKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            newNotePad();
                        } catch (CannotRedoException ignored) {
                        }
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(openNotePadKeyStroke, "openNotePadKeyStroke");
        txt
                .getActionMap().put("openNotePadKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            open();
                        } catch (CannotRedoException ignored) {
                        }
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(saveKeyStroke, "saveKeyStroke");
        txt
                .getActionMap().put("saveKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (!savedSmw)
                                saveAs();
                            else
                                save();
                        } catch (CannotRedoException ignored) {
                        }
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("control shift S"), "savaAsKeyStroke");
        txt
                .getActionMap().put("savaAsKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            saveAs();
                        } catch (CannotRedoException ignored) {
                        }
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(printKeyStroke, "printKeyStroke");
        txt
                .getActionMap().put("printKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            printToPrinter();
                        } catch (CannotRedoException ignored) {
                        }
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(resetZoomKeyStroke, "redoKeyStroke");
        txt
                .getActionMap().put("redoKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fontSize = 10;
                        txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
                        lblZoom.setText("100%   ");
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(zoomOutKeyStroke, "zoomOutKeyStroke");
        txt
                .getActionMap().put("zoomOutKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (fontSize > 1) {
                            fontSize -= 1;
                            txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
                        }
                        lblZoom.setText((fontSize * 10) + "%   ");
                    }
                });

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(zoomInKeyStroke, "zoomInKeyStroke");
        txt
                .getActionMap().put("zoomInKeyStroke", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (fontSize < 50) {
                            fontSize += 1;
                            txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
                        }
                        lblZoom.setText((fontSize * 10) + "%   ");
                    }
                });
        //endregion

        //region MouseAction
        Action someAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (fontSize < 50) {
                    fontSize += 1;
                    txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
                }
                lblZoom.setText((fontSize * 10) + "%   ");
            }
        };

        txt.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        txt.getActionMap().put(SOME_ACTION, someAction);
        //endregion


        //endregion

        //region ScrollPane
        JScrollPane scrollPane = new JScrollPane(txt);
        add(scrollPane, BorderLayout.CENTER);
        //endregion

        //region ComponentsSouthBar
        mnSouth = new JMenuBar();
        JPanel pnlSouth = new JPanel(new GridLayout(1, 2));
        JPanel pnlLeftGrid = new JPanel(new GridBagLayout());
        GridBagConstraints layoutConstraintLeft = new GridBagConstraints();
        JPanel pnlRightGrid = new JPanel(new GridBagLayout());
        GridBagConstraints layoutConstraintsRight = new GridBagConstraints();
        //endregion

        //region LeftSouthBar
        lblLineCol.setFont(new Font(lblLineCol.getFont().getName(), lblLineCol.getFont().getStyle(), 10));
        lblCharNumber.setFont(new Font(lblCharNumber.getFont().getName(), lblCharNumber.getFont().getStyle(), 10));

        layoutConstraintLeft.gridy = 0;
        layoutConstraintLeft.gridx = 0;

        layoutConstraintLeft.weightx = 1.0;

        pnlLeftGrid.add(lblLineCol, layoutConstraintLeft);
        JSeparator sepLeft = new JSeparator(SwingConstants.VERTICAL);
        layoutConstraintLeft.gridy = 0;
        layoutConstraintLeft.gridx = 1;
        layoutConstraintLeft.fill = GridBagConstraints.VERTICAL;
        pnlLeftGrid.add(sepLeft, layoutConstraintLeft);
        layoutConstraintLeft.gridy = 0;
        layoutConstraintLeft.gridx = 2;
        pnlLeftGrid.add(lblCharNumber, layoutConstraintLeft);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLeft.add(pnlLeftGrid);
        //endregion

        //region RightSouthBar
        layoutConstraintsRight.gridy = 0;
        layoutConstraintsRight.gridx = 0;

        layoutConstraintsRight.weightx = 1.0;

        JLabel lblUTF = new JLabel("   UTF-16");
        lblUTF.setFont(new Font(lblUTF.getFont().getName(), lblUTF.getFont().getStyle(), 10));
        lblZoom.setFont(new Font(lblZoom.getFont().getName(), lblZoom.getFont().getStyle(), 10));
        pnlRightGrid.add(lblZoom, layoutConstraintsRight);
        JSeparator sepRight = new JSeparator(SwingConstants.VERTICAL);
        layoutConstraintsRight.gridy = 0;
        layoutConstraintsRight.gridx = 1;
        layoutConstraintsRight.fill = GridBagConstraints.VERTICAL;
        pnlRightGrid.add(sepRight, layoutConstraintsRight);
        layoutConstraintsRight.gridy = 0;
        layoutConstraintsRight.gridx = 2;
        pnlRightGrid.add(lblUTF, layoutConstraintsRight);

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlRight.add(pnlRightGrid);
        //endregion

        //region AddingToFrame
        pnlSouth.add(pnlLeft);
        pnlSouth.add(pnlRight);
        mnSouth.add(pnlSouth);
        add(mnSouth, BorderLayout.SOUTH);
        //endregion
    }

    //region FileMenuMethods
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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

        //TODO already exists condition

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
            savedSmw = true;
            save();
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

    private void printToPrinter() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new OutputPrinter(txt.getText()));
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.fillInStackTrace();
            }
        }
    }
    //endregion

    //region ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mniNew) {
            newNotePad();
        }
        if (e.getSource() == mniOpen) {
            open();
        }
        if (e.getSource() == mniSave) {
            if (!savedSmw) {
                saveAs();
            } else {
                save();
            }
        }
        if (e.getSource() == mniSaveAs) {
            saveAs();
        }
        if (e.getSource() == mniExit) {
            if (saved) {
                dispose();
            } else {
                int confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (confirmation == 0) {
                    saveAs();
                    dispose();
                } else if (confirmation == 1) {
                    dispose();
                }
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
        if (e.getSource() == mncStatusBar) {
            mnSouth.setVisible(mncStatusBar.getState());
        }
        if (e.getSource() == mncWordWrap) {
            txt.setLineWrap(mncWordWrap.getState());
        }
        if (e.getSource() == mniPrint) {
            printToPrinter();
        }
        if (e.getSource() == mniCut) {
            txt.cut();
        }
        if (e.getSource() == mniCopy) {
            txt.copy();
        }
        if (e.getSource() == mniPaste) {
            txt.paste();
        }
        if (e.getSource() == mniDelete) {
            int start = txt.getSelectionStart();
            int end = txt.getSelectionEnd();
            StringBuilder newTxt = new StringBuilder(txt.getText());
            newTxt.delete(start, end);
            txt.setText(newTxt.toString());
        }
        if (e.getSource() == mniUndo) {
            try {
                manager.undo();
            } catch (CannotUndoException cue) {
                cue.fillInStackTrace();
            }
        }
        if (e.getSource() == mniFindReplace) {
            Find_Replace findReplace = new Find_Replace(txt);
            findReplace.setVisible(true);
        }
    }
    //endregion

    //region DocListener
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
    //endregion

    //region WindowListener
    @Override
    public void windowClosing(WindowEvent e) {
        int confirmation = -1;
        if (!savedSmw || !saved) {
            confirmation = JOptionPane.showConfirmDialog(this, "would you like to save?", "are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
        } else {
            dispose();
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
    //endregion

    //region CaretListener
    @Override
    public void caretUpdate(CaretEvent e) {
        int caretPos = txt.getCaretPosition();
        int row;
        try {
            row = txt.getLineOfOffset(caretPos);
            int column = caretPos - txt.getLineStartOffset(row);
            lblLineCol.setText("Ln " + (row + 1) + ",Col " + (column + 1) + "   ");
            lblCharNumber.setText("   " + txt.getText().length() + " characters");
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
    //endregion

    //region MouseListener
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                JComponent component = (JComponent) e.getComponent();
                Action action = component.getActionMap().get(SOME_ACTION);
                if (action != null)
                    action.actionPerformed(null);
            } else {
                if (fontSize > 1) {
                    fontSize -= 1;
                    txt.setFont(new Font(txt.getFont().getName(), txt.getFont().getStyle(), fontSize));
                }
                lblZoom.setText((fontSize * 10) + "%   ");
            }
        }
    }
    //endregion
}
