package Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * основной класс парсинга
 * возмножно не самый быстрый метод, но имеет плюс в том, что может работать практически на любых количествах не физической памяти.
 * Физической памяти на момент парсинга нужно на исходный файл и на маленькие файлы ещё чуть больше чем требует исходник, которые потом удалятся.
 * p.s Под физической памятью подразумеваются носители HDD и SSD
 */
public class BigParser {

    /**
     * Метод разбиения большого файла на более маленькие
     * @param path путь к файлу в формате String
     * @param pathToSaveFile путь куда сохранять маленькие файлы в формате String
     * @param countRowForFile количество строк в каждом файле в формате long
     * @return возвращает количество созданных файлов
     */
    public static long SplitBigFile(String path, String pathToSaveFile, long countRowForFile){
        long numberFiles = 0;
        long fileCount = 0;
        try{

            long countRowBuffer = 0;


            BufferedReader reader = Files.newBufferedReader(Paths.get(path));

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(pathToSaveFile+File.separatorChar+numberFiles+"split.txt"));

            String line;
            while((line = reader.readLine()) != null) {
                outputWriter.write(line);
                outputWriter.newLine();
                countRowBuffer++;
                if(countRowBuffer>=countRowForFile){
                    numberFiles++;
                    outputWriter.flush();
                    outputWriter.close();
                    outputWriter = new BufferedWriter(new FileWriter(pathToSaveFile+File.separatorChar+numberFiles+"split.txt"));
                    countRowBuffer=0;
                }
            }
            outputWriter.flush();
            outputWriter.close();
            fileCount = Files.list(Paths.get(pathToSaveFile)).collect(Collectors.toList()).size();
            reader.close();


        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return fileCount;
    }

    /**
     * Метод сравнивающий каждый файл с каждым и оставляющий только уникальные значения
     * @param countFiles количество файлов в формате long
     * @param countRowInOneFile количество строк в одном файле в формате long
     * @param pathToSaveFile путь где лежат маленькие файлы и куда сохранять итоговые файлы в формате String
     */
    public static void CompareAndGetUniqueStringFromFiles(long countFiles, long countRowInOneFile, String pathToSaveFile){

        try{
            //Проверяем на количество файлов
            if(countFiles>1){
                Stream<String> resultStream = null;
                //Первый цикл по всем файлам где i - это номер файла
                for(long i = 0; i<countFiles; i++){
                    //Считываем первый файл в поток
                    Stream<String> iStream = Files.lines(Paths.get(pathToSaveFile+File.separatorChar+i+"split.txt"));
                    //Второй цикл по всем файлам где j - это номер файла
                    for(long j = 0; j<countFiles; j++){
                        //Пропускаем в тот момент когда оба циклы останавливаеются на одном файле
                        if(i!=j){

                            //Считываем второй файл в поток
                            Stream<String> jStream = Files.lines(Paths.get(pathToSaveFile+File.separatorChar+j+"split.txt"));

                            //Объединяем потоки и оставляем только одинаковые значения
                            resultStream = Stream.concat(iStream,jStream).distinct();

                            //Превращаем результирующий поток в ArrayList
                            ArrayList<String> arrayListIpAddr = getArrayListFromStream(resultStream);

                            //Обнуляем потоки
                            iStream = null;
                            jStream = null;

                            //Удаляем первичные файлы для экономии места
                            File iFile = new File(pathToSaveFile+File.separatorChar+i+"split.txt");
                            File jFile = new File(pathToSaveFile+File.separatorChar+j+"split.txt");

                            if(iFile.exists()){
                                iFile.delete();
                            }
                            if(jFile.exists()){
                                jFile.delete();
                            }

                            //Получаем итоговый размер результирующего списка
                            long countRowResult = arrayListIpAddr.size();

                            BufferedWriter iWriter = new BufferedWriter(new FileWriter(pathToSaveFile+File.separatorChar+i+"split.txt"));
                            BufferedWriter jWriter = new BufferedWriter(new FileWriter(pathToSaveFile+File.separatorChar+j+"split.txt"));

                            long countIter = 0;
                            //Цикл по результирующему списку, если строк в итоге меньше или равно количеству в одном файле
                            //то записываем всё в один файл, иначе до середины записываем в один файл, а после в другой
                            for(String oneIppr : arrayListIpAddr) {

                                if(countRowResult >= countRowInOneFile){
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

                            //Т.к файлы изменились нам нужно переопределить поток
                            iStream = Files.lines(Paths.get(pathToSaveFile+File.separatorChar+i+"split.txt"));
                        }

                    }
                }
            }else{
                //если файл всего один, то сразу обрабатываем и выводим количество
                Stream<String> streamIpAddr = Files.lines(Paths.get(pathToSaveFile+File.separatorChar+0+"split.txt")).distinct();
                File file = new File(pathToSaveFile+File.separatorChar+0+"split.txt");
                if(file.exists()){
                    file.delete();
                }
                System.out.println("Количество строк = "+streamIpAddr.count());
            }



        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

    /**
     * Итоговый подсчёт количества уникальных строк
     * @param pathToSaveFile путь где лежат файлы в формате String
     */
    public static void resultCountDistinctRows(String pathToSaveFile){

        long resultCount = 0;

        List<Path> filesPaths = null;

        try {
            filesPaths = Files.list(Paths.get(pathToSaveFile)).collect(Collectors.toList());
            for(int i = 0; i<filesPaths.size(); i++){
                resultCount = resultCount + Files.lines(Paths.get(filesPaths.get(i).toString())).count();
                File file = new File(filesPaths.get(i).toString());
                file.delete();

            }
            System.out.println("Количество строк = "+resultCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Дженерик метод превращения потока в ArrayList
     * @param stream поток в формате Stream<T>
     * @param <T> тип потока
     * @return Поток в формате ArrayList
     */
    public static <T> ArrayList<T> getArrayListFromStream(Stream<T> stream)
    {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

}
