package api.http;

import api.AsyncTask;

import java.io.*;
import java.net.*;
import java.util.HashMap;

//TODO Improve based on: https://www.baeldung.com/java-http-request
public abstract class HTTPAsyncTask extends AsyncTask {



    //TODO All MIME types as listed here: https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types

    private final String url;
    private final HashMap<String, String> params;
    private final RequestMethod requestMethod;
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private MimeType mimeType = MimeType.CONTENT_TYPE_JSON;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT = 5000;

    public HTTPAsyncTask(String url, ParameterMap params, RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
        this.params = params.get();
        try {
            if (requestMethod == RequestMethod.GET) {
                this.url = url + "?" + QueryStringMaker.makeQueryString(this.params);
            } else {
                this.url = url;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        setConnectTimeout(30000);
        setReadTimeout(30000);
    }

    public void setContentType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    @Override
    protected void onPreExecute() { }

    @Override
    protected void doInBackground() {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(requestMethod.toString());
            con.setRequestProperty(MimeType.CONTENT_TYPE_KEY, mimeType.getText());
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            con.setDoOutput(true);

            if (requestMethod == RequestMethod.POST) {
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(QueryStringMaker.makeQueryString(params));
                out.flush();
                out.close();
            }

            int statusCode = con.getResponseCode();

            Reader streamReader = null;
            if (statusCode > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            onResponseReceived(content.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void onResponseReceived(String response);

    @Override
    protected void onPostExecute() { }

}
