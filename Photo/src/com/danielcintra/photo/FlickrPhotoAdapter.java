package com.danielcintra.photo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class FlickrPhotoAdapter extends BaseAdapter {
	
	private ArrayList<FlickrPhoto> photos; 
	private Context mContext;
	//private LayoutInflater inflater = null;
	
	public FlickrPhotoAdapter(Context a, ArrayList<FlickrPhoto> p) {
		// TODO Auto-generated constructor stub
		this.mContext = a;
		this.photos = p;
		
	}
	private class ViewHolder {
        ImageView img;
        TextView title;
        TextView desc;
        TextView oName;
        TextView date;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photos.size();
	}

	@Override
	public FlickrPhoto getItem(int position) {
		// TODO Auto-generated method stub
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return photos.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.textViewTitle);
			holder.desc = (TextView) convertView.findViewById(R.id.textViewDescription);
			holder.oName = (TextView) convertView.findViewById(R.id.ownerName);
			holder.img = (ImageView) convertView.findViewById(R.id.icon);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);

			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		FlickrPhoto photo = (FlickrPhoto) getItem(position);
		String date = photo.getDate().substring(0, 7);
		
		holder.desc.setText(photo.getDesc());
		holder.title.setText(photo.getTitle());
		holder.oName.setText(photo.getOwner());
		holder.date.setText(date);
		UrlImageViewHelper.setUrlDrawable(holder.img, photo.imageURLSmall, R.drawable.ajax_loader);
		
		
		
		
		
		return convertView;
		
	
	}

}
