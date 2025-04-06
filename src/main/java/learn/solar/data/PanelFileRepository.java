package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PanelFileRepository implements PanelRepository{

    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";
    private static final String HEADER = "id,row,column,section,installationYear,material,tracking";
    private final String filePath;

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }

    //private method
    public List<Panel> findAll() throws DataException {

        ArrayList<Panel> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Panel panel = deserialize(line);
                if (panel != null) {
                    result.add(panel);
                }
            }
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            throw new DataException(ex.getMessage(), ex);
        }

        return result;
    }

    private int getNextId(List<Panel> allPanels) {
        int nextId = 0;
        for (Panel p : allPanels) {
            nextId = Math.max(nextId, p.getId());
        }
        return nextId + 1;
    }

    private void writeAll(List<Panel> panels) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Panel p : panels) {
                writer.println(serialize(p));
            }
        } catch (IOException ex) {
            throw new DataException(ex.getMessage(), ex);
        }
    }

    @Override
    public Panel add(Panel panel) throws DataException {
        List<Panel> all = findAll();
        panel.setId(getNextId(all));
        all.add(panel);
        writeAll(all);
        return panel;
    }

    @Override
    public boolean deleteById(int id) throws DataException {
        List<Panel> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == id) {
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> all = findAll();
        List<Panel> listBySection = new ArrayList<>(List.of());
        for (Panel p : all){
            if (p.getSection().equals(section)){
                listBySection.add(p);
            }
        }

        return listBySection;
    }

    @Override
    public boolean update(Panel panel) throws DataException {
        List<Panel> all = findAll();
        for (Panel p : all){
            if(p.getId() == panel.getId()){
                p.setRow(panel.getRow());
                p.setColumn(panel.getColumn());
                p.setSection(panel.getSection());
                p.setInstallationYear(panel.getInstallationYear());
                p.setMaterial(panel.getMaterial());
                p.setTracking(panel.isTracking());
                writeAll(all);
                return true;
            }
        }
        return false;
    }


    private String serialize(Panel panel) {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                panel.getId(),
                panel.getRow(),
                panel.getColumn(),
                panel.getSection(),
                panel.getInstallationYear(),
                panel.getMaterial(),
                panel.isTracking()
        );
    }

    private Panel deserialize(String line) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length == 7) {
            int id = Integer.parseInt(fields[0]);
            int row = Integer.parseInt(fields[1]);
            int column = Integer.parseInt(fields[2]);
            Material material = Material.valueOf(fields[5]);
            boolean tracking = Boolean.parseBoolean(fields[6]);
            String section = fields[3];
            Panel panel = new Panel(row, column, section, material, tracking);
            panel.setId(id);
            panel.setInstallationYear(Integer.parseInt(fields[4]));
            return panel;
        }
        return null;
    }
}
