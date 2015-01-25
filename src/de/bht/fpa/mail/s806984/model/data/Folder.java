package de.bht.fpa.mail.s806984.model.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author Simone Strippgen
 *
 */
@Entity
public class Folder extends Component implements Serializable {
    
    @Id
    @GeneratedValue
    private long id;
    
    private boolean expandable;
    
    @Transient
    private transient ArrayList<Component> content;
    
    @Transient
    private transient List<Email> emails;
    
    public Folder() {
        super(new File(""));
        content = new ArrayList<Component>();
        this.expandable = false;
        emails = new ArrayList<Email>();
    }
    
    public Folder(File path, boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<Component>();
        emails = new ArrayList<Email>();
    }

    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail(Email message) {
        emails.add(message);
    }
    
//    @Override
//    public String toString(){
//        if(this.emails.size() > 0){
//            return this.getName() + "(" + this.getEmails().size() + ")";
//        }
//        return this.getName();    
//    }
    
 }
