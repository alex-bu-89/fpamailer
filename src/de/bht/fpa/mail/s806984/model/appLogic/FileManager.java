package de.bht.fpa.mail.s806984.model.appLogic;

import de.bht.fpa.mail.s806984.model.data.FileElement;
import de.bht.fpa.mail.s806984.model.data.Folder;
import java.io.File;

/**
 * FileManager class implements composite pattern
 * 
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class FileManager implements FolderManagerIF {

    private Folder topFolder;
   
    /**
     * Constructs a new FileManager object which manages a folder hierarchy, 
     * where file contains the path to the top directory. 
     * The contents of the  directory file are loaded into the top folder
     * @param path File which points to the top directory
     */
     public FileManager(File path) {
        if (path.isFile()) {
            throw new UnsupportedOperationException("Files not supported. Only folder allowt!");
        }
        this.topFolder = new Folder(path, true);
        loadContent(this.topFolder);
    }

    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
    
    /**
     * Loads all relevant content in the directory path of a folder
     * object into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    @Override
    public void loadContent(Folder f) {
      
        // if folder is empty   
        if(new File(f.getPath()).listFiles() == null){
            return;
        }
        
        for (File file : new File(f.getPath()).listFiles()) {
            if (file.canRead() && file.isFile()) {
                // we shouldn't show  the files anymore
                //this.topFolder.addComponent(new FileElement(file.getAbsoluteFile()));
            }
            else if (file.canRead() && file.isDirectory()) {
                
                if(file.listFiles().length <= 0){
                    this.topFolder.addComponent(new Folder(file, false));
                    f.addComponent(new Folder(file, false));
                } else {
                    boolean hasContents = false;
                    for(File temp : file.listFiles()){
                        if(temp.isDirectory()){
                            hasContents = true;
                            break;
                        }
                    }
                    f.addComponent(new Folder(file, hasContents));
                }
            }
            else {
                System.out.println("acces denied for: " + file.getAbsoluteFile());
           }
        }
    }

    @Override
    public String toString() {
        return "FileManager{" + "topFolder=" + topFolder + '}';
    }
    
}
