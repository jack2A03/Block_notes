import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class NotePad extends JFrame implements ActionListener {

    private JTextArea txt = null;

    public NotePad(){
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("New NotePad");
        initUi();
        setVisible(true);
    }

    private void initUi() {

        JMenuBar menuBar = new JMenuBar();
        JMenu mnFile = new JMenu("File");
        JMenuItem mniNew = new JMenuItem("New");
        mniNew.addActionListener(this);
        JMenuItem mniopen = new JMenuItem("Open...");
        mniopen.addActionListener(this);
        mnFile.add(mniNew);
        mnFile.add(mniopen);
        mnFile.addSeparator();
        JMenuItem mniSave = new JMenuItem("Save");
        mniSave.addActionListener(this);
        JMenuItem mniSaveAs = new JMenuItem("Save as...");
        mniSaveAs.addActionListener(this);
        mnFile.addSeparator();
        JMenuItem mniExit = new JMenuItem("Exit");
        mniExit.addActionListener(this);
        menuBar.add(mnFile);

        add(menuBar, BorderLayout.NORTH);

        txt = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txt);
        add(scrollPane);
    }

    private void SaveAs(){
        try {
            PrintWriter pw = new PrintWriter("prova"+".txt");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
