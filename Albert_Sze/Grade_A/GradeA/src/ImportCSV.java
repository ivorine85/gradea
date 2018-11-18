import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map; 

public class ImportCSV extends Adjustments {

	public static void main(String[] args) throws Exception   {
		Hashtable<String, GradeBreakDown> courseBreakDown = new Hashtable();
		Hashtable<String, ArrayList<Student>> studentList = new Hashtable(0);
		Hashtable<String, ArrayList<GradeBreakDown>> assignmentBreakDown = new Hashtable(0);
		Hashtable<String, Double> tempStudentGrades = new Hashtable();
		ArrayList<String> csvData = new ArrayList<String>(1);
		String assignType;
		String studentName;
		String studentEmail;
		String sid;
		String year;
		String type;
		String labSection;
		int tempNumAssign;
		int tempNum;
		int numCategories;
		int tempIndex;
		double score;
		double percent;
		Student tempStudent;
		//Need to change file directory if you are going to run this code
		File file = new File("C:\\Users\\alber\\Google Drive\\Boston University\\CS591_Object_Oriented_Design_in_Java\\Project\\Github\\gradeA\\Albert_Sze\\Grade_A\\GradeA\\src\\Excel_template_3.csv");
		
		studentList.put("undergrad", new ArrayList<Student>(0));
		studentList.put("grad", new ArrayList<Student>(0));		
		
		//Read CSV file in
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String st;
		while ((st = br.readLine()) != null) {
			csvData.add(st);
		}
		// split the first two  3 rows
		if (csvData.get(0).substring(csvData.get(0).length()-1).equals(",")) {
			csvData.set(0, csvData.get(0).substring(6,csvData.get(0).length()-1));
		}
		else{
			csvData.set(0, csvData.get(0).substring(6));
		}
		csvData.set(2, csvData.get(2).substring(6));
		//for (int i = 0; i < csvData.size(); i++) {
 		//	System.out.println(csvData.get(i));
		//}
		System.out.println("");
		// split the rows by comma delimiters.
		String[] assignments = csvData.get(0).split(",,");
		for (int i = 0; i < assignments.length; i++) {
			assignments[i] = assignments[i].toLowerCase();
		}
		String[] assignmentDetails = csvData.get(2).split(",");

		//Assign grade break downs
		for (int i = 0; i < assignments.length;i++) {
			assignType = assignments[i].substring(0, assignments[i].indexOf("_"));

			if (!courseBreakDown.containsKey(assignType)) {
				tempStudentGrades.put(assignType, 0.0);
				courseBreakDown.put(assignType, new GradeBreakDown(assignType,0.0,0.0,0,0,0.0,1));
				ArrayList<GradeBreakDown> temp = new ArrayList<GradeBreakDown>(0);
				assignmentBreakDown.put(assignType,temp);
			}
			else {
				tempNumAssign = courseBreakDown.get(assignType).getNumAssign();
				courseBreakDown.get(assignType).setNumAssign(tempNumAssign+1);
			}			
			assignmentBreakDown.get(assignType).add(new GradeBreakDown(assignType, Double.parseDouble(assignmentDetails[(2*i)+1].substring(0, assignmentDetails[(2*i)+1].length()-1))/100.0, Double.parseDouble(assignmentDetails[(2*i)+1].substring(0, assignmentDetails[(2*i)+1].length()-1))/100.0, 0, Integer.parseInt(assignmentDetails[2*i]),0.0, 1));
			
			if (assignType.toLowerCase().equals("project")) {
				for (int j = 1; j < 4; j++) {
					percent = assignmentBreakDown.get(assignType).get(assignmentBreakDown.get(assignType).size()-1).getUndergradAssignPercent();
					int totalPts = assignmentBreakDown.get(assignType).get(assignmentBreakDown.get(assignType).size()-1).getTotalPoints();
					percent += Double.parseDouble(assignmentDetails[(2*(i+j))+1].substring(0, assignmentDetails[(2*(i+j))+1].length()-1))/100.0;
					totalPts += Integer.parseInt(assignmentDetails[2*(i+j)]);
					assignmentBreakDown.get(assignType).get(assignmentBreakDown.get(assignType).size()-1).setUndergradAssignPercent(percent);
					assignmentBreakDown.get(assignType).get(assignmentBreakDown.get(assignType).size()-1).setGradAssignPercent(percent);
					assignmentBreakDown.get(assignType).get(assignmentBreakDown.get(assignType).size()-1).setTotalPoints(totalPts);
				}
				i += 3  ;
			}
		}
		numCategories = courseBreakDown.size();
				
		for (int i = 4;i < csvData.size() ; i++) {
			String[] tempStudentDetail = csvData.get(i).split(",");
			studentName = tempStudentDetail[0];
			studentEmail = tempStudentDetail[1];
			sid = tempStudentDetail[2];
			year = tempStudentDetail[3];
			type = tempStudentDetail[4];
			labSection = tempStudentDetail[5];
			studentList.get(type.toLowerCase()).add(new Student(studentName,sid,type,Integer.parseInt(year),studentEmail,0));
			tempIndex = studentList.get(type.toLowerCase()).size()-1;
			
			for (int j = 0; j < assignments.length; j++) {
				assignType = assignments[j].substring(0, assignments[j].indexOf("_"));
				tempNum = Integer.parseInt(assignments[j].substring(assignments[j].lastIndexOf("_")+1))-1;
				if (assignType.toLowerCase().equals("project")) {
					studentList.get(type.toLowerCase()).get(tempIndex).getCourseWork().add(new Project(assignType, Integer.parseInt(tempStudentDetail[j*2+6]), Integer.parseInt(tempStudentDetail[(j+1)*2+6]), Integer.parseInt(tempStudentDetail[(j+2)*2+6]),Integer.parseInt(tempStudentDetail[(j+3)*2+6]), (Integer.parseInt(assignmentDetails[(j)*2])+Integer.parseInt(assignmentDetails[(j+1)*2])+Integer.parseInt(assignmentDetails[(j+2)*2])+Integer.parseInt(assignmentDetails[(j+3)*2]))));
					percent = (Double.parseDouble(assignmentDetails[j*2+1].substring(0, assignmentDetails[j*2+1].indexOf("%")))+Double.parseDouble(assignmentDetails[(j+1)*2+1].substring(0, assignmentDetails[(j+1)*2+1].indexOf("%")))+Double.parseDouble(assignmentDetails[(j+2)*2+1].substring(0, assignmentDetails[(j+2)*2+1].indexOf("%")))+Double.parseDouble(assignmentDetails[(j+3)*2+1].substring(0, assignmentDetails[(j+3)*2+1].indexOf("%"))))/100.0;
					j += 3;
				}
				else {
					studentList.get(type.toLowerCase()).get(tempIndex).getCourseWork().add(new Assignment (assignType,  Integer.parseInt(tempStudentDetail[j*2+6]), Integer.parseInt(assignmentDetails[j*2]), 0));
					percent = (Double.parseDouble(assignmentDetails[j*2+1].substring(0, assignmentDetails[j*2+1].indexOf("%"))))/100.0;
				}
				tempNumAssign = studentList.get(type.toLowerCase()).get(tempIndex).getCourseWork().size();
				score = studentList.get(type.toLowerCase()).get(tempIndex).getCourseWork().get(tempNumAssign-1).getPercent();
				tempStudentGrades.put(assignType, Calcfinalgrade(tempStudentGrades.get(assignType), score, percent));
				assignmentBreakDown.get(assignType).get(tempNum).setAverage(Calcaverage(assignmentBreakDown.get(assignType).get(tempNum).getAverage(), score, i-4));;
			}
			
			for (Map.Entry<String, Double> entry : tempStudentGrades.entrySet()) {
				score = studentList.get(type.toLowerCase()).get(tempIndex).getGrade();
				if (type.equals("undergrad")) {
					studentList.get(type.toLowerCase()).get(tempIndex).setGrade(Calcfinalgrade(score, entry.getValue(), courseBreakDown.get(entry.getKey()).getUndergradAssignPercent()));
				}
				else {
					studentList.get(type.toLowerCase()).get(tempIndex).setGrade(Calcfinalgrade(score, entry.getValue(), courseBreakDown.get(entry.getKey()).getGradAssignPercent()));
				}
				tempStudentGrades.put(entry.getKey(), 0.0);	
			}
			
		}
		
		for (Map.Entry<String, ArrayList<GradeBreakDown>> entry : assignmentBreakDown.entrySet()) {
			System.out.print(entry.getKey() + ": ");
			courseBreakDown.get(entry.getKey()).setUndergradAssignPercent(100.0/numCategories);
			courseBreakDown.get(entry.getKey()).setGradAssignPercent(100.0/numCategories);
			System.out.println(courseBreakDown.get(entry.getKey()).getGradAssignPercent());
			for (int i = 0; i < entry.getValue().size();i++) {
				System.out.print(entry.getValue().get(i).getAssignType() + (i+1) + ": ");
				//System.out.print(entry.getValue().get(i).getGradAssignPercent()+"    ");
				//System.out.println(entry.getValue().get(i).getTotalPoints());
				System.out.println(entry.getValue().get(i).getAverage());
			}
			System.out.println(" ");
		}
		
/*		for (Map.Entry<String, ArrayList<Student>> entry : studentList.entrySet()) {
			System.out.print(entry.getKey() + ": ");
			System.out.println(entry.getValue().size());
			for (int i = 0; i < entry.getValue().size(); i++) {
				tempStudent = entry.getValue().get(i);
				System.out.println(tempStudent.getName());
				for (int j = 0; j <entry.getValue().get(i).getCourseWork().size(); j++) {
					System.out.print(entry.getValue().get(i).getCourseWork().get(j).getType());
					System.out.println(entry.getValue().get(i).getCourseWork().get(j).getPercent());
				}
				System.out.println("");
			}
		}
*/

	}

}
