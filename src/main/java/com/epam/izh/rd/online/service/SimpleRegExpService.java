package com.epam.izh.rd.online.service;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleRegExpService implements RegExpService {

    /**
     * Метод должен читать файл sensitive_data.txt (из директории resources) и маскировать в нем конфиденциальную информацию.
     * Номер счета должен содержать только первые 4 и последние 4 цифры (1234 **** **** 5678). Метод должен содержать регулярное
     * выражение для поиска счета.
     *
     * @return обработанный текст
     */
    @Override
    public String maskSensitiveData() {
        URL resource = getClass().getClassLoader().getResource("sensitive_data.txt");
        File file = new File(resource.getFile());
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            content = reader.lines().collect(Collectors.joining());
            Matcher matcher = Pattern.compile("\\d{4}\\s*\\d{4}\\s*\\d{4}\\s*\\d{4}").matcher(content);

            while (matcher.find()) {
                String sensitiveData = matcher.group();
                String unSensitiveData = sensitiveData.replaceAll("\\s\\d{4}\\s*\\d{4}\\s*", " **** **** ");
                content = content.replace(sensitiveData, unSensitiveData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * Метод должен считыввать файл sensitive_data.txt (из директории resources) и заменять плейсхолдер ${payment_amount} и ${balance} на заданные числа. Метод должен
     * содержать регулярное выражение для поиска плейсхолдеров
     *
     * @return обработанный текст
     */
    @Override
    public String replacePlaceholders(double paymentAmount, double balance) {
        URL resource = getClass().getClassLoader().getResource("sensitive_data.txt");
        File file = new File(resource.getFile());
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            content = reader.lines().collect(Collectors.joining());

            content = content.replaceAll("\\$\\{(payment_amount)\\}", String.valueOf((long) paymentAmount));
            content = content.replaceAll("\\$\\{(balance)\\}", String.valueOf((long) balance));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}