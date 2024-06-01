package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {
    static Properties properties;
    static String path="C:\\Users\\ssathi\\IdeaProjects\\RestAssured_CRUD\\src\\test\\resources\\Config\\config.properties";
    static
    {
        properties=new Properties();
    }


    public static void readConfig() {
        try {
            FileInputStream fis = new FileInputStream(path);
            properties.load(fis);
//            fis.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getdebugmode() {
        readConfig();
        String value= properties.getProperty("debugMode");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("debugMode is not specified in config File");
        }
    }
    public static String getAzureBookCart_url() {
        readConfig();
        String value= properties.getProperty("AzureBookCart_url");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("AzureBookCart URL not specified in config File");
        }
    }

    public static String getEcommerce_url() {
        readConfig();
        String value= properties.getProperty("Ecommerce_url");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("Ecommerce URL not specified in config File");
        }
    }

    public static String getReqres_url() {
        readConfig();
        String value= properties.getProperty("Reqres_url");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("Ecommerce URL not specified in config File");
        }
    }
    public static String getRestfulBooker_url() {
        readConfig();
        String value= properties.getProperty("RestfulBooker_url");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("Ecommerce URL not specified in config File");
        }
    }
    public static String getRSAMaps_url() {
        readConfig();
        String value= properties.getProperty("RSAMaps_url");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("Ecommerce URL not specified in config File");
        }
    }
    public static String getStoreRestAPI_url() {
        readConfig();
        String value= properties.getProperty("StoreRestAPI_url");
        if(value!=null)
        {
            return value;
        }
        else
        {
            throw new RuntimeException("Ecommerce URL not specified in config File");
        }
    }
}
