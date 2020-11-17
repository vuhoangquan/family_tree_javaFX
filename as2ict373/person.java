/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as2ict373;

/**
 *
 * @author Vu Hoang Quan
 */
import java.util.ArrayList;
import java.io.Serializable;

public class person implements Serializable {

    private String firstname = "unspecified";// not null
    private String surname = "unspecified";
    private String gender = "unspecified";// not null
    private String surname_after_marriage = "unspecified";
    private String address = "unspecified";
    private String bio = "";

    // may have a spouse or none
    private person spousePerson;
    // may have none or many childrens
    private ArrayList<person> chilPersonsList = new ArrayList<person>();
    private person fathePerson;// maybe unknown/empty
    private person mothePerson;// maybe unknown/empty also

    public person(String firstname, String surname, String gender) {
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
    }

    //add a empty person - for Labeling the TreeView
    public person(String labelText) {
        this.bio = labelText;
    }

    // add a new person with full infomation
    public person(String firstname, String surname, String gender, String surname_after_marriage, String address,
            String bio) {
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.surname_after_marriage = surname_after_marriage;
        this.address = address;
        this.bio = bio;
    }

    // print a person basic infomation - for testing only
    public void printPersonInfo() {
        System.out.print("   " + firstname + "|");
        System.out.print(" " + surname + "|");
        System.out.print(" " + gender + "|");
        System.out.print(" " + surname_after_marriage + "|");
        System.out.print(" " + address + "|");
        System.out.print(" " + bio + "|");
        System.out.println();
    }

    // add spouse > the other will have this person as their spouse as well
    public void addSpouse(person newPerson) {
        if (!newPerson.gender.equals(this.gender)) {
            this.spousePerson = newPerson;
            newPerson.spousePerson = this;
            // if a person already have childs
            if (!this.chilPersonsList.isEmpty()) {
                // childrens will have same parent
                for (person eachChild : chilPersonsList) {
                    eachChild.mothePerson = newPerson;
                }
                // then spouse will have the same children
                newPerson.chilPersonsList = new ArrayList<person>(this.chilPersonsList);
            }
        }
    }

    // add father > that father will have this person as child
    public void addFather(person father) {
        if ("male".equals(father.gender)) {
            this.fathePerson = father;
            father.chilPersonsList.add(this);
        } else {
            System.out.println("person " + this.firstname + " father " + "must be male");
        }
        if (father.spousePerson != null) {
            // add father's spouse as mother 
            this.mothePerson = father.spousePerson;
            //mother will have this person as children
            mothePerson.chilPersonsList.add(this);
        }
    }

    // add mother > that mother will have this person as child
    public void addMother(person mother) {
        if ("female".equals(mother.gender)) {
            this.mothePerson = mother;
            mother.chilPersonsList.add(this);
        } else {
            System.out.println("person " + this.firstname + " mother " + "must be female");
        }
    }

    // add children > that child will take this person as their parent
    // (consider this person spouse as well)
    public void addChildren(person child) {
        if ("male".equals(this.gender)) {
            child.fathePerson = this;
        } else {
            child.mothePerson = this;
        }
        if (this.spousePerson != null) {
            if ("male".equals(this.gender)) {
                child.fathePerson = this;
                child.mothePerson = this.spousePerson;
            } else {
                child.fathePerson = this.spousePerson;
                child.mothePerson = this;
            }
            spousePerson.chilPersonsList.add(child);
        }
        this.chilPersonsList.add(child);
    }

    public void setMarriedSurname(String marriedSurname) {
        this.surname_after_marriage = marriedSurname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getGender() {
        return this.gender;
    }

    public String getMarriedSurname() {
        return this.surname_after_marriage;
    }

    public String getAddress() {
        return this.address;
    }

    public String getBio() {
        return this.bio;
    }

    public person getSpouse() {
        return this.spousePerson;
    }

    public person getFather() {
        return this.fathePerson;
    }

    public person getMother() {
        return this.mothePerson;
    }

    public ArrayList<person> getChildList() {
        return chilPersonsList;
    }

    //override for TreeItem<person> to show text instead of person name
    //   example: clicked into "parent:" will show as "unspecified"
    @Override
    public String toString() {
        if (firstname.equals("unspecified")) {
            return bio;
        } else {
            return this.firstname + " " + this.surname;
        }
    }
}
