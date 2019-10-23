package Services;

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
		// TODO fix the following based on where you will have your config file stored

//		String config = System.getProperty("user.dir") 
//				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 
		
//		String config = System.getProperty("user.home")
//				+ System.getProperty("file.separator")+ "bin" 
//				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 
		File file = new File("flickr_api_keys.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
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
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
			
			String query = keyword;
			
	        PhotosInterface photos = flickr.getPhotosInterface();
	        SearchParameters params = new SearchParameters();
	        params.setSort(SearchParameters.RELEVANCE);
	        params.setMedia("photos"); 
	        params.setText(query);
	        
	        PhotoList<Photo> results = photos.search(params, resultsPerPage, page);
	        
	        	        
	        for (Photo photo: results) {
	        	try {
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
		return imageFilenames;
	}	
}
