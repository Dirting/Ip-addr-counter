package Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Дополнительные решения, если имеется ОЧЕНЬ много не физической памяти
 */
public class Parsers {

    /**
     * Дополнительное решение.
     * Метод с использованием Stream API потребляем очень много памяти, но решение в одну строку.
     * @param path String путь до файла
     */
    public static void parsingStream(String path){
        try {
            long beforeTime = System.currentTimeMillis();
            long count = Files.lines(Paths.get(path)).parallel().distinct().count();
            long afterTime = System.currentTimeMillis();
            System.out.println("Количество строк = "+count);
            System.out.println("Время исполнения = "+(afterTime - beforeTime) + " ms.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Дополнительное решение.
     * Так же считываем Stream и кладём уникальные значения в TreeMap, но внутри дополнительная проверка
     * @param path String путь до файла
     */
    public static void parsingСycle(String path){
        try{
            //Т.к мы не знаем сколько всего элементов в нашем файле,
            //и файл очень большой, выбор пал на TreeMap, если бы мы знали общее количество строк в файле
            //то можно было бы использовать HashMap. Конечно если есть много памяти можно взять HashMap и увеличить скорость
            //но нам нужна более стабильная система
            Map<String, Boolean> resMap = new TreeMap<>();
            long beforeTime = System.currentTimeMillis();
            Files.lines(Paths.get(path)).forEach(ipAddr -> {
                if(!resMap.containsKey(ipAddr)){
                    resMap.put(ipAddr,null);
                }

            });

            long afterTime = System.currentTimeMillis();
            System.out.println("Количество строк = "+resMap.size());
            System.out.println("Время исполнения = "+(afterTime - beforeTime) + " ms.");
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }



}
