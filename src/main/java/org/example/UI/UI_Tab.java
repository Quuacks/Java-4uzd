package org.example.UI;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import org.example.Control.Student;

public abstract class UI_Tab {
    String name;

    abstract Tab createTab();

    public UI_Tab(String name){
        this.name = name;
    }
}
