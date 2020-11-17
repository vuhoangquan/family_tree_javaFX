# family_tree_javaFX

This program shows a family tree view of all member and their relationship to each other. It has a proper GUI based on JavaFX 8 version. The program has 2 main tabs: 
•	A view tab where overall family tree can be viewed, and each person specific information will be shown when clicked on that person name. 
•	An edit tab where a new person can be added to the family tree.
The program will show an empty tree when started and can add a sample data with a quick button for fast viewing and testing. If the tree is empty, from edit tab, the new person will be added as root, otherwise the new person must have relation to existing person. The tree can export and import data from file (in ArrayList<person> type, save in a .dat file). Each file contain data for 1 family tree. If multiple family tree needs to be saved, then the files must be created manually before it can be saved by the program. The program can read or write data to a chosen file at any location. 

# User guide
The program can be run from “As2ict373.java” file which contain the main. The program was created with NetBeans using JavaFX therefore is should work by import these file to a new project, then select “run file” inside “As2ict373.java” by right-click. Data file for save or read to the program must be in “.dat” format. The “.dat” data file should not be manually modified, or it may corrupt the data. “.dat” does not need to be added into project.

# Structure and design
## Class diagram (open with StarUML): 
familyTreeClassDesign.mdj 
This program has 2 classes and a main. the personHandler class taking care of UI and I/O to the person class. Main() will call instance of personHandler class. Main class (As2ict373.java) handle design and arrangement of the tabs and buttons location as well as action when click on these button or label. Person class contain basic information of 1 person and pointers as relation to other people (1 pointer for spouse, 1 for father, 1 for mother and an Array List for children(s). When a new person is added, every immediate relative to this person will change their relation accordingly. 
## Function calls: 
*	As2ict373.java
	  start(Stage topView) 
  	  createUI(topView)
•	      createPersonTree(new ArrayList<>())
o	        showPersonInfo(event, treeView)
•	      createViewUI(topView)
o	        clickLoadSample()
            getSample()
o	        clickSaveToFile(topView)
            fileChooserDialogWindow(topView, message)
o	        clickReadFromFile(topView)
          	fileChooserDialogWindow(topView, message)
          	reloadTreeView()
•     	createEditUI()
o	        doClick(evt)
          	reloadTreeView
•	personHandler.java
o	  generateFamilyTree()
    	familyTreeUI()
•	      has1ChildAndIsCallingPerson()
•	person.java

createPersonTree function will return a nested TreeItem type for ViewTab.
showPersonInfo() run when clicked on a person in the TreeView (can click on text or spaces next to it)
createViewUI() show specific information of 1 person and 3 button to get a sample as data, save or write family tree’s data to file. These buttons use FileChoose to choose specific file to read/write to.
createEditUI() show a form to add new people to the family tree. doClick() runs when clicked on button “Add this new person to Tree”.

# Assumptions:
•	person’s address is simplified to just a simple String
•	instead of showing up 1 level in the tree, the whole tree is shown by recursive call
•	edit tab should add new people into tree not changing existing people (or removing)
•	add new person from empty tree by let "relationship with" and "relationship type" empty  
•	a new person must have at least firstname, gender
•	a couple is 1 male and 1 female (male spouse will be female and vice versa)
•	each person can only have 0 or 1 spouse
•	spouse person will have different parent (did not enforce this)
•	if a person already has child(s) and that person has a spouse, both will have the same child(s)
•	each person will only be shown in the tree once
o	if a child has 2 parents then the parents will not show each other as spouse
o	if a parent has 1 child and that child has already shown in the tree, then the parent will not be showing this child again (vice versa)
•	a person can have same firstname, surname, gender but is considered to be different person (their relationship could be different)

# Limitation
•	in Edit tab, if the family is not empty, the added person relation is empty, it will still be added to program but will not shows up in the tree view. 
•	in Edit tab, if the family is not empty, the “relationship with” person name is not found, the new person will still be added to program but will not shows up in the tree view. 
•	Cannot delete existing people or changing them from GUI. 
•	In Tree View, labels such as “parent:” will show as empty person with bio as “parent:”
