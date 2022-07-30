package com.tmtl_ecu;

import android.app.AlertDialog;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class upload {
    AlertDialog.Builder dialog = null;
    private String attachmentName;
    private String attachmentFileName;
    private String crlf;
  //  @RequiresApi(api = Build.VERSION_CODES.S)
    public int uploadFile (String sourceFileUri){

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String fileName = sourceFileUri;

        String filename[]=sourceFileUri.split(File.separator);
        String actualFilename="";
        for(int i=0;i<filename.length;i++)
        {
            actualFilename=filename[i] ;
        }
        actualFilename=actualFilename;
        System.out.println("actual filename .....................:"+actualFilename);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
         attachmentName = actualFilename;
         attachmentFileName = actualFilename;
         crlf = "\r\n";

        int serverResponseCode = 0;
        try {



            FileInputStream fileInputStream=new FileInputStream(sourceFileUri);

       //     URL url = new URL("http://192.168.1.1/fileupload?name='"+actualFilename+"'");
            URL url = new URL("http://192.168.1.1/fileupload?filename=File1.bin");




            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
          //  conn.setRequestProperty("uploaded_file", fileName);
            conn.setRequestProperty("filename", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
         //   dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + actualFilename + "" + lineEnd);
           // dos.writeBytes("Content-Disposition: multipart/form-data; name=File1.bin;filename=" + "File1.bin" + "" + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName + "\";filename=\"" +
                 this.attachmentFileName + "\"" + this.crlf);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                System.out.println("bytes read :"+bytesRead);

            }
System.out.println();
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {

               

                        String msg = "File Upload Completed";
                                


                      
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            conn.disconnect();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();

          
                    
                    

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            //  dialog.dismiss();
            e.printStackTrace();
            
            Log.e("Upload  Exception", "Exception : "
                    + e.getMessage(), e);
        }
       ;
        return serverResponseCode;

        /*       } // End else block*/
        //   return serverResponseCode;
    }


    public int uploadFilestm (String sourceFileUri){

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String fileName = sourceFileUri;

        String filename[]=sourceFileUri.split(File.separator);
        String actualFilename="";
        for(int i=0;i<filename.length;i++)
        {
            actualFilename=filename[i] ;
        }

        System.out.println("actual filename .....................:"+actualFilename);
        System.out.println("sourceFileUri .....................:"+sourceFileUri);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        attachmentName = actualFilename;
        attachmentFileName = actualFilename;
        crlf = "\r\n";

        int serverResponseCode = 0;
        try {



            FileInputStream fileInputStream=new FileInputStream(sourceFileUri);

            //     URL url = new URL("http://192.168.1.1/fileupload?name='"+actualFilename+"'");
            URL url = new URL("http://192.168.1.1/upload?name="+actualFilename);




            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //  conn.setRequestProperty("uploaded_file", fileName);
            conn.setRequestProperty("name", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //   dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + actualFilename + "" + lineEnd);
            // dos.writeBytes("Content-Disposition: multipart/form-data; name=File1.bin;filename=" + "File1.bin" + "" + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; filename=\"" + this.attachmentName + "\";name=\"" +
                    this.attachmentFileName + "\"" + this.crlf);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                System.out.println("bytes read :"+bytesRead);

            }
            System.out.println();
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {



                String msg = "File Upload Completed";




            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            conn.disconnect();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();





            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            //  dialog.dismiss();
            e.printStackTrace();

            Log.e("Upload  Exception", "Exception : "
                    + e.getMessage(), e);
        }
        ;
        return serverResponseCode;

        /*       } // End else block*/
        //   return serverResponseCode;
    }

    public int updateFileota (String sourceFileUri){

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String fileName = sourceFileUri;

        String filename[]=sourceFileUri.split(File.separator);
        String actualFilename="";
        for(int i=0;i<filename.length;i++)
        {
            actualFilename=filename[i] ;
        }
        System.out.println("actual filename .....................:"+actualFilename);
        System.out.println("sourceFileUri .....................:"+sourceFileUri);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        attachmentName = actualFilename;
        attachmentFileName = actualFilename;
        crlf = "\r\n";

        int serverResponseCode = 0;
        try {



            FileInputStream fileInputStream=new FileInputStream(sourceFileUri);

            //     URL url = new URL("http://192.168.1.1/fileupload?name='"+actualFilename+"'");
            URL url = new URL("http://192.168.1.1/update?name="+actualFilename);




            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //  conn.setRequestProperty("uploaded_file", fileName);
            conn.setRequestProperty("name", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //   dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + actualFilename + "" + lineEnd);
            // dos.writeBytes("Content-Disposition: multipart/form-data; name=File1.bin;filename=" + "File1.bin" + "" + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; filename=\"" + this.attachmentName + "\";name=\"" +
                    this.attachmentFileName + "\"" + this.crlf);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                System.out.println("bytes read :"+bytesRead);

            }
            System.out.println();
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if (serverResponseCode == 200) {



                String msg = "File Upload Completed";




            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            conn.disconnect();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();





            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            //  dialog.dismiss();
            e.printStackTrace();

            Log.e("Upload  Exception", "Exception : "
                    + e.getMessage(), e);
        }
        ;
        return serverResponseCode;

        /*       } // End else block*/
        //   return serverResponseCode;
    }


   // @RequiresApi(api = Build.VERSION_CODES.S)
    public int startflash(int filesize) {
        int code=0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String UR = "http://192.168.1.1/startflash?fsize="+filesize+"";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(UR);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            String size= Integer.toString(filesize);
            nameValuePairs.add(new BasicNameValuePair("fsize",size ));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
        StatusLine status=    response.getStatusLine();
    code=status.getStatusCode();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return code;
    }


public String adminLogin(String userName, String password)
{

    String status=null;
    StrictMode.ThreadPolicy policy = new
            StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    String UR = "http://192.168.1.1/admin?USERNAME="+userName+"&PASSWORD="+password;
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(UR);
    try {
        // Add your data
       /* List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        String size=Integer.toString(filesize);
        nameValuePairs.add(new BasicNameValuePair("fsize",size ));

        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);

     //   StatusLine status=    response.getStatusLine();
        HttpEntity hentity = response.getEntity();
        Header encodingHeader = hentity.getContentEncoding();
        String json = EntityUtils.toString(hentity);

        JSONObject obj = (JSONObject) JSONValue.parse(json);
       status = obj.get("admin").toString();

    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    }
    return status;
}


   // @RequiresApi(api = Build.VERSION_CODES.S)
    public int reflash() {
        System.out.println("reflash started :");
        int code=0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String UR = "http://192.168.1.1/reflash";
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(UR);

        try {
            // Add your data
          /*  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            String size=Integer.toString(filesize);
            nameValuePairs.add(new BasicNameValuePair("fsize",size ));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            StatusLine status=    response.getStatusLine();
            code=status.getStatusCode();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return code;
    }






   // @RequiresApi(api = Build.VERSION_CODES.S)
    public String hextobin (String sourceFileUri){

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String filename=null;


        String url = "http://ec2-65-0-102-137.ap-south-1.compute.amazonaws.com:3000/api/hextobin";
        File file = new File(sourceFileUri);
        try {
            HttpClient client = new DefaultHttpClient();
           HttpPost httppost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("title", new StringBody("position.csv", Charset.forName("UTF-8")));

            FileBody fileBody = new FileBody(file);
            entity.addPart("file", fileBody);
            httppost.setEntity(entity);

            HttpResponse response = client.execute(httppost);
            System.out.println(response);
            int responseCode = response.getStatusLine().getStatusCode();
            HttpEntity hentity = response.getEntity();
            Header encodingHeader = hentity.getContentEncoding();
            String json = EntityUtils.toString(hentity);
            System.out.println("response  :"+ json);
            JSONObject obj = (JSONObject) JSONValue.parse(json);
             filename = obj.get("fileName").toString();
        /*    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                        Charsets.toCharset(encodingHeader.getValue());
            }
*/


        } catch (Exception e) {
            // show error
        }



        return filename;
    }





  //  @RequiresApi(api = Build.VERSION_CODES.S)
    public String getRTS() {

            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String UR = "http://192.168.1.1/getstatus";
            String rts = null;

            try {

                URL urll = new URL(UR);
                HttpURLConnection connection = (HttpURLConnection) urll.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(40000);
                connection.setReadTimeout(40000);
                connection.setRequestProperty("Accept","*/*");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
int statusCode=connection.getResponseCode();
                if(connection.getResponseCode()==200) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());

                    String response = convertStreamToString(in);
                    System.out.println("response is :" + response);
                    String jsonString = response;
                    JSONObject obj = (JSONObject) JSONValue.parse(response);
                    String progress = obj.get("progress").toString();
                    String rtsvalue = obj.get("RTS").toString();
                        rts=progress+" "+rtsvalue;

                }
                else
                {
                    rts="NO RESPONSE";
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();;
                System.out.println("Error in getdata.java class getsin():" + e.getMessage());
            }
            return rts;
        }




    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
                System.out.println("the string is :" + sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();

    }


    public String DownloadFiles(String downloadfilename) {
        try {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            URL u = new URL("http://ec2-65-0-102-137.ap-south-1.compute.amazonaws.com:3000/api/bin/"+downloadfilename);
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

          //  FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + "data/test.kml"));
            FileOutputStream fos = new FileOutputStream(new File(folder+ File.separator+downloadfilename));

            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }

        } catch (MalformedURLException mue) {
          System.out.println(mue.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (SecurityException se) {
            System.out.println(se.getMessage());
        }
        System.out.println("Download Complete...........................");
        return "Download Complete";
    }

    public String UserLogin(String userName, String password) {
        String status = "Test";
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Format f = new SimpleDateFormat("M-d-yyyy");
        String strDate = f.format(new Date());


        System.out.println("user name :" + userName);
        System.out.println("password :" + password);
        String UR = "http://192.168.1.1/user?USERNAME=" + userName + "&PASSWORD=" + password + "&STATUS=1&DATE=" + strDate;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(UR);
        try {

            HttpResponse response = httpclient.execute(httppost);

            HttpEntity hentity = response.getEntity();
            Header encodingHeader = hentity.getContentEncoding();
            String json = EntityUtils.toString(hentity);

            JSONObject obj = (JSONObject) JSONValue.parse(json);
            status = obj.get("userlogin").toString();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return status;
    }

    public int stmflashing() {
System.out.println("in stm flashing");
        int status=0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String username="Thingsalive";
        String password="Thingsalive";
        String UR = "http://192.168.1.1/stm32-ota?USERNAME="+username+"&PASSWORD="+password;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(UR);

        try {

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusCode=response.getStatusLine();
            status=statusCode.getStatusCode();


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return status;
    }

    public int espflashing() {
        int status=0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String UR = "http://192.168.1.1/esp-ota?USERNAME=Thingsalive&PASSWORD=Thingsalive";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(UR);
        try {

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusCode=response.getStatusLine();
            status=statusCode.getStatusCode();


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return status;
    }

    public int confirmUpload() {
        int status=0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String UR = "http://192.168.1.1/upload?name='filename'";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(UR);
        try {

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusCode=response.getStatusLine();
            status=statusCode.getStatusCode();


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return status;
    }

    public int confirmUpdate() {
        int status=0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String UR = "http://192.168.1.1/update?name='filename'";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(UR);
        try {

            HttpResponse response = httpclient.execute(httppost);
            StatusLine statusCode=response.getStatusLine();
            status=statusCode.getStatusCode();


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return status;
    }
}
