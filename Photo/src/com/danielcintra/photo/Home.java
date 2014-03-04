package com.danielcintra.photo;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class Home extends ListActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ArrayList<Contacts> listContacts;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	ProgressDialog m_dialog;
	static final int PICK_CONTACT= 1;
	public static final String PREF_QUERY = "query";
	public static final String API_KEY = "6ac3f568073aea1cca183c3ca08e974c&tags=";
	public static final String FLICKR_PHOTO_SEARCH = "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=";
	public static final String FLICKR_PHOTO_SEARCH_END = "&page=1&extras=date_taken,owner_name,description";
	PullToRefreshListView pullToRefreshList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		//Pull to refresh setup and listener
		pullToRefreshList = (PullToRefreshListView) findViewById(android.R.id.list);
		pullToRefreshList.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				String url1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(PREF_QUERY, null);
				
				getNextFlickrPage(url1);
				
			}
		});

		m_dialog = new ProgressDialog(this);
		
		//Perform hardcoded query so that app start up with photos vs blank screen
		String homepage = "Dogs";
		String url = FLICKR_PHOTO_SEARCH+API_KEY+homepage+FLICKR_PHOTO_SEARCH_END;
		
		getNextFlickrPage(url);
		new DownloadXmlTask().execute(url);
		
		//Display photo in browser when clicking on item in list 
		ListView l = getListView();
		
		l.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				FlickrPhoto listItem = (FlickrPhoto) getListAdapter().getItem(position);
				
				Intent i = new Intent(Intent.ACTION_VIEW);
				 i.setData(Uri.parse(listItem.imageURLMed));;
				 startActivity(i);						
			}
		});
    	

		//Load Phone contacts into left drawer 
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		listContacts = new ContactsLoader(this).fetchAll();
		ContactsAdapter adapterContacts = new ContactsAdapter(this, listContacts);
		mDrawerList.setAdapter(adapterContacts);
		
		//When contact is clicked in drawer perform new search 
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
					Contacts contactListItem = (Contacts)  adapter.getItemAtPosition(position);
					String contactName = contactListItem.name;
					
					String url;
					try {
						url = FLICKR_PHOTO_SEARCH+API_KEY+URLEncoder.encode(contactName, "ISO-8859-1")+FLICKR_PHOTO_SEARCH_END;
						
						new DownloadXmlTask().execute(url);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
				
			}
		});
		

		//Drawer settings 
		mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

	}
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	
		return super.onPrepareOptionsMenu(menu);
	}
	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Pass the event to ActionBarDrawerToggle, if it returns
	        // true, then it has handled the app icon touch event
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	        } else if (item.getItemId() == R.id.action_addressBook){
	        	
	        	// Check if addressbook button was clicked on in Actionbar and handle with intent
	        	
	        	Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	        	i.setType(ContactsContract.Contacts.CONTENT_TYPE);
	        	startActivityForResult(i, PICK_CONTACT);
					
	        }	        

	        return super.onOptionsItemSelected(item);
	    }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Handle selected contact and refresh search results
		 if (resultCode == RESULT_OK && requestCode == PICK_CONTACT) {
			 // Extract name value from result extras
			 Uri contactData = data.getData();
			 Cursor c= getContentResolver().query(contactData, null, null, null, null);
			 if (c.moveToFirst()) 
			 {
				 String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				 
				 String url;
				 try {
					url = FLICKR_PHOTO_SEARCH+API_KEY+URLEncoder.encode(name, "ISO-8859-1")+FLICKR_PHOTO_SEARCH_END;
					new DownloadXmlTask().execute(url);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 

			 }		

		 }
	 } 


	//Add Search bar and search functionality tied to AsyncTask
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home, menu);
	    MenuItem searchItem = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) searchItem.getActionView();
	    
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
	    //Search Bar setup and functionality
			public boolean onQueryTextSubmit(String query) {
				
				String url;
				try {
					url = FLICKR_PHOTO_SEARCH+API_KEY+URLEncoder.encode(query, "ISO-8859-1")+FLICKR_PHOTO_SEARCH_END;
					new	DownloadXmlTask().execute(url);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				
				return true;
			}
	
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

	   return super.onCreateOptionsMenu(menu);
	}
	
	

	//AsyncTask - fetch Flickr data in background and return an Array with all the Flickr Photos
	public class DownloadXmlTask extends AsyncTask<String, Void, ArrayList<FlickrPhoto>> {
		
		protected void onPreExecute() {
			// Whilst processing request display Spinner 
			m_dialog.setTitle("Searching for Photos...");
			m_dialog.setMessage("Back in a jiffy");
			m_dialog.setIndeterminate(true);
			m_dialog.setCancelable(true);
			m_dialog.show();	
		}

		@Override
	    protected ArrayList<FlickrPhoto> doInBackground(String... params) {
			String url = params[0];
			
				//Create ArrayList to hold the photos returned from Flickr API
	    		ArrayList<FlickrPhoto> photos = new ArrayList<FlickrPhoto>(); 
	    		
	    		//Instantiate the parser to then parse the XML data returned from Flickr
	        	JDomParser xml = new JDomParser();
	        	try {
	        		InputStream stream = xml.downloadUrl(url);
					photos = xml.parse(stream);
					//Store last fetched URL to repurpose for Pull to refresh
					PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(PREF_QUERY, url).commit();

					return photos;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	         
	          return photos;  
	         
	    }
	    
	   
	    protected void onPostExecute(ArrayList<FlickrPhoto> result) {  
	    	//If the query was unsuccessful - display Toast 
	    	if(result.isEmpty()){
	    		Context context = getApplicationContext();
	    		CharSequence text = "Bummer we found no photos...";
	    		int duration = Toast.LENGTH_LONG;

	    		Toast toast = Toast.makeText(context, text, duration);
	    		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    		toast.show();
	    	}
	    	//Close out the spinner and display results
	    	FlickrPhotoAdapter adapter = new FlickrPhotoAdapter(getApplicationContext(), result);
	    	setListAdapter(adapter);	
	    	m_dialog.dismiss();

	    }
	   
	}
	
	//Pull down to reload page function - Parse Stored URL and add increment by one page and repull data
	public void getNextFlickrPage(String url){
		
		int start = url.indexOf("page=");
		int end = url.indexOf("&extras");
		String pageParam = url.substring(start+5, end);
		int newPageValue = Integer.parseInt(pageParam)+1;
		String newParam = Integer.toString(newPageValue);
		System.out.println(newPageValue);
		String newURL = url.substring(0, start+5)+newParam+url.substring(end, url.length());
		System.out.println(newURL);
		new DownloadXmlTask().execute(newURL);

		pullToRefreshList.onRefreshComplete();
		
	}
	
}
