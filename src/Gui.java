import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Gui {
    final static int SCREEN_WIDTH = 500;
    final static int SCREEN_HEIGHT = 500;
    static JButton saveButton;
    static JButton openButton;
    static JTextArea textArea;
    static JFrame jFrame;
    static int counter = 1;
    public static void initFrame() {
         jFrame = new JFrame("Notepad");
        jFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(getPanel());
        jFrame.setVisible(true);
    }

    public static JPanel getPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(getButtonsPanel(), BorderLayout.NORTH);
        textArea = new JTextArea("Type here: ");
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin( new Insets(10, 10, 10,10));
        jPanel.add(textArea, BorderLayout.CENTER);
        return jPanel;
    }

    public static JPanel getButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
         saveButton = new JButton("Save");
         openButton = new JButton("Open");
        addButtonListeners();
        buttonsPanel.add(saveButton);
        buttonsPanel.add(openButton);

        return buttonsPanel;

    }

    public static void addButtonListeners()
    {
        saveButton.addActionListener(getSaveButtonListener());
        openButton.addActionListener(getOpenButtonListener());
    }

    public static ActionListener getSaveButtonListener()
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
                    File textFile = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".txt"); //if txt is appended then it will write the contents to a txt file
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))){
                        writer.write(text);
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
                    File file = fileChooser.getSelectedFile();
                    String lines = "";
                    String line;
                    try(BufferedReader br = new BufferedReader(new FileReader(file))){
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
