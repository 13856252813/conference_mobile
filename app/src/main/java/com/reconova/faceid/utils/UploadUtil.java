package com.reconova.faceid.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.UUID;
  
import android.util.Log;

import com.common.utlis.ULog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class UploadUtil {  
    private static UploadUtil uploadUtil;  
    private static final String BOUNDARY =  UUID.randomUUID().toString();
    private static final String PREFIX = "--";  
    private static final String LINE_END = "\r\n";  
    private static final String CONTENT_TYPE = "multipart/form-data";
    private UploadUtil() {  
  
    }  

    public static UploadUtil getInstance() {  
        if (null == uploadUtil) {  
            uploadUtil = new UploadUtil();  
        }  
        return uploadUtil;  
    }

    public static String sendFilesPost(String url, String fileNames) {
        HttpClient httpClient = null;
        HttpPost httpPost;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);

            String[] filenames=fileNames.split(";");
            MultipartEntity reqEntity = new MultipartEntity();
            for(int i=0;i<filenames.length;i++) {
                String fileName=filenames[i];
                ULog.i(TAG, "fileName: " + fileName);
                FileBody file = new FileBody(new File(fileName), "image/*");

                reqEntity.addPart("faceimage", file);// file1为请求后台的File upload;属性

            }
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if (null != response && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                if (null != resEntity) {
                    result = EntityUtils.toString(resEntity, HTTP.UTF_8);
                    System.out.println(result);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    private static final String TAG = "UploadUtil";  
    private int readTimeOut = 10 * 1000;
    private int connectTimeout = 10 * 1000;

    private static int requestTime = 0;  
      
    private static final String CHARSET = "utf-8";
  

    public static final int UPLOAD_SUCCESS_CODE = 1;  

    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;  

    public static final int UPLOAD_SERVER_ERROR_CODE = 3;  
    protected static final int WHAT_TO_UPLOAD = 1;  
    protected static final int WHAT_UPLOAD_DONE = 2;

    public static final String requestURL = "http://192.168.1.33:3301/facelogin/";
    public static final String requestURL2 = "http://192.168.1.33:3301/";
    //public static final String requestURL = "https://192.168.16.53:3304/facelogin/";
    public static final String faceImageKey = "faceimage";
    public static final String faceImageKey2 = "faceimages";
    public static final String test_token = "240748842c7541e5339e0e8b9c547ef81b56edad08136ac614a0faaba26ee632";
    public static final String test_url = requestURL2 + "api/faceuploads/?token=" + test_token;

    private static int _timeout_connection = 30000;
    private static int _timeout_so = 30000;
    public void uploadFile(String filePath, String fileKey, String RequestURL ) {
        if (filePath == null) {  
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"address");
            return;  
        }  
        try {  
            File file = new File(filePath);  
            uploadFile(file, fileKey, RequestURL );
        } catch (Exception e) {  
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"address");
            e.printStackTrace();  
            return;  
        }  
    }  
  

    public void uploadFile(final File file, final String fileKey,  
            final String RequestURL ) {
        if (file == null || (!file.exists())) {  
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE,"address");
            return;  
        }  
  
        Log.i(TAG, " address URL=" + RequestURL);
        Log.i(TAG, "addressfileName=" + file.getName());
        Log.i(TAG, "addressfileKey=" + fileKey);
        new Thread(new Runnable() {
            @Override  
            public void run() {  
                toUploadFile(file, fileKey, RequestURL);
            }  
        }).start();  
          
    }


    public class MyX509TrustManager implements X509TrustManager {


        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }

    private void toUploadFile(File file, String fileKey, String RequestURL ) {
        String result = null;  
        requestTime= 0;




        boolean isHttps = RequestURL.startsWith("https://");
        if (isHttps) {
            HttpsURLConnection conn = null;
            try {

                TrustManager[] tm = {new MyX509TrustManager()};
                //SSLContext sslContext = SSLContext.getInstance("SSL");
                //sslContext.init(null, tm, new java.security.SecureRandom();
                //SSLSocketFactory ssf = sslContext.getSocketFactory();
            /*KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory socketFactory = new SSLSocketFactoryEx(trustStore);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);*/

                final String boundaryString = "--------------------------" + java.util.UUID.randomUUID().toString();
                final String end = "\r\n";
                final String twoHyphens = "--";
                URL uri = new URL(RequestURL);


                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tm, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(
                        context.getSocketFactory());

                conn = (HttpsURLConnection) uri.openConnection();
                //conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setChunkedStreamingMode(1024 * 10);
                conn.setConnectTimeout(20000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
//              conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundaryString);

                StringBuilder sbHeader = new StringBuilder();
                sbHeader.append(end);
                sbHeader.append(twoHyphens);
                sbHeader.append(boundaryString);
                sbHeader.append(end);
                OutputStream dos = conn.getOutputStream();
                dos.write(sbHeader.toString().getBytes());

                dos.write(("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"").getBytes());
                dos.write(end.getBytes());
                dos.write("Content-Type: application/octet-stream".getBytes());
                dos.write(end.getBytes());
                dos.write(end.getBytes());
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int count = 0;
                long total = 0;
                while ((count = is.read(buffer)) != -1) {
                    total += count;
                    onUploadProcessListener.onUploadProcess(count);
                    dos.write(buffer, 0, count);
                }
                is.close();
                dos.write(end.getBytes());
                StringBuilder sbEnd = new StringBuilder();
                sbEnd.append(twoHyphens);
                sbEnd.append(boundaryString);
                sbEnd.append(twoHyphens);
                sbEnd.append(end);

                dos.write(sbEnd.toString().getBytes());
                dos.flush();

                int statusCode = conn.getResponseCode();
                if (statusCode == HttpStatus.SC_OK) {
                    //LoadView.dismiss();
                    //String result = StreamUtility.readStream(conn.getInputStream());
                    //Logger.error("HttpInvokeEngine",result);
                    ULog.i(TAG, "request success");
                    InputStream input = conn.getInputStream();
                /*StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();*/

                    result = streamToString(input);
                    ULog.i(TAG, "result : " + result);
                    sendMessage(UPLOAD_SUCCESS_CODE, result);

                    if (conn != null) {
                        conn.disconnect();
                    }

                    ULog.i(TAG, "result1 : " + result);
                    return;

                } else {

                    StringBuilder sb = new StringBuilder();
                    sb.append("Upload image failed. ");
                    sb.append("The http response status code is ");
                    sb.append(statusCode);
                    sb.append(". The request url is ");
                    sb.append(RequestURL);
                    //Logger.error("HttpInvokeEngine", sb.toString());
                    ULog.i(TAG, "request error:" + sb);
                }
            } catch (Exception e) {
                ULog.i(TAG, "Exception : " + e);
            }
            sendMessage(UPLOAD_SERVER_ERROR_CODE, result);
            if (conn != null) {
                conn.disconnect();
            }
        } else {
            HttpURLConnection conn = null;
            try {

                final String boundaryString = "--------------------------" + java.util.UUID.randomUUID().toString();
                final String end = "\r\n";
                final String twoHyphens = "--";
                URL uri = new URL(RequestURL);


                conn = (HttpURLConnection) uri.openConnection();
                //conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setChunkedStreamingMode(1024 * 10);
                conn.setConnectTimeout(20000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
//              conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundaryString);

                StringBuilder sbHeader = new StringBuilder();
                sbHeader.append(end);
                sbHeader.append(twoHyphens);
                sbHeader.append(boundaryString);
                sbHeader.append(end);
                OutputStream dos = conn.getOutputStream();
                dos.write(sbHeader.toString().getBytes());

                dos.write(("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"").getBytes());
                dos.write(end.getBytes());
                //dos.write("Content-Type: application/octet-stream".getBytes());
                dos.write("Content-Type: image/jpeg".getBytes());
                dos.write(end.getBytes());
                dos.write(end.getBytes());
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int count = 0;
                long total = 0;
                while ((count = is.read(buffer)) != -1) {
                    total += count;
                    //onUploadProcessListener.onUploadProcess(count);
                    dos.write(buffer, 0, count);
                }
                is.close();
                dos.write(end.getBytes());

                StringBuilder sbEnd = new StringBuilder();
                sbEnd.append(twoHyphens);
                sbEnd.append(boundaryString);
                sbEnd.append(twoHyphens);
                sbEnd.append(end);
                dos.write(sbEnd.toString().getBytes());
                StringBuilder sb1 = new StringBuilder();
                sb1.append("Content-Disposition: form-data; name=\""
                        + "uaccount" + "\"");
                sb1.append("\r\n");
                sb1.append("\r\n");
                sb1.append("wsj");
                sb1.append("\r\n");
                byte[] data = sb1.toString().getBytes();
                dos.write(data);

                dos.write(sbEnd.toString().getBytes());
                dos.flush();

                int statusCode = conn.getResponseCode();
                if (statusCode == HttpStatus.SC_OK) {
                    //LoadView.dismiss();
                    //String result = StreamUtility.readStream(conn.getInputStream());
                    //Logger.error("HttpInvokeEngine",result);
                    ULog.i(TAG, "request success");
                    InputStream input = conn.getInputStream();
                /*StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();*/

                    result = streamToString(input);
                    ULog.i(TAG, "result : " + result);
                    //sendMessage(UPLOAD_SUCCESS_CODE, result);

                    if (conn != null) {
                        conn.disconnect();
                    }

                    ULog.i(TAG, "result1 : " + result);
                    return;

                } else {

                    StringBuilder sb = new StringBuilder();
                    sb.append("Upload image failed. ");
                    sb.append("The http response status code is ");
                    sb.append(statusCode);
                    sb.append(". The request url is ");
                    sb.append(RequestURL);
                    //Logger.error("HttpInvokeEngine", sb.toString());
                    ULog.i(TAG, "request error:" + sb);
                }
            } catch (Exception e) {
                ULog.i(TAG, "Exception : " + e);
            }
            //sendMessage(UPLOAD_SERVER_ERROR_CODE, result);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public String streamToString(InputStream stream) throws Exception{
        String json = null;
        InputStreamReader reader = null;
        ByteArrayOutputStream os = null;
        OutputStreamWriter osw = null;
        try {
            reader = new InputStreamReader(stream, HTTP.UTF_8);
            os = new ByteArrayOutputStream();
            osw = new OutputStreamWriter(os);
            char[] bs = new char[1024];
            int len = 0;
            while ((len = reader.read(bs)) != -1) {
                osw.write(bs, 0, len);
            }
            osw.flush();
            os.flush();
            json = os.toString();
            return json;
        }finally{
            if(osw!=null){
                osw.close();
                osw = null;
            }

            if(os!=null){
                os.close();
                os = null;
            }

            if(reader!=null){
                reader.close();
                reader = null;
            }
        }
    }
    private void sendMessage(int responseCode,String responseMessage)  
    {  
        onUploadProcessListener.onUploadDone(responseCode, responseMessage);  
    }  
      

    public static interface OnUploadProcessListener {  

        void onUploadDone(int responseCode, String message);  

        void onUploadProcess(int uploadSize);  

        void initUpload(int fileSize);  
    }  
    private OnUploadProcessListener onUploadProcessListener;  
      
      
  
    public void setOnUploadProcessListener(  
            OnUploadProcessListener onUploadProcessListener) {  
        this.onUploadProcessListener = onUploadProcessListener;  
    }  
  
    public int getReadTimeOut() {  
        return readTimeOut;  
    }  
  
    public void setReadTimeOut(int readTimeOut) {  
        this.readTimeOut = readTimeOut;  
    }  
  
    public int getConnectTimeout() {  
        return connectTimeout;  
    }  
  
    public void setConnectTimeout(int connectTimeout) {  
        this.connectTimeout = connectTimeout;  
    }  

    public static int getRequestTime() {  
        return requestTime;  
    }  
      
    public static interface uploadProcessListener{  
          
    }  
      
      
      
      
}  