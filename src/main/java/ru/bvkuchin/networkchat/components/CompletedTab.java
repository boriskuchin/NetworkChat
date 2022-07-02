package ru.bvkuchin.networkchat.components;

import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class CompletedTab extends Tab {


    ListView<String> convertsationHistory = new ListView<>();
    AnchorPane anchorPane = new AnchorPane(convertsationHistory);





    public CompletedTab(String tabName) {
        super(tabName);
        this.setContent(anchorPane);
        AnchorPane.setRightAnchor(convertsationHistory, 0.0);
        AnchorPane.setLeftAnchor(convertsationHistory, 0.0);
        AnchorPane.setBottomAnchor(convertsationHistory, 0.0);
        AnchorPane.setTopAnchor(convertsationHistory, 0.0);

    }

    public ListView<String> getConvertsationHistory() {
        return convertsationHistory;
    }
}
