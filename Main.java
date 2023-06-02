package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws Exception {
        JarFile jarFile = new JarFile("/Users/tenzo/Desktop/JavaProjects/TraningJava/untitled/src/main/java/org/example/FXKu.jar");
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
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/tenzo/Desktop/JavaProjects/TraningJava/untitled/src/main/result/FXKu/" + path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void linksMap(String pathController) {
        String targetPattern = "private";
        HashMap<String, List> linkRatio = new HashMap<>();
        HashMap<String, String> tableQuery = new HashMap<>();
        List<String> contents = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("/Users/tenzo/Desktop/JavaProjects/TraningJava/untitled/src/main/result/FXKu/" + pathController))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(targetPattern)) {
                    Pattern pattern = Pattern.compile("private JInvTable\\<.+\\>");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String content = matcher.group(0);
                        List retLink = cutArray(Collections.singletonList((content)));
                        contents.add(retLink.toString());

                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String content : contents) {
            String pathTable = String.valueOf(pathController.substring(0, pathController.lastIndexOf("/")) + "/" + content.replaceAll("\\[", "").replaceAll("\\]", "") + ".java");
            String query = parseQuery("/Users/tenzo/Desktop/JavaProjects/TraningJava/untitled/src/main/result/FXKu/" + pathTable);
            if (linkRatio.containsKey(pathController)) {
                linkRatio.get(pathController).add(pathTable);
            } else {
                List<String> retLinks = new ArrayList<>();
                retLinks.add(pathTable.replaceAll("\\s", ""));
                linkRatio.put(pathController, retLinks);
            }
            if (query != null) {
//                tableQuery.put(content, query);
                tableQuery.put(content, cutQuery(query));

            }
        }

        System.out.println(tableQuery);
    }

    private static String cutQuery(String query) {
        String X = query.replaceAll("^.+query", "").replaceAll("^.+?\"", "").replaceAll("\".+?\"", "").replaceAll("\n", "");
        return X;
    }

    private static List cutArray(List<String> rtLink) {
        List<String> linksArray = new ArrayList<>();
        for (String link : rtLink) {
            int startIndex = link.indexOf("<") + 1;
            int endIndex = link.indexOf(">");
            String trimmedLink = link.substring(startIndex, endIndex);
            linksArray.add(trimmedLink);
        }
        return linksArray;
    }

    private static String parseQuery(String pathFile) {
        String targetPattern = "@NamedNativeQuery";
        if (Files.exists(Path.of(pathFile))) {
            try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
                String line;
                Integer countOpenBracket = 0;
                boolean flagStartCountBracket = false;
                String resultLine = "";
                while ((line = br.readLine()) != null) {
                    if (line.contains(targetPattern) || flagStartCountBracket) {
                        countOpenBracket = countBracket(line, countOpenBracket);
                        resultLine += line;
                        flagStartCountBracket = true;
                        if (countOpenBracket == 0) {
                            return resultLine;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static Integer countBracket(String bracketLine, Integer countOpenBracket) {
        for (int i=0; i<bracketLine.length(); i++){
            if (bracketLine.charAt(i) == '('){
                countOpenBracket++;
            }
            if (bracketLine.charAt(i) == ')'){
                countOpenBracket--;
            }
        }
        return countOpenBracket;
    }
}

//        try (BufferedReader reader = new BufferedReader(new FileReader(pathTable))) {
//            String line;
//            String query = null;
//            while ((line = reader.readLine()) != null) {
//                Pattern pattern = Pattern.compile(regex);
//                Matcher matcher = pattern.matcher(line);
//                if (matcher.find()) {
//                    return query;
//                }
//            }
//            return null;
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }




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
