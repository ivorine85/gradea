import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

abstract class Adjustments extends Calculations{
	protected  <E> ArrayList<E> Additem(ArrayList<E> array_list, E item) {
		array_list.add(item);
		return array_list;		
	}
	
	protected <E> ArrayList<E> Deleteitem(ArrayList<E> array_list, int index) {
		array_list.remove(index);
		return array_list;
	}
	
	protected Profile Createuser() {
		boolean valid = false;
		String username;
		String password;
		String action = "null";
		String[] questions = new String[3];
		String[] answers = new String[3];
		String[] questionset = new String[3];
		String[] questionset1 = new String[3];
		String[] questionset2 = new String[3];
		String[] questionset3 = new String[3];
		
		//Security Question set 1
		questionset1[0] = "What is the first and last name of your first boyfriend or girlfriend?";
		questionset1[1] = "Which phone number do you remember most from your childhood?";
		questionset1[2] = "What is your favorite social media website";
		//Security Question set 2
		questionset2[0] = "What is your favorite color?";
		questionset2[1] = "What was the make of your first car?";
		questionset2[2] = "What was your high school mascot?";
		//Security Question set 3
		questionset3[0] = "Which is your favorite web browser?";
		questionset3[1] = "What street did you grow up on?";
		questionset3[2] = "In what city were you born?";
		Scanner scan = new Scanner(System.in).useLocale(Locale.US);  // Reading from System.in
		
		System.out.print("Enter a Username: ");
		//need to implement username check
		username = scan.next();
		
		do {
			System.out.print("Enter a password: ");
			password = scan.next();
			System.out.print("Reenter password: ");
			action = scan.next();
			
			if(password.equals(action)) {
				valid = true;
			}
			else {
				System.out.println("Passwords do not match, try again.");
			}
		}while(!valid);
		System.out.println("");
		
		for ( int i = 1; i < 4; i++) {
			System.out.println("Select question " + i + ":");
			if (i == 1) {
				questionset = questionset1;
			}
			else if (i == 2) {
				questionset = questionset2;
			}
			else {
				questionset = questionset3;
			}
			for(int j = 0; j < 3; j++) {
				System.out.println(j+1 + ". "+ questionset[j]);				
			}
			System.out.print("Select question: ");
			action = scan.next();
			questions[i-1] = questionset[Integer.parseInt(action)];
			System.out.print("Enter answer: ");
			action = scan.next();
			answers[i-1] = action;
		}
		scan.close();
		
		Profile userprofile = new Profile(username, password, answers, questions);
		return userprofile;
	}
	
	protected boolean Checklogin(Profile profile, String password) {
		if (password.equals(profile.getPassword())){
			return true;
		}
		else {
			return false;
		}
	}
	
	protected Profile Forgetpassword(Profile profile) {
		boolean valid = true;
		String password;
		String answer = "null";
		Scanner scan = new Scanner(System.in).useLocale(Locale.US);  // Reading from System.in
		
		for (int i = 0; i < 3; i++) {
			System.out.println("Question" + (i+1));
			System.out.println(profile.getSecurityQuestions()[i]);
			System.out.print("Answer: ");
			answer = scan.next();
			
			if (answer.toLowerCase().equals(profile.getSecurityAnswers()[i].toLowerCase())) {
				valid = false;
			}
		}
		
		if (valid) {
			valid = false;
			do {
				System.out.print("Enter a password: ");
				password = scan.next();
				System.out.print("Reenter password: ");
				answer = scan.next();
				
				if(password.equals(answer)) {
					valid = true;
					profile.setPassword(password);
				}
				else if (password.toLowerCase().equals("cancel")) {
					valid = true;
				}
				else {
					System.out.println("Passwords do not match, try again.");
				}
			}while(!valid);
		}
		
		scan.close();
		return profile;
	}
}
