import Utils.Parsers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

public class Application {

    public static void main(String[] args){

        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("configuration.properties");
            property.load(fis);
            String path = property.getProperty("path");
            Parsers.parsingСycle(path);
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

}
