package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        JarFile jarFile = new JarFile("C:\\\\Users\\\\tenz0\\\\IdeaProjects\\\\main1\\\\src\\\\main\\\\java\\\\org\\\\example\\\\FXKu.jar");
        Enumeration entries = jarFile.entries();

        ArrayList<String> controllerPaths = new ArrayList<>();
        ArrayList<String> otherPaths = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String name = entry.getName();
            if (name.endsWith("Controller.java")) {
                if (checkValid(name, "Table<([^>]+)>\\s+([^;]+);")) {
                    controllerPaths.add(name);
                    linksMap(name);
                }
            } else {
                otherPaths.add(name);
            }
        }
        jarFile.close();
    }

    private static boolean checkValid(String path, String regex) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\tenz0\\IdeaProjects\\main1\\src\\main\\result\\FXKu\\" + path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void linksMap(String path) {
        String targetPattern = "private";
        HashMap<String, ArrayList> linkRatio = new HashMap<>();
        List<String> contents = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/tenz0/IdeaProjects/main1/src/main/result/FXKu/" + path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(targetPattern)) {
                    Pattern pattern = Pattern.compile("@Table\\(\n\s+name = \".+\"");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String content = matcher.group(1);
                        contents.add(content);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(contents);
    }
}

//        private static void extractParameters(String path) {
//        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\tenz0\\IdeaProjects\\main1\\src\\main\\result\\FXKu\\" + path))) {
//            String line;
//            String directoryPath = path.substring(0, path.lastIndexOf("/") + 1);
//            System.out.println(directoryPath);
//
//            ArrayList<String> tablePath = new ArrayList<>();
//            while ((line = reader.readLine()) != null) {
//                String regex = "Table<([^>]+)>\\s+([^;]+);";
//                Pattern pattern = Pattern.compile(regex);
//                Matcher matcher = pattern.matcher(line);
//                if (matcher.find()) {
//                    String parameterType = matcher.group(1).replaceAll("\\s", "");
//                    String parameterName = matcher.group(2);
//                    tablePath.add("C:/Users/tenz0/IdeaProjects/main1/src/main/result/FXKu/" + directoryPath + parameterType );
////                    System.out.println(parameterType);
//
//                    String filePath = ("C:/Users/tenz0/IdeaProjects/main1/src/main/result/FXKu/" + directoryPath + parameterType + ".class").replaceAll("/source", "");;
////                    System.out.println(filePath);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void extractNameAndQuery(String filePath) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            String tableName = null;
//            String query = null;
//            while ((line = reader.readLine()) != null) {
//                if (tableName == null) {
//                    String tableNameRegex = "@Table\\(\n\s+name = \".+\"";
//                    Pattern tableNamePattern = Pattern.compile(tableNameRegex);
//                    Matcher tableNameMatcher = tableNamePattern.matcher(line);
//                    if (tableNameMatcher.find()) {
//                        tableName = tableNameMatcher.group(1);
//                        System.out.println("Table Name: " + tableName);
//                    }
//                }
//
//                if (query == null) {
//                    String queryRegex = "@NamedNativeQuery\\(\n\\s*name = \"([^\"]+)\",\n\\s*query = \"([^\"]+)\"";
//                    Pattern queryPattern = Pattern.compile(queryRegex);
//                    Matcher queryMatcher = queryPattern.matcher(line);
//                    if (queryMatcher.find()) {
//                        String queryName = queryMatcher.group(1);
//                        String queryText = queryMatcher.group(2);
//                        query = queryName + ": " + queryText;
//                        System.out.println("Query: " + query);
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    private static void extractParameters(String path) {
//        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\tenz0\\IdeaProjects\\main1\\src\\main\\result\\FXKu\\" + path))) {
//            String line;
//            String directoryPath = path.substring(0, path.lastIndexOf("/") + 1);;
//
//            ArrayList<String> tablePath = new ArrayList<>();
//            while ((line = reader.readLine()) != null) {
//                String regex = "Table<([^>]+)>\\s+([^;]+);";
//                Pattern pattern = Pattern.compile(regex);
//                Matcher matcher = pattern.matcher(line);
//                if (matcher.find()) {
//                    String parameterType = matcher.group(1);
//                    String parameterName = matcher.group(2);
//                    tablePath.add("C:/Users/tenz0/IdeaProjects/main1/src/main/result/FXKu/" + directoryPath + parameterType + ".class");
//                    System.out.println(path);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



//        JarFile jarFile = new JarFile("C:\\Users\\tenz0\\IdeaProjects\\main1\\src\\main\\java\\org\\example\\FXKu.jar");
//        try {
//            jarFile.stream().forEach(entry -> {
//                try {
//                    InputStream inputStream = jarFile.getInputStream(entry);
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        extractParameters(line, String.valueOf(entry));
//                    }
//                    reader.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } finally {
//            jarFile.close();
//        }




//    private static void extractParameters(String line, String filename) {
//        String regex = "Table<([^>]+)>\\s+([^;]+);";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(line);
//        if (matcher.find()) {
//            String parameterType = matcher.group(1);
//            String parameterName = matcher.group(2);
//            System.out.println("File name: " + filename);
//            System.out.println("Parameter Type: " + parameterType);
//            System.out.println("Parameter Name: " + parameterName);
//        }
//    }



//name, type, url, query

//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//        String directoryPath = "./result/FXKu/ru/inversion/fxku/bdac";
//        String fileNamePattern = ".*Controller\\.class";
//
//        List<File> foundFiles = findFiles(directoryPath, fileNamePattern);
//        for (File file : foundFiles) {
//            System.out.println(file.getAbsolutePath());
//        }
//    }
//
//    public static List<File> findFiles(String directoryPath, String fileNamePattern) {
//        List<File> foundFiles = new ArrayList<>();
//        File directory = new File(directoryPath);
//
//        if (directory.isDirectory()) {
//            searchFiles(directory, fileNamePattern, foundFiles);
//        }
//
//        return foundFiles;
//    }
//
//    private static void searchFiles(File directory, String fileNamePattern, List<File> foundFiles) {
//        File[] files = directory.listFiles();
//
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    searchFiles(file, fileNamePattern, foundFiles);
//                } else if (file.getName().matches(fileNamePattern)) {
//                    foundFiles.add(file);
//                }
//            }
//        }
//    }
//}
