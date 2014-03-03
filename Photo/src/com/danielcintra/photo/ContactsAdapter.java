package com.danielcintra.photo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactsAdapter extends ArrayAdapter<Contacts> {

	
	public ContactsAdapter(Context context, ArrayList<Contacts> contacts) {

		super(context, 0, contacts);
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        Contacts contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.contact_list_item, null);
        }
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.contact);
        
        tvName.setText(contact.name);

        return view;
    }
}
