import java.io.*;
import java.util.*;


public class InputOutput {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        File filePhrase = new File("files/phrase.txt");
        File fileBlock1 = new File("files/block1.txt");
        File fileBlock2 = new File("files/block2.txt");
        File fileBlock3 = new File("files/block3.txt");
        File fileBlock4 = new File("files/block4.txt");
        File fileBlock5 = new File("files/block5.txt");
        //file.mkdir();
        File fileBook = new File("files/И.Т. Мищенко + 4 копии.txt");
        //Задание 1
        printByteArrayFromFileToConsole(filePhrase);
        //Задание 2
        combineFiles(fileBlock1, fileBlock2, fileBlock3, fileBlock4, fileBlock5);
        //Задание 3
        startConsoleReader(fileBook);
    }

    public static void printByteArrayFromFileToConsole(File file){
        System.out.println("Размер файла: " + file.length() + " байт");
        byte[] bufByte = new byte[(int) file.length()];
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8")){
            int x;
            System.out.println("Вывод содержимого файла:");
            while ((x = isr.read()) != -1) {
                System.out.print((char) x);
            }

            FileInputStream fis = new FileInputStream(file);
            fis.read(bufByte);
            System.out.println("\nПечать байтового массива в диапазоне -128 до 127:");
            System.out.println(Arrays.toString(bufByte));
        } catch (IOException e){
            e.printStackTrace();
        }

        ByteArrayInputStream in = new ByteArrayInputStream(bufByte);
        int x;
        System.out.println("Печать байтового массива в диапазоне int от 0 до 255:");
        while ((x = in.read()) != -1) {
            System.out.print(x + " ");
        }
    }

    public static void combineFiles(File... files){
        long t = System.currentTimeMillis();
        ArrayList<InputStream> arrayList = new ArrayList<>();
        try {
            for (File f: files){
                arrayList.add(new FileInputStream(f));
            }
            SequenceInputStream sequenceInputStream = new SequenceInputStream(Collections.enumeration(arrayList));
            int x;
            File outFile = new File("files/combineFile.txt");
            //Пришлось ограничить БуферАутСтрим на 512, иначе не записывает мелкие файлы.
            OutputStream out = new BufferedOutputStream(new  FileOutputStream(outFile), 512);
            while ((x = sequenceInputStream.read()) != -1) {
                out.write(x);
            }
            sequenceInputStream.close();
            System.out.println("\nСоздан файл: " + outFile.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis()-t)/1000 + "сек.");
    }

    /*public static void consoleReaderNotWorkingCorrectly(File file, int page){
        long t = System.currentTimeMillis();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            for (int i = page; i < page + 1800; i++) { //даже не стоит заморачиваться с интервалами, не кодирует символы метод
                randomAccessFile.seek(i); //shit а не метод
                System.out.print((char) randomAccessFile.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n" + "Время печати страницы: " + (System.currentTimeMillis()-t)/1000 + "сек.");
    }*/

    public static void startConsoleReader(File file){
        //решено не делать через RandomAccessFile, так как не кодирует кириллицу и нелогично делит страницы по символам,
        //что может привести к нечитаемости страницы(деление слова) и неравномерной ширине страницы. В общем задание
        //оцениваю плохо. Формулировка "за страницу можно принять 1800 символов" не катируется.
        int page = 0;
        int check = 0;
        System.out.println("\nКоличество строк в текущем докумете: " + getCountOfLines(file));
        int pages = (int) Math.ceil(getCountOfLines(file) / 50f);
        System.out.println("Количество страниц в текущем докумете: " + pages);
        do {
            System.out.print("Введите номер страницы: ");
            page = getCheckedInput(pages);
            printPage(file, page);
            System.out.print("Введите 1 для выхода из программы, 2 для ввода следующей страницы: ");
            check = getCheckedInput(2);
        } while (check != 1);
    }

    public static int getCountOfLines(File file){
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((reader.readLine()) != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void printPage (File file, int page){
        System.out.println("-----Страница" + page + "-----");
        page--;                                             //декремент для передачи на подсчет страниц для скипа
        long t = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str;
            reader.skip(getCountToSkip(file, page));
            for (int i = 0; i < 50; i++){   //выводим страницу
                str = reader.readLine();
                if (str != null){
                    System.out.println(str);
                } else {
                    System.out.println("---Конец документа---");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Печать страницы выполнена за " + (double)(System.currentTimeMillis()-t)/1000 + "сек.");
    }

    public static int getCountToSkip(File file, int page){
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < page * 50; i++){
                    count = count + reader.readLine().length() + 2; //+2 символа на перевод каретки
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    static int getCheckedInput(int limit){
        int check;
        do {
            if (scanner.hasNextInt()) {             //предотвращение ввода не целого числа
                check = scanner.nextInt();
                if (check >= 1 && check <= limit)       //проверка на допустимый диапазон ввода
                    return check;
            }
            System.out.print("Нужно ввести число от 1 до " + limit + ":  ");
            scanner.nextLine();
        } while (true);
    }

}
