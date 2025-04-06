package learn.solar.domain;

import learn.solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelResult {

    private ArrayList<String> messages = new ArrayList<>();
    private Panel panel;

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public boolean isSuccess() {
        return messages.isEmpty();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

}
