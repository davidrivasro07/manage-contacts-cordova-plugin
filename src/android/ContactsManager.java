package com.dbaq.cordova.contactsPhoneNumbers;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class ContactsManager extends CordovaPlugin {

    private CallbackContext callbackContext;

    private JSONArray executeArgs;

    public static final String ACTION_ADD_CONTACTS = "add";

    private static final String LOG_TAG = "Contact Phone Numbers";

    public ContactsManager() {}

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback context used when calling back into JavaScript.
     * @return                  True if the action was valid, false otherwise.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        this.executeArgs = args;

        if (ACTION_ADD_CONTACTS.equals(action)) {
          Context context = this.cordova.getActivity().getApplicationContext();

          Log.d("respuesta logout:" ,String.valueOf(args));

          JSONObject contactData = (JSONObject) args.get(0);
          JSONArray phoneNumbers = (JSONArray) contactData.getJSONArray("phones");


          final Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

          if(contactData.has("name")){
             intent.putExtra(ContactsContract.Intents.Insert.NAME, contactData.getString("name"));
          }

          if(phoneNumbers.length() > 0){
            for (int i = 0; i < phoneNumbers.length(); i++) {
              JSONObject phone = (JSONObject) phoneNumbers.get(i);
              switch(i){
                case 0:
                  intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone.getString("number"));
                  intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                  break;
                case 1:
                  intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, phone.getString("number"));
                  intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                  break;
                case 2:
                  intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, phone.getString("number"));
                  intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                  break;
              }
            }
          }

          context.startActivity(intent);

          return true;
        }

        return false;
    }

    /**
     * Retrieve the type of the phone number based on the type code
     * @param type the code of the type
     * @return a string in caps representing the type of phone number
     */
    private String getPhoneTypeLabel(int type) {
        String label = "OTHER";
        if (type == Phone.TYPE_HOME)
            label = "HOME";
        else if (type == Phone.TYPE_MOBILE)
            label = "MOBILE";
        else if (type == Phone.TYPE_WORK)
            label = "WORK";

        return label;
    }
}
