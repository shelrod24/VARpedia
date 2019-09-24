package Services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DirectoryServices {


    private static final File _audiofiles = new File("./Audio");
    private static final File _creationfiles = new File("./Creations");


    public static boolean SearchDirectoryForName(String name){

        File[] listoffiles = _audiofiles.listFiles();
        boolean exists = false;

        for (File file: listoffiles) {

            if(file.getName().equals(name+".wav")) {

                exists = true;

            }
        }
        return exists;
    }

    /**
     *This method is responsible for Listing the files in the Creations Directory in alphabetical order
     **/
    public static ArrayList<String> listCreations() {

        File[] listoffiles = _creationfiles.listFiles();
        ArrayList<String> _completedcollections = new ArrayList<String>();

        for (File file: listoffiles) {

            _completedcollections.add(file.getName());
        }

        Collections.sort(_completedcollections, String.CASE_INSENSITIVE_ORDER);

        return _completedcollections;

    }




}
