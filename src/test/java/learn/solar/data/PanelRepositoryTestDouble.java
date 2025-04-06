package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelRepositoryTestDouble implements PanelRepository{
    private final ArrayList<Panel> panels = new ArrayList<>();

    public PanelRepositoryTestDouble() {
        Panel p1 = new Panel(1, 1, "Main", Material.MCSI, true);
        p1.setInstallationYear(2020);
        p1.setId(1);

        Panel p2 = new Panel(2, 1, "Main", Material.MONOSI, false);
        p2.setInstallationYear(2019);
        p2.setId(2);

        Panel p3 = new Panel(3, 2, "UpperHill", Material.ASI, true);
        p3.setInstallationYear(2018);
        p3.setId(3);

        panels.add(p1);
        panels.add(p2);
        panels.add(p3);
    }

    @Override
    public Panel add(Panel panel) throws DataException {
        int nextId = 0;
        for (Panel p : panels) {
            nextId = Math.max(nextId, p.getId());
        }
        nextId++;
        panel.setId(nextId);

        panels.add(panel);
        return panel;
    }

    @Override
    public boolean deleteById(int id) throws DataException {
        for (int i = 0; i < panels.size(); i++) {
            Panel panel = panels.get(i);
            System.out.print(panel.getId());
            if ((panels.get(i).getId()) == id) {
                panels.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataException {
        ArrayList<Panel> result = new ArrayList<>();
        for (Panel p : panels) {
            if (p.getSection().equalsIgnoreCase(section)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public boolean update(Panel p) throws DataException {
        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getId() == p.getId()) {
                panels.set(i, p);
                return true;
            }
        }
        return false;
    }
}
