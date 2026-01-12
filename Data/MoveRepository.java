// Seokhyun
package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository {
    public void saveMoves(String gameName, List<String> moves) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(gameName + ".log"))) {
            for (String m : moves) {
                w.write(m);
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadMoves(String gameName) {
        List<String> moves = new ArrayList<>();
        File f = new File(gameName + ".log");
        if (!f.exists()) return moves;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                moves.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moves;
    }

    public List<String> getReplayNames() {
        List<String> names = new ArrayList<>();
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".log"));
        if (files != null) {
            for (File f : files) {
                String n = f.getName();
                names.add(n.substring(0, n.length() - 4));
            }
        }
        return names;
    }

    public void deleteReplay(String gameName) {
        File f = new File(gameName + ".log");
        if (f.exists()) f.delete();
    }
}