package de.bht.fpa.mail.s806984.model.appLogic;

import de.bht.fpa.mail.s806984.model.data.Folder;
import java.io.File;

/**
 *
 * @author Dominik
 */
public interface EmailManagerIF {
    
    /***
     * Get the folder handled by this EmailManager.
     * @return The handled folder.
     */
    Folder getFolder();
    
    
    /***
     * Loads the emails of the given folder into its email list by calling the private
     * parseXmlFile() method.
     * @param f Folder that shall have its email list filled.
     */
    void loadEmails(Folder f);
    
}
