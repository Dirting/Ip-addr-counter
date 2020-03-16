import Utils.BigParser;
import Utils.Parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    public static void main(String[] args){

        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("configuration.properties");
            property.load(fis);
            String path = property.getProperty("path");

            long countPath = BigParser.SplitBigFile(path);
            BigParser.CompareAndGetUniqueStringFromFiles(countPath,5);
            long countFiles = BigParser.renamerFiles();
            BigParser.resultCountDistinctRows(countFiles);
//            Parsers.parsingСycle(path);
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

}
