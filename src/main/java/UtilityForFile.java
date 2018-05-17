import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UtilityForFile extends JFrame {
    private File path;

    public  void saveFile(String text) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.json","*.*");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            try (FileWriter fr = new FileWriter(fileChooser.getSelectedFile()))
            {
                fr.write(text);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void openFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt","txt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);// выбрать тип диалога Open или Save
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        setPath(file);
        setVisible(false);
        dispose();
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
