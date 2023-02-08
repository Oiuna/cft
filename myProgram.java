package org.example;

import java.io.*;
import java.util.*;

public class myProgram {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] userData = args;

        if (args == null)
        {
            System.out.println("Введите попорядку через пробел тип данных (-i/-s), имя выходного файла (*.txt) и имена входных файлов (тоже *.txt) ");
            userData = scanner.nextLine().split(" ");
        }

        myProgram mp = new myProgram();
        mp.start(userData);
    }

    public void start(String[] userData) {

        try
        {
            //String[] userData;
            char dataType = ' ';
            String outputFileName = "";
            String[] inputFileNames;

            Scanner scanner = new Scanner(System.in);

/*
            do {
                System.out.println("Введите попорядку через пробел тип данных (-i/-s), имя выходного файла (*.txt) и имена входных файлов (тоже *.txt) ");
                userData = scanner.nextLine().split(" ");
            }
            while (userData.length < 3);


 */
            //Проверка ввода типа данных
            if (!(userData[0].equals("-i") || userData[0].equals("-s")))
            {
                do {
                    System.out.println("Кажется Вы забыли ввести тип данных. Попробуем еще раз? Введите -s или -i");
                    userData[0] = scanner.nextLine();
                }
                while (!(userData[0].equals("-i") || userData[0].equals("-s")));
            }

            if (userData[0].equals("-i"))
                dataType = 'i';
            else if (userData[0].equals("-s"))
                dataType = 's';


            //Проверка ввода имени выходного файла
            if (!userData[1].endsWith(".txt"))
            {
                do {
                    System.out.println("Жду имя выходного файла с расширением .txt");
                    userData[1] = scanner.nextLine();
                }
                while (!userData[1].endsWith(".txt"));
            }
            else
                outputFileName = userData[1];


            //Проверка ввода имени входного файла
            int k = 0;
            inputFileNames = new String[userData.length - 2];

            for (int i = 2; i < userData.length; i++)
            {
                if (!userData[i].endsWith(".txt"))
                {
                    do {
                        System.out.println("Это глупая программа, поэтому просьба переписать имя Вашего " + (i - 1) + "-ого/его входного файла с расширением .txt. Спасибо!");
                        userData[i] = scanner.nextLine();
                    }
                    while (!userData[i].endsWith(".txt"));
                }
                inputFileNames[k] = userData[i];
                k++;
            }

            //Обработка ненайденного файла
            int count = inputFileNames.length;

            Object[] inputFiles;
            Object[] output;

            if (openFile(inputFileNames[0]) == null)
            {
                do {
                    System.out.println( "У нас неполадки. Файл c именем " + inputFileNames[0] + " не найден. Введите имя существующего файла с раширением .txt");
                    inputFileNames[0] = scanner.nextLine();
                    output = openFile(inputFileNames[0]);
                }
                while (openFile(inputFileNames[0]) == null);
            }
            else
                output = openFile(inputFileNames[0]);


            for (int i = 1; i < count; i++)
            {
                if (openFile(inputFileNames[i]) == null)
                {
                    do {
                        System.out.println( "У нас неполадки. Файл c именем " + inputFileNames[i] + " не найден. Введите имя существующего файла с раширением .txt");
                        inputFileNames[i] = scanner.nextLine();
                        inputFiles = openFile(inputFileNames[i]);
                    }
                    while (openFile(inputFileNames[i]) == null);
                }
                else
                    inputFiles = openFile(inputFileNames[i]);

                //И наконец-то сортировка
                output = mergeSort(dataType, output, inputFiles);

            }

            if (saveFile(outputFileName, output))
                System.out.println( "Скорее проверьте результат работы в файле " + outputFileName);
            else System.out.println( "Что-то пошло не так. Файл не сохранен");
        }
        catch(Exception e)
        {

            System.out.print("Sorry, программа не сработала, потому что " + e.getMessage());
            e.printStackTrace();
        }

    }

    public Object[] mergeSort(char dataType, Object[] in1, Object[] in2)
    {
        Object[] out = new Object[in1.length + in2.length];
        int i = 0, j = 0;

        if (dataType == 'i')
        {

            for (int k = 0; k < out.length; k++)
            {
                if (i > in1.length - 1)
                {
                    out[k] = in2[j];
                    j++;
                }
                else if (j > in2.length - 1)
                {
                    out[k] = in1[i];
                    i++;
                }
                else if (Integer.valueOf((String) in1[i]) <= Integer.valueOf((String) in2[j]))
                {
                    out[k] = in1[i];
                    i++;
                }
                else if (Integer.valueOf((String) in1[i]) > Integer.valueOf((String) in2[j]))
                {
                    out[k] = in2[j];
                    j++;
                }
            }

        }
        if (dataType == 's')
        {
            for (int k = 0; k < out.length; k++)
            {
                if (i > in1.length - 1)
                {
                    out[k] = in2[j];
                    j++;
                }
                else if (j > in2.length - 1)
                {
                    out[k] = in1[i];
                    i++;
                }
                else if (in1[i].toString().length() <= in2[j].toString().length())
                {
                    out[k] = in1[i];
                    i++;
                }
                else if (in1[i].toString().length() > in2[j].toString().length())
                {
                    out[k] = in2[j];
                    j++;
                }
            }
        }

        return  out;
    }

    public Object[] openFile(String filename) {

        List<String> dataList = new ArrayList();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(".\\"+filename))) {
            while ((line = reader.readLine()) != null) {
                if (!line.contains(" "))
                    dataList.add(line);
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

        Object[] dataFile = dataList.toArray();
        return dataFile;
    }

    public Boolean saveFile(String filename, Object[] outData) {

        try (BufferedWriter writter = new BufferedWriter(new FileWriter(".\\"+filename))) {
            for (Object value : outData) {
                writter.write(value + "\n");
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}