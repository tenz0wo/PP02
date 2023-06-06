package ru.inversion;

import ru.inversion.models.Controller;
import ru.inversion.models.ControllerFolder;
import ru.inversion.models.ControllerTable;
import ru.inversion.models.Controllers;

import java.io.IOException;
import java.util.*;

public class ControllerDBLinks {
    Controllers controllers = new Controllers();
    ParsePath parsePath = new ParsePath();
    ParseController parseController = new ParseController();
    ParseQuery parseQuery = new ParseQuery();
    ArrayList<ControllerTable> listControllerTable = new ArrayList<>();
    ControllerDBLinks() throws IOException {
        Controller controller;
        List<Controller> listControllers;
        ControllerFolder folder;
        List<ControllerFolder> folders = new ArrayList<>();

        parsePath.findControllerPathFolders();

        for (String path : parsePath.controllerFolders.keySet()) {
            listControllers = new ArrayList<>();
            for (String pathFile : findPathFile(path)){
                fillHashTables(pathFile);
                controller = new Controller();
                controller.setTables(listControllerTable);
                controller.setPath(pathFile);
                listControllers.add(controller);
            }

            folder = new ControllerFolder();
            folder.setFolderPath(path);
            folder.setControllers(listControllers);
            folders.add(folder);
        }
        controllers.setControllerList(folders);

        System.out.println("+");
    }


    private ArrayList<String> findPathFile(String pathFolder) throws IOException {
        return parsePath.findControllerPathFiles(pathFolder);
    }

    private void fillHashTables(String pathController) {
        HashMap<String, String> tableQuery = new HashMap<>();
        parseController.findTables(pathController);
        ArrayList <Controller> listController = new ArrayList<>();
        listControllerTable = new ArrayList<>();

        for (String table : parseController.listTable) {
            String pathTable = createPathTable(pathController, table);
            String query = executeQuery(pathTable);

            if (query != null) {
                addQueryToList(tableQuery, table, query);
                addControllerTable(pathController, table, tableQuery);
                tableQuery = new HashMap<>();
            } else {
                addNoneQueryToList(tableQuery, table);
                addNoneControllerTable(pathController, table);
                tableQuery = new HashMap<>();
            }
        }
    }

    private String createPathTable(String pathController, String content) {
        return String.valueOf(pathController.substring(0, pathController.lastIndexOf("/")) + "/" + content.replaceAll("\\[", "").replaceAll("\\]", "") + ".java");
    }

    private String executeQuery(String pathTable) {
        return parseQuery.executeQuery("C:\\\\Users\\\\Koryshev.INVERSION\\\\IdeaProjects\\\\untitled\\\\src\\\\main\\\\result\\\\FXKu\\\\" + pathTable);
    }

    private void addQueryToList(HashMap<String, String> tableQuery, String tableName, String query) {
        tableQuery.put(tableName, parseQuery.cutQuery(query));
    }

    private void addControllerTable(String pathController, String content, HashMap<String, String> tableQuery) {
        ControllerTable controllerTable = new ControllerTable();
        controllerTable.setTable(content);
        controllerTable.setQuery(new ArrayList<>(tableQuery.values()));
        showInfo(controllerTable);

        listControllerTable.add(controllerTable);
    }

    private void addNoneQueryToList(HashMap<String, String> tableQuery, String tableName) {
        tableQuery.put(tableName, null);
    }

    private void addNoneControllerTable(String pathController, String content) {
        ControllerTable controllerTable = new ControllerTable();
        controllerTable.setTable(content);
        controllerTable.setQuery(null);
        showInfo(controllerTable);

        listControllerTable.add(controllerTable);
    }

    private void addControllersToControllerList(ArrayList<ControllerFolder> listController) {
        controllers.setControllerList(listController);
    }

    private void showInfo(ControllerTable controllerTable){
        System.out.println(controllerTable.getTable());
        System.out.println(controllerTable.getQuery());
    }
}
