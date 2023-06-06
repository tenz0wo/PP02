package ru.inversion.models;

import java.util.ArrayList;

public class Controller {
    String path;
    ArrayList<ControllerTable> tables;

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public ArrayList<ControllerTable> getTables(){
        return tables;
    }

    public void setTables(ArrayList<ControllerTable> tables){
        this.tables = tables;
    }
}


