package Utils.SplitMergeAndSortUtils.Sorts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MergeSort {

    /**
     * Слияние файлов попарно (вторая часть сортировки слиянием)
     * Кроме сортировки так же происходит отсев не уникальных, оставляя только одну их копию
     * @param nameFirst название/путь первого файла в формате String
     * @param nameSecond название/путь второго файла в формате String
     */
    public static void mergeArray(String nameFirst, String nameSecond) {

        try {
            BufferedReader readerFirstFile = Files.newBufferedReader(Paths.get(nameFirst));
            BufferedReader readerSecondFile = Files.newBufferedReader(Paths.get(nameSecond));


            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(nameFirst+"split.txt"));

            String wordFromFirstFile = readerFirstFile.readLine();
            String wordFromSecondFile = readerSecondFile.readLine();

            String elderFirstFile = "";
            String elderSecondFile = "";
            boolean flag = true;
            //пока флаг будет true продолжаем читать из файлов
            while(flag) {
                //как только файлы закончаться, то выходим из цикла
                if(wordFromFirstFile==null && wordFromSecondFile==null){
                    flag = false;
                }else{
                    //Обработка случая когда первый файл закончился раньше
                    if(wordFromFirstFile==null){
                        //elderSecondFile и elderFirstFile служат для запоминания предыдущей итерации, для сравнения с текущей
                        if(!wordFromSecondFile.equals(elderSecondFile)){

                            outputWriter.write(wordFromSecondFile);
                            outputWriter.newLine();
                            elderSecondFile = wordFromSecondFile;
                            wordFromSecondFile = readerSecondFile.readLine();

                        }else{
                            wordFromSecondFile = readerSecondFile.readLine();
                        }
                        //обработка случая когда второй файл закончился раньше
                    }else if(wordFromSecondFile==null){
                        if(!wordFromFirstFile.equals(elderFirstFile)){

                            outputWriter.write(wordFromFirstFile);
                            outputWriter.newLine();
                            elderFirstFile = wordFromFirstFile;
                            wordFromFirstFile = readerFirstFile.readLine();

                        }else{
                            wordFromFirstFile = readerFirstFile.readLine();
                        }

                    }else if(wordFromFirstFile.compareTo(wordFromSecondFile)<0){
                        if(!wordFromFirstFile.equals(elderFirstFile)){

                            outputWriter.write(wordFromFirstFile);
                            outputWriter.newLine();
                            elderFirstFile = wordFromFirstFile;
                            wordFromFirstFile = readerFirstFile.readLine();

                        }else{
                            wordFromFirstFile = readerFirstFile.readLine();
                        }

                    }else{
                        if(wordFromFirstFile.compareTo(wordFromSecondFile)>0){
                            if(!wordFromSecondFile.equals(elderSecondFile)){

                                outputWriter.write(wordFromSecondFile);
                                outputWriter.newLine();
                                elderSecondFile = wordFromSecondFile;
                                wordFromSecondFile = readerSecondFile.readLine();

                            }else{
                                wordFromSecondFile = readerSecondFile.readLine();
                            }

                        }else{
                            //обработка не уникальных результатов
                            if(wordFromFirstFile.equals(wordFromSecondFile)){
                                outputWriter.write(wordFromSecondFile);
                                outputWriter.newLine();
                                String elderNotUnique = wordFromFirstFile;
                                while (wordFromFirstFile.equals(wordFromSecondFile)){

                                    wordFromSecondFile = readerSecondFile.readLine();

                                    if(wordFromSecondFile==null){
                                        break;
                                    }
                                }
                                while(wordFromFirstFile.equals(elderNotUnique)){

                                    wordFromFirstFile = readerFirstFile.readLine();
                                    if(wordFromFirstFile==null){
                                        break;
                                    }

                                }

                            }
                        }
                    }

                }


            }
            readerFirstFile.close();
            readerSecondFile.close();
            outputWriter.flush();
            outputWriter.close();




        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
