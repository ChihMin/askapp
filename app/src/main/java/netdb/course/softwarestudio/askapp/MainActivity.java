package netdb.course.softwarestudio.askapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import netdb.course.softwarestudio.askapp.model.Definition;
import netdb.course.softwarestudio.askapp.service.rest.RestManager;


public class MainActivity extends ActionBarActivity {

    private RestManager restMgr;

    private EditText keywordEdt;
    private EditText definitionEdt;
    private Button searchBtn;
    private Button definitionBtn;
    private TextView titleTxt;
    private TextView descriptionTxt;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restMgr = RestManager.getInstance(this);

        keywordEdt = (EditText) findViewById(R.id.edt_keyword);
        definitionEdt = (EditText) findViewById(R.id.edt_definition);
        searchBtn = (Button) findViewById(R.id.btn_search);
        definitionBtn = (Button) findViewById(R.id.btn_definition);
        titleTxt = (TextView) findViewById(R.id.txt_title);
        descriptionTxt = (TextView) findViewById(R.id.txt_description);
        progressBar = (ProgressBar) findViewById(R.id.pgsb_loading);

        searchBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordEdt.getText().toString().replaceAll("\\s", "");
                if (keyword.length() != 0) {
                    // send a search request
                    progressBar.setVisibility(View.VISIBLE);
                    searchKeyword(keyword);
                } else {
                    // clear results
                    titleTxt.setText("");
                    descriptionTxt.setText("");
                }
            }
        });

        definitionBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = keywordEdt.getText().toString().replaceAll("\\s", "");
                String description = definitionEdt.getText().toString().replaceAll("\\s", "");
                if (description.length() != 0 && title.length() != 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    addDefinition(title, description);
                    // searchKeyword(title);
                }
            }
        });

    }

    private void addDefinition(String title, String description) {
        final Definition def = new Definition();
        def.setTitle(title);
        def.setDescription(description);

        restMgr.postResource(Definition.class, def, new RestManager.PostResourceListener<Definition>() {
            @Override
            public void onResponse(int code, Map<String, String> headers) {
                searchKeyword( def.getTitle() );
                return;
                // TODO: Finish this activity

            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {
                onError(null, null, code, headers);
            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {
                Log.d(this.getClass().getSimpleName(), "" + code + ": " + message);

                setResult(RESULT_CANCELED);
                finish();
            }
        }, null);
    }

/*
    private void addKeyword(String title, String description) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");

        try {
            description = URLEncoder.encode(description, StandardCharsets.UTF_8.name());
            title = URLEncoder.encode(title, StandardCharsets.UTF_8.name());
            Log.d("TAG", "Description : " + description);
            Log.d("TAG", "Title : " + title);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        restMgr.postResource(Definition.class, title, description, header,
                new RestManager.PostResourceListener<Definition>(){
                    @Override
                    public void onResponse(int code, Map<String, String> headers, Definition resource){

                    }
                }
                , null);

        HttpClient httpClient = new DefaultHttpClient();
        String URL = getResources().getString(R.string.rest_server_url);


        HttpPost httpPost = new HttpPost( URL +  "definitions");
        Log.d("TAG", URL +  "definitions" );

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("title", title));
            nameValuePairs.add(new BasicNameValuePair("description", description));

            JSONObject jsonobj = new JSONObject();
            jsonobj.put("title", title);
            jsonobj.put("description", description);



            StringEntity se = new StringEntity(jsonobj.toString());
            se.setContentType("application/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
            httpPost.setEntity( se );
            // Execute HTTP Post Request
            Log.d( "TAG", jsonobj.toString() );
            HttpResponse response = httpClient.execute(httpPost);
            Log.d( "TAG", "POST SUCCESSFUL" );

        } catch (ClientProtocolException e) {
            Toast.makeText(MainActivity.this, "Fail!!",Toast.LENGTH_SHORT);
            return;
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch( JSONException e ){
            e.printStackTrace();
        }

    }
*/
    private void searchKeyword(String keyword) {

        // Set header
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");

        try {
            keyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
            Log.d("TAG", keyword);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        restMgr.getResource(Definition.class, keyword, null, header, new RestManager.GetResourceListener<Definition>() {
            @Override
            public void onResponse(int code, Map<String, String> headers, Definition resource) {

                progressBar.setVisibility(View.INVISIBLE);

                titleTxt.setText(resource.getTitle());
                descriptionTxt.setText(resource.getDescription());
            }

            @Override
            public void onRedirect(int code, Map<String, String> headers, String url) {

            }

            @Override
            public void onError(String message, Throwable cause, int code, Map<String, String> headers) {

                progressBar.setVisibility(View.INVISIBLE);

                if (code == 404) {
                    Toast.makeText(MainActivity.this, getString(R.string.info_not_found),
                            Toast.LENGTH_SHORT).show();

                }
            }
        }, null);
    }
}
