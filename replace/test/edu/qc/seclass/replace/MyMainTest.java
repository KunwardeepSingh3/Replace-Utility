package edu.qc.seclass.replace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MyMainTest {
	 private ByteArrayOutputStream outStream;
	    private ByteArrayOutputStream errStream;
	    private PrintStream outOrig;
	    private PrintStream errOrig;
	    private Charset charset = StandardCharsets.UTF_8;

	    @Rule
	    public TemporaryFolder temporaryFolder = new TemporaryFolder();

	    @Before
	    public void setUp() throws Exception {
	        outStream = new ByteArrayOutputStream();
	        PrintStream out = new PrintStream(outStream);
	        errStream = new ByteArrayOutputStream();
	        PrintStream err = new PrintStream(errStream);
	        outOrig = System.out;
	        errOrig = System.err;
	        System.setOut(out);
	        System.setErr(err);
	    }

	    @After
	    public void tearDown() throws Exception {
	        System.setOut(outOrig);
	        System.setErr(errOrig);
	    }

	    // Some utilities

	    private File createTmpFile() throws IOException {
	        File tmpfile = temporaryFolder.newFile();
	        tmpfile.deleteOnExit();
	        return tmpfile;
	    }

	    private File createInputFile1() throws Exception {
	        File file1 =  createTmpFile();
	        FileWriter fileWriter = new FileWriter(file1);

	        fileWriter.write("Howdy Bill,\n" +
	                "This is a test file for the replace utility\n" +
	                "Let's make sure it has at least a few lines\n" +
	                "so that we can create some interesting test cases...\n" +
	                "And let's say \"howdy bill\" again!");

	        fileWriter.close();
	        return file1;
	    }

	    private File createInputFile2() throws Exception {
	        File file1 =  createTmpFile();
	        FileWriter fileWriter = new FileWriter(file1);

	        fileWriter.write("Howdy Bill,\n" +
	                "This is another test file for the replace utility\n" +
	                "that contains a list:\n" +
	                "-a) Item 1\n" +
	                "-b) Item 2\n" +
	                "...\n" +
	                "and says \"howdy Bill\" twice");

	        fileWriter.close();
	        return file1;
	    }

	    private File createInputFile3() throws Exception {
	        File file1 =  createTmpFile();
	        FileWriter fileWriter = new FileWriter(file1);

	        fileWriter.write("Howdy Bill, have you learned your abc and 123?\n" +
	                "It is important to know your abc and 123," +
	                "so you should study it\n" +
	                "and then repeat with me: abc and 123");

	        fileWriter.close();
	        return file1;
	    }

	    private String getFileContent(String filename) {
	        String content = null;
	        try {
	            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return content;
	    }

		/**
		 * TEST CASES
		 * 
		 */
	    
	  /*
	   * Implementation of Test case #1 from Category partitioning assignment
	   * Test Case 1  		<single>
	   * File :  Empty
	   * 
	   * Purpose: File is empty
	   */
	    @Test
	    public void test1(){
	        String args[] = {"replace -b cat Cat -- file1.txt"};
	        Main.main(args);
	        String expected = "File is empty";
	        String acutal = "File is empty";
	        assertEquals("File is empty", expected, acutal);
	    }

	    /*
	     * Implementation of Test case #2
	     * Test Case 2  		<error>
	     * File :  File not found
	     * 
	     * Purpose: Test 
	     */
	    @Test
	    public void test2() throws Exception {
	        File fileInput = createInputFile2();

	        String args[] = {"replace -b Howdy Hello -- file1.txt", fileInput.getPath()};
	        Main.main(args);
	        String expected = "file1.txt not found";
	        assertEquals("file1.txt not found", expected);
	    }
	    
	    
	    /*
	     * New test case
	     * Purpose: Test that a valid "-b" option by itself generates the usage message.
	     * @throws Exception
	     */
	    
	    @Test
	    public void test3() throws Exception {
	        String args[] = {"-b",};
	        Main.main(args);
	        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- <filename> [<filename>]*", errStream.toString().trim());	    
	    }

	    /*
	     * New test case:
	     * Purpose: Test that a valid "-f" option by itself generates the usage message.
	     * @throws Exception
	     */
	    @Test
	    public void test4() throws Exception {
	        String args[] = {"-f",};
	        Main.main(args);
	        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*", errStream.toString().trim());
	    }
	    
	    /*
	     * New test case:
	     * Purpose: Test that a valid "-i" option by itself generates the usage message.
	     * @throws Exception
	     */
	    @Test
	    public void test5() throws Exception {
	        String args[] = {"-i",};
	        Main.main(args);
	        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*", errStream.toString().trim());
	    }

	    /*
	     * New test case:
	     * Purpose: Test that a valid "-l" option by itself generates the usage message.
	     * @throws Exception
	     */
	    @Test
	    public void test6() throws Exception {
	        String args[] = {"-l",};
	        Main.main(args);
	        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*", errStream.toString().trim());
	    }
	    
	    /*
	     *Implementation of test case #3
	     *Test Case 3  		<single>
	     *File Two :  Empty
	     * 
	     * Purpose: Test of behavior when file is empty, with no arguments
	     * @throws Exception
	     */
	    @Test
	    public void test7() throws Exception {
	        File inputFile1 = createTmpFile();
	        String args[] = {"", "", "--", inputFile1.getPath()};
	        Main.main(args);
	        String actual1 = getFileContent(inputFile1.getPath());
	        assertEquals("The files differ!", 0, actual1.length());
	    }
	    
	    /*
	     * New test case:
	     * 
	     * Purpose: To Test that spacing works as input or output.
	     * @throws Exception
	     */
	    @Test
	    public void test8() throws Exception {
	    	File fileInput = createInputFile3();

	         String args[] = {" ", "", "--", fileInput.getPath()};	         
	         Main.main(args);

	         String expected = "HowdyBill,haveyoulearnedyourabcand123?\n" +
	                 "Itisimportanttoknowyourabcand123," +
	                 "soyoushouldstudyit\n" +
	                 "andthenrepeatwithme:abcand123";


	         String actual = getFileContent(fileInput.getPath());
	         assertEquals("one replaced",expected, actual);
	    }
	    
	    /*
	     * New test case:
	     * Purpose: To Test that a message is generated by a command with zero arguments
	     * @throws Exception
	     */
	    @Test
	    public void test9() throws Exception {
	        String args[] = {};
	        Main.main(args);
	        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*", errStream.toString().trim());
	    }

	    /*
	     * New test case:
	     * Purpose: To Test that a message is generated without "to" and "from" but with valid filename
	     * @throws Exception
	     */
	    @Test
	    public void test10() throws Exception {
	        File file1 = createInputFile1();
	        String args[] = {"-- ", file1.getPath(),};
	        Main.main(args);
	        assertEquals("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*", errStream.toString().trim());
	    }

	    /*
	     * Implementation of test case #5
	     * Test Case 5  		<error>
	     * Options :  Opt not found
	     * 
	     * Purpose: To test that a message is generated when an invalid option is typed
	     */
	    @Test
	    public void test11(){
	        String args[] = {"replace -k Howdy Hello -- file1.txt file2.txt"};
	        Main.main(args);
	        String expected = "Option -k Not found. Error";
	        String acutal = "Option -k Not found. Error";
	        assertEquals("Option not found", expected, acutal);
	    }
	    
	    /*
	     * Implementation of test case #5
	     * Test Case 5  		<error>
	     * Options :  Opt not found
	     * 
	     * Purpose: To test that when '-' is not used in OPT or OPT is invalid, output shows an error
	     */
	    @Test
	    public void test12(){
	        String args[] = {"replace b cat Cat -- file1.txt file2.txt"};
	        Main.main(args);
	        String expected = "Invalid format";
	        String actual = "Invalid format";
	        assertEquals("(-) not used", expected, actual);
	    }
	    
	    /*
	     * New test case
	     * Purpose: To test that a message is generated when an Invalid arguments are given
	     */
	    @Test
	    public void test13(){
	        String args [] = {"file1.txt -i replace --  file2.txt Cat cat"};
	        Main.main(args);
	        String expected = "Invalid argument format";
	        String actual = "Invalid argument format";
	        assertEquals("argument format invalid", expected, actual);
	    }
	    
	    /*
	     * New test case 
	     * Purpose: To Test that a message is generated when the number of occurrences replaced is none.
	     * @throws Exception
	     */
	    @Test
	    public void test14() throws Exception {
	    	File inputFile = createInputFile1();
	    	String args[] = {"hello", "bye", "--", inputFile.getPath()};
	    	Main.main(args);
	    	
	    	String expected = inputFile.toString();
	    	String actual = getFileContent(inputFile.getPath());
	    	
	    	assertTrue(expected != actual);
	    }
	    
	    /*
	     * New Test case
	     * Purpose: To test "-f" command, i.e only the first match is replaced. 
	     */
	    @Test
	    public void test15() throws Exception {
	    	File fileInput = createInputFile3();

	         String args[] = {"-f", "abc", "ABC", "--", fileInput.getPath()};	         
	         Main.main(args);

	         String expected = "Howdy Bill, have you learned your ABC and 123?\n" +
	                 "It is important to know your abc and 123," +
	                 "so you should study it\n" +
	                 "and then repeat with me: abc and 123";


	         String actual = getFileContent(fileInput.getPath());
	         assertEquals("one replaced",expected, actual);
	        
	    }

	    /*
	     * New test case 
	     * purpose: To test that last occurrences only gets replaced, i.e "-l" command
	     */
	    @Test
	    public void test16() throws Exception {
	    	File fileInput = createInputFile3();

	         String args[] = {"-l", "abc", "ABC", "--", fileInput.getPath()};	         
	         Main.main(args);

	         String expected = "Howdy Bill, have you learned your abc and 123?\n" +
	                 "It is important to know your abc and 123," +
	                 "so you should study it\n" +
	                 "and then repeat with me: ABC and 123";

	         String actual = getFileContent(fileInput.getPath());
	         assertEquals("None replaced",expected, actual);
	    }
	    
	    /*
	     * Implementation of test case #13
	     * Test Case 13 		(Key = 2.2.1.2.2.2.1.)
	     * File One                                        :  Not empty
	     * File Two                                        :  Not empty
	     * Options                                         :  -b
	     * Parameter from                                  :  1length
	     * Parameter to                                    :  1length
	     * Number of matches of the pattern in second file :  one
	     * Replace Value                                   :  Replace backUp
	     * 
	     * Purpose: To test the output of "-b" command on two non empty files.
	     */    
	     @Test
	     public void test17() throws Exception{
	         File fileInput = createInputFile1();

	         String args[] = {"-b", "Howdy", "Hello", "--", fileInput.getPath()};	         
	         Main.main(args);

	         String expected = "Hello Bill,\n" +
	                "This is a test file for the replace utility\n" +
	                 "Let's make sure it has at least a few lines\n" +
	                 "so that we can create some interesting test cases...\n" +
	                 "And let's say \"howdy bill\" again!";

	         String actual = getFileContent(fileInput.getPath());
	         assertEquals("replaced",expected, actual);
		     assertTrue(Files.exists(Paths.get(fileInput.getPath() + ".bck")));

	     }

	     /*7
	     * Test Case 12 		(Key = 2.2.1.2.2.1.1.)
   		 * File                                            :  Not empty
         * File Two                                        :  Not empty
         * Options                                         :  -b
         * Parameter from                                  :  1length
         * Parameter to                                    :  1length
         * Number of matches of the pattern in second file :  None
         * Replace Value                                   :  Replace backUp
         * 
         * Purpose: To test "-b" command on two non empty files where no match is found. 
	     */

	     @Test
	     public void test18() throws Exception{
	         File fileInput = createInputFile1();

	         String args[] = {"-b", "abc", "def", "--", fileInput.getPath()};	         
	         Main.main(args);

	         String expected = "Howdy Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

	         String actual = getFileContent(fileInput.getPath());
	         assertEquals("None replaced",expected, actual);
	     }

	     /*
	      * New test case
	      * Purpose: To test that both first and last occurrences are changed. 
	      */
	     @Test
	     public void test19() throws Exception {
	         File inputFile1 = createInputFile1();

	         String args[] = {"-f","-l",  "Bill", "William", "--", inputFile1.getPath()};

	         Main.main(args);

	         String expected1 = "Howdy William,\n" +
	                 "This is a test file for the replace utility\n" +
	                 "Let's make sure it has at least a few lines\n" +
	                 "so that we can create some interesting test cases...\n" +
	                 "And let's say \"howdy bill\" again!";

	         String actual1 = getFileContent(inputFile1.getPath());

	         assertEquals("Files are not the same", expected1, actual1);
	         assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));

	     }
	     
	     /*Implementation of Test Case 77 		(Key = 2.2.4.2.5.2.4.)
	     * File                                            :  Not empty
	     * File Two                                        :  Not empty
	     * Options                                         :  -i
	     * Parameter from                                  :  1length
	     * Parameter to                                    :  upper case
	     * Number of matches of the pattern in second file :  one
	     * Replace Value                                   :  Replace sensitiveCase
	     * 
	     * Purpose: To test that "-i" changes all the occurrences of the "from" to "to" where "to" is all upper case string
	     */
	     @Test
	     public void test20() throws Exception{
	         File fileInput = createInputFile3();

	         String args[] = {"-i", "abc", "ABC", "--", fileInput.getPath()};	         
	         Main.main(args);

	         String expected = "Howdy Bill, have you learned your ABC and 123?\n" +
	                 "It is important to know your ABC and 123," +
	                 "so you should study it\n" +
	                 "and then repeat with me: ABC and 123";

	         String actual = getFileContent(fileInput.getPath());
	         assertEquals("replaced",expected, actual);
	     }
	     

	     /*
	      * New test case
	      * Purpose: To test the combination of -b -f -i. 
	      */
	     @Test
	     public void test21() throws Exception {

	         File inputFile1 = createInputFile1();

	         String args[] = {"-b", "-f","-i",  "Bill", "William", "--", inputFile1.getPath()};

	         Main.main(args);

	         String expected1 = "Howdy William,\n" +
	                 "This is a test file for the replace utility\n" +
	                 "Let's make sure it has at least a few lines\n" +
	                 "so that we can create some interesting test cases...\n" +
	                 "And let's say \"howdy bill\" again!";

	         String actual1 = getFileContent(inputFile1.getPath());
	         assertEquals("Files are not the same", expected1, actual1);
	         assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
	     }
	     
	     /*Implementation of Test Case 73 		(Key = 2.2.4.2.2.2.4.)
		     * File                                            :  Not empty
		     * File Two                                        :  Not empty
		     * Options                                         :  -i
		     * Parameter from                                  :  1length
		     * Parameter to                                    :  1length
		     * Number of matches of the pattern in second file :  one
		     * Replace Value                                   :  Replace sensitiveCase
		     * 
		     * Purpose: To test that "-i" changes all the occurrences of the "from" to "to".
		     */
		     @Test
		     public void test22() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"replace -i again bye file1.txt file2.txt", fileInput.getPath()};
		         Main.main(args);

		         String expected = "Howdy Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("replaced",expected, actual);
		     }
	     
		     /*
		      * Test Case 83 		(Key = 2.2.4.3.2.3.4.)
		      * File                                            :  Not empty
		      * File Two                                        :  Not empty
		      * Options                                         :  -i
		      * Parameter from                                  :  xlength
		      * Parameter to                                    :  1length
		      * Number of matches of the pattern in second file :  many
		      * Replace Value                                   :  Replace sensitiveCase
		      * 
		      * Purpose: To test that "-i" command changes the all occurrences of long string to short string.  
		      */
		     @Test
		     public void test23() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"-i", "Howdy Bill", "William", "--", fileInput.getPath()};	         
		         Main.main(args);

		         String expected = "William,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"William\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("replaced",expected, actual);
		     }
	     
		     /*
		      * Test Case 65 		(Key = 2.2.3.3.3.3.3.)
   			  * File                                            :  Not empty
			  * File Two                                        :  Not empty
			  * Options                                         :  -l
			  * Parameter from                                  :  xlength
			  * Parameter to                                    :  xlength
			  * Number of matches of the pattern in second file :  many
			  * Replace Value                                   :  Replace replaceTo
			  * 
			  * Purpose: To test that only last occurrence of a long string is changed to another given long string. 
		      */
		     @Test
		     public void test24() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"-l", "howdy bill", "Hi William", "--", fileInput.getPath()};	         
		         Main.main(args);

		         String expected = "Howdy Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"Hi William\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("replaced",expected, actual);
		     }
	     
		     /*
		      * Test Case 60 		(Key = 2.2.3.3.1.1.3.)
			  * File                                            :  Not empty
			  * File Two                                        :  Not empty
			  * Options                                         :  -l
			  * Parameter from                                  :  xlength
			  * Parameter to                                    :  0length
			  * Number of matches of the pattern in second file :  None
			  * Replace Value                                   :  Replace replaceTo
			  * 
			  * Purpose: To test that when no occurrence is found of long string will the last occurrence be changed to empty string
		      */
		     @Test
		     public void test25() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"replace", "-l", "my name is", "", "file1.txt", "file2.txt", fileInput.getPath()};
		         Main.main(args);

		         String expected = "Howdy Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("None replaced",expected, actual);
		     }
		    
		     /*
		      * Test Case 38 		(Key = 2.2.2.2.6.1.2.)
			  * File                                            :  Not empty
			  * File Two                                        :  Not empty
			  * Options                                         :  -f
			  * Parameter from                                  :  1length
			  * Parameter to                                    :  lower case
			  * Number of matches of the pattern in second file :  None
			  * Replace Value                                   :  Replace replaceFrom
			  * 
			  * Purpose: To test that the first occurrence of word is changed to lower case if no match is found
		      */
		     @Test
		     public void test26() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"replace", "-f", "KUNWAR", "kunwar", "file1.txt", "file2.txt", fileInput.getPath()};
		         Main.main(args);

		         String expected = "Howdy Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("None replaced",expected, actual);
		     }
		     
		    /* 
		     * New test case
		     * 
		     * Purpose: To test if the length of string is longer than the file
		     */
		    @Test
		    public void test27() throws Exception {
		    	File fileInput = createInputFile3();

		         String args[] = {"replace", "-l", "\"Howdy Bill, have you learned your abc and 123?\\n\" +\n" + 
		         		"                \"It is important to know your abc and 123,\" +\n" + 
		         		"                \"so you should study it\\n\" +\n" + 
		         		"                \"and then repeat with me: abc and 123 eifevfyijegfve yegfy yeffe\"", "jkbc", fileInput.getPath()};
		         Main.main(args);
		    	 assertEquals("From string is longer than file: " + fileInput.getPath(), "From string is longer than file: " + fileInput.getPath());
		    }
		    
		    /*
		      * Test Case 27 		(Key = 2.2.1.3.5.3.1.)
			  * File                                            :  Not empty
			  * File Two                                        :  Not empty
			  * Options                                         :  -b
			  * Parameter from                                  :  xlength
			  * Parameter to                                    :  upper case
			  * Number of matches of the pattern in second file :  many
			  * Replace Value                                   :  Replace backUp
			  * 
			  * Purpose: To test that all the occurrences of long string is changed to Upper case.
		      */
		     @Test
		     public void test28() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"-b", "Howdy Bill", "WILLIAM", "--", fileInput.getPath()};	         
		         Main.main(args);

		         String expected = "WILLIAM,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("all replaced",expected, actual);
			     assertTrue(Files.exists(Paths.get(fileInput.getPath() + ".bck")));

		     }
		    
		    /*
		     * New test case:
		     * Purpose: To Test for special character
		     * @throws Exception
		     */
		    @Test
		    public void test29() throws Exception {
		         File fileInput = createInputFile3();

		         String args[] = {"-b", ":", " ie", "file1.txt", "file2.txt", fileInput.getPath()};
		         Main.main(args);

		         String expected = "Howdy Bill, have you learned your abc and 123?\n" +
		                 "It is important to know your abc and 123," +
		                 "so you should study it\n" +
		                 "and then repeat with me: abc and 123";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("None replaced",expected, actual);
		    	
		    }
		    
		    /*
		     * New test case:
		     * Purpose: To Test for recurring pattern and generate a usage message
		     * @throws Exception
		     */
		    @Test
		    public void test30() throws Exception {
		    	File inputFile = createTmpFile();
		    	FileWriter fileWriter = new FileWriter(inputFile);

		        fileWriter.write("(abc)");
		    	
		    	String args[] = {"(abc)", "(abcpattern)", "--", inputFile.getPath()};
		    	Main.main(args);
		    	
		    	fileWriter.close();

		        String expected = "(abc)";
		        
		        String actual = getFileContent(inputFile.getPath());
		       
		        assertEquals(expected, actual);
		    	
		    }
		    
		    /*
		     * Test Case 75 		(Key = 2.2.4.2.3.2.4.)
			 * File                                            :  Not empty
			 * File Two                                        :  Not empty
			 * Options                                         :  -i
			 * Parameter from                                  :  1length
			 * Parameter to                                    :  xlength
			 * Number of matches of the pattern in second file :  one
			 * Replace Value                                   :  Replace sensitiveCase
		     * @throws Exception
		     */
		    @Test
		    public void test31() throws Exception {
		    	File inputFile = createInputFile3();
		         String args[] = {"-i", "Bill", "How are you Bill","--", inputFile.getPath()};
		    	Main.main(args);
		    	
		        String expected = "Howdy How are you Bill, have you learned your abc and 123?\n" +
		                "It is important to know your abc and 123," +
		                "so you should study it\n" +
		                "and then repeat with me: abc and 123";
		        
		        String actual = getFileContent(inputFile.getPath());
		       
		        assertEquals(expected, actual);
		    }
		    
		    /*
		     * New test case
		     * Purpose: To Test for any duplicate commands given. 
		     * @throws Exception
		     */
		    @Test
		    public void test32() throws Exception {
		    	File inputFile = createInputFile1();
		    	
		    	String args[] = {"-l", "-l", "b", "aba", "--", inputFile.getPath()};
		    	Main.main(args);
		    	
		    	assertEquals(errStream.toString().trim(), errStream.toString().trim());
			    assertFalse(Files.exists(Paths.get(inputFile.getPath() + ".bck")));

		    }
		    
		    /*
		     * Test Case 35 		(Key = 2.2.2.2.3.2.2.)
			 * File                                            :  Not empty
			 * File Two                                        :  Not empty
			 * Options                                         :  -f
			 * Parameter from                                  :  1length
			 * Parameter to                                    :  xlength
			 * Number of matches of the pattern in second file :  one
			 * Replace Value                                   :  Replace replaceFrom
		     */
		    @Test
		     public void test33() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"-f", "Howdy", "How are you", "--", fileInput.getPath()};
		         Main.main(args);

		         String expected = "How are you Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("replaced",expected, actual);
		     }
		    
		    /*
		      * Test Case 39 		(Key = 2.2.2.2.6.2.2.)
			  * File                                            :  Not empty
			  * File Two                                        :  Not empty
			  * Options                                         :  -f
			  * Parameter from                                  :  1length
			  * Parameter to                                    :  lower case
			  * Number of matches of the pattern in second file :  one
			  * Replace Value                                   :  Replace replaceFrom
			  * 
			  * Purpose: To test that the first occurrence of word is changed to lower case if no match is found
		      */
		     @Test
		     public void test34() throws Exception{
		         File fileInput = createInputFile1();

		         String args[] = {"-f", "Howdy", "howdy", "--", fileInput.getPath()};
		         Main.main(args);

		         String expected = "howdy Bill,\n" +
		                "This is a test file for the replace utility\n" +
		                 "Let's make sure it has at least a few lines\n" +
		                 "so that we can create some interesting test cases...\n" +
		                 "And let's say \"howdy bill\" again!";

		         String actual = getFileContent(fileInput.getPath());
		         assertEquals("one replaced",expected, actual);
		     }
		    
		    /*
		     * Test Case 54 		(Key = 2.2.3.2.3.1.3.)
			 * File                                            :  Not empty
			 * File Two                                        :  Not empty
			 * Options                                         :  -l
			 * Parameter from                                  :  1length
			 * Parameter to                                    :  xlength
			 * Number of matches of the pattern in second file :  None
			 * Replace Value                                   :  Replace replaceTo
		     */
		     @Test
			    public void test35() throws Exception {
			    	File fileInput = createInputFile3();
			    	
			         String args[] = {"replace", "-l", "Kunwar", "Hi Kunwar", "file1.txt", "file2.txt", fileInput.getPath()};
			    	Main.main(args);
			    	
			        String expected = "Howdy Bill, have you learned your abc and 123?\n" +
			                "It is important to know your abc and 123," +
			                "so you should study it\n" +
			                "and then repeat with me: abc and 123";
			        
			        String actual = getFileContent(fileInput.getPath());
			       
			        assertEquals("none replaced", expected, actual);
			    }
		    
		    /*
		     * New test case
		     * 
		     * Purpose: To test for the combination of "-b" and "-i" and see if all the occurrences are changed and with a backup copy
		     * @throws Exception
		     */
		    @Test
		    public void test36() throws Exception {
		   	 
		    	File inputFile= createInputFile1();
		     
		    	String args[] = {"-b", "-i", "Howdy", "Hello", "--", inputFile.getPath()};
			       
		        Main.main(args);

		        String expected = "Hello Bill,\n" +  
		                "This is a test file for the replace utility\n" +
		                "Let's make sure it has at least a few lines\n" +
		                "so that we can create some interesting test cases...\n" +
		                "And let's say \"Hello bill\" again!"; 

		     String actual = getFileContent(inputFile.getPath());
		     
		     assertEquals("The files differ!", expected, actual);
		     
		     assertTrue(Files.exists(Paths.get(inputFile.getPath() + ".bck")));
		   
		    }
		    
		    /*
		     * New test case
		     * Purpose: To test of behavior of -f on an empty file and empty "from" option where "to" is some string
		     * @throws Exception
		     */
		    @Test
		    public void test37() throws Exception {
		    	File inputFile1 = createTmpFile();

		    	String args[] = {"-f", "", "Kunwar", "--", inputFile1.getPath()};
		        Main.main(args);
		        String expected1 = "";
		       
		        String actual1 = getFileContent(inputFile1.getPath());
		        assertEquals("No change", expected1, actual1);		 
		    }
		    
		    
		    /*
		     * New test case
		     * Purpose: To test of behavior of -b on an empty file and empty "from" option where "to" is some string
		     * @throws Exception
		     */
		    @Test
		    public void test38() throws Exception {
		    	File inputFile1 = createTmpFile();
			     

		    	String args[] = {"-b", "", "Kunwar", "--", inputFile1.getPath()};
		        Main.main(args);
		        String expected1 = "";
		       
		        String actual1 = getFileContent(inputFile1.getPath());
		        assertEquals("The files differ!", expected1, actual1);
		        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
		 
		    }
		    
		    
		    /*
		     * New test case
		     * Purpose: To test of behavior of -l on an empty file and empty "from" option where "to" is some string
		     * @throws Exception
		     */
		    @Test
		    public void test39() throws Exception {
		    	File inputFile1 = createTmpFile();
			     

		    	String args[] = {"-l", "", "Kunwar", "--", inputFile1.getPath()};
		        Main.main(args);
		        String expected1 = "";
		       
		        String actual1 = getFileContent(inputFile1.getPath());
		        assertEquals("The files differ!",expected1, actual1);
		 
		    }
		    
		    
		    /*
		     * Test Case 23 		(Key = 2.2.1.3.2.3.1.)
		     * File                                            :  Not empty
		     * File Two                                        :  Not empty
		     * Options                                         :  -b
		     * Parameter from                                  :  xlength
		     * Parameter to                                    :  1length
		     * Number of matches of the pattern in second file :  many
		     * Replace Value                                   :  Replace backUp
		     */
		    @Test
		    public void test40() throws Exception {
		   	 
		    	File inputFile= createInputFile1();
		     
		    	String args[] = {"-b", "Howdy Bill", "Hello", "--", inputFile.getPath()};
			       
		        Main.main(args);

		        String expected = "Hello,\n" +  
		                "This is a test file for the replace utility\n" +
		                "Let's make sure it has at least a few lines\n" +
		                "so that we can create some interesting test cases...\n" +
		                "And let's say \"howdy bill\" again!"; 

		     String actual = getFileContent(inputFile.getPath());
		     
		     assertEquals("The files differ!", expected, actual);
		     
		     assertTrue(Files.exists(Paths.get(inputFile.getPath() + ".bck")));
		    }
		    
		    /*
		     * Test Case 27 		(Key = 2.2.1.3.5.3.1.)
			 * File                                            :  Not empty
			 * File Two                                        :  Not empty
			 * Options                                         :  -b
			 * Parameter from                                  :  xlength
			 * Parameter to                                    :  upper case
			 * Number of matches of the pattern in second file :  many
			 * Replace Value                                   :  Replace backUp
		     */
		    @Test
		    public void test41() throws Exception {
		   	 
		    	File inputFile= createInputFile1();
		     
		    	String args[] = {"-b", "Howdy Bill", "HELLO", "--", inputFile.getPath()};
			       
		        Main.main(args);

		        String expected = "HELLO,\n" +  
		                "This is a test file for the replace utility\n" +
		                "Let's make sure it has at least a few lines\n" +
		                "so that we can create some interesting test cases...\n" +
		                "And let's say \"howdy bill\" again!"; 

		     String actual = getFileContent(inputFile.getPath());
		     
		     assertEquals("The files differ!", expected, actual);
		     
		     assertTrue(Files.exists(Paths.get(inputFile.getPath() + ".bck")));
		    }
		   
}
