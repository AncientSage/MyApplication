package com.example.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.widget.*;
import android.os.Bundle;
import android.view.*;
import java.io.*;
import android.net.*;
import android.os.AsyncTask;
import android.content.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private TextView msg;
    public final static String COUNT = "com.example.app.COUNT";
    public final static String USER = "com.example.app.USER";
    private MainActivity app = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        msg = (TextView) findViewById(R.id.msg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void login(View view) {
        String siteURL = "http://powerful-plains-7245.herokuapp.com/users/login";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            new json_handler().execute(siteURL);
        }
    }

    public void addUser(View view) {
        String siteURL = "http://powerful-plains-7245.herokuapp.com/users/add";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            new json_handler().execute(siteURL);
        }
    }

    private class json_handler extends AsyncTask {

        @Override
        protected Object doInBackground(Object ... params) {
            try {
                String site_url = (String) params[0];
                return requester(site_url);
            } catch (IOException e) {
                return null;
            }
        }
        protected void onPostExecute(Object o) {
            try {
                JSONObject json = (JSONObject) o;
                if (json != null) {
                    switch (json.getInt("errCode")) {
                        case -1:
                            msg.setText("Invalid username and password combination. Please try again.");
                            break;
                        case -2:
                            msg.setText("This user name already exists. Please try again");
                            break;
                        case -3:
                            msg.setText("The user name should be non-empty and at most 128 characters long. Please try again.");
                            break;
                        case -4:
                            msg.setText("The password should be at most 128 characters long. Please try again.");
                            break;
                        default:
                            Intent intent = new Intent(app, SuccessActivity.class);
                            intent.putExtra(COUNT, json.getInt("count"));
                            intent.putExtra(USER, username.getText().toString());
                            startActivity(intent);
                            break;
                    }
                }
            } catch (Exception e) {
                msg.setText("Something unexpected occurred.");
            }
        }
        private JSONObject requester(String siteURL) throws IOException {
            InputStream input = null;
            HttpClient http_client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(http_client.getParams(), 10000);
            HttpResponse http_response;
            JSONObject json = new JSONObject();
            try {
                HttpPost post_req = new HttpPost(siteURL);
                json.put("user", username.getText().toString());
                json.put("password", password.getText().toString());
                StringEntity res_str = new StringEntity(json.toString());
                res_str.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post_req.setEntity(res_str);
                http_response = http_client.execute(post_req);
                if(http_response != null){
                    input = http_response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        input.close();
                    } catch (IOException e) {
                    }
                    String jsonString =  sb.toString();
                    return new JSONObject(jsonString);
                } else {
                    return null;
                }
            } catch(Exception e) {
                return null;
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.s_activity, container, false);
            return rootView;
        }
    }

}
