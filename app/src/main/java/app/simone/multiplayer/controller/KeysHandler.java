package app.simone.multiplayer.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo on 02/08/2017.
 */

public class KeysHandler {

    private List<String> list;

    public KeysHandler(){
        list = new ArrayList<>();
    }

    public void addToList(String s){
        this.list.add(s);
    }

    public void clearList(){
        this.list.clear();
    }

    public String getElement(int index){
        return list.get(index);
    }

    public List<String> getList(){
        return this.list;
    }



}
