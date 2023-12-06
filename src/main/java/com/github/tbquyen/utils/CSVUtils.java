package com.github.tbquyen.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVUtils {
  public static String TYPE = "text/csv";

  public static boolean hasCSVFormat(MultipartFile file) {
    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public static <T extends Object> List<T> parseCsv(Class<T> type, MultipartFile file, String[] HEADERs)
      throws Exception {
    InputStream is = file.getInputStream();
    List<T> tutorials = new ArrayList<T>();
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder().setHeader(HEADERs).build());) {

      Class<?> cls = type.getClass();
      Field fieldlist[] = cls.getDeclaredFields();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      for (CSVRecord csvRecord : csvRecords) {
        type.getDeclaredConstructor().newInstance();
        T tutorial = type.getDeclaredConstructor().newInstance();

        for (Field field : fieldlist) {
          field.set(tutorial, csvRecord.get(field.getName()));
        }

        tutorials.add(tutorial);
      }
    }

    return tutorials;
  }
}
