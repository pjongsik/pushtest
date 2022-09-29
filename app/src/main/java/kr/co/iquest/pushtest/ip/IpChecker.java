package kr.co.iquest.pushtest.ip;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class IpChecker {
    private static final String TAG = "IpChecker";
    private static String publicIp = "";
    private static String localIp = "";

    public static String getPublicIp() {
        if ("".equals(publicIp)) {
            getIpInfo();
        }
        return publicIp;
    }

    private static void getIpInfo() {
        try
        {
            InetAddress localhost = InetAddress.getLocalHost();
            localIp = localhost.getHostAddress();
           // System.out.println("Local IP Address = " + localIp);
            Log.d(TAG,"Local IP Address = " + localIp);
        }
        catch(Exception e)
        {
          //  System.out.println("Exception: " +e);
            Log.e(TAG,"Exception = " + e.getMessage());
        }
        try
        {
            URL ipfinder = new URL("https://checkip.amazonaws.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(ipfinder.openStream()));
            publicIp = br.readLine();
          //  System.out.println("Public IP Address = " + publicIp);
            Log.d(TAG,"Public IP Address = " + publicIp);
        }
        catch(Exception e)
        {
          //  System.out.println("Exception: " +e);
            Log.e(TAG,"Exception = " + e.getMessage());
        }
    }
}
