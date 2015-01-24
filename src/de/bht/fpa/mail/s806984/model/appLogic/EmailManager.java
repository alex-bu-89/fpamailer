package de.bht.fpa.mail.s806984.model.appLogic;

import de.bht.fpa.mail.s806984.model.data.Email;
import de.bht.fpa.mail.s806984.model.data.Folder;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;

/**
 * EmailManager class implements composite pattern
 *
 * @author Jules Doering
 * @author Alexander Buyanov
 */
public class EmailManager implements EmailManagerIF {

    private Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param path File which points to the top directory
     */
    public EmailManager(Folder f) {
        this.topFolder = f;
        loadEmails(f);
    }

    @Override
    public Folder getFolder() {
        return topFolder;
    }

    @Override
    public void loadEmails(Folder f) {
        
        // if folder is empty   
        if(new File(f.getPath()).listFiles() == null){
            return;
        }
        
        for (File file : new File(f.getPath()).listFiles()) {
            //addes Email Object for every readable xmlfile
            if (file.canRead() && file.isFile() && file.getName().endsWith(".xml")) {
                this.topFolder.addEmail(parseEmail(file));
            }
        }
    }

    /**
     * Reads an XML file that contains Email Information.
     *
     * @param file
     * @return Email
     */
    private Email parseEmail(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(Email.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Email email = (Email) unmarshaller.unmarshal(file);
            return email;
        } catch (JAXBException e) {
            System.out.println("Something went wrong by unmarshal an email");
            e.getStackTrace();
        }
        return null;
    }

    /**
     * Writes email into XML file
     *
     * @param email email object
     * @param file path where email will be stored
     */
    public void writeEmail(Email email, File file) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Email.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            System.out.println(file);
            m.marshal(email, file);
        } catch (JAXBException e) {
            System.out.println("Something went wrong marshaling an email");
            e.getStackTrace();
        }
    }
}
