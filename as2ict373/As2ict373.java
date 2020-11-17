package as2ict373;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;

import javafx.geometry.Orientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

/**
 *
 * @author Vu Hoang Quan
 */

public class As2ict373 extends Application {

    private personHandler personInterface = new personHandler();
    private FlowPane fPane = new FlowPane();
    private Label UI_person_firstname_label, UI_person_surname_label,
            UI_person_married_surname_label, UI_person_address_label,
            UI_person_bio_label, UI_person_gender_label;
    private TextField UI_edit_person_firstname, UI_edit_person_surname,
            UI_edit_person_gender, UI_edit_person_address,
            UI_edit_person_married_surname, UI_edit_person_bio,
            UI_edit_person_relation, UI_edit_existed_person;
    private Button Submit_new_person, Export_data_to_file, Import_data_from_file,
            Load_sample_data;

    @Override
    public void start(Stage topView) {
        createUI(topView);
        topView.show();
    }

    public static void main(String[] argv) {
        launch(argv);
    }

    // design of 2 tab, 2 collumns on 1st tab
    private void createUI(Stage topView) {
        TabPane tp = new TabPane();
        tp.tabClosingPolicyProperty();
        fPane.getChildren().add(createPersonTree(new ArrayList<>()));
        fPane.getChildren().add(createViewUI(topView));

        Tab tabView = new Tab("TestView", fPane);
        Tab tabEdit = new Tab("Edit", createEditUI());
        tabView.setClosable(false);
        tabEdit.setClosable(false);
        tp.getTabs().addAll(tabView, tabEdit);
        topView.setScene(new Scene(new FlowPane(Orientation.HORIZONTAL)));
        topView.setTitle("Family Tree View Program 2.0");
        ((FlowPane) (topView.getScene().getRoot())).getChildren().add(tp);
    }

