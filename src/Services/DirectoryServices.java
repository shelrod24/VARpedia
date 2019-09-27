package Services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DirectoryServices {


    private static final File _audiofiles = new File("./audio");
    private static final File _creationfiles = new File("./creations");


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
    
    public static boolean creationExists(String name){
        File[] listoffiles = new File("./creations").listFiles();
        // no files in folder file doesnt exist
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
    
    public static ArrayList<String> listAudioFolders() {
    	// getting all folders in /audio
        File[] listoffiles = _audiofiles.listFiles();
        ArrayList<String> _completedcollections = new ArrayList<String>();
        for (File file: listoffiles) {
            _completedcollections.add(file.getName());
        }
        Collections.sort(_completedcollections, String.CASE_INSENSITIVE_ORDER);
        return _completedcollections;
    }



    
    public static ArrayList<String> listAudio(String folder){
        File[] listoffiles = new File("./audio/"+folder).listFiles();
        ArrayList<String> _completedcollections = new ArrayList<String>();
        for (File file: listoffiles) {
            _completedcollections.add(file.getName());
        }
        return _completedcollections;
    }
    
    public static ArrayList<String> listImages() {
        File[] listoffiles = new File("./temps/image").listFiles();
        ArrayList<String> _completedcollections = new ArrayList<String>();
        for (File file: listoffiles) {
            _completedcollections.add(file.getName());
        }
        return _completedcollections;
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



    public final static void CreateDirectories() throws IOException {

        ProcessBuilder makedirectoriesprocessbuilder = new ProcessBuilder("sh", "-c", "./scripts/make_directories.sh");
        Process makedirectoriesprocess = makedirectoriesprocessbuilder.start();

    }




}
