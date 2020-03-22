package Utils.SplitMergeAndSortUtils;

import Utils.SplitMergeAndSortUtils.Sorts.QuickSort;

import java.io.*;
import java.util.Arrays;

public class SplittersFileUtil {

    private static String notFullLine = "";

    /**
     * Разбиение большого файла на более мелкие и их сортировка(применяется быстрая сортировка), отличается от первой части
     * сортировки слиянием, тем что здесь мы не доходим до одного элемента, а оставляем на наше усмотрение
     * @param pathToSourceFile путь до исходного файла
     * @param pathToSaveFile путь куда сохранять маленькие файлы
     * @param countSplits количество разбиений большого файла, в итоге получится countSplits+1 файл на выходе, ВНИМАНИЕ! ФАЙЛЫ ВРЕМЕННЫЕ! в конце останеться один большой файл
     * @throws Exception
     */
    public static void splitAndSortFile(String pathToSourceFile, String pathToSaveFile, long countSplits) throws Exception
    {
        RandomAccessFile raf = new RandomAccessFile(pathToSourceFile, "r");
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize/ countSplits;
        long remainingBytes = sourceSize % countSplits;

        int maxReadBufferSize = 8 * 1024; //8KB

        for(int destIx = 1; destIx <= countSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(pathToSaveFile+File.separatorChar+"split."+destIx));
            if(bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit/maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for(int i=0; i<numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if(numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            }else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();
        }
        if(remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(pathToSaveFile+File.separatorChar+"split."+(countSplits +1)));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }else{
            if(notFullLine!=null){
                BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(pathToSaveFile+File.separatorChar+"split."+(countSplits +1)));
                bw.write(notFullLine.getBytes());
                bw.close();
            }
            raf.close();
        }

    }

    /**
     * Метод чтения байтов и записи в файл.
     * Т.к мы не можем гарантировать что в процессе разбиения не разобьются сами ip адреса, данный метод
     * всегда забирает последнюю строку из текущей итерации и добавляет её в следующую, для хранения данной информации
     * используется notFullLine
     * @param raf файл откуда будем читать
     * @param bw файл куда будем записывать
     * @param numBytes количество байт
     * @throws IOException
     */
    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
            String s = new String(buf);
            s = notFullLine + s;
            String[] p = s.split("\n");
            if(p.length>0){
                if(p.length<2){
                    bw.write((p[0]+System.lineSeparator()).getBytes());
                }else{
                    notFullLine = p[p.length-1];
                    if(s.lastIndexOf("\n")==s.length()-1){
                        notFullLine+="\n";
                    }
                    p = Arrays.copyOf(p, p.length-1);
                    QuickSort.quickSort(p,0,p.length-1);
                    String joinedString = String.join(System.lineSeparator(), p);
                    bw.write(joinedString.getBytes());
                }
            }

        }

    }

}
