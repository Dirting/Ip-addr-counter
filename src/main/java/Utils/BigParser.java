package Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BigParser {


    public static long SplitBigFile(String path){
        long numberFiles = 0;
        try{

            AtomicLong countRow = new AtomicLong();
            Boolean flag = false;
            AtomicLong countRowInFile = new AtomicLong(999);

                ArrayList<String> resultForFile = new ArrayList<>();
                while(!flag){
                        long skipCount = numberFiles*5;
                        BufferedWriter outputWriter = new BufferedWriter(new FileWriter("splitedFiles/"+numberFiles+"split.txt"));
                        Files.lines(Paths.get(path)).skip(skipCount).limit(5).distinct().forEach(ipIddr ->{
                            try {
                                outputWriter.write(ipIddr);
                                outputWriter.newLine();
                                countRow.getAndIncrement();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        if(countRow.get() ==0){
                            File file = new File("splitedFiles/"+numberFiles+"split.txt");
                            if(file.exists()){
                                outputWriter.flush();
                                outputWriter.close();
                                file.delete();
                            }
                            flag=true;
                        }else{
                            outputWriter.flush();
                            outputWriter.close();
                            numberFiles++;
                            countRow.set(0);
                        }
                }



        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return numberFiles;
    }

    public static void CompareAndGetUniqueStringFromFiles(long countFiles){

        try{
            Stream<String> resultStream = null;
            for(long i = 0; i<countFiles-1; i++){
                Stream<String> iStream = Files.lines(Paths.get("splitedFiles/"+i+"split.txt"));
                for(long j = i+1; j<countFiles; j++){
                    Stream<String> jStream = Files.lines(Paths.get("splitedFiles/"+j+"split.txt"));
                    resultStream = Stream.concat(iStream,jStream).distinct();
                    ArrayList<String> arrayListIpAddr = getArrayListFromStream(resultStream);
//                    iStream = null;
//                    jStream = null;
                    File iFile = new File("splitedFiles/"+i+"split.txt");
                    File jFile = new File("splitedFiles/"+j+"split.txt");
                    if(iFile.exists()){
                        iFile.delete();
                    }
                    if(jFile.exists()){
                        jFile.delete();
                    }
                    long countRowResult = arrayListIpAddr.size();
                    BufferedWriter iWriter = new BufferedWriter(new FileWriter("splitedFiles/"+i+"split.txt"));
                    BufferedWriter jWriter = new BufferedWriter(new FileWriter("splitedFiles/"+j+"split.txt"));
                    long countIter = 0;
                    for(String oneIppr : arrayListIpAddr) {

                        if(countRowResult > 1){
                            if(countIter <= countRowResult / 2){
                                iWriter.write(oneIppr);
                                iWriter.newLine();
                                countIter++;
                            }else{
                                jWriter.write(oneIppr);
                                jWriter.newLine();
                                countIter++;
                            }
                        }else{
                            iWriter.write(oneIppr);
                            iWriter.newLine();
                        }

                    }
                    iWriter.flush();
                    jWriter.flush();
                    iWriter.close();
                    jWriter.close();
//                    if(countRowResult==1){
//                        iWriter.flush();
//                        iWriter.close();
//                        iWriter.close();
//                        jWriter.close();
//                    }else{
//                        iWriter.flush();
//                        jWriter.flush();
//                        iWriter.close();
//                        jWriter.close();
//                    }
//                    if(i==countFiles){
//                        resultStream.close();
//                        iWriter.close();
//                        jWriter.close();
//                        iStream.close();
//                        jStream.close();
//                    }
//                    resultStream = null;

                    iStream = Files.lines(Paths.get("splitedFiles/"+i+"split.txt"));
                }
            }


        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

    public static long resultCountDistinctRows(long countFiles){

        long resultCount = 0;

        for(long i = 0; i<countFiles; i++){
            try {
                resultCount = resultCount + Files.lines(Paths.get("splitedFiles/"+i+"splited.txt")).count();
                File file = new File("splitedFiles/"+i+"splited.txt");
                if(file.exists()){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(resultCount);
        return resultCount;
    }


    public static long renamerFiles(){

        List<Path> filesPaths = null;
        long result = 0;
        try {
            filesPaths = Files.list(Paths.get("./splitedFiles")).collect(Collectors.toList());
            for(int i = 0; i<filesPaths.size(); i++){
                File file = new File(filesPaths.get(i).toString());
                File newFile = new File("splitedFiles/"+i+"splited.txt");
                file.renameTo(newFile);

            }
            result = filesPaths.size()-1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }


    public static <T> ArrayList<T>
    getArrayListFromStream(Stream<T> stream)
    {

        // Convert the Stream to List
        List<T> list = stream.collect(Collectors.toList());

        // Create an ArrayList of the List

        // Return the ArrayList
        return new ArrayList<T>(list);
    }

}
