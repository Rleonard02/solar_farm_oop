package learn.solar.models;

import java.util.Objects;


public class Panel {
    private int id;
    private String section;
    private int row;
    private int column;
    private int installationYear;
    private Material material;
    private boolean tracking;


    public Panel(int row, int column, String section, Material material, boolean tracking){
        this.section = section;
        this.row = row;
        this.column = column;
        this.material = material;
        this.tracking = tracking;
    }

    public int getId() {
        return id;
    }

    public String getSection() {
        return section;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getInstallationYear() {
        return installationYear;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setInstallationYear(int installationYear) {
        this.installationYear = installationYear;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Panel panel = (Panel) o;
        return id == panel.id &&
                row == panel.row &&
                column == panel.column &&
                Objects.equals(section, panel.section) &&
                Objects.equals(material, panel.material) &&
                installationYear == panel.installationYear &&
                tracking == panel.tracking;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, column, section, material, installationYear, tracking);
    }
}
