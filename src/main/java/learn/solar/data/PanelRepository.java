package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.List;

public interface PanelRepository {

    Panel add(Panel encounter) throws DataException;

    boolean deleteById(int id) throws DataException;

    List<Panel> findBySection(String section) throws DataException;

    public boolean update(Panel p) throws DataException;
}
