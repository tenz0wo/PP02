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
    ControllerFolder folder = new ControllerFolder();
    ArrayList<ControllerTable> listControllerTable = new ArrayList<>();
    ArrayList<ControllerFolder> listFolfer = new ArrayList<>();
    ControllerDBLinks() throws IOException {
        parsePath.findControllerPath();
        String oldPath = parsePath.controllerPaths.get(0);
        Controller controller= new Controller();
        ArrayList<Controller> listControllers= new ArrayList<>();
        for (String path : parsePath.controllerPaths) {
            fillHashTables(path);
            controller.setTables(listControllerTable);
            controller.setPath(path);
            listControllers.add(controller);
            controller = new Controller();
            if (!oldPath.equals(path)) {
                folder.setFolderPath(path);
                folder.setControllers(listControllers);
                listFolfer.add(folder);
                folder = new ControllerFolder();
            } else {
                folder.setFolderPath(oldPath);
                folder.setControllers(listControllers);
            }
            oldPath = path;
        }
        controllers.setControllerList(listFolfer);

        System.out.println("+");
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
            } else if (query == null) {
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
        return parseQuery.executeQuery("C:\\Users\\Koryshev.INVERSION\\IdeaProjects\\untitled\\src\\main\\result\\FXKu\\" + pathTable);
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
