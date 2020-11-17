/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as2ict373;

import java.util.ArrayList;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Vu Hoang Quan
 */

//handle UI and I/O of person class
public class personHandler {

    private ArrayList<person> data;

    public personHandler() {
        data = new ArrayList<>();
    }

    //new person added to Tree must at least has a firstname 
    //and gender must be male or female
    public void addPersonToTree(person newPerson) {
        if (newPerson.getGender().equals("male")
                || newPerson.getGender().equals("female")) {
            if (!newPerson.getFirstname().equals("")) {
                data.add(newPerson);
            }
        }
    }

    // find person with matching name ( firstname/lastname/fullname with space)
    public person getPersonWithName(String personName) {
        for (person eachPerson : data) {
            if (eachPerson.getFirstname().equals(personName)
                    || eachPerson.getSurname().equals(personName)
                    || personName.equals(eachPerson.getFirstname() + " " + eachPerson.getSurname())) {
                return eachPerson;
            }
        }
        return null;
    }

    //call familyTreeUI  and return a nested TreeItem for UI
    public TreeItem<person> generateFamilyTree(person person1) {
        return this.familyTreeUI(person1, person1);
    }
    
    //this function is a helper for generateFamilyTree() 
    //return a Tree <person> of people firstname
    //param: calling_person is the trailing person and root is the current person in the recursive search
    public TreeItem<person> familyTreeUI(person calling_person, person root) {
        TreeItem<person> itemRoot = new TreeItem<>(root);
        if (root.getSpouse() != null && root.getSpouse() != calling_person) {
            boolean child_calls = false;
            for (person eachChild : root.getChildList()) {
                if (eachChild == calling_person) {
                    child_calls = true;
                    break;
                }
            }
            if (child_calls == false) {
                TreeItem<person> spouseList = new TreeItem<>(new person("Spouse: "));
                spouseList.getChildren().add(familyTreeUI(root, root.getSpouse()));
                itemRoot.getChildren().add(spouseList);
            }
        }
        if (root.getFather() != null || root.getMother() != null) {
            if (root.getFather() != calling_person && root.getMother() != calling_person) {
                TreeItem<person> parentList = new TreeItem<>(new person("Parent:"));
                if (root.getFather() != null) {
                    if (root.getFather() != calling_person && root.getMother() != calling_person) {
                        parentList.getChildren().add(familyTreeUI(root, root.getFather()));
                    }
                }
                if (root.getMother() != null) {
                    if (root.getMother() != calling_person && root.getFather() != calling_person) {
                        parentList.getChildren().add(familyTreeUI(root, root.getMother()));
                    }
                }
                itemRoot.getChildren().add(parentList);
            }
        }
        if (!root.getChildList().isEmpty() && root.getSpouse() != calling_person /* && AllChildAreNotCallingPerson(calling_person,root)==false*/) {
            //prevent showing "child:" with nothing in it
            if (has1ChildAndIsCallingPerson(calling_person, root) == false) {
                TreeItem<person> childList = new TreeItem<>(new person("Child:"));
                for (person eachPerson : root.getChildList()) {
                    if (eachPerson != calling_person) {
                        childList.getChildren().add(familyTreeUI(root, eachPerson));
                    }
                }
                itemRoot.getChildren().add(childList);
            }
        } else {
            //System.out.println("child list is null");
        }
        return itemRoot;
    }

    //return true if all childs are not the person called this function
    private boolean has1ChildAndIsCallingPerson(person calling_person, person root) {
        if (root.getChildList().size() == 1 && root.getChildList().get(0) == calling_person) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<person> getData() {
        return this.data;
    }

    public void setData(ArrayList<person> data) {
        this.data = data;
    }
}
