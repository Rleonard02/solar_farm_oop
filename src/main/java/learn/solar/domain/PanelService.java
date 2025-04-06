package learn.solar.domain;

import learn.solar.data.PanelRepository;
import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PanelService {
    private PanelRepository repository;

    public PanelService(PanelRepository repository){
        this.repository = repository;
    }

    public List<Panel> findBySection(String section){
        List<Panel> panels = repository.findBySection(section);
        return panels;
    }

    public PanelResult add(Panel panel){
        PanelResult result = validate(panel);
        if (!result.isSuccess()) {
            return result;
        }

        //check for duplicate
        //we have a duplicate if two panels have the same row, column, and section
        //we also cannot add a panel with an existing id
        List<Panel> panels = repository.findBySection(panel.getSection());
        for (Panel p: panels){
            if (panel.getRow() == p.getRow() && panel.getColumn() == p.getColumn() && panel.getSection().equals(p.getSection())){
                result.addMessage("Duplicate panel is not allowed");
                return result;
            }
            else if(panel.getId() == p.getId()){
                result.addMessage("Duplicate panel is not allowed");
                return result;
            }
        }
        panel = repository.add(panel);
        result.setPanel(panel);
        return result;
    }

    public PanelResult update(Panel panel){
        PanelResult result = validate(panel);
        if (!result.isSuccess()) {
            return result;
        }

        boolean didUpdate = repository.update(panel);
        if(!didUpdate){
            result.addMessage("Panel not found, could not update");
            return result;
        }
        result.setPanel(panel);
        return result;
    }

    public PanelResult deleteById(int id){
        PanelResult result = new PanelResult();
        boolean didDelete = repository.deleteById(id);
        if (!didDelete){
            result.addMessage("Panel not found, could not delete");
            return result;
        }

        return result;
    }

    private PanelResult validate(Panel panel){
        PanelResult result = new PanelResult();
        if (panel == null) {
            result.addMessage("Panel cannot be null");
            return result;
        }

        if (panel.getSection() == null || panel.getSection().trim().isEmpty()) {
            result.addMessage("Section is required");
        }

        if (panel.getRow() < 1 || panel.getRow() > 250) {
            result.addMessage("Row is invalid. Valid Range: [1-250]");
        }

        if (panel.getColumn() < 1 || panel.getColumn() > 250) {
            result.addMessage("Column is invalid. Valid Range: [1-250]");
        }

        //hardcoded current year for now
        if (panel.getInstallationYear() > 2025 || panel.getInstallationYear() < 0) {
            result.addMessage("Installation year must be valid");
        }

        if (panel.getMaterial() != Material.MONOSI && panel.getMaterial() != Material.ASI && panel.getMaterial() != Material.MCSI
                && panel.getMaterial() != Material.CDTE && panel.getMaterial() != Material.CIGS) {
            result.addMessage("Material is invalid");
        }


        return result;
    }
}
