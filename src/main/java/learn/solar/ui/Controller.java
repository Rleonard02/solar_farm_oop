package learn.solar.ui;

import learn.solar.domain.PanelResult;
import learn.solar.domain.PanelService;
import learn.solar.models.Panel;

import java.util.List;

public class Controller {
    private View view;
    private PanelService panelService;

    public Controller(View view, PanelService panelService){
        this.view = view;
        this.panelService = panelService;
    }

    public void run(){
        view.printHeader("Welcome to Solar Farm");
        System.out.print('\n');
        int response;
        do{
            view.printHeader("Main Menu");
            response = view.chooseOptionMenu();

            switch (response){
                case 1:
                    viewBySection();
                    break;
                case 2:
                    addPanel();
                    break;
                case 3:
                    updatePanel();
                    break;
                case 4:
                    deletePanel();
                    break;
                default:
                    break;
            }
        } while(response != 0);
    }

    private void viewBySection(){
        view.printHeader("Find Panels By Section");
        String sectionName = view.readSection();

        List<Panel> sectionPanels = panelService.findBySection(sectionName);
        view.printPanels(sectionName, sectionPanels);
    }

    private void addPanel(){
        view.printHeader("Add a Panel");
        PanelResult result;
        Panel newPanel = view.makePanel();
        result = panelService.add(newPanel);
        if(result.isSuccess()){
            System.out.printf("Success! Panel %d was added\n\n", newPanel.getId());
        }
        view.printResult(result);
    }

    private void updatePanel(){
        view.printHeader("Update a Panel");
        String sectionName = view.readSection();

        List<Panel> panelsInSection = panelService.findBySection(sectionName);

        if (panelsInSection == null || panelsInSection.isEmpty()) {
            System.out.println("No panels found for that section.\n");
            return;
        }

        view.printPanels(sectionName, panelsInSection);

        Panel toUpdate = view.choosePanel(sectionName, panelsInSection);
        if (toUpdate == null) {
            System.out.println("Update cancelled.\n");
            return;
        }

        toUpdate = view.updatePanel(toUpdate);

        PanelResult result = panelService.update(toUpdate);

        if(result.isSuccess()){
            System.out.printf("Success! Panel %d was updated!\n\n", toUpdate.getId());
        }

        view.printResult(result);
    }

    private void deletePanel(){
        view.printHeader("Remove a Panel");
        String sectionName = view.readSection();
        List<Panel> panelsInSection = panelService.findBySection(sectionName);

        if (panelsInSection == null || panelsInSection.isEmpty()) {
            System.out.println("No panels found for that section.\n");
            return;
        }

        view.printPanels(sectionName, panelsInSection);

        Panel toDelete = view.choosePanel(sectionName, panelsInSection);
        if(toDelete != null){
            PanelResult result = panelService.deleteById(toDelete.getId());
            view.printResult(result);

            if(result.isSuccess()){
                System.out.printf("Success! Panel was deleted!\n\n", toDelete.getId());
            }
        }

    }
}
