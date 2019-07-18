import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import edu.duke.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BabyBirths {
    //Printing total count ,total boys names,total girls names
    public void totalBirth(FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;

        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
            } else {
                totalGirls += numBorn;
            }
        }
        System.out.println("total births= " + totalBirths);
        System.out.println("Boys: " + totalBoys);
        System.out.println("Girls: " + totalGirls);
    }
   //returning rank for given year,name and gender
    public long getRank(int year, String name, String gender) {
        FileResource fr = new FileResource("/home/zadmin/Downloads/us_babynames_test/yob" + year + "short.csv");
        CSVParser parser = fr.getCSVParser(false);
        long rank = -1;
        long maleStartingRank = 0;
        if (gender.equals("M")) {
            maleStartingRank = getMaleRank(year);
        }
        for (CSVRecord record : parser) {
            if (record.get(0).equals(name) && record.get(1).equals(gender))  {
                rank = record.getRecordNumber() - maleStartingRank;

            }

        }
        return rank;

    }
//returning male starting rank for given year
    public long getMaleRank(int year) {
        long rank = 0;
        FileResource fr = new FileResource("/home/zadmin/Downloads/us_babynames_test/yob" + year + "short.csv");
        CSVParser parser = fr.getCSVParser(false);
        for (CSVRecord record : parser) {
            if (record.get(1).equals("M")) {
                rank = record.getRecordNumber() - 1;
                break;
            }
        }
        return rank;
    }
//returning name for the given year,rank,gender
    public String getName(int year, int rank, String gender) {
        FileResource fr = new FileResource("/home/zadmin/Downloads/us_babynames_test/yob" + year + "short.csv");
        CSVParser parser = fr.getCSVParser(false);
        String name = "";
        long maleStartingRank = 0;
        if (gender.equals("M")) {
            maleStartingRank = getMaleRank(year);
        }

        for (CSVRecord record : parser) {

            long reqRank = record.getRecordNumber() - maleStartingRank;
            String reqGender = record.get(1);
            String reqName = record.get(0);
            if (reqRank == rank && reqGender.equals(gender)) {
                name = reqName;
            }
        }

        if (name != "") {
            return name;
        } else {
            return "NO NAME";
        }

    }
//printing the name for the required year based ranking from the given year,name and gender
    public void whatIsNameInYear(String name, int year, int newYear, String gender) {
        int rank = (int) getRank(year, name, gender);
        String nameInRequiredYear = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + nameInRequiredYear + " if she was born in " + newYear);

    }
//returning year with highest rank among the selected files for the given name and gender
    public int yearOfHighestRank(String name, String gender) {
        long highestRank = 0;
        int yearOfHighestRank = -1;
        long maleStartingRank = 0;
        String fileName = "";
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            if (gender.equals("M")) {
                int year=Integer.parseInt(f.getName().replaceAll("[\\D]", ""));
                maleStartingRank = getMaleRank(year);
            }

            // Iterate through all records in file
            for (CSVRecord record : parser) {


                if (record.get(0).equals(name) && record.get(1).equals(gender)) {
                    long reqRank = record.getRecordNumber() - maleStartingRank;

                    if (highestRank == 0) {
                        highestRank = reqRank;
                        fileName = f.getName();
                    } else {
                        if (highestRank > reqRank) {
                            highestRank = reqRank;
                            fileName = f.getName();
                        }
                    }
                }
            }
        }
        //Getting year from the fileName by replacing characters with ""
        fileName = fileName.replaceAll("[\\D]", "");
        yearOfHighestRank = Integer.parseInt(fileName);

        return yearOfHighestRank;
    }
//returning the average rank of the name and gender over the selected files
    public double getAverageRank(String name, String gender) {
        // Initialize a DirectoryResource
        DirectoryResource dr = new DirectoryResource();
        double avgRank=-1,sumOfranks=0.0;
        int noofRanks=0;
        long maleStartingRank = 0;
        String fileName = "";
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            if (gender.equals("M")) {
                int year=Integer.parseInt(f.getName().replaceAll("[\\D]", ""));
              maleStartingRank = getMaleRank(year);
            }
            // Iterate through all records in file
            for (CSVRecord record : parser) {

                if (record.get(0).equals(name) && record.get(1).equals(gender)) {
                    long reqRank = record.getRecordNumber() - maleStartingRank;
                    sumOfranks += reqRank;
                    noofRanks++;
                }
            }
    }
    avgRank=sumOfranks/noofRanks;
        return avgRank;
}   //returning  the total number of births of those names with the same gender and same year who are ranked higher than given name
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        int totalBirthsRankedHigher=0;
        long maleStartingRank=0;
        int rank=(int)getRank(year,name,gender);
        FileResource fr = new FileResource("/home/zadmin/Downloads/us_babynames_test/yob" + year + "short.csv");
        CSVParser parser = fr.getCSVParser(false);
        if (gender.equals("M")) {
            maleStartingRank = getMaleRank(year);
        }

        for(CSVRecord record:parser){
            if(record.get(1).equals(gender)){
            if(record.getRecordNumber()-maleStartingRank>=rank)
           {
                break;
            }

            totalBirthsRankedHigher+=Integer.parseInt(record.get(2));

        }}

return totalBirthsRankedHigher;
    }
    public void test(){
           FileResource fr=new FileResource("./dataset/yob2014short.csv");
          totalBirth(fr);
         //  System.out.println(getRank(2012,"Mason","M"));
        //    System.out.println(getName(2012,2,"M"));

      //  whatIsNameInYear("Isabella", 2012, 2014, "F");
   // System.out.println(yearOfHighestRank("Mason", "M"));
        //System.out.println(getAverageRank("Mason", "M"));
//System.out.println(getTotalBirthsRankedHigher(2012,"Noah","M"));

    }



    public static void main(String args[]) {

        BabyBirths b = new BabyBirths();
        b.test();

    }



}
