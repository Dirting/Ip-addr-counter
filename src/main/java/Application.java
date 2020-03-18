import Utils.BigParser;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Application {

    public static void main(String[] args){

        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("configuration.properties");
            property.load(fis);
            String path = property.getProperty("path");
            String savePath = property.getProperty("save_path");
            long countRowForFile = Long.valueOf(property.getProperty("count_row_for_file"));

            File file = new File(path);
            if(file.exists()){
                long freeSpace = new File(savePath).getFreeSpace();
                if(freeSpace<file.length()){
                    System.out.println("Не хватает места на диске");
                }else{
                    long beforeTime = System.currentTimeMillis();
                    long countPath = BigParser.SplitBigFile(path,savePath,countRowForFile);

                    //Проверка на количество файлов, если файл один, то обрабтка результата ненужна
                    //т.к результат выводится сразу в методе CompareAndGetUniqueStringFromFiles
                    //p.s switch не получилось реализовать потому что countPath имеет тип long
                    if(countPath>1){
                        BigParser.CompareAndGetUniqueStringFromFiles(countPath,countRowForFile,savePath);
                        BigParser.resultCountDistinctRows(savePath);
                    }else{
                        if(countPath>0){
                            BigParser.CompareAndGetUniqueStringFromFiles(countPath,countRowForFile,savePath);
                        }else {
                            System.out.println("Количество строк = 0");
                        }

                    }

                    long afterTime = System.currentTimeMillis();
                    System.out.println("Время исполнения = "+(afterTime - beforeTime) + " ms.");

                }
            }else{
                System.out.println("Файла не существует");
            }




        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

}
