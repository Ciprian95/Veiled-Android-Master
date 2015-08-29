package com.Veiled.SqlConnection.Tables;

public class Preference {
    public String id;
    public int pref_id;
    public String pref_name;

    public Preference(int i_pref_id, String i_pref_name){
        pref_id = i_pref_id;
        pref_name = i_pref_name;
    }


    /* SENT Preferences to Mobile Service Database

      Preference pref = new Preference(10, "Coffee");

      MobileServiceTable<Preference> preferenceTable = serviceClient.getTable(Preference.class);
      preferenceTable.insert(pref,
      new TableOperationCallback<Preference>() {
        public void onCompleted(Preference entity, Exception exception, ServiceFilterResponse response) {
       }});

     */

}
