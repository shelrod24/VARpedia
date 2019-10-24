package services;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

public class FlickrAPIService {
	public static String getAPIKey(String key) throws Exception {
		//open file flickr_api_keys.txt
		File file = new File("flickr_api_keys.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		//split line by =, and check if it is the right required key as specified by input
		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				//if right key, close reader and return key
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}
	
	public static List<String> getImages(String keyword, int resultsPerPage, int page) {
		List<String> imageFilenames = new ArrayList<String>();
		try {
			// get the two API keys
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = keyword;
			
			// set up the quesry
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
	        
	        	        
	        for (Photo photo: results) {
	        	try {
	        		//get photos
	        		BufferedImage image = photos.getImage(photo,Size.MEDIUM);
		        	String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
		        	File outputfile = new File("temps/image",filename);
		        	ImageIO.write(image, "jpg", outputfile);
		        	imageFilenames.add(filename);
	        	} catch (FlickrException fe) {
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return image filenames
		return imageFilenames;
	}	
}
