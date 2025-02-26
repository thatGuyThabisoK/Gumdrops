package com.candy.gumdrops;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import androidx.appcompat.app.AlertDialog;


public class JavaHotMail extends AsyncTask {


    private Context context;
    private Session session;
    private String email,message,password;
    private Handler handler;
    private ArrayList<File> picture;

    private String GumdropsEmail = "candy@gumdrops.co.za";

    public JavaHotMail(Context myContext, String myEmail,String pass, String message, ArrayList<File> photo){

        password = pass;
        context = myContext;
        email = myEmail;
        this.message = message;
        handler = new Handler();
        picture = photo;

    }


    private void send_Email(){

        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");
        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        });

        try{
            MimeMessage myMessage = new MimeMessage(session);
            myMessage.setFrom(new InternetAddress(email));
            myMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(GumdropsEmail));
            myMessage.setSubject("New Picture from a user");

            BodyPart messageB = new MimeBodyPart();
            messageB.setText(message);

            Multipart attachments = new MimeMultipart();
            attachments.addBodyPart(messageB);
           // DataSource source = new FileDataSource(picture.get(index).getPath());
            //myMessage.setDataHandler(new DataHandler(source));
           // myMessage.setFileName("Beautiful_photo_"+index);

            for(int index = 0;index < picture.size();++index){
                MimeBodyPart file = new MimeBodyPart();
                try {
                    file.attachFile(picture.get(index).getPath());
                }catch (IOException E){
                    E.printStackTrace();
                }
                attachments.addBodyPart(file);
            }
            myMessage.setContent(attachments);



            Transport.send(myMessage);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    DashBoard.myPhotos = new ArrayList<>();
                }
            });

        }catch (MessagingException e){

            if(e.toString().contains("AuthenticationFailedException")){

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        new AlertDialog.Builder(context)
                                .setTitle("ALERT!!")
                                .setMessage("We have encountered an error while trying to sent picture/s to our team\nPlease make sure the password you " +
                                        "used is the linked to your email address")
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();



                        Toast.makeText(context, "FAILED TO SEND MESSAGE", Toast.LENGTH_SHORT).show();
                    }
                });


                //Log.e("--------------","");
            }

            e.printStackTrace();
        }

    }


    @Override
    protected Object doInBackground(Object[] objects) {

        send_Email();

        return null;
    }
}
