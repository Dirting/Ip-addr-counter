package Utils.SplitMergeAndSortUtils;

import Utils.SplitMergeAndSortUtils.Sorts.MergeSort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalSort {

    private static long countUniqueAddr = 0;

    /**
     * Метод для считывания файлов и запускающий процесс слияния попарно
     * @param pathToSaveFile путь куда сохранять файлы
     * @param countSplits количество разбиений, нужно для метода resultUniqueCount, который вызывается внутри
     */
    public static void mergeAllFiles(String pathToSaveFile, long countSplits) {
        try {
            List<Path> allFiles = Files.list(Paths.get(pathToSaveFile)).collect(Collectors.toList());
            while (allFiles.size() > 1) {
                if (allFiles.size() <= 3) {
                    MergeSort.mergeArray(allFiles.get(0).toString(), allFiles.get(1).toString());
                    Files.delete(allFiles.get(0));
                    Files.delete(allFiles.get(1));
                } else {
                    if (allFiles.size() % 2 == 0) {

                        for (int i = 0; i < allFiles.size(); i += 2) {

                            MergeSort.mergeArray(allFiles.get(i).toString(), allFiles.get(i + 1).toString());
                            Files.delete(allFiles.get(i));
                            Files.delete(allFiles.get(i + 1));
                        }

                    } else {

                        for (int i = 0; i < allFiles.size() - 1; i += 2) {

                            MergeSort.mergeArray(allFiles.get(i).toString(), allFiles.get(i + 1).toString());
                            Files.delete(allFiles.get(i));
                            Files.delete(allFiles.get(i + 1));
                        }
                    }
                }

                allFiles = Files.list(Paths.get(pathToSaveFile)).collect(Collectors.toList());


            }
            resultUniqueCount(allFiles.get(0).toString(),countSplits);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Подсчёт уникальных адресов В УЖЕ ГОТОВОМ ФАЙЛЕ, аналогичен методу слияния файлов, только без записи, с читой пробежкой по файлу
     * @param nameFile название/путь файла в котором нужно подсчитать уникальные адреса
     * @param countSplits количество разбиений
     */
    public static void resultUniqueCount(String nameFile, long countSplits) {
        try {
            RandomAccessFile raf = new RandomAccessFile(nameFile, "r");
            long sourceSize = raf.length();
            long bytesPerSplit = sourceSize / countSplits;
            long remainingBytes = sourceSize % countSplits;

            int maxReadBufferSize = 8 * 1024; //8KB

            for (int destIx = 1; destIx <= countSplits; destIx++) {
                if (bytesPerSplit > maxReadBufferSize) {
                    long numReads = bytesPerSplit / maxReadBufferSize;
                    long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                    for (int i = 0; i < numReads; i++) {
                        summUnique(raf,maxReadBufferSize);
                    }
                    if (numRemainingRead > 0) {
                        summUnique(raf,numRemainingRead);
                    }
                } else {
                    summUnique(raf,bytesPerSplit);
                }
                if (remainingBytes > 0) {
                    summUnique(raf,remainingBytes);
                }
            }
            System.out.println("Количество уникальных строк: " +countUniqueAddr);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

    /**
     * Подсчёт элементов на каждой итерации, подсчёт ведётся исходя из количества символа \n
     * @param raf файл откуда считываем
     * @param numBytes количество байтов
     */
    public static void summUnique(RandomAccessFile raf, long numBytes){
        try {
            byte[] buf = new byte[(int) numBytes];
            int val = raf.read(buf);
            if(val != -1) {
                String s = new String(buf);
                countUniqueAddr+=s.chars().filter(ch -> ch == '\n').count();
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

}