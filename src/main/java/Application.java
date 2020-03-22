import Utils.BigParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static Utils.SplitMergeAndSortUtils.ExternalSort.mergeAllFiles;
import static Utils.SplitMergeAndSortUtils.SplittersFileUtil.splitAndSortFile;


public class Application {

    public static void main(String[] args){

        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("configuration.properties");
            property.load(fis);
            String path = property.getProperty("path");
            String savePath = property.getProperty("save_path");
            long countSplits = Long.valueOf(property.getProperty("count_splits"));

            File file = new File(path);
            if(file.exists()){
                long freeSpace = new File(savePath).getFreeSpace();
                if(freeSpace<file.length()*2){
                    System.out.println("Не хватает места на диске");
                }else{
                    file = null;
                    long beforeTime = System.currentTimeMillis();
                    //разбиваем и сортируем
                    splitAndSortFile(path,savePath,countSplits);
                    //сливаем, сортируем и отбираем уникальные
                    mergeAllFiles(savePath,countSplits);

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
