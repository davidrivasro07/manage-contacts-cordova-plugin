package com.callpal.cordova.manageContacts;

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
import android.provider.Settings;

public class ContactsManager extends CordovaPlugin {

    private CallbackContext callbackContext;

    private JSONArray executeArgs;

    public static final String ACTION_ADD_CONTACTS = "add";

    private static final String LOG_TAG = "Manage Contacts";

    public ContactsManager() {}

      /**
    * Current Cordova callback context (on this thread)
    */
   protected CallbackContext currentContext;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback context used when calling back into JavaScript.
     * @return                  True if the action was valid, false otherwise.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        this.executeArgs = args;
        currentContext = callbackContext;
        try {
          if (action.equals("add")){
            addContact();
            callbackContext.success();
          } else if (action.equals("switchToLocationSettings")){
            switchToLocationSettings();
            callbackContext.success();
          } else if(action.equals("isLocationEnabled")) {
            callbackContext.success(isGpsLocationEnabled() || isNetworkLocationEnabled() ? 1 : 0);
          }else {
              handleError("Invalid action");
              return false;
          }
        }catch(Exception e ) {
            handleError("Exception occurred: ".concat(e.getMessage()));
            return false;
        }
        return true;
    }

    public void addContact() {

      Context context = cordova.getActivity().getApplicationContext();

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
    }

    public void switchToLocationSettings() {
        Log.d(LOG_TAG, "Switch to Location Settings");
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        cordova.getActivity().startActivity(settingsIntent);
    }

    public boolean isGpsLocationEnabled() throws Exception {
        int mode = getLocationMode();
        boolean result = (mode == 3 || mode == 1) && isLocationAuthorized();
        Log.d(LOG_TAG, "GPS enabled: " + result);
        return result;
    }

    public boolean isNetworkLocationEnabled() throws Exception {
        int mode = getLocationMode();
        boolean result = (mode == 3 || mode == 2) && isLocationAuthorized();
        Log.d(TAG, "Network enabled: " + result);
        return result;
    }

    /**
     * Returns current location mode
     */
    private int getLocationMode() throws Exception {
        int mode;
        if (Build.VERSION.SDK_INT >= 19){ // Kitkat and above
            mode = Settings.Secure.getInt(this.cordova.getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        }else{ // Pre-Kitkat
            if(isLocationProviderEnabled(LocationManager.GPS_PROVIDER) && isLocationProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                mode = 3;
            }else if(isLocationProviderEnabled(LocationManager.GPS_PROVIDER)){
                mode = 1;
            }else if(isLocationProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                mode = 2;
            }else{
                mode = 0;
            }
        }
        return mode;
    }

    private boolean isLocationAuthorized() throws Exception {
        boolean authorized = hasPermission(permissionsMap.get("ACCESS_FINE_LOCATION")) || hasPermission(permissionsMap.get("ACCESS_COARSE_LOCATION"));
        Log.v(LOG_TAG, "Location permission is "+(authorized ? "authorized" : "unauthorized"));
        return authorized;
    }

    private boolean isLocationProviderEnabled(String provider) {
        LocationManager locationManager = (LocationManager) this.cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(provider);
    }

    /************
     * Internals
     ***********/
    /**
     * Handles an error while executing a plugin API method  in the specified context.
     * Calls the registered Javascript plugin error handler callback.
     * @param errorMsg Error message to pass to the JS error handler
     */
    private void handleError(String errorMsg, CallbackContext context){
        try {
            Log.e(LOG_TAG, errorMsg);
            context.error(errorMsg);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

}
