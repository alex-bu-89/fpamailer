package de.bht.fpa.mail.s806984.model.appLogic;

import de.bht.fpa.mail.s806984.model.data.Folder;
import java.util.List;

/*
 * This is the interface for classes that manage
 * folders.
 * 
 * @author Simone Strippgen
 */

public interface FolderManagerIF {

    /**
     * Get current root folder.
     * @return current root folder.
     */
    Folder getTopFolder();
    
    
    /**
     * Loads all relevant content in the directory path of a folder
     * into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    void loadContent(Folder f);     
}
