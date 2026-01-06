package Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileGameStorage implements GameStorage {
    private final String path;

    public FileGameStorage(String path) {
        this.path = path;
    }

    @Override
    public SavedGameData load() {
        File f = new File(path);
        if (!f.exists()) return null;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            SavedGameData out = new SavedGameData();
            line = r.readLine();
            if (line == null) return null;
            out.currentPlayerIndex = Integer.parseInt(line.trim());
            out.p1vp = Integer.parseInt(r.readLine().trim());
            out.p1chips = parseChipsLine(r.readLine());
            out.p2vp = Integer.parseInt(r.readLine().trim());
            out.p2chips = parseChipsLine(r.readLine());
            int cardCount = Integer.parseInt(r.readLine().trim());
            out.cards = new ArrayList<>();
            for (int i=0;i<cardCount;i++) {
                String cardLine = r.readLine();
                if (cardLine == null) break;
                String[] parts = cardLine.split("\\|", -1);
                SavedCardData sc = new SavedCardData();
                sc.id = parts[0];
                sc.vp = Integer.parseInt(parts[1]);
                sc.cost = parseCostStringToMap(parts[2]);
                out.cards.add(sc);
            }
            return out;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(SavedGameData data) {
        File f = new File(path);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            w.write(Integer.toString(data.currentPlayerIndex)); w.newLine();
            w.write(Integer.toString(data.p1vp)); w.newLine();
            w.write(chipsMapToLine(data.p1chips)); w.newLine();
            w.write(Integer.toString(data.p2vp)); w.newLine();
            w.write(chipsMapToLine(data.p2chips)); w.newLine();
            w.write(Integer.toString(data.cards.size())); w.newLine();
            for (SavedCardData sc : data.cards) {
                w.write(sc.id + "|" + sc.vp + "|" + costMapToString(sc.cost));
                w.newLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void clear() {
        File f = new File(path);
        if (f.exists()) f.delete();
    }

    private Map<String,Integer> parseChipsLine(String line) {
        Map<String,Integer> m = new HashMap<>();
        if (line == null || line.isEmpty()) return m;
        String[] parts = line.split(",");
        for (String p : parts) {
            String[] kv = p.split(":");
            if (kv.length == 2) m.put(kv[0], Integer.parseInt(kv[1]));
        }
        return m;
    }

    private Map<String,Integer> parseCostStringToMap(String s) {
        Map<String,Integer> m = new HashMap<>();
        if (s == null || s.isEmpty()) return m;
        int i=0;
        while (i < s.length()) {
            char ch = s.charAt(i);
            i++;
            int start = i;
            while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
            if (start == i) continue;
            int val = Integer.parseInt(s.substring(start, i));
            String key;
            switch (ch) {
                case 'R': key = "RED"; break;
                case 'B': key = "BLUE"; break;
                case 'G': key = "GREEN"; break;
                case 'K': key = "BLACK"; break;
                case 'W': key = "WHITE"; break;
                default: continue;
            }
            m.put(key, val);
        }
        return m;
    }

    private String chipsMapToLine(Map<String,Integer> m) {
        StringBuilder sb = new StringBuilder();
        boolean first=true;
        for (String k : new String[]{"RED","BLUE","GREEN","BLACK","WHITE"}) {
            if (!first) sb.append(','); first=false;
            sb.append(k).append(':').append(m.getOrDefault(k, 0));
        }
        return sb.toString();
    }

    private String costMapToString(Map<String,Integer> m) {
        if (m == null) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Integer> e : m.entrySet()) {
            String key = e.getKey();
            String letter = key.substring(0,1);
            if (key.equals("BLACK")) letter = "K";
            if (e.getValue() > 0) sb.append(letter).append(e.getValue());
        }
        return sb.toString();
    }
}
