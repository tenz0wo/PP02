package ru.inversion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseController {
    List<String> listTable = new ArrayList<>();

    public String findTables(String pathController) {
        listTable = new ArrayList<>();
        String targetPattern = "private";
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\\\Users\\\\Koryshev.INVERSION\\\\IdeaProjects\\\\untitled\\\\src\\\\main\\\\result\\\\FXKu\\\\" + pathController))) {
            return findForeignTable(br, targetPattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String findForeignTable(BufferedReader br, String targetPattern) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains(targetPattern)) {
                Pattern pattern = Pattern.compile("private JInvTable\\<.+\\>");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String content = matcher.group(0);
                    List<String> retLink = cutArray(Collections.singletonList((content)));
                    listTable.add(retLink.toString());
                }
            }
        }
        return line;
    }

    private List <String> cutArray(List<String> rtLink) {
        List<String> linksArray = new ArrayList<>();
        for (String link : rtLink) {
            int startIndex = link.indexOf("<") + 1;
            int endIndex = link.indexOf(">");
            String trimmedLink = link.substring(startIndex, endIndex);
            linksArray.add(trimmedLink);
        }
        return linksArray;
    }

}
