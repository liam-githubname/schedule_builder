
/*
F2F
COP 3330 Final Project
Liam Harvell, Jack Cook, Mitchell Knight
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;



public class Main {
  public static void main(String[] args) {
    String pathname = null;
    boolean fileNotFound;
    System.out.print("Enter the absolute path of the file: ");

    do {
      try {
        
        Scanner inputBuf = new Scanner(System.in);
        pathname = inputBuf.nextLine();
        File file = new File(pathname);
        fileNotFound = false;
        if (!file.exists()) {throw new NullPointerException();}

      } catch (NullPointerException e) {

        System.out.println("Sorry no such file: ");
        System.out.print("Try again: ");
        fileNotFound = true;

      }
    } while (fileNotFound);
    
    LecFile lecFile = new LecFile(pathname);
    Schedule schedule = new Schedule(lecFile);
    menu(schedule);

  }








  static int validIntInput() {
    Scanner inputBuf = new Scanner(System.in);
    boolean invalidInput;
    int output = 0;
    do {
      try {
          output = inputBuf.nextInt();
          inputBuf.nextLine();
          invalidInput = false;
      } catch (InputMismatchException e) {
          System.out.print("invalid input.\nPlease enter a valid input: ");
          inputBuf.nextLine();
          invalidInput = true;
      }
    } while (invalidInput);

    return output;
  }

  static void menu(Schedule schedule) {
    Scanner input = new Scanner(System.in);
    boolean deletionFlag = false;
    boolean exit = false;
    int choice = 0;
    do{
        System.out.println("*****************************************");
        System.out.println("Choose one of these options:");
        System.out.println("\t1- Add a new Faculty to the schedule");
        System.out.println("\t2- Enroll a Student to a Lecture");
        System.out.println("\t3- Print the schedule of a Faculty");
        System.out.println("\t4- Print the schedule of an TA");
        System.out.println("\t5- Print the schedule of a Student");
        System.out.println("\t6- Delete a Lecture");
        System.out.println("\t7- Exit");

        System.out.print("\t\tEnter your choice: ");

        choice = validIntInput();

        switch (choice) {
          case 1:
            schedule.addFaculty();
            break;
          case 2:
            schedule.addStudent();
            break;
          case 3:
            schedule.printFaculty();
            break;
          case 4:
            schedule.printTa();
            break;
          case 5:
            schedule.printStudent();
            break;
          case 6:
            schedule.deleteLecture();
            deletionFlag = true;
            break;
          case 7:
            if (deletionFlag) {
              System.out.print("You have made a deletion of at least one lecture. Would you like to print the copy of lec.txt? Enter y/Y for Yes or n/N for No: ");
              String answer = input.next();
              while(!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")){
                System.out.print("Is that a yes or no? Enter y/Y for Yes or n/N for No: ");
                answer = input.next();
              }
              if(answer.equalsIgnoreCase("y")){
                System.out.println("\n");
                schedule.printLectures();
              } else {
                System.out.print("Bye!");
              }
            }
              return;
          default:
            System.out.println("Invalid choice please select 1 - 7. ");
            break;
      }

    } while (!exit);
  }

  static String getUCFId() {
      String idBuf = "0";
      Scanner inputBuf = new Scanner(System.in);

      do {
        try {
            idBuf = inputBuf.nextLine();
          if (idBuf.length() != 7) {
            throw new IdException();
          }
        } catch (IdException e){
          System.out.println(e.toString());
          System.out.print("Please enter a valid id: ");
        }
      } while (idBuf.length() != 7);

    return idBuf;
  }

static class IdException extends Exception {
  public String toString() {
    return ">>>>>>>>>>>>>Sorry incorrect format. (Ids are 7 digits)";
  }
}

  static abstract class Person {

    Person() {};
    private String id;
    private String name;
    private ArrayList<String> crnList = new ArrayList<String>();

    public Person(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public void addCrn(String crn) {
      crnList.add(crn);
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean crnSearch(String crn) {
      for (String e : this.crnList) {
        if (e.equalsIgnoreCase(crn)) {
          return true;
        }
      }
      return false;
    }

    public ArrayList<String> getCrnList() {
      return crnList;
    }

    public void setCrnList(ArrayList<String> crnList) {
      this.crnList = crnList;
    }
  }

  static class Faculty extends Person {
    private String rank;
    private String officeLocation;

    public Faculty(String rank, String officeLocation) {
      this.rank = rank;
      this.officeLocation = officeLocation;
    }

    public Faculty(String id, String name, String rank, String officeLocation) {
      super(id, name);
      this.rank = rank;
      this.officeLocation = officeLocation;
    }

    public String getRank() {
      return rank;
    }

    public void setRank(String rank) {
      this.rank = rank;
    }

    public String getOfficeLocation() {
      return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
      this.officeLocation = officeLocation;
    }

  }

  static class Student extends Person {

    private ArrayList<String> labList = new ArrayList<String>();


    public Student() {
    }

    public Student(String id, String name) {
      super(id, name);
    }

    public ArrayList<String> getLabList() {
      return labList;
    }

    public void setLabList(ArrayList<String> labList) {
      this.labList = labList;
    }

  }

  static class TA extends Person {
    private String supervisor;
    private String degreeSeeking;

    public TA(String id, String name, String supervisor, String degreeSeeking) {
      super(id, name);
      this.supervisor = supervisor;
      this.degreeSeeking = degreeSeeking;
    }
    public String getSupervisor() {
      return supervisor;
    }
    public void setSupervisor(String supervisor) {
      this.supervisor = supervisor;
    }
    public String getDegreeSeeking() {
      return degreeSeeking;
    }
    public void setDegreeSeeking(String degreeSeeking) {
      this.degreeSeeking = degreeSeeking;
    }
  }

  static class LecFile {

    private File inputFile;
    private Scanner scannerObj;
    ArrayList<Lecture> lectureList = new ArrayList<Lecture>();

    LecFile(String pathname) {
      String tempBuf;
      String tokens[];
      inputFile = new File(pathname);

      try {
        scannerObj = new Scanner(inputFile);
      } catch (FileNotFoundException e) {
        System.out.println("File not found.");
        e.printStackTrace();
      }

      while (scannerObj.hasNext()) {
        tempBuf = scannerObj.nextLine();
        tokens = tempBuf.split(",");
        if (tokens.length == 7) {
          String tempCrn = tokens[0];
          String tempPrefix = tokens[1];
          String tempTitle = tokens[2];
          String tempEducationLevel = tokens[3];
          String tempModality = tokens[4];
          String tempRoom = tokens[5];
          String tempLab = tokens[6];

          lectureList.add(new Lecture(tempCrn, tempPrefix, tempTitle, tempEducationLevel, tempModality, tempRoom, tempLab));

        }

        if (tokens.length == 5) {
          String tempCrn = tokens[0];
          String tempPrefix = tokens[1];
          String tempTitle = tokens[2];
          String tempEducationlevel = tokens[3];
          String tempModality = tokens[4];

          lectureList.add(new Lecture(tempCrn, tempPrefix, tempTitle, tempEducationlevel, tempModality));
        }

        if (tokens.length == 2) {
          lectureList.get(lectureList.size() - 1).getLabCrns().add(tempBuf);
        }

      }
    }

    Lecture searchLectures(String crn) {
      for (Lecture temp : lectureList) {
        if (temp.getCrn().equalsIgnoreCase(crn)) {
          return temp;
        }
      }
      return null;
    }
  }


  static class Lecture {

  public Lecture(String crn, String prefix, String title, String educationLevel, String modality) {
    this.crn = crn;
    this.prefix = prefix;
    this.title = title;
    this.educationLevel = educationLevel;
    this.modality = modality;
  }

  public Lecture(String crn, String prefix, String title, String educationLevel, String modality, String room,
      String lab) {
    this.crn = crn;
    this.prefix = prefix;
    this.title = title;
    this.educationLevel = educationLevel;
    this.modality = modality;
    this.room = room;
    this.lab = lab;
  }

  private String crn = "null";
  private String prefix = "null";
  private String title = "null";
  private String educationLevel = "null";
  private String modality = "null";
  private String room = "null";
  private String lab = "null";

  private ArrayList<String> labCrns = new ArrayList<String>();

  public Lecture() {
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEducationLevel() {
    return educationLevel;
  }

  public void setEducationLevel(String educationLevel) {
    this.educationLevel = educationLevel;
  }

  public String getModality() {
    return modality;
  }

  public void setModality(String modality) {
    this.modality = modality;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public ArrayList<String> getLabCrns() {
    return labCrns;
  }

  public void setLabCrns(ArrayList<String> labCrns) {
    this.labCrns = labCrns;
  }

  public String getLab() {
    return lab;
  }

  public String getCrn() {
    return crn;
  }

  public void setCrn(String crn) {
    this.crn = crn;
  }

  public void setLab(String lab) {
    this.lab = lab;
  }

  public String toString() {
    if (this.modality.equalsIgnoreCase("Online")) {
      return "[" + crn + "/" + title + "]" + "[Online]";
    }

    return "[" + crn + "/" + prefix + "/" + title + "]";
  }


}


static class Schedule {

    private ArrayList<Person> scheduleList = new ArrayList<Person>();
    private ArrayList<String> usedCrns = new ArrayList<String>();
    private LecFile lecFile;

    public Schedule(LecFile lecfile) {
      this.lecFile = lecfile;
    }

    public boolean duplicateId(String requestedId) {
      for (Person person : scheduleList) {
        if (person.id.equalsIgnoreCase(requestedId)) {
          return true;
        }
      }
      return false;
    }

    public void addStudent() {
      String tempName;
      String tempId;
      String crnString;
      String crnTokens[];
      Scanner inputBuf = new Scanner(System.in);
      Lecture lectureRef = null;
      Person tempPersonObj;

      System.out.print("Enter UCF id: ");
      tempId = getUCFId();

      do {
        tempPersonObj = searchFacultyId(tempId); {
          if (tempPersonObj != null) {
            System.out.print("Id found to be a faculty member: please enter a new id: ");
            tempId = getUCFId();
          }
        }
    } while (tempPersonObj != null);


      tempPersonObj = searchStudentId(tempId);
      Person tempObj = searchTaId(tempId);

      if (tempPersonObj == null) {

        if (tempObj != null) {
          System.out.println("Record found/Name: " + tempObj.getName());
          tempPersonObj = new Student(tempObj.getId(), tempObj.getName());
        } else {
          System.out.print("Enter name: ");
          tempName = inputBuf.nextLine();
          tempPersonObj = new Student(tempId, tempName);
        }
      }

      System.out.print("Which lecture to enroll [" + tempPersonObj.getName() + "] in? ");
      crnString = inputBuf.nextLine();
      if (crnString.equalsIgnoreCase("0")) {
        System.out.println("Student created!");
        return;
      }

      Person tempTaObj = searchTaId(tempPersonObj.getId());

      if (tempTaObj != null) {
        for (String taCrn : tempTaObj.getCrnList()) {
          for (String labCrn : lecFile.searchLectures(crnString).getLabCrns()) {
            if (taCrn.equalsIgnoreCase(labCrn)) {
              System.out.println("Student cannot be enrolled in lecture, Student is TA for this lecture.");
              return;
            }
          }
        }
      }

      crnTokens = crnString.split(" ");
      for (String crn : crnTokens) {
        lectureRef = lecFile.searchLectures(crn);
        for (String lectureCrns : tempPersonObj.getCrnList()) {
          Lecture tempLecture = lecFile.searchLectures(lectureCrns);
          if (lectureRef.getPrefix().equalsIgnoreCase(tempLecture.getPrefix())) {
            crn = new String("used");
          }
        }

        System.out.print(lectureRef.toString());
        if (lectureRef.getLab().equalsIgnoreCase("yes")) {
          System.out.println(" has these labs:");
          for (String lab : lectureRef.getLabCrns()) {
            System.out.println("\t" + lab);
          }

          Random random = new Random();
          String tempLabCrn = lectureRef.getLabCrns().get(random.nextInt(lectureRef.getLabCrns().size()));
          String labTokens[] = tempLabCrn.split(",");
          System.out.println("[" + tempPersonObj.getName() + "] is added to lab: " + labTokens[0] );
          ((Student)(tempPersonObj)).getLabList().add(tempLabCrn);
        } else if (crn.contains("used")) {
          System.out.println("Student is already taking " + lectureRef.getPrefix());
          continue;
        }

        tempPersonObj.getCrnList().add(crn);

        System.out.println("\nStudent enrolled!");
      }

      scheduleList.add(tempPersonObj);



    }

    public void printFaculty() {
      System.out.print("Enter UCF id: ");
      String ucfId;
      Lecture lecture;
      Faculty faculty;

      ucfId= getUCFId();
      faculty = ((Faculty) searchFacultyId(ucfId));
      if (faculty == null) {
        System.out.println("No Faculty with that id.");
        return;
      }

      System.out.println(faculty.getName() + " is teaching the following lectures: ");
      for (String crn : faculty.getCrnList()) {
        lecture = lecFile.searchLectures(crn);
        if (lecture.getModality().equalsIgnoreCase("online")) {
          System.out.println(lecture.toString());
        } else if (lecture.getLab().equalsIgnoreCase("yes")) {
          System.out.println(lecture.toString() + " with Labs:");
          for (String lab : lecture.getLabCrns()) {
            System.out.println("\t[" + lab + "]");
          }
        } else {
          System.out.println(lecture.toString() + " Added!");
        }
      }
    }

    public void printStudent() {
      System.out.print("Enter UCF id: ");
      String ucfId;
      Student student;
      Lecture lecture;
      int labIndex = 0;

      ucfId = getUCFId();
      student =  ((Student) searchStudentId(ucfId));
      if (student == null) {
        System.out.println("No Student with that id.");
        return;
      }

      System.out.println(student.getName() + " is in the following lectures: ");
      for (String crn : student.getCrnList()) {
        lecture = lecFile.searchLectures(crn);
        if (lecture.getLab().equalsIgnoreCase("yes")) {
          String labCrn[] = lecture.getLabCrns().get(labIndex).split(",");
          System.out.println("[" + lecture.prefix + "/" + lecture.title + "]" + "/[Lab: " + labCrn[0] + "]");
          labIndex++;
        } else {
          System.err.println(lecture.toString());
        }
      }

      for (String lab : student.getLabList()) {
        System.out.println("\t[" + lab + "]");
      }
    }

    public void printTa() {
      TA ta;
      String ucfId;
      System.out.print("Enter a TA id: ");
      ucfId = getUCFId();

      ta = ((TA) searchTaId(ucfId));

      if (ta == null) {
        System.out.println("No TA with that id.");
        return;
      }

      System.out.println(ta.getName() + " is a TA for the following labs: ");

      for (String lab : ta.getCrnList()) {
       System.out.println("\t[" + lab + "]");
      }
    }

    public Person searchFacultyId(String requestedId) {
      for (Person person : scheduleList) {
        if (person.getClass().getSimpleName().equalsIgnoreCase("faculty")) {
          if (person.getId().equalsIgnoreCase(requestedId)){
            return person;
          }
        }
      }
      return null;
    }

    public Person searchStudentId(String requestedId) {
      for (Person person : scheduleList) {
        if (person.getClass().getSimpleName().equalsIgnoreCase("student")) {
          if (person.getId().equalsIgnoreCase(requestedId)){
            return person;
          }
        }
      }
      return null;
    }

    public Person searchTaId(String requestedId) {
      for (Person person : scheduleList) {
        if (person.getClass().getSimpleName().equalsIgnoreCase("ta")) {
          if (person.getId().equalsIgnoreCase(requestedId)){
            return person;
          }
        }
      }
      return null;
    }

    public void addFaculty() {
      Person tempFaculty;

      Scanner inputBuf = new Scanner(System.in);
      String idBuf;
      String nameBuf;
      String rankBuf;
      String officeBuf;
      int numOfLectures;
      String crnBuf;
      String inputCrnTokens[];

      System.out.print("\tEnter UCF id: ");
      idBuf = getUCFId();

      do {
        tempFaculty = searchStudentId(idBuf); {
          if (tempFaculty != null) {
            System.out.print("\t\tId found to be a student: please enter a new id: ");
            idBuf = getUCFId();
          }
        }
      } while (tempFaculty != null);

      tempFaculty = searchFacultyId(idBuf);
      
      if (tempFaculty == null) {
        System.out.print("\tEnter name: ");
        nameBuf = inputBuf.nextLine();

        System.out.print("\tEnter rank: ");
        rankBuf = inputBuf.nextLine();

        System.out.print("\tEnter office location: ");
        officeBuf = inputBuf.nextLine();

        tempFaculty = new Faculty(idBuf, nameBuf, rankBuf, officeBuf);

        scheduleList.add(tempFaculty);

      }

      boolean invalidInput;
      do {
        try {
          System.out.print("\tEnter how many lectures: ");
          numOfLectures = inputBuf.nextInt();
          inputBuf.nextLine();
          invalidInput = false;
        } catch (InputMismatchException e) {
          System.out.println("\tinvalid input.");
          inputBuf.nextLine();
          invalidInput = true;
          continue;
        }
      } while (invalidInput);
    
      System.out.print("\tEnter the crn(s) of the lecture(s): ");
      crnBuf = inputBuf.nextLine();
      inputCrnTokens = crnBuf.split(" ");

      Lecture tempLecture;
      for (String crn : inputCrnTokens) {
        for (String usedCrn : usedCrns) {
          if (crn.equalsIgnoreCase(usedCrn)) {
            crn = new String("used");
            System.out.println("This lecture has already been assigned.");
          }
        }

        if (crn.equalsIgnoreCase("used")) {
          continue;
        } else {
          tempFaculty.getCrnList().add(crn);
        }

        tempLecture = lecFile.searchLectures(crn);
        
        if (tempLecture.getLab().equalsIgnoreCase("yes")) {
          Person tempTaObj;
          String tempTaId;
          String tempSupervisor;
          String tempDegree;
          String tempName;
          boolean invalidId = false;

          System.out.println("\t\t" + tempLecture.toString() + " has these labs: ");
          for (String lab : tempLecture.getLabCrns()) {
            System.out.println("\t\t\t" + lab);
          }

          for (String lab : tempLecture.getLabCrns()) {
            String labTokens[] = lab.split(",");
            System.out.print("\tEnter the TA's id for " + labTokens[0] + ": ");

            do {

              tempTaId = getUCFId();
              do {
                tempTaObj = searchFacultyId(tempTaId); {
                  if (tempTaObj != null) {
                    System.out.print("\tId found to be a faculty member: please enter a new id: ");
                    tempTaId = getUCFId();
                  }
                }
              } while (tempTaObj != null);


              tempTaObj = searchTaId(tempTaId);
              if (tempTaObj == null) {
                tempTaObj = searchStudentId(tempTaId);
                if (tempTaObj == null) {
                  System.out.print("\tEnter TA's name: ");
                  tempName = inputBuf.nextLine();

                  System.out.print("\tEnter TA's supervisor's name: ");
                  tempSupervisor = inputBuf.nextLine();

                  System.out.print("\tEnter Degree being sought: ");
                  tempDegree = inputBuf.nextLine();
                  
                  tempTaObj = new TA(tempTaId, tempName, tempSupervisor, tempDegree);
                  
                  tempTaObj.addCrn(lab);

                  scheduleList.add(tempTaObj);
                  invalidId = false;
                  
                } else {
                  
                  for (String studentCrn : tempTaObj.getCrnList()) {
                    if (studentCrn.equalsIgnoreCase(crn)) {
                      invalidId = true;
                      System.out.println("\tTA status unavailable, student is enrolled in lecture.");
                      System.out.print("\tPlease enter a different TA's id: ");
                    }
                  }
                   if (invalidId) {
                    continue;
                   }
                  System.out.println("\tTA found as student: " + tempTaObj.getName());

                  System.out.print("\tEnter the TA's supervisor's name: ");
                  tempSupervisor = inputBuf.nextLine();

                  System.out.print("\tEnter Degree being sought: ");
                  tempDegree = inputBuf.nextLine();

                  scheduleList.add((Person)new TA(tempTaId, tempTaObj.getName(), tempSupervisor, tempDegree));
                  scheduleList.get(scheduleList.size() - 1).getCrnList().add(lab);
                  invalidId = false;

                }
              } else {

                System.out.println("\tTA found as TA: " + tempTaObj.getName());
                System.out.println("\tTA's supervisor's name: " + ((TA)(tempTaObj)).getSupervisor());
                System.out.println("\tDegree seeking: " + ((TA)(tempTaObj)).getDegreeSeeking());

                ((TA)(tempTaObj)).getCrnList().add(lab);
                invalidId = false;
              }

            } while (invalidId);
          }
        }
        
        usedCrns.add(crn);
        System.out.println("\t\t" + tempLecture.toString() + " Added!");
        }
      }

    public ArrayList<Person> getScheduleList() {
      return scheduleList;
    }

    public void setScheduleList(ArrayList<Person> scheduleList) {
      this.scheduleList = scheduleList;
    }

    public ArrayList<String> getUsedCrns() {
      return usedCrns;
    }

    public void setUsedCrns(ArrayList<String> usedCrns) {
      this.usedCrns = usedCrns;
    }

    public LecFile getLecFile() {
      return lecFile;
    }

    public void setLecFile(LecFile lecFile) {
      this.lecFile = lecFile;
    }

    public void deleteLecture() {
      Scanner inputBuf = new Scanner(System.in);
      System.out.print("Enter the crn of lecture to delete: ");
      String crnToDelete = inputBuf.nextLine();
      Lecture lectureToDelete = lecFile.searchLectures(crnToDelete);
      ArrayList<String> labsToDelete = lectureToDelete.getLabCrns();

      System.out.println(lectureToDelete.toString() + " Deleted.");
      lecFile.lectureList.remove(lectureToDelete);

      for (Person person : scheduleList) {
        for (int i = 0; i < person.getCrnList().size(); i++) {
          if (person.getCrnList().get(i).equalsIgnoreCase(crnToDelete)) {
            person.getCrnList().remove(i);
          }
        }

      }

      for (String lab : labsToDelete) {
        for (Person person : scheduleList) {
          if (person.getClass().getSimpleName().equalsIgnoreCase("student")) {
            for (int i = 0; i < ((Student)(person)).getLabList().size(); i++) {
              if (((Student)(person)).getLabList().get(i).equalsIgnoreCase(lab)) {
                ((Student)(person)).getLabList().remove(i);
              }
            }
          }

          if (person.getClass().getSimpleName().equalsIgnoreCase("ta")) {
            for (int i = 0; i < person.getCrnList().size(); i++) {
              if (person.getCrnList().get(i).equalsIgnoreCase(lab)) {
                person.getCrnList().remove(i);
              }
            }
          }
        }
      }


      FileWriter fileWriter;
      PrintWriter printWriter;
      if (lecFile.inputFile.delete()) {
        try {
          fileWriter = new FileWriter("lec.txt");
          printWriter = new PrintWriter(fileWriter, true);
          for (Lecture lecture : lecFile.lectureList) {
            printWriter.flush();
            if (lecture.modality.equalsIgnoreCase("online")) {
              printWriter.write(lecture.crn + "," + lecture.prefix + "," + lecture.title + "," + lecture.educationLevel + "," + lecture.modality + "\n");
            } else {
              printWriter.write(lecture.crn + "," + lecture.prefix + "," + lecture.title + "," + lecture.educationLevel + "," + lecture.modality + "," + lecture.room + "," + lecture.lab + "\n");

              if (lecture.lab.equalsIgnoreCase("yes")) {
                for (String lab : lecture.labCrns) {
                  printWriter.write(lab + "\n");
                }
              }
            }
            printWriter.flush();
          }
        } catch (IOException e) {
          e.toString();
        }


      }
    }

    public void printLectures() {
      for (Lecture lecture : lecFile.lectureList) {

        if (lecture.modality.equalsIgnoreCase("online")) {
          System.out.print(lecture.crn + "," + lecture.prefix + "," + lecture.title + "," + lecture.educationLevel + "," + lecture.modality + "\n");
        } else {
          System.out.print(lecture.crn + "," + lecture.prefix + "," + lecture.title + "," + lecture.educationLevel + "," + lecture.modality + "," + lecture.room + "," + lecture.lab + "\n");

          if (lecture.lab.equalsIgnoreCase("yes")) {
            for (String lab : lecture.labCrns) {
              System.out.print(lab + "\n");
            }
          }
        }
      }
    }
  }

}
