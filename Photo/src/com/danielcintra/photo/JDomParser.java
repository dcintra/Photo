package com.danielcintra.photo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import android.util.Log;

import com.danielcintra.photo.FlickrPhoto;

public class JDomParser {
	
	public InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        Log.i("downloadUrl", urlString);
        InputStream stream = conn.getInputStream();
        return stream;
    }
	
	public ArrayList<FlickrPhoto> parse(InputStream stream) {
		
		SAXBuilder builder = new SAXBuilder();
		ArrayList<FlickrPhoto> photos = new ArrayList<FlickrPhoto>();
		
		try {
			//Build Document with stream
			Document document = (Document) builder.build(stream);
			
			//Find Root Node 
			org.jdom2.Element rootNode = document.getRootElement();
			System.out.println("root :"+rootNode.getName());
			
			//Find the photos node and create a list with all it's child nodes
			org.jdom2.Element nodePhotos = rootNode.getChild("photos");
			System.out.println("photos node :"+nodePhotos.getName());
			List<org.jdom2.Element> list = nodePhotos.getChildren("photo");
			
			//Loop through each element and save the photo attributes in addition to the description text
			for (Element node : list) {
				FlickrPhoto photo = new FlickrPhoto();
				String id = node.getAttributeValue("id");
				String secret = node.getAttributeValue("secret");
				String server = node.getAttributeValue("server");
				String farm = node.getAttributeValue("farm");
				String title = node.getAttributeValue("title");
				String owner = node.getAttributeValue("ownername");
				String date = node.getAttributeValue("datetaken");
				String desc = node.getChildText("description");
				
				photo.setId(id);
				photo.setSecret(secret);
				photo.setServer(server);
				photo.setFarm(farm);
				photo.setTitle(title);
				photo.setOwner(owner);
				photo.setDate(date);
				photo.setImageURLSmall("http://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_m.jpg");
				photo.setImageURLMed("http://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_c.jpg");
				photo.setDesc(desc);
				
				//add the photo information from current node to ArrayList
				photos.add(photo);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Exception", e.getMessage());
		} 
		//return ArrayList with all photos elements for further processing
		return photos;
	}
}
