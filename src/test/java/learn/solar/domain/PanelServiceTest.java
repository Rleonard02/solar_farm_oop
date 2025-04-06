package learn.solar.domain;

import learn.solar.data.PanelRepositoryTestDouble;
import learn.solar.models.Material;
import learn.solar.models.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PanelServiceTest {
    PanelService service;

    @BeforeEach
    void setup() {
        service = new PanelService(new PanelRepositoryTestDouble());
    }

    @Test
    void shouldFindBySection() {
        var mainPanels = service.findBySection("Main");
        assertEquals(2, mainPanels.size());

        var upperHillPanels = service.findBySection("UpperHill");
        assertEquals(1, upperHillPanels.size());

        var none = service.findBySection("DoesNotExist");
        assertEquals(0, none.size());
    }

    @Test
    void shouldAddValidPanel() {
        Panel panel = new Panel(10, 10, "LowerHill", Material.CIGS, true);
        panel.setInstallationYear(2005);

        PanelResult result = service.add(panel);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPanel());
        assertEquals(4, result.getPanel().getId());
    }

    @Test
    void shouldNotAddNullPanel() {
        PanelResult result = service.add(null);
        assertFalse(result.isSuccess());
        assertEquals("Panel cannot be null", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddWhenSectionMissing() {
        Panel panel = new Panel(10, 10, "", Material.CIGS, true);
        panel.setInstallationYear(2000);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
        assertEquals("Section is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddWhenRowOutOfRange() {
        Panel panel = new Panel(-1, 10, "Main", Material.CIGS, true);
        panel.setInstallationYear(2020);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
        assertEquals("Row is invalid. Valid Range: [1-250]", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddDuplicates() {
        Panel duplicate = new Panel(1, 1, "Main", Material.MONOSI, false);
        duplicate.setInstallationYear(2020);

        PanelResult result = service.add(duplicate);
        assertFalse(result.isSuccess());
        assertEquals("Duplicate panel is not allowed", result.getMessages().get(0));
    }

    @Test
    void shouldUpdateValid() {
        Panel existing = service.findBySection("Main").get(0);
        assertEquals(1, existing.getId());

        existing.setInstallationYear(1990);

        PanelResult result = service.update(existing);
        assertTrue(result.isSuccess());
        assertEquals(1990, result.getPanel().getInstallationYear());
    }

    @Test
    void shouldNotUpdateWhenNotFound() {
        Panel notFound = new Panel(1, 2, "Ghost", Material.ASI, false);
        notFound.setId(999);
        notFound.setInstallationYear(2000);

        PanelResult result = service.update(notFound);
        assertFalse(result.isSuccess());
        assertEquals("Panel not found, could not update", result.getMessages().get(0));
    }

    @Test
    void shouldDeleteExisting() {
        PanelResult result = service.deleteById(1);
        assertTrue(result.isSuccess());

        var mainPanels = service.findBySection("Main");
        assertEquals(1, mainPanels.size()); // used to be 2
        assertNotEquals(1, mainPanels.get(0).getId());
    }

    @Test
    void shouldNotDeleteMissing() {
        PanelResult result = service.deleteById(999);
        assertFalse(result.isSuccess());
        assertEquals("Panel not found, could not delete", result.getMessages().get(0));
    }


}
