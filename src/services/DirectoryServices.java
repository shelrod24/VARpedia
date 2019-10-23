package services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryServices {



    public static boolean SearchDirectoryForName(String term, String name){

        File[] listoffiles = new File("./audio/"+term).listFiles();
        boolean exists = false;

        if (listoffiles==null) {

        	return false;
        }

        for (File file: listoffiles) {

            if(file.getName().equals(name+".wav")) {
            	
                exists = true;

            }
        }

        return exists;
    }




    public static boolean creationExists(String name) {

        File[] listoffiles = new File("./creations").listFiles();

        if (listoffiles==null) {

        	return false;

        }

        for (File file: listoffiles) {

            if(file.getName().equals(name+".mp4")) {
            	// file found;
                return true;

            }
        }

        // file not found
        return false;
    }



    public static List<String> ListFilesInDir(String fullpath){

        File[] listoffiles = new File(fullpath).listFiles();
        ArrayList<String> _arrayoffiles = new ArrayList<String>();

        for (File file: listoffiles) {

            _arrayoffiles.add(file.getName());

        }

        Collections.sort(_arrayoffiles, String.CASE_INSENSITIVE_ORDER);
        return _arrayoffiles;

    }



    public final static void CreateDirectories() throws IOException {

        ProcessBuilder makedirectoriesprocessbuilder = new ProcessBuilder("sh", "-c", "./scripts/make_directories.sh");
        makedirectoriesprocessbuilder.start();

    }



}
