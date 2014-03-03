package com.danielcintra.photo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.content.CursorLoader;

public class ContactsLoader {
	
	private Context context;

    public ContactsLoader(Context c) {
        this.context = c;
    }
    
    public ArrayList<Contacts> fetchAll() {
        ArrayList<Contacts> listContacts = new ArrayList<Contacts>();
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        String select = ContactsContract.Contacts.DISPLAY_NAME + " IS NOT NULL";
        CursorLoader cursorLoader = new CursorLoader(context, RawContacts.CONTENT_URI, 
        		null, // the columns to retrieve (all)
                select, // the selection criteria (none)
                null, // the selection args (none)
                sortOrder // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst()) {
            do {
                Contacts contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }
    
    private Contacts loadContactData(Cursor c) {
        // Get Contact ID
        int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
        String contactId = c.getString(idIndex);
        // Get Contact Name
        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactDisplayName = c.getString(nameIndex);
        Contacts contact = new Contacts(contactId, contactDisplayName);
        return contact;
    }

}
