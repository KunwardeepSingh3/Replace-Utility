package edu.qc.seclass.replace;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO: Empty skeleton method
		List<String> list_File = new ArrayList<>();
		List<String> list_From = new ArrayList<>();
		List<String> list_To = new ArrayList<>();
		int tracker = 0;

		boolean b_flag = false, f_flag = false, l_flag = false, i_flag = false;

		if (args.length < 3) {
			usage();
			return;
		}
		
		//Parsing the arguments - options, filenames, from and to
		for (int i=0; i<args.length; i++) {
			if (tracker == 0) {
				if (args[i].equals("-b")) {
					b_flag = true;
					continue;
				}
				if (args[i].equals("-f")) {
					f_flag = true;
					continue;
				}
				if (args[i].equals("-l")) {
					l_flag = true;
					continue;
				}
				if (args[i].equals("-i")) {
					i_flag = true;
					continue;
				}
				if (args[i].equals("--")) {
					tracker += 1;
					continue;
				}
				if (!args[i].startsWith("-")) {
					list_From.add(args[i]);
					tracker = 2;
					continue;
				}
				if (args[i].startsWith("-") && !args[i].equals("-f") && !args[i].equals("-l") && !args[i].equals("-b") && !args[i].equals("-i")) {
					usage();
					return;
				}
			}
			if (tracker == 1) {
				list_From.add(args[i]);
				tracker = 2;
				continue;
			}

			if (tracker == 2) {
				list_To.add(args[i]);
				tracker = 3;
				continue;
			}
			if (tracker == 3 && args[i].equals("--")) {
				tracker = 4;
				continue;
			}
			if (tracker == 3 && !args[i].equals("--")) {
				list_From.add(args[i]);
				tracker = 2;
				continue;
			}
			if (tracker == 4) {
				list_File.add(args[i]);
				continue;
			}
		}

		if ((list_File.size() == 0) || (list_From.size() == 0) || (list_To.size() == 0) || (list_From.size() != list_To.size())) {
			usage();
			return;
		}
	
		String str = "";
		if (i_flag) {
			str = "(?i)";
		}

		for (int i = 0; i < list_From.size(); i++) {
			String from_String = list_From.get(i);
			if (list_From.get(i).equals("")) {
				usage();
				return;
			}
			String to_String = list_To.get(i);

			//replacing and backup for different commands
			for (int j=0; j<list_File.size(); j++) {
				try {
					// "-b" command: if bflag is true
					if (b_flag) { 
						int index = list_File.get(j).lastIndexOf(File.separator);
						String fileShortName = list_File.get(j).substring(index + 1);
						if (Files.notExists(Paths.get(list_File.get(j) + ".bck"))) {
							String info = new String(Files.readAllBytes(Paths.get(list_File.get(j))), StandardCharsets.UTF_8);
							FileWriter fw = new FileWriter(list_File.get(j) + ".bck");
							fw.write(info);
							fw.close();
						} else {
							System.err.println(fileShortName + ": Backup file already exists");
							continue;
						}
					}

					String info = new String(Files.readAllBytes(Paths.get(list_File.get(j))), StandardCharsets.UTF_8);
					FileWriter fw = new FileWriter(list_File.get(j));
					
					// if none of first or last flag is true
					if (!f_flag && !l_flag) {
						info = info.replaceAll(str + from_String, to_String);
					}
					// if first or last flag is true
					else {
						if (f_flag) {
							info = info.replaceFirst(str + from_String, to_String);
						}
						if (l_flag) {
							String r_info = new StringBuffer(info).reverse().toString();
							from_String = new StringBuffer(from_String).reverse().toString();
							to_String = new StringBuffer(to_String).reverse().toString();
							r_info = r_info.replaceFirst(str + from_String, to_String);
							info = new StringBuffer(r_info).reverse().toString();
						}
					}
					fw.write(info);
					fw.close();

				}

				catch (Exception e) {
					int index = list_File.get(j).lastIndexOf(File.separator);
					String sub_str = list_File.get(j).substring(index + 1);
					System.err.println("File " + sub_str + " not found");
				}
			}
		}

	}

	private static void usage() {
		System.err.println("Usage: Replace [-b] [-f] [-l] [-i] <from> <to> -- " + "<filename> [<filename>]*");
	}

}