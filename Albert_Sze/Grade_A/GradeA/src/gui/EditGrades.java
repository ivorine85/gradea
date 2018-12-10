package gui;
import java.awt.EventQueue;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.Font;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AssignmentDAO;
import dao.GradeBreakDownDAO;
import dao.LabDAO;
import dao.StudentDAO;
import entity.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class EditGrades extends Adjustments {

    private JFrame frame;
    private JTable tableGrades;
    private static Lab currentLab;
    //    public static void main(String[] args) {
    public static void ShowPage(){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditGrades window = new EditGrades(currentLab);
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // Create connection to sql database
    // Connection connection = null;
    //public EditGrades(Course newCourse, String currentLabSection) {
    public EditGrades(Lab lab) {
        currentLab = lab;
        LabDAO lb = new LabDAO();
//        currentLab = lb.findLabOfCourse("cs591").get(0);
        initialize();
        //initialize(newCourse, currentLabSection);
    }

    //private void initialize(Course newCourse, String currentLabSection) {
    private void initialize() {
        StudentDAO studentDAO = new StudentDAO();
        List<Student> allStudents = studentDAO.findStudentByLab(currentLab.getSection());
        ArrayList<String> header = new ArrayList<String>(0);
        ArrayList<ArrayList<String>> allStudentData = new ArrayList<ArrayList<String>>(0);
        ArrayList<String> studentData;
        List<Assignment> allAssignment;
        HashMap<String, Integer> assignCount = new HashMap<String, Integer>(0);
        String[][] arrayAllStudentData;
        int assignnum;
        double sum = 0.0;
        DefaultTableModel model;
        JLabel editGradesTitle;
        JButton saveButton;
        JButton backButton;
        JScrollPane scrollGrades;
        boolean valid=false;

        /*********************************** Get current lab section ********************************************/
        //////////////////////////////////ANDY CHANGE HERE////////////////////////////////////////////////
        //Need to load lab data here
        //////////////////////////////////////////////////////////////////////////////////////////////////

        /*********************************** Generate frame for Edit Grades Page *******************************/
        frame = new JFrame();
        frame.getContentPane().setForeground(new Color(0, 0, 0));
        frame.setBounds(100, 100, 801, 487);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        /******************************* Generate table of student grades **************************************/
        header.add("Student Name");
        //////////////////////////////////ANDY CHANGE HERE////////////////////////////////////////////////
        // get all undergrad and grad students in allStudents;

        AssignmentDAO assignmentDAO = new AssignmentDAO();
        allAssignment = assignmentDAO.findAssignmentByCourse(currentLab.getCourseName());

        studentData = new ArrayList<>();
        for (int i = 0; i < allStudents.size();i++) {				// for loop of all students in particular lab	            // need this variable to construct a double arraylist to display
            //change allStudents.get(i).genName() to students name on at a time
            Student cur = allStudents.get(i);
            studentData.add(cur.getName());
            GradeBreakDownDAO gd = new GradeBreakDownDAO();
            List<GradeBreakDown> gradeOfCurrentStudent = gd.getGradeByStudent(cur.getSid(),currentLab.getCourseName());
            //TODO:In this loop , cur is the object of current student;
            //TODO:The gradeOfCurrentStudent is a list of grades of current student;
            //TODO:The totalPoint is the final grade of current student
            double totalPoint = 0;
            for(GradeBreakDown gbd:gradeOfCurrentStudent){
                if (i == 0){
                    header.add(gbd.getCwName() + " Pts Lost");
                    header.add(gbd.getCwName() + "%");
                }
                int total = gbd.getTotalPoint();
                int lost = gbd.getPointLost();
                float percentPoint = ((float)total-lost+gbd.getWeight())/total*100;
                studentData.add(Integer.toString(lost));
                studentData.add(Float.toString(percentPoint));
                totalPoint += percentPoint*gbd.getPercentage()*gbd.getTypePercentage();
            }
            // Add current Final Grade to StudentData
            studentData.add(Double.toString((double)Math.round(allStudents.get(i).getGrade()*10000)/100));
            //Add the studentData to all student Data
            allStudentData.add(studentData);
        }
        //Add the title final grade to header
        header.add("Final Grade");


        /******************************* Calculate Average and add to the bottom of the table **************************************/
        //Reset studentData to empty array list
        studentData = new ArrayList<String>(0);
        studentData.add("Average");
        //For each item in header
        for (int i = 1; i < header.size(); i++) {
            sum = 0.0;
            for (int j = 0; j < allStudentData.size(); j ++ ) {
                //update average of item using Calcaverage from Adjustment Class
                sum = Calcaverage(sum, Double.parseDouble(allStudentData.get(j).get(i)), j);
            }
            //Add average to studentData
            studentData.add(Double.toString((double)Math.round(sum*100)/100));
        }
        //Add average to end of allStudentData array list
        allStudentData.add(studentData);

        //Convert allStudentData to String[][] so that the table can read it properly
        arrayAllStudentData = new String[allStudentData.size()][];
        for (int i = 0; i < allStudentData.size(); i++) {
            ArrayList<String> row = allStudentData.get(i);
            arrayAllStudentData[i] = row.toArray(new String[row.size()]);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////

        //String [][] data={{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"},{"","","","","","Select","Select"}};
        model = new DefaultTableModel (arrayAllStudentData,header.toArray()) {
            public boolean isCellEditable(int row, int col)
            {
                //If you didn't want the first column to be editable
                if(col%2 == 0 || row == allStudentData.size()-1 || col == header.size()-1) {
                    return false;
                }
                else {
                    return true;
                }
            }
        };

        /********************************************** Edit Grades Title **********************************************************/
        editGradesTitle = new JLabel("Edit Grades");
        editGradesTitle.setFont(new Font("Tahoma", Font.PLAIN, 36));
        editGradesTitle.setBounds(10, 11, 212, 44);
        frame.getContentPane().add(editGradesTitle);

        /****************************************** Add Back Button **************************************************************/
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LabPage labPageReturn = new LabPage(currentLab);
                //LabPage labPageReturn = new LabPage(newCourse, currentLabSection);
                labPageReturn.ShowPage();
                frame.dispose();
            }
        });
        backButton.setBounds(686, 414, 89, 23);
        frame.getContentPane().add(backButton);

        /****************************************** Add Save Button **************************************************************/
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //////////////////////////////////ANDY CHANGE HERE////////////////////////////////////////////////
                // Andy: update the data base with edited data


                //LabPage labPageReturn = new LabPage();
                //LabPage labPageReturn = new LabPage(newCourse, currentLabSection);
                //labPageReturn.ShowPage();
                //////////////////////////////////////////////////////////////////////////////////////////////////


                frame.dispose();
                //ShowPage();
            }
        });
        saveButton.setBounds(587, 414, 89, 23);
        frame.getContentPane().add(saveButton);

        /****************************************** Back Button **************************************************************/
//        backButton = new JButton("Back");
//        backButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                LabPage labPageReturn = new LabPage(currentLab);
//                //LabPage labPageReturn = new LabPage(newCourse, currentLabSection);
//                labPageReturn.ShowPage();
//                frame.dispose();
//            }
//        });
//        backButton.setBounds(489, 414, 89, 23);
//        frame.getContentPane().add(backButton);

        /************************************ Add Scroll Panel for Grades **********************************************************/
        scrollGrades = new JScrollPane();
        scrollGrades.setBounds(25, 78, 738, 325);
        frame.getContentPane().add(scrollGrades);

        /************************************ Add table of Grades **********************************************************/
        tableGrades = new JTable(model);
        scrollGrades.setViewportView(tableGrades);

        /************************************ Detects when value is changed in tableGrades ****************************************/
        tableGrades.getModel().addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent e){
                try{
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    int edit = Integer.parseInt((String)tableGrades.getValueAt(row, col));
                    saveButton.setEnabled(true);
                } catch (NumberFormatException nfe) {
                    saveButton.setEnabled(false);
                    //JOptionPane.showMessageDialog(null,"not valid edit");
                }
            }
        });
    }
}
