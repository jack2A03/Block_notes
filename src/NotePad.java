import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class NotePad extends JFrame implements ActionListener {

    private JMenuItem mniNew = null;
    private JMenuItem mniopen = null;

    private JMenuItem mniSave = null;
    private JMenuItem mniSaveAs = null;

    private JMenuItem mniExit = null;

    private JTextArea txt = null;

    private String title = "Untitled";

    public NotePad(){
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        menuBar.add(mnFile);

        add(menuBar, BorderLayout.NORTH);

        txt = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txt);
        add(scrollPane);
    }

    private void save(){
        try {
            PrintWriter pw = new PrintWriter(title+".txt");
            pw.println(txt.getText());
            pw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mniSave){
            save();
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }
}
