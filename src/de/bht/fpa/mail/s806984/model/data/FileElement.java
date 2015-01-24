package de.bht.fpa.mail.s806984.model.data;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/*
 * This is the leaf part of a composite pattern.
 * 
 * @author Simone Strippgen
 */

public class FileElement extends Component implements Serializable {

    public FileElement(File path) {
        super(path);
    }

    @Override
    public boolean isExpandable() {
        return false;
    }
    
}
