package screancasttest;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gemswin.screencastrecevertest.PrefManager;
import com.example.gemswin.screencastrecevertest.R;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity  {
    Button button,canvas,download,resetIP,openBrowser;
    String downloadIp,filename;
    Dialog dialogRegupdate;

    int portFTP;
    JSONParser jParser5 = new JSONParser();
    JSONObject json;
    JSONParser jParser6 = new JSONParser();
    JSONObject json1;
    JSONParser jParser7 = new JSONParser();
    JSONObject json2;
    PrefManager pref;
    public static String portString;
    ArrayList<String>planetList;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    Dialog dialogDoubt1;

    public static List<String> ipArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        planetList = new ArrayList<String>();
        pref = new PrefManager(getApplicationContext());
        button= (Button) findViewById(R.id.browse);
        canvas= (Button) findViewById(R.id.canvas);



        download=(Button) findViewById(R.id.download);
        openBrowser=(Button) findViewById(R.id.openBrowser);

        canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas();
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               fileBrowser();

            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogBox();

            }
        });
        openBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent search = new Intent(MainActivity.this,webview.class);
                //search.putExtra(SearchManager.QUERY, "download");
                startActivity(search);

            }
        });





    }
private void canvas(){
    Intent intent=new Intent(this,CanvasMain.class);
    startActivity(intent);
}
    private void fileBrowser() {
        Intent intent=new Intent(this,FileBrowser.class);
        startActivity(intent);

    }


         public class Download extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                FTPClient con = null;

                try
                {
                    con = new FTPClient();
                    con.connect(downloadIp,portFTP);

                    if (con.login("FTPUser", "ankit"))
                    {
                        con.enterLocalPassiveMode(); // important!
                        con.setFileType(FTP.BINARY_FILE_TYPE);
                        String data = "/sdcard/Download/"+ filename;

                        OutputStream out = new FileOutputStream(new File(data));
                        boolean result = con.retrieveFile(filename, out);
                        out.close();
                        if (result) Log.v("download result", "succeeded");
                        con.logout();
                        con.disconnect();
                    }
                }
                catch (Exception e)
                {
                    Log.v("download result","failed");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
      dialog.dismiss();

            }

             private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

             /** progress dialog to show user that the backup is processing. */
             /** application context. */

             protected void onPreExecute() {
                 this.dialog.setMessage("Downloading file...");
                 this.dialog.show();
             }



         }


    protected void DialogBox () {
        // TODO Auto-generated method stub

        dialogRegupdate = new Dialog(MainActivity.this);
        dialogRegupdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRegupdate.setContentView(R.layout.dialog);

        Button submit = (Button)dialogRegupdate.findViewById(R.id.submit);



        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                downloadIp= ((EditText)dialogRegupdate.findViewById(R.id.ip)).getText().toString();


                portFTP=   Integer.parseInt(((EditText)dialogRegupdate.findViewById(R.id.port)).getText().toString());
                filename= ((EditText)dialogRegupdate.findViewById(R.id.filename)).getText().toString();




                new Download().execute();




                dialogRegupdate.dismiss();




            }
        });

        dialogRegupdate.show();
    }

    private class getIPs extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }




        @Override
        protected String doInBackground(String... urls)
        {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class", pref.getclass()));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/getIPs.php";


            json1 = jParser6.makeHttpRequest(log1, "POST", params);






            //visible
            return null;



        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {  //gone
            // //System.out.println("RESULT : " + result);

            this.dialog.dismiss();
            try {
                // Checking for SUCCESS TAG




                //Toast.makeText(MainActivity.this, (CharSequence) json, 1).show();
                ipArray = new ArrayList<>() ;

                JSONArray account= json1.getJSONArray("IPs");
                for(int i = 0; i < account.length(); i++) {
                    json1 = account.getJSONObject(i);

                    String IpString = json1.getString("IP");

                    portString = json1.getString("PORT");

                    if(!IpString.equals("0.0.0.0"))
                        ipArray.add(IpString);

                }

                 if(ipArray != null)
                     Toast.makeText(getApplicationContext(),"IPs are synchronised.",Toast.LENGTH_LONG).show();



            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Network connection Error!!!",Toast.LENGTH_LONG).show();

            }




        }
    }


    private class resetIPs extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage("Resetting...");
            this.dialog.show();
        }




        @Override
        protected String doInBackground(String... urls)
        {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class", pref.getclass()));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/resetIPs.php";


            json2 = jParser7.makeHttpRequest(log1, "POST", params);






            //visible
            return null;



        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {  //gone
            // //System.out.println("RESULT : " + result);

            this.dialog.dismiss();
            try {
                // Checking for SUCCESS TAG




                //Toast.makeText(MainActivity.this, (CharSequence) json, 1).show();
                String account= json2.getString("client");
                if(account.equals("FAILED"))
                    Toast.makeText(MainActivity.this, "IPs not Resseted", Toast.LENGTH_SHORT).show();
                else if(account.equals("SUCCESS")) {
                    Toast.makeText(MainActivity.this, "Successfully Resseted.", Toast.LENGTH_SHORT).show();
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Network connection Error!!!",Toast.LENGTH_LONG).show();

            }


           /* int i =1;
            Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getBaseContext(), 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);*/

        }
    }
    private class viewdoubttask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
            this.dialog.setMessage("Opening DoubtList...");
            this.dialog.show();
        }




        @Override
        protected String doInBackground(String... urls)
        {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //  params.add(new BasicNameValuePair("roll_number", username));
            //params.add(new BasicNameValuePair("doubt", doubtstring));
           /* params.add(new BasicNameValuePair("name", pref.getName()));
            params.add(new BasicNameValuePair("roll", pref.getSerialNo()));
*/
            String log = "http://176.32.230.250/anshuli.com/ScreenCast/seeDoubtListMaster.php";  // change php file name


            json = jParser5.makeHttpRequest(log, "POST", params);



            try {
                // Checking for SUCCESS TAG
                planetList.clear();
                JSONArray account= json.getJSONArray("client");
                for(int i = 0; i < account.length(); i++)
                {
                    json =account.getJSONObject(i);



                    String doubt = json.getString("DOUBT");

                    planetList.add(doubt);
                }



            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Network connection Error!!!",Toast.LENGTH_LONG).show();
            }


            //visible
            return null;



        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {  //gone
            // //System.out.println("RESULT : " + result);


            this.dialog.dismiss();

            DoubtBox1();






        }
    }


    protected void DoubtBox1 () {
        // TODO Auto-generated method stub

        dialogDoubt1 = new Dialog(MainActivity.this);
        dialogDoubt1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDoubt1.setContentView(R.layout.activity_doubtlist);  //here

        mainListView = (ListView)dialogDoubt1.findViewById(R.id.mainListView);


        //list ki shuruat




		/*String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
				"Jupiter", "Saturn", "Uranus", "Neptune"};
		planetList.addAll( Arrays.asList(planets) );*/

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.simplerow, planetList);
        mainListView.setAdapter(listAdapter);




        dialogDoubt1.show();

    }     //end of doubt box


}
