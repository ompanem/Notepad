import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Gui {
    final static int SCREEN_WIDTH = 500;
    final static int SCREEN_HEIGHT = 500;
    static JButton saveAsButton;
    static JButton openButton;
    static JButton saveButton;
    static JTextArea textArea;
    static JFrame jFrame;
    static int counter = 1;
    static JScrollPane jScrollPane;
    static File textFile;

    public static void initFrame() {
         jFrame = new JFrame("Notepad");
        jFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(getPanel());
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setSize(20,200);

        textArea.setCaretPosition(0);
        jFrame.add(jScrollPane);

        jFrame.setVisible(true);
    }

    public static JPanel getPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(getButtonsPanel(), BorderLayout.NORTH);
        textArea = new JTextArea("");
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(10, 10, 10,10));
        jPanel.add(textArea, BorderLayout.CENTER);

        return jPanel;
    }

    public static JPanel getButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
         saveAsButton = new JButton("Save as");
         openButton = new JButton("Open");
         saveButton = new JButton("Save");
        addButtonListeners();
        buttonsPanel.add(saveAsButton);
        buttonsPanel.add(openButton);
        buttonsPanel.add(saveButton);

        return buttonsPanel;

    }

    public static void addButtonListeners()
    {
        saveAsButton.addActionListener(getSaveAsButtonListener());
        openButton.addActionListener(getOpenButtonListener());
        saveButton.addActionListener(getSaveButtonListener());
    }

    public static ActionListener getSaveAsButtonListener()
    {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String path = System.getProperty("user.home");
                String text = textArea.getText();
                File downloads = new File(path, "Downloads");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(downloads);
                fileChooser.setFileFilter(getTxtFilter());
                fileChooser.setDialogTitle("Save File");
                int res = fileChooser.showSaveDialog(null);
                if(res == JFileChooser.APPROVE_OPTION)
                {
                     textFile = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".txt"); //if txt is appended then it will write the contents to a txt file
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))){
                        writer.write(text);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static ActionListener getSaveButtonListener(){
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(textFile!=null)
                {
                    String text = textArea.getText();
                    try(PrintWriter pw = new PrintWriter(textFile)){
                        pw.println(text);
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    public static ActionListener getOpenButtonListener(){
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String path = System.getProperty("user.home");
                File downloads = new File(path, "Downloads");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(downloads);
                fileChooser.setFileFilter(getTxtFilter());
                fileChooser.setDialogTitle("Open file");
                int res = fileChooser.showOpenDialog(null);
                if(res == JFileChooser.APPROVE_OPTION){
                    textFile = fileChooser.getSelectedFile();
                    String lines = "";
                    String line;
                    try(BufferedReader br = new BufferedReader(new FileReader(textFile))){
                        while ((line = br.readLine())!=null){
                            lines+=line+ "\n";
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    textArea.setText(lines);
                }
            }
        });
    }

    public static FileFilter getTxtFilter(){
        return (new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "";
            }
        });
    }


}
