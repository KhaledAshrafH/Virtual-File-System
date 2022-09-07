package SystemControl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class DiskDataControl {
    public static void FWrite(File file, String r) throws Exception {
        r+="\n";
        Files.write(Paths.get(String.valueOf(file)), r.getBytes(), StandardOpenOption.APPEND);
    }

    public static ArrayList<String> FRead(File file) throws Exception {
        BufferedReader bufReader = new BufferedReader(new FileReader(file));
        ArrayList<String> listOfLines = new ArrayList<>();
        String line = bufReader.readLine(); while (line != null) { listOfLines.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();
        return listOfLines;
    }
    public static void removeLine(File f, String path) throws IOException {
        File tempFile = new File("C:\\Users\\Eng Khaled\\VFS_V2\\DataBase\\myTempFile.vfs");
        BufferedReader reader = new BufferedReader(new FileReader(f));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.contains(path)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }//ContiguousAllocation.vfs
        writer.close();
        reader.close();
        f.delete();
        tempFile.renameTo(f);
    }
    public static void removeLines(String path, int startline, int numOfLines) throws IOException {

            BufferedReader br=new BufferedReader(new FileReader(path));

            //to store contents of the file
            StringBuffer sb=new StringBuffer("");

            int linenumber=1;
            String line;

            while((line=br.readLine())!=null)
            {
                if(linenumber<startline||linenumber>=startline+numOfLines)
                    sb.append(line).append("\n");
                linenumber++;
            }
            br.close();

            FileWriter fw=new FileWriter(new File(path));
            fw.write(sb.toString());
            fw.close();
    }


}
