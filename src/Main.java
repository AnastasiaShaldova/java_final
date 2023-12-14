import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            makeRecord();
            System.out.println("Успех 👍");
        } catch (FileSystemException e) {
            System.out.println("Ошибка файловой системы: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    public static void makeRecord() throws Exception {
        System.out.println("Введите ФИО, Дату рождения, Номер телефона (пример ввода: 999-888-7766) и Пол (f или m), разделенные пробелом: ");

        String text;
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
            text = bf.readLine();
        } catch (IOException e) {
            throw new Exception("Ошибка чтения ввода: У вас сломалась консоль! 😢");
        }

        String[] array = text.split(" ");
        if (array.length != 6) {
            throw new Exception("Ошибка: Вы ввели неверное количество параметров! 😕");
        }

        String surname = array[0];
        String name = array[1];
        String patronymic = array[2];

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date birthdate;
        try {
            birthdate = format.parse(array[3]);
        } catch (ParseException e) {
            throw new ParseException("Ошибка парсинга даты рождения. Введите дату в формате dd.MM.yyyy! 😕", e.getErrorOffset());
        }

        String phoneNumber = array[4];

        String sex = array[5];
        if (!sex.toLowerCase().equals("m") && !sex.toLowerCase().equals("f")) {
            throw new RuntimeException("Неверно введен пол! 😕");
        }

        String directoryName = "files";
        String fileName = directoryName + File.separator + surname.toLowerCase() + ".txt";

        Path directoryPath = Paths.get(directoryName);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new FileSystemException("Ошибка создания директории! 😕");
            }
        }

        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            if (file.length() > 0) {
                fileWriter.write('\n');
            }
            fileWriter.write(String.format("%s %s %s %s %s %s", surname, name, patronymic, format.format(birthdate), phoneNumber, sex));
        } catch (IOException e) {
            throw new FileSystemException("Ошибка при работе с файлом! 😕");
        }
    }
}