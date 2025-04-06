package learn.solar.ui;

import learn.solar.domain.PanelResult;
import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.List;
import java.util.Scanner;

public class View {

    private Scanner console = new Scanner(System.in);

    public int chooseOptionMenu(){
        System.out.print("0. Exit\n" +
                "1. Find Panels by Section\n" +
                "2. Add a Panel\n" +
                "3. Update a Panel\n" +
                "4. Remove a Panel\n" +
                "Select [0-4]: ");
        int response = Integer.parseInt(console.nextLine().trim());
        System.out.print('\n');
        return response;
    }

    public void printHeader(String text){
        System.out.printf("%s\n", text);
        for (int i = 0; i < text.length(); i++){
            System.out.print("=");
        }
        System.out.print("\n\n");
    }

    public void printResult(PanelResult result){
        if(!result.isSuccess()){
            for(int i = 0; i < result.getMessages().size(); i++){
                System.out.print(result.getMessages().get(i));
            }
            System.out.print('\n');
        }

    }

    public void printPanels(String sectionName, List<Panel> panels){
        System.out.println("Row Col Year Material Tracking");
        for (Panel p : panels) {

            System.out.printf(
                    "%3d %3d %4d %8s %8s\n",
                    p.getRow(),
                    p.getColumn(),
                    p.getInstallationYear(),
                    p.getMaterial(),
                    p.isTracking() ? "yes" : "no"
            );
        }
        System.out.print('\n');
    }

    public Panel choosePanel(String sectionName, List<Panel> panels){
        System.out.println("Choose a panel:");
        for (int i = 0; i < panels.size(); i++) {
            Panel p = panels.get(i);
            System.out.printf("%d: Row=%d Col=%d Year=%d Material=%s Tracking=%s\n",
                    i + 1,
                    p.getRow(),
                    p.getColumn(),
                    p.getInstallationYear(),
                    p.getMaterial(),
                    p.isTracking() ? "yes" : "no");
        }
        System.out.println("0: Cancel");
        int index = readInt(String.format("Select [0-%d]: ", panels.size()), 0, panels.size());
        if (index == 0) {
            return null;
        }

        return panels.get(index - 1);
    }

    public Panel makePanel(){

        String section = readRequiredString("Section: ");
        int row = readInt("Row: ", 1, 250);

        int column = readInt("Column: ", 1, 250);

        Material material = readMaterial();

        int year = readInt("Installation Year: ", 1, 2024);

        boolean tracking = readBoolean("Tracked [y/n]: ");

        Panel panel = new Panel(row, column, section, material, tracking);
        panel.setInstallationYear(year);

        return panel;
    }

    public Panel updatePanel(Panel original){
        System.out.printf("Editing %s-%d-%d\n",
                original.getSection(),
                original.getRow(),
                original.getColumn());

        System.out.println("Press [Enter] to keep original value.\n");

        // section
        String section = readString(
                String.format("Section (%s): ", original.getSection()));
        if (!section.trim().isEmpty()) {
            original.setSection(section);
        }

        // row
        String rowStr = readString(String.format("Row (%d): ", original.getRow()));
        if (!rowStr.trim().isEmpty()) {
            try {
                int rowValue = Integer.parseInt(rowStr);
                original.setRow(rowValue);
            } catch (NumberFormatException ex) {
                // If parse fails, we can ignore or handle differently
                System.out.println("Invalid row input, keeping old value.");
            }
        }

        // column
        String colStr = readString(String.format("Column (%d): ", original.getColumn()));
        if (!colStr.trim().isEmpty()) {
            try {
                int colValue = Integer.parseInt(colStr);
                original.setColumn(colValue);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid column input, keeping old value.");
            }
        }

        // Material

        Material updatedMaterial = readMaterial();
        if (updatedMaterial != null) {
            original.setMaterial(updatedMaterial);
        }


        // year
        String yearStr;
        int yearValue;
        do{
            yearStr = readString(String.format("Installation Year (%d): ",
                    original.getInstallationYear()));
            if (yearStr.trim().isEmpty()) {
                break;
            }
            yearValue = Integer.parseInt(yearStr);
            if(yearValue < 1800 || yearValue > 2024){
                System.out.print("Enter a valid year\n");
            }
            else{
                original.setInstallationYear(yearValue);
            }

        }while(yearValue < 1800 || yearValue > 2024);


        // tracking
        String trackPrompt;
        String trackInput;
        do {
            trackPrompt = String.format("Tracked (%s) [y/n]: ",
                    original.isTracking() ? "yes" : "no");
            trackInput = readString(trackPrompt);
            if (trackInput.trim().isEmpty()) {
                break;
            }
            trackInput = trackInput.trim().toLowerCase();
            if(!trackInput.equals("y") && !trackInput.equals("n")){
                System.out.print("Enter a valid response: (y/n)\n");
            }
        }while(!trackInput.equals("y") && !trackInput.equals("n"));

        if (trackInput.equals("y")) {
            original.setTracking(true);
        } else {
            original.setTracking(false);
        }

        System.out.print('\n');
        return original;
    }

    public String readSection(){
        System.out.print("Section Name: ");
        String sectionName = console.nextLine();
        System.out.print('\n');
        return sectionName;
    }

    private String readString(String string){
        System.out.print(string);
        return console.nextLine();
    }

    private String readRequiredString(String string){
        while (true) {
            System.out.print(string);
            String input = console.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Value is required.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = console.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Value must be an integer.");
            }
        }
    }

    private int readInt(String string, int min, int max){
        while (true) {
            int value = readInt(string);
            if (value < min || value > max) {
                System.out.printf("Value must be between %d and %d.%n", min, max);
            } else {
                return value;
            }
        }

    }

    private Material readMaterial(){

        boolean isValid;

        do{
            System.out.print("Material: ");
            String materialStr = console.nextLine();
            //check for enter
            if(materialStr.trim().isEmpty()){
                return null;
            }

            switch (materialStr){
                case "MCSI":
                    return Material.MCSI;
                case "MONOSI":
                    return Material.MONOSI;
                case "CIGS":
                    return Material.CIGS;
                case "CDTE":
                    return Material.CDTE;
                case "ASI":
                    return Material.ASI;
                default:
                    System.out.print("Invalid material.\n");
                    isValid = false;
            }
        }while(!isValid);
        return null;
    }

    private boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = console.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Please enter [y] or [n].");
            }
        }
    }
}

