package learn.solar;

import learn.solar.data.PanelFileRepository;
import learn.solar.data.PanelRepository;
import learn.solar.domain.PanelService;
import learn.solar.ui.Controller;
import learn.solar.ui.View;

public class App{
    static final String TEST_PATH = "./data/panel-test.csv";

    private static final PanelRepository repository = new PanelFileRepository(TEST_PATH);
    private static final PanelService panelService = new PanelService(repository);
    private static final View view = new View();
    private static final Controller controller = new Controller(view, panelService);

    public static void main(String[] args) {
        controller.run();
    }
}