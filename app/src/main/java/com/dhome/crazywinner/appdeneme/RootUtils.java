package com.dhome.crazywinner.appdeneme;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



class RootUtils {
    private static String TAG="KTsu";
    private static SU su;
    private static boolean issuworking=false;




    static boolean rooted() {
        return existBinary("su");
    }

    static boolean rootAccess() {
        SU su = getSU();
        su.runCommand("echo /testRoot/",true);
        return !su.denied;
    }

    public static boolean busyboxInstalled() {
        return existBinary("busybox");
    }

    private static boolean existBinary(String binary) {
        for (String path : System.getenv("PATH").split(":")) {
            if (!path.endsWith("/")) path += "/";

            if (new File(path + binary).exists()) return true;
        }
        return false;
    }




    static void runCommand(String command) {
        getSU().runCommand(command,false);
    }
    static String runCommandCallback(String command) {
        return getSU().runCommand(command,true);
    }
    private static SU getSU() {
        if(su==null)issuworking=true;
       if(issuworking){
           su=null;
           su=new SU();
           issuworking=false;
           Log.i("KTsu","Su initialized");

       }

        return su;
    }


    private static class SU {

        private Process process;
        private BufferedWriter bufferedWriter;
        private BufferedReader bufferedReader;
        private boolean closed;
        private boolean denied;

        SU() {
            try {

                process = Runtime.getRuntime().exec("su");

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            } catch (IOException e) {
                Log.e(TAG, "Failed to run shell as su");
                denied=true;
            }
        }

        synchronized String runCommand(final String command,boolean callbackagain) {

               issuworking=true;

            try {
                String callback = "/shellCallback/";
                bufferedWriter.write(command + "\necho " + callback + "\n");
                bufferedWriter.flush();
                if(callbackagain){
                StringBuilder sb = new StringBuilder();



                int i;
                char[] buffer = new char[256];
                while (true) {
                    sb.append(buffer, 0, bufferedReader.read(buffer));
                    if ((i = sb.indexOf(callback)) > -1) {
                        sb.delete(i, i + callback.length());
                        break;
                    }
                }
                return sb.toString().trim();}
            } catch (IOException e) {
                closed = true;
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                denied = true;
            } catch (Exception e) {
                e.printStackTrace();
            }




        issuworking=false;
            return null;


        }



    }
}