    // left collumn of view tab's design
    private TreeView<person> createPersonTree(ArrayList<person> data) {
        //handle empty Tree 
        if (data.isEmpty()) {
            TreeView<person> treeView = new TreeView<>(new TreeItem<>(new person("empty tree")));
            return treeView;
        } else {
            personInterface.setData(data);
            person searchPerson = personInterface.getData().get(0);//1st person added = root
            //searchPerson = personInterface.getPersonWithName("may");
            //hard code this function to test tree view with different person as tree root
            TreeView<person> treeView = new TreeView<>(personInterface.generateFamilyTree(searchPerson));

            EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
                showPersonInfo(event, treeView);
            };
            //create tree view to contain the root
            treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
            return treeView;
        }
    }

    // action to perform when clicked on family tree 
    private void showPersonInfo(MouseEvent event, TreeView<person> treeView) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if ((node instanceof Text || node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).toString();
            person name2 = (person) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
            UI_person_firstname_label.setText("firstname:" + name2.getFirstname());
            UI_person_surname_label.setText("surname:" + name2.getSurname());
            UI_person_married_surname_label.setText("married surname:" + name2.getMarriedSurname());
            UI_person_gender_label.setText("gender:" + name2.getGender());
            UI_person_address_label.setText("address:" + name2.getAddress());
            UI_person_bio_label.setText("bio:" + name2.getBio());
        }
    }

    // right collumn of view tab's design
    private Parent createViewUI(Stage topView) {
        VBox layoutMgr = new VBox(10);
        UI_person_firstname_label = new Label("empty");
        UI_person_surname_label = new Label("empty");
        UI_person_gender_label = new Label("empty");
        UI_person_married_surname_label = new Label("empty");
        UI_person_address_label = new Label("empty");
        UI_person_bio_label = new Label("empty");

        Export_data_to_file = new Button("Save Tree to .dat file");
        Import_data_from_file = new Button("Read .dat file to prog");
        Load_sample_data = new Button("Load a sample family tree");

        layoutMgr.getChildren().addAll(
                UI_person_firstname_label,
                UI_person_surname_label,
                UI_person_married_surname_label,
                UI_person_gender_label,
                UI_person_address_label,
                UI_person_bio_label,
                Load_sample_data,
                Export_data_to_file,
                Import_data_from_file);
        Load_sample_data.setOnMouseClicked(value -> clickLoadSample());
        Export_data_to_file.setOnMouseClicked(value -> clickSaveToFile(topView));
        Import_data_from_file.setOnMouseClicked(value -> clickReadFromFile(topView));
        return layoutMgr;
    }

    //action when click "Load a sample family tree"
    private void clickLoadSample() {
        //will change from  createPersonTree(new TreeView<person>) 
        //    to  createPersonTree(getSample())

        //reload Tree View with Sample
        fPane.getChildren().remove(0);
        fPane.getChildren().add(0, createPersonTree(getSample()));
    }

    //action when click "Save Tree to .dat file"
    private void clickSaveToFile(Stage topView) {
        try (ObjectOutputStream outputfile = new ObjectOutputStream(new FileOutputStream(
                fileChooserDialogWindow(topView, "Select Data File To Save")))) {
            outputfile.writeObject(personInterface.getData());
            // save entire ArrayList from personHandler to outputfile
            System.out.println("end of save data");
        } catch (Exception ex) {
            System.out.println("failed to save data " + ex);
        }
    }

    //action when click "Read .dat file to prog"
    private void clickReadFromFile(Stage topView) {
        try (ObjectInputStream infile = new ObjectInputStream(new FileInputStream(
                fileChooserDialogWindow(topView, "Select Data File To Read")))) {
            personInterface.setData((ArrayList<person>) infile.readObject());
            reloadTreeView();
            System.out.println("end of read data");
        } catch (Exception ex) {
            System.out.println("failed to read data " + ex);
        }
    }

    //reload Tree View
    private void reloadTreeView() {
        fPane.getChildren().remove(0);
        fPane.getChildren().add(0, createPersonTree(personInterface.getData()));
    }

    //popup window to choose file
    private String fileChooserDialogWindow(Stage topView, String message) {
        FileChooser fileChooserWindow = new FileChooser();
        fileChooserWindow.setTitle(message);
        fileChooserWindow.getExtensionFilters().addAll(
                new ExtensionFilter("data Files", "*.dat", "*.data"),
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        //this is default fileChooser location which maybe different 
        File defaultFolder = new File(".\\src\\data");
        fileChooserWindow.setInitialDirectory(defaultFolder);
        File selectedFile = fileChooserWindow.showOpenDialog(topView);
        if (selectedFile != null) {
            //topView.display(selectedFile);
            System.out.println(selectedFile.toString());
            return selectedFile.getAbsolutePath();
        } else {
            return "";
        }
    }

    //edit tab's design
    private Node createEditUI() {
        GridPane gPane = new GridPane();

        UI_edit_person_firstname = new TextField();
        UI_edit_person_surname = new TextField();
        UI_edit_person_gender = new TextField();
        UI_edit_person_address = new TextField();
        UI_edit_person_married_surname = new TextField();
        UI_edit_person_bio = new TextField();

        UI_edit_existed_person = new TextField();
        UI_edit_person_relation = new TextField();
        Submit_new_person = new Button("Add this new person to Tree");
        try {
            Submit_new_person.setOnAction(value -> doClick(value));
        } catch (Exception ex) {
            System.out.println("invalid input of new person to be added to family tree");
        }

        gPane.addRow(0, new Label("firstname "), UI_edit_person_firstname);
        gPane.addRow(1, new Label("surname "), UI_edit_person_surname);
        gPane.addRow(2, new Label("married surname (if applicable) "), UI_edit_person_married_surname);
        gPane.addRow(3, new Label("gender "), UI_edit_person_gender);
        gPane.addRow(4, new Label("address "), UI_edit_person_address);
        gPane.addRow(5, new Label("bio "), UI_edit_person_bio);

        gPane.addRow(0, new Label("Relationship with "), UI_edit_existed_person);
        gPane.addRow(1, new Label("Relationship type "), UI_edit_person_relation);
        gPane.addRow(2, new Label("  (can be \"spouse/child/father/mother\" ) "));
        gPane.addRow(6, Submit_new_person);
        return gPane;
    }

    // action when click "add this new person to Tree"
    private void doClick(Event evt) {
        //for testing - shows what needed to add a new person 
        if (!"male".equals(UI_edit_person_gender.getText())
                && !"female".equals(UI_edit_person_gender.getText())) {
            System.out.println("gender must be male or female");
        }
        if ("".equals(UI_edit_person_firstname.getText())) {
            System.out.println("a person must at least have a name");
        }
        //add this new person to tree data
        person addingPerson = new person(UI_edit_person_firstname.getText(),
                UI_edit_person_surname.getText(),
                UI_edit_person_gender.getText(),
                UI_edit_person_married_surname.getText(),
                UI_edit_person_address.getText(),
                UI_edit_person_bio.getText());
        personInterface.addPersonToTree(addingPerson);
        //add new person according to their relation
        if (!UI_edit_existed_person.getText().equals("")) {
            person existedPerson = personInterface.getPersonWithName(UI_edit_existed_person.getText());
            try {
                if ("spouse".equals(UI_edit_person_relation.getText())) {
                    existedPerson.addSpouse(addingPerson);
                }
                if ("child".equals(UI_edit_person_relation.getText())) {
                    existedPerson.addChildren(addingPerson);
                }
                if ("father".equals(UI_edit_person_relation.getText())) {
                    existedPerson.addFather(addingPerson);
                }
                if ("mother".equals(UI_edit_person_relation.getText())) {
                    existedPerson.addMother(addingPerson);
                }
            } catch (Exception ex) {
                System.out.println("Failed to add person to Tree's data " + ex);
            }
        }
        reloadTreeView();
    }

    //here are where to make up the family tree -- quick tree view - for testing
    private ArrayList<person> getSample() {
        ArrayList<person> sample = new ArrayList<>();

        person person1 = new person("tony", "junior", "male");
        person person2 = new person("will", "myth", "male");
        person person3 = new person("bob", "uncle", "male");
        person person4 = new person("howard", "tark", "male");
        person person5 = new person("jerry", "", "male");
        person person6 = new person("elise", "", "female");
        person person7 = new person("tom", "holl", "male");
        person person8 = new person("may", "pot", "female");
        person person9 = new person("maria", "Shark", "female");
        person person0 = new person("peeper", "pott", "female");
        person person10 = new person("yoda", "", "male");

        sample.add(person1);
        sample.add(person2);
        sample.add(person3);
        sample.add(person4);
        sample.add(person5);
        sample.add(person6);
        sample.add(person7);
        sample.add(person8);
        sample.add(person9);
        sample.add(person0);
        person1.addFather(person4);
        person1.addSpouse(person0);
        person0.addSpouse(person1);// would this have any issue? seem not
        person1.addChildren(person9);
        person1.addChildren(person10);
        person9.setBio("this this newly added bio");//editing existing people is okay
        person0.addMother(person8);
        person8.addChildren(person3);
        person3.addChildren(person2);
        person2.addSpouse(person6);
        person6.addChildren(person7);
        person6.addFather(person5);
        return sample;
    }
}
