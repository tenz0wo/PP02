package ru.inversion;

import ru.inversion.models.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsePath {
    ArrayList<String> controllerPaths = new ArrayList<>();
    JarFile jarFile = new JarFile("C:\\Users\\Koryshev.INVERSION\\IdeaProjects\\untitled\\src\\main\\java\\ru\\inversion\\FXKu.jar");

    public ParsePath() throws IOException {
    }

    public boolean checkExistTable(String path, String regex) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Koryshev.INVERSION\\IdeaProjects\\untitled\\src\\main\\result\\FXKu\\" + path))) {
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

    public void findControllerPath() throws IOException {
        Enumeration entries = jarFile.entries();
        controllerPaths = new ArrayList<>();
        ArrayList<Controller> controller= new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String name = entry.getName();
            if (name.endsWith("Controller.java")) {
                if (checkExistTable(name, "Table<([^>]+)>\\s+([^;]+);")) {
                    controllerPaths.add(name);
                }
            }
        }
        jarFile.close();
    }
}