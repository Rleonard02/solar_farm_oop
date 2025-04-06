package learn.solar.data;
import learn.solar.models.Material;
import learn.solar.models.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PanelFileRepositoryTest {
    static final String TEST_PATH = "./data/panel-test.csv";
//    final Panel[] testPanels = new Panel[]{
//            new Panel(1, 1, 1, "Main", Material.MCSI, true),
//            new Panel(2, 1, 2, "Main", Material.MONOSI, true),
//            new Panel(3, 2, 1, "UpperHill", Material.ASI, false),
//    };

    PanelFileRepository repository;

    @BeforeEach
    void setup() throws DataException, IOException {
        Files.copy(Paths.get("./data/panel-seed.csv"), Paths.get(TEST_PATH),
                StandardCopyOption.REPLACE_EXISTING);
        repository = new PanelFileRepository(TEST_PATH);
    }

    @Test
    void shouldFindAll() throws DataException {
        List<Panel> all = repository.findAll();

        assertNotNull(all);
        assertEquals(3, all.size());

        Panel first = all.get(0);
        assertEquals(1, first.getId());
        assertEquals("Main", first.getSection());
        assertEquals(1, first.getRow());
        assertEquals(1, first.getColumn());
        assertEquals(2020, first.getInstallationYear());
        assertEquals(Material.MCSI, first.getMaterial());
        assertTrue(first.isTracking());
    }

    @Test
    void shouldFindBySection() throws DataException {
        List<Panel> mainPanels = repository.findBySection("Main");
        assertEquals(2, mainPanels.size());

        List<Panel> upperHillPanels = repository.findBySection("UpperHill");
        assertEquals(1, upperHillPanels.size());
    }

    @Test
    void shouldNotFindBySection() throws DataException {
        List<Panel> mainPanels = repository.findBySection("NullSection");
        assertEquals(0, mainPanels.size());

    }

    @Test
    void shouldDeleteById() throws DataException {
        List<Panel> mainPanels = repository.findBySection("Main");
        assertEquals(2, mainPanels.size());

        assertTrue(repository.deleteById(1));
        //new panel list is written back correctly
        mainPanels = repository.findBySection("Main");
        assertEquals(1, mainPanels.size());
    }

    @Test
    void shouldNotDeleteById() throws DataException{
        List<Panel> mainPanels = repository.findBySection("Main");
        assertEquals(2, mainPanels.size());

        assertFalse(repository.deleteById(100));

        mainPanels = repository.findBySection("Main");
        assertEquals(2, mainPanels.size());
    }

    @Test
    void shouldUpdate() throws DataException{
        //update panel 1 section and material

        Panel panel = new Panel(1, 1, "UpperHill", Material.MONOSI, true);
        panel.setInstallationYear(2020);
        assertTrue(repository.update(panel));

    }

    @Test
    void shouldNotUpdate() throws DataException{
        //update panel 1 section and material

        Panel panel = new Panel( 1, 1, "UpperHill", Material.MONOSI, true);
        panel.setInstallationYear(2020);
        assertFalse(repository.update(panel));

    }

    @Test
    void shouldAdd() throws DataException{
        //update panel 1 section and material

        Panel panel = new Panel(1, 18, "Main", Material.MONOSI, true);
        panel.setInstallationYear(2020);
        panel = repository.add(panel);

        List<Panel> mainPanels = repository.findBySection("Main");
        assertEquals(3, mainPanels.size());

    }

    @Test
    void shouldNotAdd() throws DataException{
        //update panel 1 section and material

        Panel panel = new Panel(1, 18, "Main", Material.MONOSI, true);
        panel.setInstallationYear(2020);
        panel = repository.add(panel);

        List<Panel> mainPanels = repository.findBySection("Main");
        assertEquals(3, mainPanels.size());

    }
}
