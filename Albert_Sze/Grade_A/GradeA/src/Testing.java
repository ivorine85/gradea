import java.io.*; 
import java.util.*; 

public class Testing extends Adjustments{

	public static void main(String[] args) throws Exception {
		/*
		// Load list of profiles
		//if (profileList
		Scanner scan_test = new Scanner(System.in).useLocale(Locale.US);  // Reading from System.in
		String answer = "null";
		String answer2 = "null";
		String action = "null";
		boolean valid = false;
		ArrayList<Profile> profileList = new ArrayList<Profile> (1);
		ArrayList<String> usernameList = new ArrayList<String> (1);

// Login Page
		System.out.println("Select Option");
		for (int i=0; i<usernameList.size();i++) {
			System.out.print((i+1) + ". ");
			System.out.println(usernameList.get(i));
		}
		System.out.print((usernameList.size()+1) + ". ");
		System.out.println("Create new user");
		System.out.print("choose: ");
		answer = scan_test.next();
		// If on login page won't need for GUI
		if (Integer.parseInt(answer) == (usernameList.size()+1)) {
			action = "newuser";
		} 
		
	//Create a new user
		if (action == "newuser") {
			//go to create new user page
			profileList.add(Createuser());
			usernameList.add(profileList.get(profileList.size()-1).getUsername());			
		}
		
		System.out.println(" ");
		System.out.println("Select Option");
		for (int i=0; i<usernameList.size();i++) {
			System.out.print((i+1) + ". ");
			System.out.println(usernameList.get(i));
		}
		System.out.print((usernameList.size()+1) + ". ");
		System.out.println("Create new user");
		System.out.print("choose: ");
		answer = scan_test.next();
		
	// Authentication of password for username
		if (Integer.parseInt(answer) != usernameList.size()+1) {
			System.out.println(usernameList.get(Integer.parseInt(answer)-1));
			System.out.print("Enter Password: ");
			answer2 = scan_test.next();
			valid = Checklogin(profileList.get(Integer.parseInt(answer)-1),answer2);
		}
		
		System.out.println(valid);
		
	// forgot password action
		profileList.set(0, Forgetpassword(profileList.get(0)));
		
// adding new course
		profileList.get(0).setCourses(<Course> Additem(profileList.get(0).getCourses(), new Course("Awesome Class")));
*/
		ArrayList<String> x = new ArrayList<String>(1);
		File file = new File("C:\\Users\\alber\\Google Drive\\Boston University\\CS591_Object_Oriented_Design_in_Java\\Project\\Excel_template_3.csv"); 
		  
		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		  String st;
		  while ((st = br.readLine()) != null) 
		    //System.out.println(st); 
		  	x.add(st);
		  x.set(0, x.get(0).substring(3));
		  x.set(2, x.get(2).substring(3));
		  x.set(3, x.get(3).substring(1));
		  System.out.println(x.size());
		  for (int i = 0; i < x.size(); i++) {
			  System.out.println(x.get(i));
			  System.out.println("");
		  }
		  
	}

}
